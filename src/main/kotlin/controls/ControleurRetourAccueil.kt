package controls

import javafx.event.ActionEvent
import javafx.event.EventHandler
import model.model
import view.SelectionJoueursPane
import view.Table.Accueil

class ControleurRetourAccueil(private val view : SelectionJoueursPane, private val model : model) : EventHandler<ActionEvent>{

    override fun handle(event: ActionEvent) {
        val newPan = Accueil()
        val controleAppuyerDemarre = ControleurDemarrerAccueil(newPan,model)
        newPan.btDemarrer.onMousePressed = controleAppuyerDemarre
        newPan.btDemarrer.onMouseReleased = controleAppuyerDemarre
        val controleAppuyerRegle = ControleurAccueilRegle(newPan)
        newPan.btRegle.onMousePressed = controleAppuyerRegle
        newPan.btRegle.onMouseReleased = controleAppuyerRegle
        view.scene.root = newPan
    }

}