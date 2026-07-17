package view.Table

import iut.info1.flip7.IJoueur
import iut.info1.flip7.cartes.Carte
import iut.info1.flip7.cartes.CarteNum
import javafx.beans.binding.Bindings
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import view.tables.Basic

class Table4Joueur : Basic() {

    private val contenuMainA : HBox = HBox()
    private val contenuMainB : HBox = HBox()
    private val contenuMainC : HBox = HBox()
    private val contenuMainD : HBox = HBox()
    val r = Rectangle(-10.0, 85.0,260.0, 260.0)
    val groupeRectangle = Group()
    val coteHaut = Line(- 0.0, 70.0, 240.0, 70.0)
    val coteBas = Line(- 0.0, 360.0, 240.0, 360.0)
    val coteGauche = Line(-25.0, 95.0, -25.0, 335.0)
    val coteDroit = Line(265.0, 95.0, 265.0, 335.0)


    init {
        listeHbox = listOf(contenuMainA, contenuMainB, contenuMainC, contenuMainD)
        listeLine = listOf(coteBas , coteDroit, coteHaut,coteGauche)

        contenuMainA.alignment = Pos.CENTER
        contenuMainB.alignment = Pos.CENTER
        contenuMainA.maxHeight = USE_PREF_SIZE
        contenuMainA.maxWidth = USE_PREF_SIZE
        contenuMainB.maxHeight = USE_PREF_SIZE
        contenuMainB.maxWidth = USE_PREF_SIZE

        contenuMainC.alignment = Pos.CENTER
        contenuMainD.alignment = Pos.CENTER
        contenuMainC.maxHeight =USE_PREF_SIZE
        contenuMainD.maxWidth = USE_PREF_SIZE
        contenuMainC.maxHeight = USE_PREF_SIZE
        contenuMainD.maxWidth = USE_PREF_SIZE

        ajusterEspacement(contenuMainA)
        ajusterEspacement(contenuMainB)
        ajusterEspacement(contenuMainC)
        ajusterEspacement(contenuMainD)

        r.fill = Color.GRAY
        r.stroke = Color.DARKBLUE
        r.strokeWidth = 3.0
        contenuMainA.layoutXProperty().bind(contenuMainA.widthProperty().divide(3).multiply(-1).add(70.0))
        contenuMainA.layoutY = 360.0

        contenuMainC.layoutXProperty().bind(contenuMainC.widthProperty().divide(3).multiply(-1).add(70.0))
        contenuMainC.layoutY = -20.0

        contenuMainD.layoutXProperty().bind(contenuMainD.widthProperty().divide(3).multiply(-1).add(-110.0))
        contenuMainD.layoutY = 180.0

        contenuMainB.layoutXProperty().bind(contenuMainB.widthProperty().divide(3).multiply(-1).add(290.0))
        contenuMainB.layoutY = 180.0


        contenuMainD.rotate = -90.0

        contenuMainB.rotate = 90.0

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

        coteHaut.stroke = Color.BLUEVIOLET
        coteHaut.strokeWidth =10.0

        coteBas.stroke = Color.FORESTGREEN
        coteBas.strokeWidth = 10.0

        coteGauche.stroke = Color.BLUE
        coteGauche.strokeWidth = 10.0

        coteDroit.stroke = Color.YELLOW
        coteDroit.strokeWidth = 10.0

        groupeRectangle.children.addAll(r, coteHaut, coteBas, coteDroit, coteGauche, oracle, contenuMainA, contenuMainB, contenuMainC, contenuMainD)

        this.children.add(groupeRectangle)

    }

    override fun creerTableauScore(listejoueur: List<IJoueur>, score : Map<Int, Int>) {
        super.creerTableauScore(listejoueur,score)
        tableauScore.children[0].style = "-fx-text-fill: green;"
        tableauScore.children[1].style = "-fx-text-fill: yellow;"
        tableauScore.children[2].style = "-fx-text-fill: blueviolet ;"
        tableauScore.children[3].style = "-fx-text-fill: blue;"
    }

    override fun refreshTableauScore(tableauScore: VBox, listejoueur: List<IJoueur>, score: Map<Int, Int>) {
        super.refreshTableauScore(tableauScore, listejoueur, score)
        tableauScore.children[0].style = "-fx-text-fill: green;"
        tableauScore.children[1].style = "-fx-text-fill: yellow;"
        tableauScore.children[2].style = "-fx-text-fill: blueviolet ;"
        tableauScore.children[3].style = "-fx-text-fill: blue;"
    }

    fun ajusterEspacement(main: HBox) {
        val nbCartes = main.children.size
        if (nbCartes > 1) {
            val espaceCalcule = (largeurMaxMain - (nbCartes * largeurCarte)) / (nbCartes - 1)
            main.spacing = minOf(-0.0, espaceCalcule)
        } else {
            main.spacing = -0.0
        }
    }
}