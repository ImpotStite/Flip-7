package controls


import iut.info1.flip7.etats.EtatJoueur
import iut.info1.flip7.etats.EtatPartie
import javafx.scene.control.Button
import javafx.scene.control.Label
import model.model
import view.tables.Basic

class ControleurCibleStop(private val view : Basic, private val model : model) {

    init {
        model.etatPartieDynamique.addListener { _,_,nouvelEtat ->
            if (nouvelEtat == EtatPartie.ATTENTE_CIBLE_STOP) {
                val joueurCarteStop = model.donneJoueurCourant()
                val boutonsChoix = mutableListOf<Button>()
                for (i in 0 until model.nbJoueurs.value) {
                    if (model.flip.etatJoueur[i] == EtatJoueur.JOUE_ENCORE) {
                        val boutonJoueur = Button(model.joueurs[i].donneNom())
                        boutonJoueur.style = "-fx-font-family: 'Lucida Console'; -fx-font-weight: bold;-fx-background-color: lightblue; -fx-background-radius: 20px; -fx-text-fill: black;-fx-font-size: 25px;"
                        boutonJoueur.setOnAction {
                            this.model.validerCibleStop(i)
                            view.tableauScore.children[i].style = "-fx-text-fill: black;"
                            val joueurCourant = model.donneJoueurCourant()
                            if (model.finManche()) {
                                model.donneScore()
                                val mapScore = model.flip.score

                                view.modifierOracle("  ${model.joueurs[i].donneNom()} est stoppé \n la manche s'arrête")
                                view.refreshTableauScore(view.tableauScore,model.flip.joueurs, mapScore)
                                model.etatPartieDynamique.value = model.flip.etatPartie

                            } else {
                                view.modifierOracle("${model.joueurs[joueurCarteStop].donneNom()} : a stoppé \n ${model.joueurs[i].donneNom()} \nAu tour de ${model.joueurs[joueurCourant].donneNom()}")
                                model.etatPartieDynamique.value = model.flip.etatPartie
                            }
                        }
                        this.view.actualiserMain(model.joueurs,model.getMains(),view.listeHbox,view.largeurCarte,model.flip.joueurCourant)
                        boutonsChoix.add(boutonJoueur)
                    }
                }
                view.titreCalqueBleu.text = ", qui voulez-vous stopper ?"
                val texte = "${model.joueurs[joueurCarteStop].donneNom()}"
                val texteTitre = view.titreCalqueBleu.text
                val fusion = texte + texteTitre
                view.titreCalqueBleu.text = fusion
                this.view.afficherCalqueCibleBleu(boutonsChoix)
            } else {
                this.view.masquerCalqueCibleBleu()
                this.view.actualiserMain(model.joueurs,model.getMains(),view.listeHbox,view.largeurCarte,model.flip.joueurCourant)
            }
        }
    }
}