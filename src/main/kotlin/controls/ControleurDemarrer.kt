package controls


import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import model.model
import view.SelectionJoueursPane
import view.Table.Table2Joueur
import view.Table.Table3Joueur
import view.Table.Table4Joueur
import view.tables.Basic

class ControleurDemarrer(val view : SelectionJoueursPane, val model : model) : EventHandler<MouseEvent>{

    override fun handle(event: MouseEvent) {
        if (event.eventType == MouseEvent.MOUSE_RELEASED) {
            val nomsDesJoueurs = mutableListOf<String>()
            for (i in 0 until model.nbJoueurs.value) {
                var plaque = this.view.listeJoueurs[i]
                val texte = plaque.children.filterIsInstance<TextField>().firstOrNull()

                if (texte != null && !texte.text.isNullOrBlank()) {
                    nomsDesJoueurs.add(texte.text)
                } else {
                    nomsDesJoueurs.add("joueur ${i+1}")
                }

            }
            this.model.clicSurDemarrer(nomsDesJoueurs)
            this.view.btDemarrer.isDisable = true
            var vue : Basic = when (model.nbJoueurs.value) {
                2 -> Table2Joueur()
                3 -> Table3Joueur()
                else -> Table4Joueur()
            }
            val score: Map<Int, Int> = mapOf(0 to 0)
            vue.creerTableauScore(model.donnerListeJoueur(), score)
            vue.fixeControleurBouton(vue.boutonPioche, ControleurPioche(vue,model))
            vue.fixeControleurBouton(vue.boutonStop, ControleurBoutonStop(vue,model))
            val control3aLaSuite = ControleurCible3aLaSuite(vue,model)
            val controlStop = ControleurCibleStop(vue,model)
            val controlFinManche = ControleurFinManche(vue,model)
            view.scene.root = vue
            val joueurCourant = model.flip.joueurCourant
            vue.modifierOracle(" La partie commence ! \nAu tour de ${model.joueurs[joueurCourant].donneNom()}")
        }
    }
}

