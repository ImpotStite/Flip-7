package controls

import javafx.event.ActionEvent
import javafx.event.EventHandler
import model.model
import view.SelectionJoueursPane

class ControleurAugmenterPoints(val view : SelectionJoueursPane, val model : model) : EventHandler<ActionEvent> {

    override fun handle(event: ActionEvent) {
        if (this.model.scoreFinPartie.value <= 190) {
            this.model.scoreFinPartie.value += 10
        }
        if (this.model.scoreFinPartie.value == 200) {
            this.view.btPlus.isDisable = true
        }
        this.view.btMoins.isDisable = false
    }
}