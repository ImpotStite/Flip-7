package view

import javafx.geometry.Pos
import javafx.geometry.Rectangle2D
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.effect.ColorAdjust
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundImage
import javafx.scene.layout.BackgroundPosition
import javafx.scene.layout.BackgroundRepeat
import javafx.scene.layout.BackgroundSize
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane

class VueWin : Pane() {

    companion object {
        const val WINDOW_WIDTH = 1024.0
        const val WINDOW_HEIGHT = 551.0
        const val TITRE_WIDTH_RATIO = 220.0 / WINDOW_WIDTH
        const val TITRE_HEIGHT_RATIO = 0.13
        const val TITRE_TOP_RATIO = 0.06

        const val JOUEUR_WIDTH_RATIO = 240.0 / WINDOW_WIDTH
        const val JOUEUR_HEIGHT_RATIO = 0.10
        const val SCORE_HEIGHT_RATIO = 0.10
        const val SCORE_OFFSET_RATIO = 0.12

        const val COL_GAUCHE_RATIO = 0.20
        const val COL_DROITE_RATIO = 0.53
        const val ROW_HAUT_RATIO = 0.30
        const val ROW_BAS_RATIO = 0.62
        const val ROW_MILIEU_RATIO = 0.45

        const val COURONNE_WIDTH_RATIO = 70.0 / WINDOW_WIDTH

        const val REJOUER_WIDTH = 200.0
        const val REJOUER_HEIGHT = 50.0
        const val REJOUER_MARGE = 30.0
    }

    private val encocheImage = Image(checkNotNull(javaClass.getResource("/assets/divers/encocheBoisPetite.png")).toExternalForm())
    private val noeudsJoueurs = mutableListOf<Node>()

    val btRejouer: Button = creerBouton("/assets/boutons/BoutonVide.png", REJOUER_WIDTH, REJOUER_HEIGHT, Label("Rejouer").apply { style = "-fx-font-size: 20px;" })

    init {
        val url = javaClass.getResource("/assets/tables/TableSansLogo.png")
        val tableFond = Image(url.toExternalForm(), WINDOW_WIDTH, WINDOW_HEIGHT, false, true, false)

        val imageView = ImageView(tableFond)
        imageView.viewport = Rectangle2D(14.69, 14.35, 994.61, 522.30)
        imageView.fitWidth = WINDOW_WIDTH
        imageView.fitHeight = WINDOW_HEIGHT
        imageView.fitWidthProperty().bind(widthProperty())
        imageView.fitHeightProperty().bind(heightProperty())
        imageView.isPreserveRatio = false
        children.add(imageView)

        val encocheGrande = ImageView(Image(checkNotNull(javaClass.getResource("/assets/divers/encocheBoisGrande.png")).toExternalForm())).apply {
            layoutXProperty().bind(widthProperty().multiply(0.07))
            layoutYProperty().bind(heightProperty().multiply(0.15))
            fitWidthProperty().bind(widthProperty().multiply(0.86))
            fitHeightProperty().bind(heightProperty().multiply(0.80))
            isPreserveRatio = false
        }
        children.add(encocheGrande)

        val titre = Label("Récapitulatif:").apply {
            alignment = Pos.CENTER
            style = "-fx-text-fill: rgb(0, 0, 0); -fx-font-size: 18.79px; -fx-font-weight: bold;"
            background = Background(
                BackgroundImage(
                    encocheImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    BackgroundSize(1.0, 1.0, true, true, false, false)
                )
            )
        }
        titre.prefWidthProperty().bind(widthProperty().multiply(TITRE_WIDTH_RATIO))
        titre.prefHeightProperty().bind(heightProperty().multiply(TITRE_HEIGHT_RATIO))
        titre.layoutXProperty().bind(widthProperty().subtract(titre.prefWidthProperty()).divide(2.0))
        titre.layoutYProperty().bind(heightProperty().multiply(TITRE_TOP_RATIO))
        children.add(titre)

        btRejouer.layoutXProperty().bind(widthProperty().subtract(REJOUER_WIDTH).subtract(REJOUER_MARGE))
        btRejouer.layoutYProperty().bind(heightProperty().subtract(REJOUER_HEIGHT).subtract(REJOUER_MARGE))
        children.add(btRejouer)
    }

