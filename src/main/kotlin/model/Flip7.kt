package model

import Joueur
import iut.info1.flip7.Flip7
import iut.info1.flip7.IJoueur
import iut.info1.flip7.cartes.Carte
import iut.info1.flip7.cartes.Carte2ndeChance
import iut.info1.flip7.cartes.Carte3aLaSuite
import iut.info1.flip7.cartes.CarteBonusMultiplie
import iut.info1.flip7.cartes.CarteBonusPlus
import iut.info1.flip7.cartes.CarteNum
import iut.info1.flip7.cartes.CarteStop
import iut.info1.flip7.etats.EtatJoueur
import iut.info1.flip7.etats.EtatPartie
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.Label

class model {

    var nbJoueurs: SimpleIntegerProperty = SimpleIntegerProperty(2)
    var joueurs: ObservableList<Joueur> = FXCollections.observableArrayList(Joueur(""), Joueur(""))
    val cartes = genererPiocheSansDebug()
    val scoreFinPartie = SimpleIntegerProperty(200)
    val etatPartieDynamique = SimpleObjectProperty<EtatPartie>()
    lateinit var derniereCartePiochee : Carte
    lateinit var flip : Flip7



    fun ajouterJoueur() {
        if (nbJoueurs.value < 4) {
            nbJoueurs.value++
            joueurs.add(Joueur(""))
        }
    }

    fun retirerJoueur() {
        if (nbJoueurs.value > 2) {
            nbJoueurs.value--
            joueurs.removeLast()
        }
    }

    fun arreterJoueur() {
        flip.joueurCourantDitStop()
    }

    private fun genererPiocheSansDebug(): List<Carte> {
        val pioche = mutableListOf<Carte>(CarteNum(0))
        for (i in 1..12) {
            for (j in 1..i) {
                pioche.add(CarteNum(i))
            }
        }
        pioche.add(CarteBonusMultiplie())
        for (i in 1..5) {
            pioche.add(CarteBonusPlus(i * 2))
        }

        for (i in 0 until 3) {
            pioche.add(Carte2ndeChance())
            pioche.add(Carte3aLaSuite())
            pioche.add(CarteStop())
        }
        return pioche.toList()
    }

    fun clicSurDemarrer(nomDesJoueurs : List<String>) {
        val nouveauxJoueurs = mutableListOf<Joueur>()
        for (elem in nomDesJoueurs) {
            nouveauxJoueurs.add(Joueur(elem))
        }
        joueurs = FXCollections.observableArrayList(nouveauxJoueurs)
        flip = Flip7(nbJoueurs.value,joueurs,cartes, scoreFinPartie = this.scoreFinPartie.value)
        this.etatPartieDynamique.value = flip.etatPartie
    }

    fun donnerListeJoueur(): List<IJoueur> {
        return flip.joueurs
    }

    fun donneScore(): Map<Int, Int> {
        return flip.scoreManche()
    }

    fun donneJoueurCourant() : Int {
        return flip.joueurCourant
    }

    fun clicSurPioche() : EtatPartie {
        this.derniereCartePiochee = flip.joueurCourantPiocheUneCarte()
        this.etatPartieDynamique.value = flip.etatPartie
        return flip.etatPartie

    }



    fun finManche() : Boolean {
        return flip.etatPartie == EtatPartie.MANCHE_TERMINEE
    }

    fun enAttenteCible() : Boolean {
        return flip.etatPartie == EtatPartie.ATTENTE_CIBLE_3SUITE || flip.etatPartie == EtatPartie.ATTENTE_CIBLE_STOP
    }

    fun lancerNouvelleManche() {
        this.flip.nouvelleManche()
        this.etatPartieDynamique.value = flip.etatPartie
    }

    fun validerCible3aLaSuite(i: Int) {
        this.flip.joueurCourantCible3aLaSuite(this.derniereCartePiochee, i)
        this.etatPartieDynamique.value = flip.etatPartie
    }

    fun validerCibleStop(i: Int) {
        this.flip.joueurCourantCibleStop(this.derniereCartePiochee, i)

        this.etatPartieDynamique.value = flip.etatPartie
    }

    fun getMains() : Map<IJoueur, List<Carte>?> {
        val main = mutableMapOf<IJoueur, List<Carte>?>()
        for (i in 0 until flip.joueurs.size) {
            main[joueurs[i]] = flip.main[i]
        }
        return main
    }

    fun joueurPossedeSecondeChance(i : Int) : Boolean{
        if (flip.main[i] != null) {
            for (carte in flip.main[i]!!) {
                if (carte is Carte2ndeChance) {return true}
            }
        }
        return false
    }

    fun estFlip(main : List<Carte>?) : Boolean {

        if (main != null) {
            return flip.outilsCarte.estFlip7(main)
        }
        return false
    }

}