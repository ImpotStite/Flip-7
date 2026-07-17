# SAE 2.01 - Développement d'une application Kotlin/JavaFX


Ce document, précise les instructions d'execution du projet, renseigne nos utilisations de logiciels d'Intelligence Artificielle, et indique l'emplacement de tests logiciels propres à cette partie Developpement d'une application Kotlin/JavaFX. Enfin nous créditons les aides apportées par d'autres élèves.

## Execution du programme

Pour executer notre programme, qui est un projet Gradle, nous recommandons l'utilisation d'IntelliJ et d'utiliser une JDK21 dans la structure du projet et pour Gradle. <br>

Si vous utilisez une machine en dehors de l'IUT, le projet sera directement executable en lancant /src/main/kotlin/MainApp.kt.<br>

Si vous utilisez une machine de l'IUT, vous devez modifier comme suit les fichiers mentionnés: <br>
- Dans le fichier build.gradle.kts 

    ```repositories {
        maven {
            url = uri("http://nexus.dep-info.iut-nantes.univ-nantes.prive/repository/public/")
            isAllowInsecureProtocol = true
        }
    // mavenCentral()
    }
- Dans le fichier settings.gradle.kts

    ``` r
    repositories {
        maven {
            url = uri("http://nexus.dep-info.iut-nantes.univ-nantes.prive/repository/public/")
            isAllowInsecureProtocol = true
        }
    //     gradlePluginPortal()

    }
- Dans le fichier gradle.properties
    ```
    kotlin.code.style=official
    org.gradle.parallel=true
    org.gradle.daemon=true
    org.gradle.caching=true
    systemProp.http.proxyHost=srv-proxy-etu-2.iut-nantes.univ-nantes.prive
    systemProp.http.proxyPort=3128
    systemProp.https.proxyHost=srv-proxy-etu-2.iut-nantes.univ-nantes.prive
    systemProp.https.proxyPort=3128
    systemProp.http.nonProxyHosts=localhost|nexus.dep-info.iut-nantes.univ-nantes.prive
    systemProp.https.nonProxyHosts=localhost|nexus.dep-info.iut-nantes.univ-nantes.prive
  
Nous précisons enfin qu'il est nécéssaire d'avoir une application d'ouverture des PDF par défaut sur votre machine pour pouvoir acceder aux règles du jeu lorsque vous cliquez sur Règles dans l'écran d'accueil.

## Utilisations de l'IA 

Pour le controleur ControleurAccueilRegles ,  Gemini 3.5 nous a indiqué la classe Desktop comme utile pour ouvrir les pdf.

Gemini 3.5 nous a aussi indiqué la syntaxe suivante pour brancher un écouteur, nous l'avons beaucoup utilisée dans les contrôleurs et savons maintenant nous en servir :  

    monComposant.addListener { _, _, nouvelleLargeur -> ....}

Le même modèle d'IA nous a indiqué que les Node peuvent se superposer avec la méthode toFront(), utilisée dans src/main/view/tables/Basic pour les méthodes afficherCalqueCibleJaune() / afficherCalqueCibleBleue().

Enfin, le développement des tables dynamiques selon le nombre de joueurs s'est revelé très compliqué, et nous avons eu recours plusieurs fois à Gemini 3.5 pour qu'il propose des corrections, notemment au niveau des lignes de positionnement des mains ou des méthodes ajusterEspacement() des différentes tables.
                        
De manière générale, nous avons essayés de nous servir de l'IA comme d'une gigantesque bibliothèque et pas comme un codeur, même si nous avons ponctuellement été contraints de lui faire corriger du code.                       

## Tests associés à la troisième partie de la SAE

Vous trouverez dans /src/main/test/kotlin/TestsPartieDeveloppement une classe de tests des méthodes du modèle utilisé. Nous avons malheureusement manqué de temps pour proposer un jeu de tests plus conséquents. 


## Crédits
Les images des cartes à jouer ont été créées et partagées par Ethan CAPONE, du groupe 4. 

