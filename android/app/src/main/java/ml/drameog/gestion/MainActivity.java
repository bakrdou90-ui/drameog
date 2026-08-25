package ml.drameog.gestion;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.provider.MediaStore;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * DRAME OG BU LA QUALITÉ — coque Android.
 *
 * L'application elle-même est le fichier www/index.html, embarqué dans les
 * assets. Cette classe lui donne ce qu'une page web seule n'a pas :
 *   • un vrai fichier de données dans la mémoire privée de l'application,
 *   • l'écriture d'une sauvegarde dans le dossier Téléchargements,
 *   • le choix d'un fichier de sauvegarde à restaurer,
 *   • l'impression des factures,
 *   • le bouton Retour du téléphone.
 *
 * Aucune bibliothèque externe : le projet se compile avec le seul SDK Android.
 */
public class MainActivity extends Activity {

    private static final String FICHIER_DONNEES = "donnees.json";
    private static final int CHOISIR_SAUVEGARDE = 4201;

    private WebView vue;
    private long dernierRetour = 0;

    @Override
    protected void onCreate(Bundle etat) {
        super.onCreate(etat);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);

        vue = new WebView(this);
        setContentView(vue);

        WebSettings s = vue.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setSupportZoom(false);
        s.setBuiltInZoomControls(false);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);

        // Laisse la page appliquer son propre thème sombre plutôt qu'une inversion automatique.
        if (Build.VERSION.SDK_INT >= 33) {
            s.setAlgorithmicDarkeningAllowed(true);
        } else if (Build.VERSION.SDK_INT >= 29) {
            s.setForceDark(WebSettings.FORCE_DARK_AUTO);
        }

        vue.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView v, String url) {
                // L'application est entièrement locale : rien ne doit la faire sortir.
                if (url != null && url.startsWith("file://")) return false;
                return true;
            }
        });

        vue.addJavascriptInterface(new Pont(), "Android");
        vue.loadUrl("file:///android_asset/index.html");
    }

    /* ------------------------------------------------------------------
     * Pont JavaScript ⇄ Android
     * ------------------------------------------------------------------ */
    private class Pont {

        /** Lit les données enregistrées. Chaîne vide si l'application démarre pour la première fois. */
        @JavascriptInterface
        public String load() {
            File f = new File(getFilesDir(), FICHIER_DONNEES);
            if (!f.exists()) return "";
            try (InputStream in = new java.io.FileInputStream(f)) {
                return new String(lire(in), StandardCharsets.UTF_8);
            } catch (IOException e) {
                return "";
            }
        }

        /**
         * Enregistre les données. L'écriture passe par un fichier temporaire puis un
         * renommage : si le téléphone s'éteint au mauvais moment, l'ancienne version
         * reste intacte au lieu d'être tronquée.
         */
        @JavascriptInterface
        public boolean save(String json) {
            File finale = new File(getFilesDir(), FICHIER_DONNEES);
            if (json == null || json.isEmpty()) {
                return finale.delete() || !finale.exists();
            }
            File temp = new File(getFilesDir(), FICHIER_DONNEES + ".tmp");
            try (FileOutputStream out = new FileOutputStream(temp)) {
                out.write(json.getBytes(StandardCharsets.UTF_8));
                out.flush();
                out.getFD().sync();
            } catch (IOException e) {
                return false;
            }
            if (finale.exists() && !finale.delete()) return false;
            return temp.renameTo(finale);
        }

        /** Écrit une sauvegarde lisible dans le dossier Téléchargements du téléphone. */
        @JavascriptInterface
        public boolean saveBackup(String nom, String contenu) {
            byte[] octets = contenu.getBytes(StandardCharsets.UTF_8);
            try {
                if (Build.VERSION.SDK_INT >= 29) {
                    ContentValues v = new ContentValues();
                    v.put(MediaStore.Downloads.DISPLAY_NAME, nom);
                    v.put(MediaStore.Downloads.MIME_TYPE, "application/json");
                    v.put(MediaStore.Downloads.IS_PENDING, 1);
                    ContentResolver cr = getContentResolver();
                    Uri uri = cr.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, v);
                    if (uri == null) return false;
                    try (OutputStream out = cr.openOutputStream(uri)) {
                        if (out == null) return false;
                        out.write(octets);
                    }
                    v.clear();
                    v.put(MediaStore.Downloads.IS_PENDING, 0);
                    cr.update(uri, v, null, null);
                } else {
                    File dossier = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    if (!dossier.exists() && !dossier.mkdirs()) return false;
                    try (FileOutputStream out = new FileOutputStream(new File(dossier, nom))) {
                        out.write(octets);
                    }
                }
                signaler("Sauvegarde enregistrée dans Téléchargements");
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        /** Ouvre le sélecteur de fichiers ; la suite se passe dans onActivityResult. */
        @JavascriptInterface
        public void pickBackup() {
            runOnUiThread(() -> {
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                i.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"application/json", "text/plain", "*/*"});
                try {
                    startActivityForResult(i, CHOISIR_SAUVEGARDE);
                } catch (Exception e) {
                    signaler("Aucune application de fichiers disponible");
                }
            });
        }

        /** Envoie la page au service d'impression du téléphone (imprimante ou PDF). */
        @JavascriptInterface
        public void imprimer() {
            runOnUiThread(() -> {
                try {
                    PrintManager pm = (PrintManager) getSystemService(PRINT_SERVICE);
                    if (pm == null) { signaler("Impression indisponible"); return; }
                    String titre = "DRAME-OG-" + System.currentTimeMillis();
                    PrintDocumentAdapter adapter = vue.createPrintDocumentAdapter(titre);
                    pm.print(titre, adapter, new PrintAttributes.Builder().build());
                } catch (Exception e) {
                    signaler("Impression impossible");
                }
            });
        }

        /** Nom de la version, affiché dans les réglages si besoin. */
        @JavascriptInterface
        public String version() {
            return BuildConfig.VERSION_NAME;
        }
    }

    /* ------------------------------------------------------------------
     * Retour du fichier choisi
     * ------------------------------------------------------------------ */
    @Override
    protected void onActivityResult(int code, int resultat, Intent donnees) {
        super.onActivityResult(code, resultat, donnees);
        if (code != CHOISIR_SAUVEGARDE || resultat != RESULT_OK || donnees == null) return;

        Uri uri = donnees.getData();
        if (uri == null) return;

        String texte;
        try (InputStream in = getContentResolver().openInputStream(uri)) {
            if (in == null) { signaler("Fichier illisible"); return; }
            texte = new String(lire(in), StandardCharsets.UTF_8);
        } catch (Exception e) {
            signaler("Fichier illisible");
            return;
        }

        // JSONObject.quote produit une chaîne JavaScript correctement échappée.
        final String js = "window.recevoirSauvegarde(" + JSONObject.quote(texte) + ")";
        vue.post(() -> vue.evaluateJavascript(js, null));
    }

    /* ------------------------------------------------------------------
     * Bouton Retour : c'est l'application web qui décide
     * ------------------------------------------------------------------ */
    @Override
    public void onBackPressed() {
        vue.evaluateJavascript("window.androidBack && window.androidBack()", valeur -> {
            if ("true".equals(valeur)) return;          // la page a géré le retour
            long maintenant = System.currentTimeMillis();
            if (maintenant - dernierRetour < 2000) {
                finish();
            } else {
                dernierRetour = maintenant;
                signaler("Appuyez encore pour quitter");
            }
        });
    }

    /* ------------------------------------------------------------------
     * Utilitaires
     * ------------------------------------------------------------------ */
    private static byte[] lire(InputStream in) throws IOException {
        ByteArrayOutputStream tampon = new ByteArrayOutputStream();
        byte[] bloc = new byte[8192];
        int n;
        while ((n = in.read(bloc)) != -1) tampon.write(bloc, 0, n);
        return tampon.toByteArray();
    }

    private void signaler(String message) {
        runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
    }
}
