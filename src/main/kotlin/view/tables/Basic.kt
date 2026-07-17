package view.tables

import iut.info1.flip7.IJoueur
import javafx.animation.TranslateTransition
import iut.info1.flip7.cartes.Carte
import javafx.beans.binding.Bindings
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.util.Duration


open class Basic() : StackPane() {

    private val gridPane = GridPane()
    private val panneauGauche: Pane = Pane()
    private val panneauDroit: Pane = Pane()

    val boutonStop: Button = Button()
    val boutonPioche: Button = Button()
    val boutonDefausse: Button = Button("Defausse")

    private val gridScore: GridPane = GridPane()
    private val gridPiocheStop = GridPane()
    private val gridDefausse = GridPane()
    private val gridPaneHaut = GridPane()
    private val gridTitreScore = GridPane()
    private val gridTableauScore = GridPane()

    val tableauScore = VBox()
    private val borderScore = BorderPane()
    val titreScore = Label("Score")

    private val borderPanePioche = BorderPane()
    private val borderPaneStop = BorderPane()
    private val borderPaneDefausse = BorderPane()
    private val borderTableauScore = BorderPane()

    lateinit var listeHbox: List<HBox>
    var largeurCarte: Double = 30.0
    protected val oracle = Label("Debut de la partie !")
    val largeurMaxMain = 600.0
    lateinit var listeLine: List<Line>

    private val calqueJaune = StackPane()
    private val calqueBleu = StackPane()
    private val boutonsCibleJaune = HBox(25.0)
    private val boutonsCibleBleu = HBox(25.0)
    private val imageCalqueJaune = ImageView()
    private val imageCalqueBleu = ImageView()

    val titreCalqueJaune = Label(", qui piochera 3 cartes ?")
    val titreCalqueBleu = Label(", qui voulez-vous stopper ?")

