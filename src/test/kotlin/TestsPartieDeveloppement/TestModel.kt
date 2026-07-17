package iut.info1.flip7.TestsPartieDeveloppement

import iut.info1.flip7.Flip7
import iut.info1.flip7.cartes.Carte2ndeChance
import iut.info1.flip7.cartes.Carte3aLaSuite
import iut.info1.flip7.cartes.CarteNum
import iut.info1.flip7.cartes.CarteStop
import iut.info1.flip7.etats.EtatJoueur
import iut.info1.flip7.etats.EtatPartie
import model.model
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TestModel {

    private lateinit var modele : model

    @BeforeEach
    fun setUp() {
        modele = model()
    }

    /**
     * Entrées : un model() avec deux joueurs prédéfinis.
     *
     * Sorties : Le model() a un joueur en plus
     *
     * Explication : Vérifier qu'il est possible d'ajouter un joueur s'il n'y en a actuellement que deux.
     */
    @Test
    fun testAjouterJoueurCorrect() {
        assertDoesNotThrow {
            modele.ajouterJoueur()
        }
    }

    /**
     * Entrées : un model() avec deux joueurs prédéfinis, auquel on en rajoute deux autres.
     *
     * Sorties : Le model() a quatre joueurs mais a refusé d'en ajouter un cinquième.
     *
     * Explication : Vérifier qu'il n'est pas possible de dépasser 4 joueurs
     */
    @Test
    fun testAjouterJoueurIncorrect(){
        modele.ajouterJoueur()
        modele.ajouterJoueur()
        assertEquals(4,modele.nbJoueurs.value)
        modele.ajouterJoueur()
        assertEquals(4,modele.nbJoueurs.value)
        }

    /**
     * Entrées : un model() avec nbJoueurs fixé à quatre.
     *
     * Sorties : nbJoueurs devient 3, puis 2
     *
     * Explication : Vérifier qu'est possible de supprimer des joueurs si on en a 4 ou 3.
     */
    @Test
    fun testRetirerJoueurCorrect() {
        modele.nbJoueurs.value = 4
        modele.retirerJoueur()
        assertEquals(3,modele.nbJoueurs.value)
        modele.retirerJoueur()
        assertEquals(2,modele.nbJoueurs.value)
    }

    /**
     * Entrées : un model() avec nbJoueurs fixé à deux.
     *
     * Sorties : nbJoueurs reste à deux.
     *
     * Explication : Vérifier qu'est n'est pas possible de supprimer un joueur si on n'en a que deux.
     */
    @Test
    fun testRetirerJoueurIncorrect(){
        modele.nbJoueurs.value = 2
        modele.retirerJoueur()
        assertEquals(2,modele.nbJoueurs.value)
    }

    /**
     * Entrées : un model() avec genererPiocheSansDebug() renvoyant la pioche.
     *
     * Sorties : La taille de l'attribut cartes est de 94.
     *
     * Explication : Vérifier que la méthode genererPiocheSansDebug() renvoie une pioche valide en taille.
     */
    @Test
    fun testTaillePiocheInitiale(){
        assertEquals(94,modele.cartes.size)
    }

    /**
     * Entrées : un model() avec une pioche de 94 cartes.
     *
     * Sorties : La pioche est passée à 93 cartes.
     *
     * Explication : Vérifier que la méthode clicSurPioche() tire une carte réelle de la pioche.
     */
    @Test
    fun testPiocheDiminueReellementQuandJoueurPioche(){
        modele.flip = Flip7(modele.nbJoueurs.value, modele.joueurs,modele.cartes)
        assertEquals(94,modele.flip.taillePioche)
        modele.clicSurPioche()
        assertEquals(93,modele.flip.taillePioche)
    }

    /**
     * Entrées : un model() avec une pioche de 94 cartes.
     *
     * Sorties : La pioche est passée à 93 cartes.
     *
     * Explication : Vérifier que la méthode clicSurPioche() tire une carte réelle de la pioche.
     */
    @Test
    fun testPiocherRenvoieEtatPartieCorrect() {
        modele.flip = Flip7(modele.nbJoueurs.value, modele.joueurs,listOf(CarteStop()),debug=true)
        modele.clicSurPioche()
        assertEquals(EtatPartie.ATTENTE_CIBLE_STOP,modele.etatPartieDynamique.value)
    }
    /**
     * Entrées : un model() avec flip initialisé en mode débug pour piocher une Carte3aLaSuite au premier coup.
     * mains des joueurs vides
     *
     * Sorties : La main du joueur ciblé par validerCible3alaSuite() est passée de 0 à 4 cartes
     *
     * Explication : Vérifier que la méthode validerCible3aLaSuite() fait piocher 3 cartes à la cible + la carte spéciale donnée.
     */
    @Test
    fun testValiderCible3aLaSuite() {
        modele.flip = Flip7(modele.nbJoueurs.value, modele.joueurs,listOf(Carte3aLaSuite(),
            CarteNum(2),CarteNum(3),CarteNum(4)),debug=true)
        modele.clicSurPioche()
        assertEquals(0,modele.flip.main[1]!!.size)
        modele.validerCible3aLaSuite(1)
        assertEquals(4,modele.flip.main[1]!!.size)
    }

    /**
     * Entrées : un model() avec flip initialisé en mode débug pour piocher une CarteStop au premier coup.
     *
     * Sorties : L'etat du joueur ciblé par la méthode validerCibleStop() est passé à EtatJoueur.STOP
     *
     * Explication : Vérifier que la méthode validerCibleStop() change bien l'etat du joueur visé.
     */
    @Test
    fun testValiderCibleStop1() {
        modele.flip = Flip7(modele.nbJoueurs.value, modele.joueurs,listOf(CarteStop()),debug=true)
        modele.clicSurPioche()
        modele.validerCibleStop(1)
        assertEquals(EtatJoueur.STOP,modele.flip.etatJoueur[1])
    }

    /**
     * Entrées : un model() avec flip initialisé en mode débug pour piocher une CarteStop au premier coup.
     *
     * Sorties : L'etat dude la partie est passée en ATTENTE_CHOIX_JOUEUR
     *
     * Explication : Vérifier que la méthode validerCibleStop() permet à la partie de continuer après son appel.
     */
    @Test
    fun testValiderCibleStop2() {
        modele.flip = Flip7(modele.nbJoueurs.value, modele.joueurs,listOf(CarteStop()),debug=true)
        modele.clicSurPioche()
        modele.validerCibleStop(1)
        assertEquals(EtatPartie.ATTENTE_CHOIX_JOUEUR,modele.etatPartieDynamique.value)
    }

    /**
     * Entrées : un model() avec flip initialisé en mode débug pour piocher une Carte2ndeChance au premier coup.
     *
     * Sorties : La méthode joueurPossedeSecondeChance() renvoie true sur le joueur 0 qui a pioché.
     *
     * Explication : Vérifier que la méthode joueurPossedeSecondeChance() détecte bien la présence d'une Carte2ndeChance.
     */
    @Test
    fun testJoueurPossedeUneSecondeChance() {
        modele.flip = Flip7(modele.nbJoueurs.value, modele.joueurs,listOf(Carte2ndeChance()),debug=true)
        modele.clicSurPioche()
        assertEquals(true, modele.joueurPossedeSecondeChance(0))
    }

    /**
     * Entrées : un model() avec flip initialisé en mode débug pour piocher deux Carte2ndeChance.
     *
     * Sorties : La méthode joueurPossedeSecondeChance() renvoie true sur le joueur 0 qui a pioché les deux cartes.
     *
     * Explication : Vérifier que la méthode joueurPossedeSecondeChance() n'est pas affectée par la présence de deux cartes 2ndeChance.
     */
    @Test
    fun testJoueurPossedeDeuxSecondeChance() {
        modele.flip = Flip7(modele.nbJoueurs.value, modele.joueurs,listOf(Carte2ndeChance(), Carte2ndeChance()),debug=true)
        modele.clicSurPioche()
        modele.flip.joueurCourantDitStop()
        modele.clicSurPioche()
        assertEquals("{0=[[Carte 2nde chance], [Carte 2nde chance]], 1=[]}", modele.flip.main.toString())
        assertEquals(true, modele.joueurPossedeSecondeChance(0))
    }

    /**
     * Entrées : un model() avec flip initialisé en mode débug pour piocher une CarteNum().
     *
     * Sorties : La méthode joueurPossedeSecondeChance() renvoie false sur le joueur 0 qui a pioché la carte.
     *
     * Explication : Vérifier que la méthode joueurPossedeSecondeChance() détecte uniquement les Carte2ndeChance.
     */
    @Test
    fun testJoueurSansSecondeChance() {
        modele.flip = Flip7(modele.nbJoueurs.value, modele.joueurs,listOf(CarteNum(5)),debug=true)
        modele.clicSurPioche()
        assertEquals(false, modele.joueurPossedeSecondeChance(0))
    }







}



