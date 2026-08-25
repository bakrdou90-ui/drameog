# DRAME OG BU LA QUALITÉ — application Android

Gestion de boutique d'aliments bétail et volaille, avec service Orange Money / Wave.
Ventes et factures, prix négocié et remises avec marge en direct, correction et annulation
des écritures, achats, stock, dettes par tranches, caisses, rapports.

**Tout tient sur le téléphone.** L'application ne demande aucune connexion internet,
n'a aucune permission réseau, et vos données ne quittent jamais l'appareil.

---

# Par où commencer

**Tout part de GitHub.** Vous y envoyez ce dossier une fois, et GitHub fait le reste
gratuitement : il fabrique l'application Android, et il peut aussi mettre l'application
en ligne. Vous n'avez rien à installer sur votre ordinateur — ni Android Studio, ni
serveur, ni ligne de commande.

**Étape 0, à faire d'abord et une seule fois : envoyer le projet sur GitHub.**

1. Sur **github.com**, connectez-vous → bouton **New** → donnez un nom au dépôt
   (par exemple `drame-og`) → **Create repository**.
2. Sur la page qui s'affiche, cliquez le lien **uploading an existing file**.
3. Ouvrez le dossier `DRAME_OG_ANDROID` sur votre ordinateur, sélectionnez **tout ce
   qu'il y a dedans** (Ctrl+A) et faites-le glisser dans la page.
   ⚠️ C'est le **contenu** qu'on envoie, pas le dossier lui-même.
4. **Commit changes**, en bas.

Vérification immédiate : la page d'accueil du dépôt doit montrer `.github`, `android`,
`www`, `README.md` — et l'onglet **Actions** doit afficher trois workflows. Si Actions
est vide, c'est que le dossier d'enveloppe est parti avec : recommencez à l'étape 3.

**Ensuite, choisissez ce que vous voulez.** Les deux sont possibles, dans n'importe
quel ordre.

| Ce que vous voulez | Allez à | Combien de temps |
|---|---|---|
| **La vraie application Android**, installée sur le téléphone, qui marche sans internet. **C'est ce qu'il vous faut pour la boutique.** | section 2 | ~10 min |
| Une simple **adresse internet** à ouvrir dans Chrome, pour montrer l'application à quelqu'un ou l'essayer sans installer. Facultatif. | section 1 | ~3 min |

---

## 1. Une adresse internet pour l'application *(facultatif)*

Cette partie sert à une seule chose : obtenir un lien du genre
`https://votre-nom.github.io/drame-og/` que vous pouvez ouvrir sur n'importe quel
téléphone, envoyer à quelqu'un, ou utiliser en attendant d'avoir l'APK.

**Vous pouvez sauter cette section** et aller directement à la section 2. L'application
installée n'a besoin d'aucune adresse internet pour fonctionner.

Une fois le projet sur GitHub, l'adresse s'obtient en deux clics :

1. Dans votre dépôt : **Settings** → **Pages** (menu de gauche) →
   sous *Source*, choisissez **GitHub Actions** → c'est tout, rien à valider d'autre.
2. Onglet **Actions** → **Publier l'application web** → **Run workflow**.
   Une minute plus tard, l'adresse s'affiche en haut de la page du workflow.

Cette adresse se met à jour toute seule à chaque fois que vous modifiez le projet.

### L'ajouter à l'écran d'accueil du téléphone

