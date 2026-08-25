# Le pont JavaScript est appelé par nom depuis la page web : ne pas le renommer.
-keepclassmembers class ml.drameog.gestion.MainActivity$Pont {
    public *;
}
-keepattributes JavascriptInterface
