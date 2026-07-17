package controls

import javafx.event.ActionEvent
import javafx.event.EventHandler
import model.model
import view.SelectionJoueursPane



class ControleurCreerJoueurs(val view : SelectionJoueursPane, val model : model) : EventHandler<ActionEvent> {

    override fun handle(event: ActionEvent) {
        model.ajouterJoueur()
    }
}