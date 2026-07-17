
import controls.ControleurAccueilRegle
import controls.ControleurDemarrerAccueil
import javafx.application.Application
import javafx.scene.Scene


import javafx.stage.Stage
import model.model
import view.Table.Accueil


class Flip7SAE() : Application() {

    override fun start(primaryStage: Stage) {

        val root = Accueil()
        val model = model()
        val controleAppuyerDemarre = ControleurDemarrerAccueil(root, model)
        root.btDemarrer.onMousePressed = controleAppuyerDemarre
        root.btDemarrer.onMouseReleased = controleAppuyerDemarre
        val controleAppuyerRegle = ControleurAccueilRegle(root)
        root.btRegle.onMousePressed = controleAppuyerRegle
        root.btRegle.onMouseReleased = controleAppuyerRegle
        val scene = Scene(root, 969.0, 520.0)
        primaryStage.scene = scene
        primaryStage.show()
    }
}

fun main() {
    Application.launch(Flip7SAE::class.java)
}