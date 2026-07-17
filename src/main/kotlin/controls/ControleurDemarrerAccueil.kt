package controls

import javafx.event.EventHandler
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import model.model
import view.SelectionJoueursPane

import view.Table.Accueil

class ControleurDemarrerAccueil (val view : Accueil,val model : model) : EventHandler<MouseEvent> {


    override fun handle(event: MouseEvent?) {

        val imgDemarre = Image("/assets/boutons/Demarrer.png", 250.0, 62.5, false, true)
        if (event?.eventType == MouseEvent.MOUSE_PRESSED) {
            val imgDemarreClic = Image("/assets/boutons/DemarrerClic.png", 250.0, 62.5, false, true)
            view.btDemarrer.graphic = ImageView(imgDemarreClic)
        }

        if(event?.eventType == MouseEvent.MOUSE_RELEASED) {
            view.btDemarrer.graphic = ImageView(imgDemarre)
            val newPan = SelectionJoueursPane()
            val controlSelection = ControleurSelectionJoueurs(newPan,model)

            val controlAjoutJoueurs = ControleurCreerJoueurs(newPan, model)
            newPan.btNouveauJoueur.onAction = controlAjoutJoueurs

            val controleurSupprimerJoueur = ControleurSupprimerJoueur(newPan, model)
            newPan.btSupprimerJoueur.onAction = controleurSupprimerJoueur

            val controlAddPoints = ControleurAugmenterPoints(newPan,model)
            newPan.btPlus.onAction = controlAddPoints

            val controlLowPoints = ControleurDiminuerPoints(newPan,model)
            newPan.btMoins.onAction = controlLowPoints

            val controlDemarrer = ControleurDemarrer(newPan, model)
            newPan.btDemarrer.onMouseReleased = controlDemarrer

            val controlRetour = ControleurRetourAccueil(newPan, model)
            newPan.btRetour.onAction = controlRetour
            view.scene.root = newPan
        }

    }
}