package view



import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Slider
import javafx.scene.control.TextField
import javafx.scene.effect.ColorAdjust
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import model.model
import kotlin.jvm.javaClass

class SelectionJoueursPane() : StackPane() {

    val listeJoueurs = mutableListOf<StackPane>()
    var btPlus: Button
    var btMoins : Button
    val retour : Pane
    val btRetour : Button
    val contenu : VBox
    val hb1 : HBox
    val hb2 : HBox
    val hb3 : HBox
    val miniBox : BorderPane
    val btNouveauJoueur : Button
    val score : Label
    val bts : HBox
    val btSupprimerJoueur : Button
    val btDemarrer : Button

    init {
        this.style = "-fx-subscene-rendering-hint: speed; -fx-image-view-order: pixelated;"
        val fondJeu = javaClass.getResource("/assets/tables/TableLogo.png")
        val imgFond = Image(fondJeu.toExternalForm(), 969.0, 520.0, false, true)
        val imageView = ImageView(imgFond)
        imageView.fitWidthProperty().bind(this.widthProperty())
        imageView.fitHeightProperty().bind(this.heightProperty())
        imageView.isPreserveRatio = false
        this.children.add(imageView)

        retour = Pane()
        btRetour = creerBouton("/assets/divers/EncocheCarre.png", 65.0,65.0,Label("<").apply{style="-fx-font-size: 26px; -fx-opacity : 0.5; -fx-font-weight: bold;"})
            retour.children.add(btRetour)
        this.children.add(retour)
        setAlignment(retour, Pos.TOP_LEFT)
        setMargin(retour, Insets(45.0))

        contenu = VBox(20.0)
            setAlignment(contenu, Pos.BOTTOM_CENTER)
            contenu.maxHeightProperty().bind(this.heightProperty().multiply(0.45))
            contenu.alignment = Pos.CENTER

            hb1 = HBox(20.0)
                hb1.alignment = Pos.CENTER
                for (i in 0 until 4) {
                    listeJoueurs.add(creerPlaqueSaisie("/assets/boutons/BoutonVide.png",150.0,50.0, TextField("")))
                }
                refreshJoueurs(listeJoueurs.size)

            hb2 = HBox(40.0)
                btNouveauJoueur = creerBouton("/assets/boutons/BoutonVide.png", 200.0, 50.0,Label("Créer Joueur").apply{style="-fx-font-size: 20px;"})
                miniBox = BorderPane()
                    score = Label().apply{style="-fx-font-weight: bold; -fx-font-family: 'Lucida Console'; -fx-alignment: center;"}
                    bts = HBox(5.0)
                        btPlus = creerBouton("/assets/divers/EncocheCarre.png",30.0,30.0,Label("+"))
                        btMoins = creerBouton("/assets/divers/EncocheCarre.png",30.0,30.0,Label("-"))
                        bts.children.addAll(btPlus,btMoins)
                        bts.alignment = Pos.CENTER
                    miniBox.center = score
                    miniBox.bottom = bts


                btSupprimerJoueur = creerBouton("/assets/boutons/BoutonVide.png", 200.0, 50.0,Label("Supprimer Joueur").apply{style="-fx-font-size: 17.5px;"})
                hb2.children.addAll(btNouveauJoueur,miniBox,btSupprimerJoueur)
                hb2.alignment = Pos.CENTER

            hb3 = HBox()
                btDemarrer = creerBouton("/assets/boutons/BoutonVide.png", 250.0, 62.5,Label("Démarrer").apply{style="-fx-font-size: 28px;"})
                hb3.children.add(btDemarrer)
                hb3.alignment = Pos.CENTER

            contenu.children.addAll(hb1,hb2,hb3)

        this.children.add(contenu)


    }


    private fun creerBouton(link: String? = null, largeur: Double, hauteur: Double, text : Label? = null): Button {
        val bouton = Button()
        bouton.setPrefSize(largeur, hauteur)
        val chercheLien = javaClass.getResource(link)
        bouton.style = "-fx-background-color: transparent; -fx-padding: 0;"
        val view = ImageView(Image(chercheLien?.toExternalForm(), largeur, hauteur, false, true))
        if (text != null) {
            val conteneurGraphique = StackPane()
            text.isMouseTransparent = true
            text.style = (text.style?: "") + "; -fx-font-family: 'Lucida Console'; -fx-font-weight: bold; -fx-text-fill: black;"
            conteneurGraphique.children.addAll(view, text)
            bouton.graphic = conteneurGraphique
        } else {
            bouton.graphic = view

        }
        val sombre = ColorAdjust()
        sombre.brightness = -0.2

        if (text?.text == "Créer Joueur" || text?.text == "Supprimer Joueur" || text?.text == "Démarrer" || text?.text == "<") {
            bouton.setOnMouseEntered {
                bouton.effect = sombre
            }
            bouton.setOnMouseExited {
                bouton.effect = null
            }
        }
        return bouton
    }

    private fun creerPlaqueSaisie(link: String, largeur: Double, hauteur: Double, champTexte: TextField): StackPane {
        val conteneur = StackPane()
        conteneur.setPrefSize(largeur, hauteur)
        val chercheLien = javaClass.getResource(link)
        val view = ImageView(Image(chercheLien?.toExternalForm(), largeur, hauteur, false, true))
        champTexte.style = (champTexte.style ?: "") + "-fx-background-color: transparent;-fx-border-color: transparent;-fx-text-fill: black;-fx-prompt-text-fill: #555555;-fx-font-family: 'Lucida Console';-fx-font-weight: bold;-fx-alignment: center;"
        champTexte.style += "-fx-focus-color: transparent; -fx-faint-focus-color: transparent;"
        champTexte.setPrefSize(largeur, hauteur)
        conteneur.children.addAll(view, champTexte)
        return conteneur
    }

    private fun refreshJoueurs(nb : Int) {
        hb1.children.clear()
        for (i in 0 until nb) {
            hb1.children.add(listeJoueurs[i])
        }
    }

    fun bindNbJoueurs(nbJoueurs : SimpleIntegerProperty) {
        refreshJoueurs(nbJoueurs.value)
        nbJoueurs.addListener { _, _, newValue -> refreshJoueurs(newValue.toInt()) }
    }

    fun bindScore(score : SimpleIntegerProperty) {
        this.score.textProperty().bind(score.asString("Points MAX : %d"))
    }




}







