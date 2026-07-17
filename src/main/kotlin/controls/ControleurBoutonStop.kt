package controls

import javafx.animation.PauseTransition
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.util.Duration
import model.model
import view.tables.Basic

class ControleurBoutonStop(private val vue : Basic, private val modele : model) : EventHandler<ActionEvent>{

    override fun handle(event: ActionEvent?) {
        val joueurCourant = modele.donneJoueurCourant()
        modele.arreterJoueur()

        val labelLigne = vue.tableauScore.children[joueurCourant] as Label
        var textScore = "${modele.joueurs[joueurCourant].donneNom()} : ${modele.flip.score[joueurCourant] ?: 0}"
        var trefle ="  🍀"
        if (modele.joueurPossedeSecondeChance(joueurCourant)) {
            textScore += trefle
        }
        vue.tableauScore.children[joueurCourant].style = "-fx-text-fill: black;"
        labelLigne.text = textScore

        if (modele.finManche()) {
            modele.donneScore()
            val mapScore = modele.flip.score
            vue.modifierOracle("${modele.joueurs[joueurCourant].donneNom()} : s'est stoppé \n FIN DE MANCHE")
            vue.refreshTableauScore( vue.tableauScore,modele.flip.joueurs, mapScore)
            modele.etatPartieDynamique.value = modele.flip.etatPartie
        } else {
            val joueurCourantApresStop = modele.donneJoueurCourant()
            vue.modifierOracle("${modele.joueurs[joueurCourant].donneNom()} : s'est stoppé \nAu tour de ${modele.joueurs[joueurCourantApresStop].donneNom()}")
            modele.etatPartieDynamique.value = modele.flip.etatPartie
            vue.actualiserMain(modele.joueurs,modele.getMains(),vue.listeHbox,vue.largeurCarte,modele.flip.joueurCourant)
        }
    }
}