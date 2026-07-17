package view.Table

import controls.ControleurAccueilRegle
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.effect.ColorAdjust
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import kotlin.jvm.javaClass

class Accueil : StackPane() {

    val btDemarrer : Button
    val contenu : VBox
    val btRegle : Button

    init {
        this.style = "-fx-subscene-rendering-hint: speed; -fx-image-view-order: pixelated;"
        val imgFond = Image("/assets/tables/TableLogo.png", 969.0, 520.0, false, true)
        val imageView = ImageView(imgFond)
        imageView.fitWidthProperty().bind(this.widthProperty())
        imageView.fitHeightProperty().bind(this.heightProperty())
        imageView.isPreserveRatio = false
        this.children.add(imageView)

        contenu = VBox(20.0)
        setAlignment(contenu, Pos.BOTTOM_CENTER)
        contenu.maxHeightProperty().bind(this.heightProperty().multiply(0.45))

        btDemarrer = Button()
        val imgDemarre = Image("/assets/boutons/Demarrer.png", 250.0, 62.5, false, true)
        val imageViewDemarrer = ImageView(imgDemarre)

        btDemarrer.graphic = imageViewDemarrer
        btDemarrer.style = "-fx-background-color: transparent;-fx-cursor: hand;"

        btRegle = Button()
        val imgRegle = Image("/assets/boutons/Regle.png", 250.0, 62.5, false, true)
        val imageViewRegle = ImageView(imgRegle)
        btRegle.graphic = imageViewRegle
        btRegle.style = "-fx-background-color: transparent; -fx-cursor: hand;"

        contenu.children.add(btDemarrer)
        contenu.children.add(btRegle)
        contenu.alignment = Pos.CENTER

        this.children.add(contenu)
    }
}