    init {

        val colA = ColumnConstraints()
        colA.setPercentWidth(35.0)
        val colB = ColumnConstraints()
        colB.setPercentWidth(65.0)
        gridPane.getColumnConstraints().addAll(colA, colB)
        oracle.font = Font.font("System", FontWeight.BOLD, 19.0)


        val urlScore = javaClass.getResource("/assets/tables/TableEtScore.png")
            ?: error("Image de fond introuvable dans resources")

        val scoreImage = Image(urlScore.toExternalForm(), 600.0, 200.0, true, true, false)
        val backgroundImageScore = BackgroundImage(
            scoreImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            BackgroundSize(100.0, 100.0, true, true, true, false)
        )

        //Score Haut
        val roZ = RowConstraints()
        roZ.setPercentHeight(40.0)
        val roW = RowConstraints()
        roW.setPercentHeight(60.0)
        gridPaneHaut.rowConstraints.addAll(roZ, roW)
        gridTitreScore.rowConstraints.addAll(roZ, roW)


        val colZ = ColumnConstraints()
        colZ.setPercentWidth(20.0)
        val colW = ColumnConstraints()
        colW.setPercentWidth(80.0)
        gridTitreScore.getColumnConstraints().addAll(colZ, colW)

        val colT = ColumnConstraints()
        colT.setPercentWidth(35.0)
        val colU = ColumnConstraints()
        colU.setPercentWidth(65.0)
        gridTableauScore.getColumnConstraints().addAll(colT, colU)

        val colAc = ColumnConstraints()
        colAc.setPercentWidth(100.0)
        gridPaneHaut.getColumnConstraints().addAll(colAc)

        titreScore.font = Font.font(24.0)
        titreScore.font = Font.font("System", FontWeight.BOLD, 24.0)

        borderScore.center = titreScore
        borderTableauScore.center = tableauScore

        gridTitreScore.add(borderScore, 1, 1)
        gridPaneHaut.add(gridTitreScore, 0, 0)

        gridTableauScore.add(borderTableauScore, 1, 0)
        gridPaneHaut.add(gridTableauScore, 0, 1)

        //Grid ScoreBas
        val col1 = ColumnConstraints()
        col1.setPercentWidth(30.0)
        val col2 = ColumnConstraints()
        col2.setPercentWidth(25.0)
        val col3 = ColumnConstraints()
        col3.setPercentWidth(10.0)
        val col4 = ColumnConstraints()
        col4.setPercentWidth(25.0)

        gridPiocheStop.getColumnConstraints().addAll(col1, col2, col3, col4)

        val ro1 = RowConstraints()
        ro1.setPercentHeight(100.0)
        val ro2 = RowConstraints()
        ro2.setPercentHeight(50.0)
        val ro3 = RowConstraints()
        ro3.setPercentHeight(50.0)

        gridDefausse.rowConstraints.addAll(ro2, ro3)
        gridPiocheStop.rowConstraints.addAll(ro1)

        val colD = ColumnConstraints()
        colD.setPercentWidth(20.0)
        val colE = ColumnConstraints()
        colE.setPercentWidth(80.0)
        gridDefausse.getColumnConstraints().addAll(colD, colE)

        // Grid Score
        val roA = RowConstraints()
        roA.setPercentHeight(50.0)
        val roB = RowConstraints()
        roB.setPercentHeight(20.0)
        val roC = RowConstraints()
        roC.setPercentHeight(30.0)
        gridScore.rowConstraints.addAll(roA, roB, roC)

        val colAb = ColumnConstraints()
        colAb.setPercentWidth(100.0)
        gridScore.getColumnConstraints().addAll(colAb)

        //Ajoute child à Grid Score
        panneauGauche.children.add(gridScore)
        gridScore.add(gridPiocheStop, 0, 1)
        gridScore.add(gridDefausse, 0, 2)
        gridScore.add(gridPaneHaut, 0, 0)
        panneauGauche.prefWidthProperty().bind(this.widthProperty())
        panneauGauche.prefHeightProperty().bind(this.heightProperty())
        panneauGauche.maxWidthProperty().bind(this.widthProperty())
        panneauGauche.maxHeightProperty().bind(this.heightProperty())

        gridScore.prefWidthProperty().bind(panneauGauche.widthProperty())
        gridScore.prefHeightProperty().bind(this.heightProperty())
        gridScore.maxWidthProperty().bind(panneauGauche.widthProperty())
        gridScore.maxHeightProperty().bind(this.heightProperty())
        gridScore.minWidthProperty().bind(panneauGauche.widthProperty())
        gridScore.minHeightProperty().bind(this.heightProperty())

        val urlFond = javaClass.getResource("/assets/tables/TableEtScore.png")
            ?: error("Image de fond introuvable dans resources")
        this.style = "-fx-subscene-rendering-hint: speed; -fx-image-view-order: pixelated;"
        val imgFond = Image(urlFond.toExternalForm(), 969.0, 520.0, false, true)
        val imageView = ImageView(imgFond)
        imageView.fitWidthProperty().bind(this.widthProperty())
        imageView.fitHeightProperty().bind(this.heightProperty())
        imageView.isPreserveRatio = false
        this.children.add(imageView)

        // Bouton STOP
        boutonStop.styleClass.add("bouton-stop")
        val truc = javaClass.getResource("/assets/boutons/Stop.png").toExternalForm()
        boutonStop.style =
            "-fx-background-image: url(" + truc + ");-fx-background-size: stretch;-fx-background-color: transparent;-fx-cursor: hand;"


        // Pioche
        boutonPioche.styleClass.add("bouton-pioche")
        val trucPioche = javaClass.getResource("/assets/cartes/carteDos.png").toExternalForm()
        boutonPioche.style =
            "-fx-background-image: url(" + trucPioche + ");-fx-background-size: stretch;-fx-background-color: transparent;-fx-cursor: hand;"


        // Défausse
        boutonDefausse.styleClass.add("bouton-pioche")
        val trucDefausse = javaClass.getResource("/assets/cartes/carteDos.png").toExternalForm()
        boutonDefausse.style = "-fx-background-image: url(" + trucDefausse + ");-fx-background-size: stretch;-fx-background-color: transparent;-fx-cursor: hand;"
        boutonDefausse.rotate = 90.0

        boutonPioche.maxWidth = Double.MAX_VALUE
        boutonPioche.maxHeight = Double.MAX_VALUE
        boutonDefausse.maxHeight = Double.MAX_VALUE
        boutonStop.maxHeight = Double.MAX_VALUE

        borderPanePioche.center = boutonPioche
        borderPaneStop.center = boutonStop
        borderPaneDefausse.center = boutonDefausse

        gridPiocheStop.add(borderPaneStop, 1, 0)
        gridPiocheStop.add(borderPanePioche, 3, 0)

        gridDefausse.add(borderPaneDefausse, 1, 0)

        val linkCalqueJaune = javaClass.getResource("/assets/tables/Table3ALaSuite.png").toExternalForm()
        imageCalqueJaune.image = Image(linkCalqueJaune)
        imageCalqueJaune.opacity = 0.7
        imageCalqueJaune.fitWidthProperty().bind(this.widthProperty())
        imageCalqueJaune.fitHeightProperty().bind(this.heightProperty())
        imageCalqueJaune.isPreserveRatio = false
        titreCalqueJaune.style =
            "-fx-text-fill: white; -fx-font-size: 26px; -fx-font-family: 'Lucida Console'; -fx-font-weight: bold;-fx-background-color: black"
        boutonsCibleJaune.alignment = Pos.CENTER
        val organisationVerticale1 = VBox(50.0, titreCalqueJaune, boutonsCibleJaune)
        organisationVerticale1.alignment = Pos.TOP_CENTER
        organisationVerticale1.padding = Insets(30.0,0.0,0.0,0.0)
        calqueJaune.children.addAll(imageCalqueJaune, organisationVerticale1)
        calqueJaune.isVisible = false

        this.children.add(calqueJaune)

        val linkCalqueBleu = javaClass.getResource("/assets/tables/TableFreeze.png").toExternalForm()
        imageCalqueBleu.image = Image(linkCalqueBleu)
        imageCalqueBleu.opacity = 0.7
        imageCalqueBleu.fitWidthProperty().bind(this.widthProperty())
        imageCalqueBleu.fitHeightProperty().bind(this.heightProperty())
        imageCalqueBleu.isPreserveRatio = false
        titreCalqueBleu.style =
            "-fx-text-fill: white; -fx-font-size: 26px; -fx-font-family: 'Lucida Console'; -fx-font-weight: bold;-fx-background-color: black"
        boutonsCibleBleu.alignment = Pos.CENTER
        val organisationVerticale2 = VBox(50.0, titreCalqueBleu, boutonsCibleBleu)
        organisationVerticale2.padding = Insets(30.0, 0.0, 0.0, 0.0)
        organisationVerticale2.alignment = Pos.TOP_CENTER
        calqueBleu.children.addAll(imageCalqueBleu, organisationVerticale2)
        calqueBleu.isVisible = false

        this.children.add(calqueBleu)

        //Responsive
        boutonStop.maxHeightProperty().bind(boutonStop.widthProperty())
        val bindings = Bindings.createDoubleBinding({
            minOf(borderPaneStop.width, borderPaneStop.height)
        }, borderPaneStop.widthProperty(), borderPaneStop.heightProperty())
        boutonStop.maxWidthProperty().bind(bindings)

        boutonDefausse.maxHeightProperty().bind(boutonDefausse.widthProperty())
        val bindingsDefausse = Bindings.createDoubleBinding({
            minOf(borderPaneDefausse.width, borderPaneDefausse.height)
        }, borderPaneDefausse.widthProperty(), borderPaneDefausse.heightProperty())
        boutonDefausse.maxWidthProperty().bind(bindingsDefausse)

        titreScore.maxHeightProperty().bind(titreScore.widthProperty())
        val bindingsTitreScore = Bindings.createDoubleBinding({
            minOf(borderScore.width, borderScore.height)
        }, borderScore.widthProperty(), borderScore.heightProperty())
        titreScore.maxWidthProperty().bind(bindingsTitreScore)


        //Idée de Gemini pour le Listener, je ne savais pas comment faire autrement
        borderScore.widthProperty().addListener { _, _, nouvelleLargeur ->
            val nouvelleTaille = nouvelleLargeur.toDouble() * 0.08
            titreScore.font = Font.font(titreScore.font.family, FontWeight.BOLD, nouvelleTaille)
        }
        gridPane.add(panneauGauche, 0, 0)
        gridPane.add(panneauDroit, 1, 0)

        gridPiocheStop.isGridLinesVisible = false
        gridScore.isGridLinesVisible = false
        gridDefausse.isGridLinesVisible = false
        gridPaneHaut.isGridLinesVisible = false
        gridTitreScore.isGridLinesVisible = false

        this.children.addAll(gridPane)

    }


