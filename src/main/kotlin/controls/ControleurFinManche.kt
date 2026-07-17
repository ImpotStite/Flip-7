package controls

import iut.info1.flip7.etats.EtatPartie
import javafx.scene.control.Alert
import javafx.scene.control.TextInputDialog
import model.model
import view.VueWin
import view.tables.Basic

class ControleurFinManche(private val view : Basic, private val model : model) {

    init {
        model.etatPartieDynamique.addListener { _, _, nouvelEtat ->
            if (nouvelEtat == EtatPartie.PARTIE_TERMINEE) {
                lancerVueWin()
                return@addListener
            } else if (nouvelEtat == EtatPartie.NOUVELLE_MANCHE) {
                var gagnant = false
                for (i in 0 until model.flip.nbJoueurs) {
                    if ((model.flip.score[i] ?: 0) >= model.scoreFinPartie.value) {
                        gagnant = true
                    }
                }
                if (gagnant) {
                    lancerVueWin()
                } else {
                    informationManche()
                    this.model.lancerNouvelleManche()
                    val joueurCourant = this.model.flip.joueurCourant
                    this.view.actualiserMain(model.joueurs,model.getMains(),view.listeHbox,view.largeurCarte,joueurCourant)
                    view.modifierOracle("Nouvelle manche ! \nAu tour de ${model.joueurs[joueurCourant].donneNom()}")
                }
            }
        }
    }

    private fun lancerVueWin() {
        informationFinDePartie()
        val vueFinale = VueWin()
        vueFinale.afficherVue(model.flip.joueurs.map { it.donneNom() }, model.flip.joueurs.indices.map { i -> (model.flip.score[i] ?: 0).toString() })
        vueFinale.btRejouer.onAction = ControleurRejouer(vueFinale, model)
        view.scene.root = vueFinale
    }

    private fun informationManche() {
        val score = model.flip.score
        val indiceMeilleurJoueur = score.maxBy{it.value}.key
        val scoreMeilleurJoueur = score.maxBy{it.value}.value
        val texte = Alert(Alert.AlertType.INFORMATION)
        texte.title = "Information partie"
        if (scoreMeilleurJoueur == 0) {
            texte.contentText = "Une nouvelle manche va se lancer ! \n Personne n'a de point, il faut se reveiller !"
        } else {
            texte.contentText = "Une nouvelle manche va se lancer ! \n Le joueur ${model.joueurs[indiceMeilleurJoueur].donneNom()} est en tête, avec ${scoreMeilleurJoueur} points !"
        }
        texte.showAndWait()
    }

    private fun informationFinDePartie() {
        val texte = Alert(Alert.AlertType.INFORMATION)
        texte.title = "Information partie"
        texte.contentText = "la partie est terminée ! Score limite atteint !"
        texte.showAndWait()
    }
}