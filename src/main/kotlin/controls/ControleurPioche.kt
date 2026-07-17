package controls

import iut.info1.flip7.IJoueur
import iut.info1.flip7.cartes.Carte
import iut.info1.flip7.etats.EtatJoueur
import iut.info1.flip7.etats.EtatPartie
import javafx.animation.PauseTransition
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.util.Duration
import model.model
import view.tables.Basic

class ControleurPioche(vue : Basic, modele : model) : EventHandler<ActionEvent>{

    private val vue = vue
    private val modele = modele

    override fun handle(event: ActionEvent?) {
        val joueurCourant = modele.donneJoueurCourant()
        modele.clicSurPioche()
        val labelLigne = vue.tableauScore.children[joueurCourant] as Label
        if (modele.flip.etatJoueur[joueurCourant] !=EtatJoueur.JOUE_ENCORE  ) {
            vue.tableauScore.children[joueurCourant].style = "-fx-text-fill: black;"
            var textScore = "${modele.joueurs[joueurCourant].donneNom()} : ${modele.flip.score[joueurCourant] ?: 0}"
            textScore += "  PERDU"
            labelLigne.text = textScore

        } else if (modele.joueurPossedeSecondeChance(joueurCourant)) {
            val trefle ="  🍀"
            val labelLigne = vue.tableauScore.children[joueurCourant] as Label
            var textScore = "${modele.joueurs[joueurCourant].donneNom()} : ${modele.flip.score[joueurCourant] ?: 0}"
            textScore += trefle
            labelLigne.text = textScore
        }
        vue.actualiserMain(modele.joueurs,modele.getMains(),vue.listeHbox,vue.largeurCarte,joueurCourant)


        if (modele.finManche()) {
            modele.donneScore()
            val mapScore = modele.flip.score
            val main:  Map<IJoueur, List<Carte>?> = modele.getMains()
            if (modele.estFlip(main[modele.joueurs[joueurCourant]])) {
                vue.modifierOracle("  ${modele.joueurs[joueurCourant ].donneNom()} a \n eu un FLIP7 !")
                vue.refreshTableauScore( vue.tableauScore,modele.flip.joueurs, mapScore)
                modele.etatPartieDynamique.value = modele.flip.etatPartie
            } else {
                vue.modifierOracle("  ${modele.joueurs[joueurCourant ].donneNom()} a perdu \n : Tout le monde a perdu \n la manche s'arrête")
                vue.refreshTableauScore( vue.tableauScore,modele.flip.joueurs, mapScore)
                modele.etatPartieDynamique.value = modele.flip.etatPartie
            }
        } else {
            if (!modele.enAttenteCible()) {
                val joueurCourantApresPioche = modele.donneJoueurCourant()
                vue.modifierOracle("${modele.joueurs[joueurCourant].donneNom()} : a pioché \nAu tour de ${modele.joueurs[joueurCourantApresPioche].donneNom()}")
                modele.etatPartieDynamique.value = modele.flip.etatPartie
            }
        }
    }
}