    fun modifierOracle(s: String) {
        this.oracle.text = s
    }


    fun fixeControleurBouton(bouton: Button, action: EventHandler<ActionEvent>) {
        bouton.onAction = action
    }

    fun afficherCalqueCibleJaune(boutons: List<Button>) {
        boutonsCibleJaune.children.clear()
        boutonsCibleJaune.children.addAll(boutons)

        calqueJaune.toFront()
        calqueJaune.isVisible = true
        calqueBleu.isVisible = false
        this.boutonStop.isDisable = true
        this.boutonPioche.isDisable = true
    }

    fun masquerCalqueCibleJaune() {
        boutonsCibleJaune.children.clear()
        calqueJaune.isVisible = false
        this.boutonStop.isDisable = false
        this.boutonPioche.isDisable = false
    }

    fun afficherCalqueCibleBleu(boutons: List<Button>) {
        boutonsCibleBleu.children.clear()
        boutonsCibleBleu.children.addAll(boutons)
        calqueBleu.toFront()
        calqueBleu.isVisible = true
        calqueJaune.isVisible = false
        this.boutonStop.isDisable = true
        this.boutonPioche.isDisable = true
    }

    fun masquerCalqueCibleBleu() {
        boutonsCibleBleu.children.clear()
        calqueBleu.isVisible = false
        this.boutonStop.isDisable = false
        this.boutonPioche.isDisable = false
    }

