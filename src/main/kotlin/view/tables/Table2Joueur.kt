package view.Table

import iut.info1.flip7.IJoueur
import iut.info1.flip7.cartes.Carte
import iut.info1.flip7.cartes.CarteNum
import javafx.beans.binding.Bindings
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import view.tables.Basic

class Table2Joueur : Basic() {

    private val contenuMainA : HBox = HBox()
    private val contenuMainB : HBox = HBox()
    val r = Rectangle(-70.0, 150.0,400.0, 100.0)
    val groupeRectangle = Group()
    val coteHaut = Line(- 60.0, 130.0, 320.0, 130.0)
    val coteBas = Line(- 60.0, 270.0, 320.0, 270.0)

    init {
        listeHbox = listOf(contenuMainA,contenuMainB)
        listeLine = listOf(coteBas,coteHaut)

        oracle.font = Font.font("System", FontWeight.BOLD, 19.0)
        contenuMainA.alignment = Pos.CENTER
        contenuMainB.alignment = Pos.CENTER

        contenuMainA.maxHeight = USE_PREF_SIZE
        contenuMainA.maxWidth = USE_PREF_SIZE

        contenuMainB.maxHeight = USE_PREF_SIZE
        contenuMainB.maxWidth = USE_PREF_SIZE

        ajusterEspacement(contenuMainA, contenuMainA.children.size)
        ajusterEspacement(contenuMainB, contenuMainB.children.size)

        r.fill = Color.GRAY
        r.stroke = Color.DARKBLUE
        r.strokeWidth = 3.0

        contenuMainA.layoutXProperty().bind(r.xProperty().add(r.widthProperty().divide(2.0)).subtract(contenuMainA.widthProperty().divide(2.0)))
        contenuMainA.layoutY = 280.0

        contenuMainB.layoutXProperty().bind(r.xProperty().add(r.widthProperty().divide(2.0)).subtract(contenuMainB.widthProperty().divide(2.0)))
        contenuMainB.layoutY = 50.0

        oracle.layoutXProperty().bind(oracle.widthProperty().divide(2).multiply(-1).add(120.0))
        oracle.layoutY = 182.0

        setAlignment(groupeRectangle, Pos.CENTER)

        // Ratio Dynamique
        groupeRectangle.translateXProperty().bind(this.widthProperty().divide(6.0))
        val largeurReference = 969.0
        val hauteurReference = 520.0

        val echelleLargeur = this.widthProperty().divide(largeurReference)
        val echelleHauteur = this.heightProperty().divide(hauteurReference)
        val facteurEchelle = Bindings.min(echelleLargeur, echelleHauteur)

        groupeRectangle.scaleXProperty().bind(facteurEchelle)
        groupeRectangle.scaleYProperty().bind(facteurEchelle)

        coteHaut.stroke = Color.YELLOW
        coteHaut.strokeWidth = 10.0

        coteBas.stroke = Color.FORESTGREEN
        coteBas.strokeWidth = 10.0

        groupeRectangle.children.addAll(r, contenuMainA, contenuMainB, oracle, coteHaut, coteBas)
        this.children.add(groupeRectangle)

    }

    override fun creerTableauScore(listejoueur: List<IJoueur>, score : Map<Int, Int>) {
        super.creerTableauScore(listejoueur,score)
        tableauScore.children[0].style = "-fx-text-fill: green;"
        tableauScore.children[1].style = "-fx-text-fill: yellow;"
    }

    override fun refreshTableauScore(tableauScore: VBox, listejoueur: List<IJoueur>, score: Map<Int, Int>) {
        super.refreshTableauScore(tableauScore, listejoueur, score)
        tableauScore.children[0].style = "-fx-text-fill: green;"
        tableauScore.children[1].style = "-fx-text-fill: yellow;"
    }

    fun ajusterEspacement(main: HBox, nbCartes: Int) {
        if (nbCartes > 1) {
            val espaceCalcule = (largeurMaxMain - (nbCartes * largeurCarte)) / (nbCartes - 1)
            main.spacing = minOf(-0.0, espaceCalcule)
        } else {
            main.spacing = -0.0
        }
    }



}