package controls


import iut.info1.flip7.IJoueur
import iut.info1.flip7.cartes.Carte
import iut.info1.flip7.etats.EtatJoueur
import iut.info1.flip7.etats.EtatPartie
import javafx.scene.control.Button
import javafx.scene.control.Label
import model.model
import view.tables.Basic

class ControleurCible3aLaSuite(private val view : Basic, private val model : model) {

    init {
        model.etatPartieDynamique.addListener { _,_,nouvelEtat ->
            if (nouvelEtat == EtatPartie.ATTENTE_CIBLE_3SUITE) {
                val joueur3ALasuite = model.donneJoueurCourant()
                val boutonsChoix = mutableListOf<Button>()
                for (i in 0 until model.nbJoueurs.value) {
                    if (model.flip.etatJoueur[i] == EtatJoueur.JOUE_ENCORE) {
                    val boutonJoueur = Button(model.joueurs[i].donneNom())
                    boutonJoueur.style = "-fx-font-family: 'Lucida Console'; -fx-font-weight: bold;-fx-background-color: yellow; -fx-background-radius: 20px; -fx-text-fill: black;-fx-font-size: 25px;"
                    boutonJoueur.setOnAction {
                        this.model.validerCible3aLaSuite(i)
                        this.view.actualiserMain(model.joueurs,model.getMains(),view.listeHbox,view.largeurCarte,model.flip.joueurCourant)

                        val joueurCourant = model.donneJoueurCourant()

                        if (model.finManche()) {
                            model.donneScore()
                            val mapScore = model.flip.score
                            val main:  Map<IJoueur, List<Carte>?> = model.getMains()
                            if (model.estFlip(main[model.joueurs[i]])) {
                                view.modifierOracle("  ${model.joueurs[i].donneNom()} a \n eu un FLIP7 !")
                                view.refreshTableauScore( view.tableauScore,model.flip.joueurs, mapScore)
                                model.etatPartieDynamique.value = model.flip.etatPartie
                            } else {
                                view.modifierOracle("  ${model.joueurs[i].donneNom()} a perdu \n : Tout le monde a perdu \n la manche s'arrête")
                                view.refreshTableauScore(view.tableauScore,model.flip.joueurs, mapScore)
                                model.etatPartieDynamique.value = model.flip.etatPartie
                            }
                        } else {
                            view.modifierOracle("${model.joueurs[joueur3ALasuite].donneNom()} : a ciblé \n ${model.joueurs[i].donneNom()} \nAu tour de ${model.joueurs[joueurCourant].donneNom()}")
                            model.etatPartieDynamique.value = model.flip.etatPartie
                        }

                        val labelLigne = view.tableauScore.children[i] as Label
                        if (model.flip.etatJoueur[i] !=EtatJoueur.JOUE_ENCORE  ) {
                            view.tableauScore.children[i].style = "-fx-text-fill: black;"
                            var textScore =
                                "${model.joueurs[i].donneNom()} : ${model.flip.score[i] ?: 0}"
                            textScore += "  PERDU"
                            labelLigne.text = textScore
                        }}
                    boutonsChoix.add(boutonJoueur)

                        val labelLigne = view.tableauScore.children[i] as Label
                        if (model.flip.etatJoueur[i] !=EtatJoueur.JOUE_ENCORE  ) {
                            view.tableauScore.children[i].style = "-fx-text-fill: black;"
                            var textScore =
                                "${model.joueurs[i].donneNom()} : ${model.flip.score[i] ?: 0}"
                            textScore += "  PERDU"
                            labelLigne.text = textScore
                        }
                    }
                }
                view.titreCalqueJaune.text = ", qui piochera 3 cartes ?"
                val texte = "${model.joueurs[joueur3ALasuite].donneNom()}"
                val texteTitre = view.titreCalqueJaune.text
                val fusion = texte + texteTitre
                view.titreCalqueJaune.text = fusion
                this.view.afficherCalqueCibleJaune(boutonsChoix)
            } else {
                this.view.masquerCalqueCibleJaune()
                this.view.actualiserMain(model.joueurs,model.getMains(),view.listeHbox,view.largeurCarte,model.flip.joueurCourant)
            }
        }
    }
}