    private fun creerBouton(link: String?, largeur: Double, hauteur: Double, text: Label? = null): Button {
        val bouton = Button()
        bouton.setPrefSize(largeur, hauteur)
        val chercheLien = javaClass.getResource(link)
        bouton.style = "-fx-background-color: transparent; -fx-padding: 0;"
        val vueImage = ImageView(Image(chercheLien?.toExternalForm(), largeur, hauteur, false, true))
        if (text != null) {
            val conteneurGraphique = StackPane()
            text.isMouseTransparent = true
            text.style = (text.style ?: "") + "; -fx-font-family: 'Lucida Console'; -fx-font-weight: bold; -fx-text-fill: black;"
            conteneurGraphique.children.addAll(vueImage, text)
            bouton.graphic = conteneurGraphique
        } else {
            bouton.graphic = vueImage
        }
        val sombre = ColorAdjust()
        sombre.brightness = -0.2
        bouton.setOnMouseEntered { bouton.effect = sombre }
        bouton.setOnMouseExited { bouton.effect = null }
        return bouton
    }

    fun afficherVue(noms: List<String>, scores: List<String>) {
        val nbJoueurs = noms.size
        var joueurAvecCouronne = 0
        var max = 0
        for (i in scores.indices) {
            if (scores[i].toInt() > max) {
                max = scores[i].toInt()
                joueurAvecCouronne = i + 1
            }
        }

        children.removeAll(noeudsJoueurs)
        noeudsJoueurs.clear()

        val positions = when (nbJoueurs) {
            2 -> listOf(
                COL_GAUCHE_RATIO to ROW_MILIEU_RATIO,
                COL_DROITE_RATIO to ROW_MILIEU_RATIO
            )
            3 -> listOf(
                COL_GAUCHE_RATIO to ROW_HAUT_RATIO,
                COL_DROITE_RATIO to ROW_HAUT_RATIO,
                COL_GAUCHE_RATIO to ROW_BAS_RATIO
            )
            else -> listOf(
                COL_GAUCHE_RATIO to ROW_HAUT_RATIO,
                COL_DROITE_RATIO to ROW_HAUT_RATIO,
                COL_GAUCHE_RATIO to ROW_BAS_RATIO,
                COL_DROITE_RATIO to ROW_BAS_RATIO
            )
        }

        for (i in 0 until nbJoueurs) {
            val (leftRatio, topRatio) = positions[i]

            val encocheJoueur = Label(noms[i]).apply {
                alignment = Pos.CENTER
                style = "-fx-text-fill: rgb(0, 0, 0); -fx-font-size: 18.79px; -fx-font-weight: bold;"
                background = Background(
                    BackgroundImage(
                        encocheImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        BackgroundSize(1.0, 1.0, true, true, false, false)
                    )
                )
            }
            encocheJoueur.prefWidthProperty().bind(widthProperty().multiply(JOUEUR_WIDTH_RATIO))
            encocheJoueur.prefHeightProperty().bind(heightProperty().multiply(JOUEUR_HEIGHT_RATIO))
            encocheJoueur.layoutXProperty().bind(widthProperty().multiply(leftRatio))
            encocheJoueur.layoutYProperty().bind(heightProperty().multiply(topRatio))
            children.add(encocheJoueur)
            noeudsJoueurs.add(encocheJoueur)

            val scoreJoueur = Label(scores[i]).apply {
                alignment = Pos.CENTER
                style = "-fx-text-fill: rgb(0, 0, 0); -fx-font-size: 22px; -fx-font-weight: bold;"
            }
            scoreJoueur.prefWidthProperty().bind(encocheJoueur.prefWidthProperty())
            scoreJoueur.prefHeightProperty().bind(heightProperty().multiply(SCORE_HEIGHT_RATIO))
            scoreJoueur.layoutXProperty().bind(widthProperty().multiply(leftRatio))
            scoreJoueur.layoutYProperty().bind(heightProperty().multiply(topRatio + SCORE_OFFSET_RATIO))
            children.add(scoreJoueur)
            noeudsJoueurs.add(scoreJoueur)

            if (i + 1 == joueurAvecCouronne) {
                val couronne = ImageView(Image(checkNotNull(javaClass.getResource("/assets/divers/couronne.png")).toExternalForm())).apply {
                    fitWidthProperty().bind(widthProperty().multiply(COURONNE_WIDTH_RATIO))
                    isPreserveRatio = true
                    rotate = 20.0
                }
                couronne.layoutXProperty().bind(
                    encocheJoueur.layoutXProperty()
                        .add(encocheJoueur.prefWidthProperty())
                        .subtract(couronne.fitWidthProperty().multiply(0.65))
                )
                couronne.layoutYProperty().bind(
                    encocheJoueur.layoutYProperty().subtract(couronne.fitWidthProperty().multiply(0.45))
                )
                children.add(couronne)
                noeudsJoueurs.add(couronne)
            }
        }
    }
}
