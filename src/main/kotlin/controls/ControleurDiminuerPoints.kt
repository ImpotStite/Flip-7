package controls

import javafx.event.ActionEvent
import javafx.event.EventHandler
import model.model
import view.SelectionJoueursPane

class ControleurDiminuerPoints(val view : SelectionJoueursPane, val model : model) : EventHandler<ActionEvent> {

    override fun handle(event: ActionEvent) {
        this.model.scoreFinPartie.value -= 10
        if (this.model.scoreFinPartie.value == 50) this.view.btMoins.isDisable = true
        this.view.btPlus.isDisable = false
    }
}