Ouvrez l'adresse dans **Chrome** sur le téléphone, puis menu ⋮ →
**Ajouter à l'écran d'accueil** (ou la bannière « Installer l'application »).

L'icône DOQ rejoint vos autres applications, s'ouvre en plein écran sans barre d'adresse,
et continue de fonctionner quand le réseau est coupé.

> **Attention.** Utilisée ainsi, l'application range ses données dans la mémoire du
> navigateur. Vider l'historique ou changer de téléphone les efface. C'est très bien pour
> essayer ou pour montrer, mais **pour tenir la comptabilité de la boutique, prenez
> l'APK de la section 2.**

### Si vous avez déjà votre propre site internet

Vous n'êtes pas obligé de passer par GitHub Pages : copiez simplement le contenu du
dossier `www/` sur votre hébergement, dans un sous-dossier (par exemple `appli/`),
et ouvrez cette adresse-là. L'adresse doit commencer par `https://` — sans quoi Android
refuse le fonctionnement hors ligne.

---

## 2. L'application Android *(c'est celle-ci qu'il vous faut)*

### Sans rien installer : laissez GitHub la construire

Le projet est déjà sur GitHub (étape 0). Il reste deux clics.

1. **D'abord votre clé de signature** — onglet **Actions** →
   **Créer la clé de signature** → **Run workflow**. C'est l'étape qui décide si vous
   pourrez mettre à jour l'application plus tard **sans perdre vos données**. Cinq minutes
   maintenant, ou tout recommencer un jour. Le détail complet est en section 3.
2. Onglet **Actions** → **Construire l'APK** → **Run workflow**.
   Il démarre aussi tout seul à chaque modification du projet.
3. Environ trois minutes plus tard, en bas de la page du workflow, section **Artifacts** :
   téléchargez **DRAME-OG-apk**.

Vous obtenez deux fichiers :

- `DRAME-OG.apk` — **celle-ci est votre application.** Signée avec votre clé, donc les
  versions suivantes s'installeront par-dessus sans toucher à vos données.
  *(Sans clé configurée, elle s'appelle `DRAME-OG-non-signe.apk` — elle fonctionne, mais
  aucune mise à jour ne pourra s'installer par-dessus.)*
- `DRAME-OG-essai.apk` — une version d'essai qui s'installe **à côté** de la vraie sans
  l'écraser, pour montrer l'application ou tester une nouveauté. Jamais pour la comptabilité.

**Installer l'APK sur le téléphone :** copiez le fichier sur l'appareil, ouvrez-le, et
autorisez « Installer des applications inconnues » pour l'application de fichiers quand
Android le demande. C'est normal : l'application ne vient pas du Play Store.

---

### Avec Android Studio

1. **File → Open** → choisissez le dossier `android/`.
2. Laissez Gradle se synchroniser (il télécharge ce qu'il faut la première fois).
3. **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
4. L'APK apparaît dans `android/app/build/outputs/apk/`.

Pour signer avec votre clé depuis Android Studio, placez votre `drame-og.jks` où vous
voulez et lancez la construction avec les variables d'environnement `DOQ_KEYSTORE`
(chemin du fichier) et `DOQ_KEYSTORE_PASS` — ou passez par
**Build → Generate Signed Bundle / APK**, qui produit aussi le format `.aab` attendu par
le Play Store.

Pour tester en direct : branchez le téléphone en USB, activez le débogage USB, puis **Run ▶**.

---

## 3. Mises à jour et sauvegardes — à lire avant de commencer

**Vos données ne sont pas dans l'application.** Elles sont dans un fichier séparé, sur le
téléphone. Installer une nouvelle version remplace le programme, pas le fichier. Et quand
le format des données évolue, l'application convertit automatiquement l'ancien fichier à
l'ouverture. **Une mise à jour ne vous fait donc pas repartir de zéro.**

Trois choses, en revanche, effacent tout. Il faut les connaître.

### La clé de signature — le point le plus important

Android n'installe une mise à jour par-dessus une application existante que si les deux
sont signées avec **la même clé**. Signée avec une autre clé, Android refuse : il faudrait
désinstaller, et la désinstallation efface les données de la boutique.

Créez donc votre clé **une seule fois, avant la première vraie installation** :

1. Onglet **Actions** → **Créer la clé de signature** → **Run workflow**.
   Choisissez un mot de passe d'au moins 6 caractères et notez-le.
2. Téléchargez l'artefact `cle-de-signature` (il n'est gardé qu'un jour, c'est voulu).
3. **Rangez le fichier `drame-og.jks` en lieu sûr** avec son mot de passe : clé USB,
   disque externe, coffre. Perdu, il ne se remplace pas.
4. Dans le dépôt : **Settings → Secrets and variables → Actions → New repository secret**.
   Créez `KEYSTORE_BASE64` (le contenu de `cle-en-base64.txt`) et `KEYSTORE_PASSWORD`
   (votre mot de passe).
5. Relancez **Construire l'APK**. Toutes les versions produites ensuite seront des mises à
   jour de la même application.

Si vous préférez la créer sur votre ordinateur, c'est la même chose en une commande :

```bash
keytool -genkey -v -keystore drame-og.jks -keyalg RSA \
        -keysize 2048 -validity 10000 -alias drame-og
```

### La désinstallation

Désinstaller l'application supprime son fichier de données. Aucune récupération possible —
sauf par une sauvegarde.

### Le navigateur

Ouverte dans Chrome plutôt qu'installée, l'application range ses données dans la mémoire du
navigateur. Vider l'historique, changer de navigateur ou de téléphone, et elles disparaissent.
C'est très bien pour essayer, mais **pour la boutique, installez l'application**. Elle vous
le dit d'ailleurs dans ses réglages quand elle tourne dans un navigateur.

### La sauvegarde, qui règle les trois cas

**Réglages → Exporter un fichier.** Le fichier part dans le dossier **Téléchargements** ;
envoyez-le-vous par WhatsApp ou par e-mail, ou copiez-le sur une clé USB. Sur un téléphone
sans possibilité d'enregistrer un fichier, **Copier la sauvegarde** met tout dans un texte
que vous collez dans un message.

L'application compte les jours pour vous : au-delà d'une semaine sans sauvegarde, un
bandeau apparaît sur l'accueil et ne s'en va qu'une fois la sauvegarde faite. La page
Réglages affiche la date de la dernière.

Pour restaurer, sur ce téléphone ou sur un autre : **Restaurer un fichier**, ou
**Coller une sauvegarde**. Le format est identique entre l'application installée et la
version web — une sauvegarde passe de l'une à l'autre sans conversion.

En plus de cela, l'application garde en permanence une **copie de secours interne** de la
dernière version saine de vos données. Si le fichier principal devient illisible, elle
repart de cette copie toute seule au démarrage et vous le signale. Ce filet rattrape un
incident technique, pas un téléphone perdu : la sauvegarde reste indispensable.

---

## 3 bis. Si quelque chose ne marche pas sur GitHub

### L'onglet Actions ne montre aucun workflow

C'est le cas le plus fréquent, et il a toujours la même cause : **le dossier `.github`
n'est pas à la racine du dépôt.** GitHub ne cherche les workflows qu'à un seul endroit,
`/.github/workflows/`, jamais plus profond.

Si vous avez envoyé le dossier `DRAME_OG_ANDROID` **entier**, tout se retrouve un cran
trop bas. La page d'accueil de votre dépôt doit afficher ceci, et rien qui les enveloppe :

```
.github/     android/     www/     README.md     .gitignore
```

Si vous voyez à la place un unique dossier `DRAME_OG_ANDROID`, entrez dedans, et
renvoyez **son contenu** — pas lui.

### « Get Pages site failed » ou une erreur sur la publication web

Les Pages ne sont pas encore activées : **Settings → Pages → Source : GitHub Actions**,
puis relancez le workflow. Cette activation ne se fait qu'une fois.

### La construction de l'APK échoue tout de suite

Vérifiez que le fichier `android/gradlew` est bien présent dans le dépôt, ainsi que
`android/gradle/wrapper/gradle-wrapper.jar`. Certains outils d'envoi ignorent les fichiers
sans extension ou les `.jar` : ce sont eux qui manquent le plus souvent.

### Faut-il laisser un assistant IA modifier le projet ?

Non, et ce n'est pas de la méfiance de principe : le code est terminé et vérifié, il n'y a
rien à y corriger. Un assistant en mode « autopilot », lui, réécrit des fichiers sans
demander — un workflow reformaté, et la construction s'arrête de marcher.

Pour envoyer le projet sur GitHub, vous n'avez besoin d'aucun assistant ni d'aucune
commande — l'étape 0, en haut de ce document, se fait entièrement dans le navigateur.

Si vous utilisez quand même un assistant, choisissez le mode **Interactive** plutôt
qu'**Autopilot** : il vous montre chaque modification avant de la faire.

---

## 4. Comment fonctionne l'application

### Premier lancement

Trois choix vous sont proposés :

- **Démarrer ma boutique** — base vide, avec le catalogue d'aliments déjà rempli.
  C'est le choix pour un usage réel. Ajustez les prix dans **Stock**, entrez votre
  stock par la page **Achats**, et vos soldes de caisse dans **Réglages**.
- **Voir une démonstration** — 45 jours d'opérations fictives pour explorer.
- **Restaurer une sauvegarde** — reprendre un fichier venu d'un autre téléphone.

### Le sens de l'argent

Quatre caisses : **Caisse** (espèces), **Orange Money**, **Wave**, **Banque**.
Chaque opération y écrit un mouvement, consultable dans le journal de trésorerie.

Pour le mobile money, le sens compte :

| Opération | Caisse | Flotte de l'opérateur |
|---|---|---|
| Dépôt, Envoi | **+** le client remet des espèces | **−** |
| Retrait, Réception | **−** vous sortez des espèces | **+** |

La commission est toujours un gain en espèces. L'application refuse une opération
qui rendrait une caisse négative. Pour recharger la flotte, faites un transfert
depuis la page **Caisse**.

### Prix négocié, remise et marge

Le marchandage fait partie du métier, alors l'application le prend au sérieux.

Au moment d'ajouter un article, le champ **Prix négocié** part du prix catalogue mais
s'écrase librement. Juste en dessous s'affiche, en direct : le prix catalogue, votre prix
d'achat, la remise que vous êtes en train d'accorder, et **ce que la ligne vous rapporte**.
Si vous descendez sous le prix d'achat, c'est écrit en rouge avant même que l'article
entre dans le panier.

Une fois le panier constitué, **Remise sur le total** applique le geste commercial de fin
de négociation — les « je te fais 2 000 F de moins ». Le bandeau sous le panier annonce
alors le total, la remise accordée et la marge en francs et en pourcentage ; en dessous de
8 % il vire à l'orange, et une vente à perte demande une confirmation explicite avant
d'être enregistrée. Elle reste possible : déstocker un lot qui tourne mal est parfois la
bonne décision, elle doit simplement être prise en connaissance de cause.

La facture montre le sous-total, la remise et le total. Le rapport, lui, ajoute une ligne
« dont remises accordées aux clients » : à la fin du mois, vous savez ce que la
négociation vous a coûté.

### Corriger une erreur

Un chiffre mal tapé, un client qui rend la marchandise, une vente saisie deux fois : cela
arrive tous les jours, et une application qui ne le permet pas oblige à tenir un second
cahier à côté.

Ouvrez la facture depuis l'historique des ventes. Deux boutons y attendent :

- **Corriger** — pour ajuster le montant réellement encaissé et le mode de paiement.
  La caisse et la dette du client suivent automatiquement ; si le montant reste inférieur
  au total, la vente redevient une vente à crédit.
- **Annuler la vente** — l'écran énumère d'abord ce qui va se passer : le stock qui revient,
  l'argent à retirer de la caisse (donc à rendre au client), la dette qui disparaît.
  Vous pouvez noter un motif.

La facture annulée **reste dans l'historique**, barrée et marquée « Annulée » : la trace de
l'erreur est conservée, elle ne compte simplement plus dans aucun chiffre. C'est la règle
en comptabilité, et c'est aussi ce qui vous permet de vous justifier plus tard.

Le même bouton **Annuler** existe sur les achats, les opérations Orange Money / Wave, les
dépenses et les règlements de tranches. Un achat déjà revendu ne peut pas être annulé —
l'application le refuse en expliquant pourquoi, et vous renvoie vers l'ajustement de stock,
qui garde une trace de ce qui s'est réellement passé.

### Dettes et tranches

Une vente réglée en partie crée une créance sur le client ; un achat réglé en partie
crée une dette envers le fournisseur. Les règlements suivants s'imputent d'office sur
les documents les plus anciens, et le compte du tiers montre facture par facture ce
qui reste dû. Un trop-perçu devient un avoir.

### Impression des factures

Le bouton **Imprimer** passe par le service d'impression d'Android : imprimante Wi-Fi
ou Bluetooth si vous en avez une, sinon « Enregistrer au format PDF » pour envoyer la
facture par message.

---

## 5. Organisation des fichiers

```
www/                        L'application — c'est ici que tout se passe
  index.html                Toute l'application : interface, calculs, données
  manifest.webmanifest      Nom, icône et couleurs de l'application installée
  sw.js                     Fonctionnement hors ligne
  icons/                    Icônes 192 et 512 px, dont les versions « maskable »

android/                    La coque Android (Java, aucune bibliothèque externe)
  app/src/main/java/ml/drameog/gestion/MainActivity.java
                            WebView, fichier de données, sauvegardes, impression,
                            bouton Retour
  app/src/main/AndroidManifest.xml
  app/src/main/res/         Icônes de lancement, thèmes clair et sombre
  app/build.gradle          Le dossier www/ est embarqué d'ici — pas de copie à faire
  gradlew, gradle/          Wrapper Gradle : rien à installer d'autre que Java

.github/workflows/
  creer-cle.yml             Crée la clé de signature (à lancer une seule fois)
  build-apk.yml             Construit l'APK sur GitHub
  pages.yml                 Publie www/ sur GitHub Pages
```

**Un seul endroit à modifier.** Toute l'application est dans `www/index.html`.
Le projet Android embarque ce dossier au moment de la construction : après une
modification, reconstruisez l'APK et le changement y est.

---

## 6. Détails techniques

- **Android 7.0 minimum** (API 24), ciblage Android 14 (API 34).
- **Aucune dépendance externe** : ni AndroidX, ni Capacitor, ni Cordova. La coque tient
  en un fichier Java. Rien à mettre à jour, rien qui casse.
- **Aucune permission réseau.** La seule permission demandée est l'écriture des
  sauvegardes, et uniquement sur les Android antérieurs à 10.
- **Polices du système** (Roboto) : aucun téléchargement, donc aucun temps d'attente
  au démarrage et un fonctionnement identique hors ligne.
- **Thème clair et sombre** suivant le réglage du téléphone, avec un bouton pour forcer
  l'un ou l'autre.
- Les données sont écrites dans un fichier privé de l'application, par écriture
  temporaire puis renommage : une coupure de courant ne peut pas corrompre le fichier.
- Une **copie de secours** de la dernière version saine est tenue en parallèle et relue
  automatiquement si le fichier principal devient illisible.
- Le format des données porte un numéro de version ; l'ouverture convertit les anciens
  fichiers, sans intervention et sans perte.
- La version de publication est signée avec votre clé si `DOQ_KEYSTORE` et
  `DOQ_KEYSTORE_PASS` sont fournis, ce que fait GitHub à partir de vos secrets.
  La version d'essai porte un identifiant distinct (`.debug`) : elle cohabite avec la
  vraie au lieu de l'écraser.

## 7. Et l'application PHP ?

Elle reste utile là où elle est bonne : plusieurs vendeurs sur le même stock, depuis un
ordinateur de la boutique. Les deux ne se parlent pas encore. Si un jour vous voulez que
le téléphone et le serveur partagent les mêmes données, il faudra ajouter une API au
code PHP et une synchronisation dans l'application — c'est un travail à part entière,
à décider quand le besoin se présentera vraiment.
