package controls

import model.model
import view.SelectionJoueursPane

class ControleurSelectionJoueurs(val view: SelectionJoueursPane,val model: model) {

    init {
        view.bindNbJoueurs(model.nbJoueurs)
        view.bindScore(model.scoreFinPartie)
    }
}