    open fun creerTableauScore(listejoueur: List<IJoueur>, score: Map<Int, Int>) {
        for (i in 0 until listejoueur.size) {
            val nomJoueur = listejoueur[i].donneNom()
            val scoreJoueur = score[i] ?: 0
            val texte = Label("$nomJoueur : $scoreJoueur")
            texte.font = Font.font("System", FontWeight.BOLD, 19.0)

            tableauScore.children.add(texte)
        }
    }

    open fun refreshTableauScore(tableauScore : VBox, listejoueur: List<IJoueur>, score: Map<Int, Int>) {
        for (i in 0 until listejoueur.size) {
            val nomJoueur = listejoueur[i].donneNom()
            val scoreJoueur = score[i] ?: 0
            val texte = Label("$nomJoueur : $scoreJoueur")
            texte.font = Font.font("System", FontWeight.BOLD, 19.0)

            tableauScore.children[i] = texte
        }
    }

    fun actualiserMain(listeJoueurs: List<IJoueur>,listeMain: Map<IJoueur, List<Carte>?>,listeHbox: List<HBox>,largeurCarte: Double,courant: Int){
            val nbJoueurs = listeJoueurs.size
            for (indexJoueur in 0 until nbJoueurs) {
                for (indexJoueur in 0 until nbJoueurs) {
                    val joueur = listeJoueurs[indexJoueur]
                    val indexHbox = (indexJoueur - courant + nbJoueurs) % nbJoueurs
                    val hboxDuJoueur = listeHbox[indexHbox]

                    hboxDuJoueur.children.clear()
                    val cartesDuJoueur = listeMain[joueur]
                    if (cartesDuJoueur != null) {
                        for (carte in cartesDuJoueur) {
                            val imageCarte = Image(
                                "/assets/cartes/${carte}.png",
                                largeurCarte,
                                largeurCarte * (16.0 / 9.0),
                                true,
                                true,
                                false
                            )
                            val imageViewCarte = ImageView(imageCarte)
                            hboxDuJoueur.children.add(imageViewCarte)
                        }
                    }
                }
            }
            changerBarre(courant,this.listeLine)
    }

    fun changerBarre(courant:Int, listeLine:List<Line>){
        val listCouleurs = listOf(Color.FORESTGREEN, Color.YELLOW, Color.BLUEVIOLET,Color.BLUE)
        for (i in 0 until listeLine.size){
            if (this.tableauScore.children[(courant+i)%listeLine.size].style.contains("black")){
                listeLine[i].stroke = Color.BLACK
            } else {
                listeLine[i].stroke = listCouleurs[(courant + i) % listeLine.size]
            }
        }
    }
}