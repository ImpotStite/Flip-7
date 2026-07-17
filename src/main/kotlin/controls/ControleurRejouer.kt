package controls

import javafx.event.ActionEvent
import javafx.event.EventHandler
import model.model
import view.SelectionJoueursPane
import view.VueWin

class ControleurRejouer(private val view: VueWin, private val model: model) : EventHandler<ActionEvent> {

    override fun handle(event: ActionEvent) {
        val newPan = SelectionJoueursPane()
        val newModel = model()
        ControleurSelectionJoueurs(newPan, newModel)

        val controlAjoutJoueurs = ControleurCreerJoueurs(newPan, newModel)
        newPan.btNouveauJoueur.onAction = controlAjoutJoueurs

        val controleurSupprimerJoueur = ControleurSupprimerJoueur(newPan, newModel)
        newPan.btSupprimerJoueur.onAction = controleurSupprimerJoueur

        val controlAddPoints = ControleurAugmenterPoints(newPan, newModel)
        newPan.btPlus.onAction = controlAddPoints

        val controlLowPoints = ControleurDiminuerPoints(newPan, newModel)
        newPan.btMoins.onAction = controlLowPoints

        val controlDemarrer = ControleurDemarrer(newPan, newModel)
        newPan.btDemarrer.onMousePressed = controlDemarrer
        newPan.btDemarrer.onMouseReleased = controlDemarrer

        val controlRetour = ControleurRetourAccueil(newPan, newModel)
        newPan.btRetour.onAction = controlRetour

        view.scene.root = newPan
    }
}
