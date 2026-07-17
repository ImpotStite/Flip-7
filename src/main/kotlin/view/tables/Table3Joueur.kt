package view.Table

import iut.info1.flip7.IJoueur
import iut.info1.flip7.cartes.Carte
import iut.info1.flip7.cartes.CarteNum
import javafx.beans.binding.Bindings
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Polygon
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import view.tables.Basic

class Table3Joueur : Basic() {
    private val contenuMainA : HBox = HBox()
    private val contenuMainB : HBox = HBox()
    private val contenuMainC : HBox = HBox()

    val coteGauche = Line(183.0, 40.0, 33.0, 290.0)
    val coteBas = Line(50.0, 320.0, 350.0, 320.0)
    val coteDroit = Line(367.0, 283.0, 217.0, 40.0)
    val triangle = Polygon(
        200.0, 50.0,
        50.0, 300.0,
        350.0, 300.0
    )



    init {
        listeHbox = listOf(contenuMainA, contenuMainC, contenuMainB)
        listeLine = listOf(coteBas,coteDroit,coteGauche)

        oracle.font = Font.font("System", FontWeight.BOLD, 19.0)

        contenuMainA.style = "-fx-background-color: transparent;"
        contenuMainB.style = "-fx-background-color: transparent;"
        contenuMainC.style = "-fx-background-color: transparent;"

        // On centre les cartes à l'intérieur de chaque main
        contenuMainA.alignment = Pos.CENTER
        contenuMainB.alignment = Pos.CENTER
        contenuMainC.alignment = Pos.CENTER

        // Empêche les HBox de prendre tout l'écran
        contenuMainA.maxHeight = HBox.USE_PREF_SIZE
        contenuMainA.maxWidth = HBox.USE_PREF_SIZE
        contenuMainB.maxHeight = HBox.USE_PREF_SIZE
        contenuMainB.maxWidth = HBox.USE_PREF_SIZE
        contenuMainC.maxHeight = HBox.USE_PREF_SIZE
        contenuMainC.maxWidth = HBox.USE_PREF_SIZE
        contenuMainB.rotate = -59.0
        contenuMainC.rotate = 59.0

        //Espacement dynamique des cartes
        ajusterEspacement(contenuMainA, contenuMainA.children.size)
        ajusterEspacement(contenuMainB, contenuMainB.children.size)
        ajusterEspacement(contenuMainC, contenuMainC.children.size)
        val groupeTriangle = Pane().apply {
            setPrefSize(400.0, 400.0)
            setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE)
            setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE)
        }

        triangle.fill = Color.GRAY
        triangle.stroke = Color.DARKBLUE
        triangle.strokeWidth = 3.0

        groupeTriangle.children.add(triangle)

        coteGauche.stroke = Color.BLUEVIOLET
        coteGauche.strokeWidth = 10.0

        coteBas.stroke = Color.FORESTGREEN
        coteBas.strokeWidth = 10.0

        coteDroit.stroke = Color.YELLOW
        coteDroit.strokeWidth = 10.0

        groupeTriangle.children.addAll(coteGauche,coteDroit,coteBas)
        setAlignment(groupeTriangle, Pos.CENTER)
        groupeTriangle.translateXProperty().bind(this.widthProperty().divide(6.0))
        val largeurReference = 969.0
        val hauteurReference = 520.0


        val echelleLargeur = this.widthProperty().divide(largeurReference)
        val echelleHauteur = this.heightProperty().divide(hauteurReference)
        val facteurEchelle = Bindings.min(echelleLargeur, echelleHauteur)

        groupeTriangle.scaleXProperty().bind(facteurEchelle)
        groupeTriangle.scaleYProperty().bind(facteurEchelle)

        this.children.add(groupeTriangle)

        contenuMainA.layoutXProperty().bind(contenuMainA.widthProperty().divide(-2).add(200.0))
        contenuMainA.layoutY = 330.0
        contenuMainB.layoutXProperty().bind(contenuMainB.widthProperty().divide(-2).add(70.0))
        contenuMainB.layoutY = 120.0
        contenuMainC.layoutXProperty().bind(contenuMainC.widthProperty().divide(-2).add(330.0))
        contenuMainC.layoutY = 120.0

        oracle.layoutXProperty().bind(oracle.widthProperty().divide(-2).add(200.0))
        oracle.layoutY = 200.0

        groupeTriangle.children.addAll(contenuMainA, contenuMainB, contenuMainC, oracle)

    }

    override fun creerTableauScore(listejoueur: List<IJoueur>, score : Map<Int, Int>) {
        super.creerTableauScore(listejoueur,score)
        tableauScore.children[0].style = "-fx-text-fill: green;"
        tableauScore.children[1].style = "-fx-text-fill: yellow;"
        tableauScore.children[2].style = "-fx-text-fill: blue;"
    }

    override fun refreshTableauScore(tableauScore: VBox, listejoueur: List<IJoueur>, score: Map<Int, Int>) {
        super.refreshTableauScore(tableauScore, listejoueur, score)
        tableauScore.children[0].style = "-fx-text-fill: green;"
        tableauScore.children[1].style = "-fx-text-fill: yellow;"
        tableauScore.children[2].style = "-fx-text-fill: blueviolet;"
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