package iut.info1.flip7

import Joueur
import iut.info1.flip7.TestFlip7Constructeur.Companion.genererPiocheSansDebug
import iut.info1.flip7.cartes.Carte
import iut.info1.flip7.cartes.Carte2ndeChance
import iut.info1.flip7.cartes.Carte3aLaSuite
import iut.info1.flip7.cartes.CarteBonusMultiplie
import iut.info1.flip7.cartes.CarteBonusPlus
import iut.info1.flip7.cartes.CarteNum
import iut.info1.flip7.cartes.CarteStop
import iut.info1.flip7.etats.EtatJoueur
import iut.info1.flip7.etats.EtatPartie
import iut.info1.flip7.exceptions.EtatPartieInvalideException
import iut.info1.flip7.exceptions.PiocheInvalideException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class TestFlip7joueurCourantPiocheUneCarte {

    private lateinit var joueur0: Joueur
    private lateinit var joueur1: Joueur
    private lateinit var joueur2: Joueur
    private lateinit var joueur3: Joueur

    @BeforeEach
    fun setup() {
        joueur0 = Joueur("Alice")
        joueur1 = Joueur("Bob")
        joueur2 = Joueur("Claire")
        joueur3 = Joueur("David")

    }
    companion object {

        fun genererFlip7(deuxJoueurs : List<IJoueur>, cartes : List<Carte>) : Flip7 {
            return Flip7(2 , deuxJoueurs, cartes, true)
        }
    }




    /**
     * Entrées : Partie Flip7 à 2 joueurs avec pioche standard, après deux appels à joueurCourantDitStop().
     *
     * Sorties : EtatPartieInvalideException levée lors de joueurCourantPiocheUneCarte().
     *
     * Explication : Simuler l'avancée d'une manche pour sortir etatPartie de ATTENTE_CHOIX_JOUEUR, puis tenter de piocher une carte.
     */
    @Test
    fun testEtatPartieIncorrect() {
        val flip = Flip7(2,listOf(joueur0,joueur1), genererPiocheSansDebug())
        flip.joueurCourantDitStop()
        flip.joueurCourantDitStop()
        assertThrows<EtatPartieInvalideException> {
            flip.joueurCourantPiocheUneCarte()
        }

    }

    /**
     * Entrées : Flip7 à 2 joueurs avec pioche de 2 cartes numériques, puis cycle complet de manche (pioches, stops, score, nouvelle manche).
     *
     * Sorties : Aucune exception lors de joueurCourantPiocheUneCarte() après reconstitution de la pioche.
     *
     * Explication : Vérifier que la pioche est reconstituée depuis la défausse lorsqu'elle est vide.
     */
    @Test
    fun testRempilageDefausse() {
        val flip7 = genererFlip7(listOf(joueur0,joueur1), listOf(CarteNum(1),CarteNum(2)))
            flip7.joueurCourantPiocheUneCarte()
            flip7.joueurCourantDitStop()
            flip7.joueurCourantPiocheUneCarte()
            flip7.joueurCourantDitStop()
            flip7.scoreManche()
            flip7.nouvelleManche()
        assertDoesNotThrow {  flip7.joueurCourantPiocheUneCarte() }
    }


    /**
     * Entrées : Tentative de création d'une partie Flip7 avec une pioche vide.
     *
     * Sorties : PiocheInvalideException.
     *
     * Explication : Vérifier qu'une partie sans cartes disponibles lève une exception.
     */
    @Test
    fun testAucuneCarteDisponible() {
        assertThrows<PiocheInvalideException> {
            val flip7 = genererFlip7(listOf(joueur0,joueur1), listOf())
        }
    }


    /**
     * Entrées : Flip7 à 2 joueurs avec pioche de 6 cartes variées.
     *
     * Sorties : taillePioche vaut 6, puis 5, puis 2 après quatre pioches successives.
     *
     * Explication : Vérifier la cohérence de la taille de la pioche à chaque pioche.
     */
    @Test
    fun testCoherenceTaillePioche() {
        val flip7 = genererFlip7(listOf(joueur0,joueur1), listOf(Carte2ndeChance(),CarteNum(2),CarteNum(5),CarteNum(8),CarteNum(3),
            CarteBonusMultiplie()))
        assertEquals(6,flip7.taillePioche)
        flip7.joueurCourantPiocheUneCarte()
        assertEquals(5,flip7.taillePioche)
        flip7.joueurCourantPiocheUneCarte()
        flip7.joueurCourantPiocheUneCarte()
        flip7.joueurCourantPiocheUneCarte()
        assertEquals(2,flip7.taillePioche)
    }


    /**
     * Entrées : Flip7 à 2 joueurs avec une unique CarteNum(5) en pioche.
     *
     * Sorties : La carte retournée par joueurCourantPiocheUneCarte() est la même référence que celle en pioche.
     *
     * Explication : Vérifier le passage par référence de la carte piochée.
     */
    @Test
    fun testPassageParReference() {
        val carteReference = CarteNum(5)
        val flip7 = genererFlip7(listOf(joueur0,joueur1), listOf(carteReference))
        assertTrue(flip7.joueurCourantPiocheUneCarte() === carteReference)
    }

    /**
     * Entrées : Flip7 avec une CarteNum(5), piochée puis remise en circulation après un cycle complet de manche.
     *
     * Sorties : La carte repiochée est la même référence que la carte initiale.
     *
     * Explication : Vérifier le passage par référence après remplissage de la pioche depuis la défausse.
     */
    @Test
    fun testPassageParReferenceDefausse() {
        val carteReference = CarteNum(5)
        val flip7 = genererFlip7(listOf(joueur0,joueur1), listOf(carteReference))
        flip7.joueurCourantPiocheUneCarte()
        flip7.joueurCourantDitStop()
        flip7.joueurCourantDitStop()
        flip7.scoreManche()
        flip7.nouvelleManche()
        assertTrue(flip7.joueurCourantPiocheUneCarte() === carteReference)
    }

    /**
     * Entrées : Pioche contenant trois Carte2ndeChance entrecoupées de cartes numériques.
     *
     * Sorties : Aucune exception lors des pioches successives malgré les cartes 2nde chance.
     *
     * Explication : Vérifier que le joueur peut continuer à piocher après plusieurs cartes 2nde chance.
     */
    @Test
    fun testTroisSecondesChances() {
        val flip = genererFlip7(listOf(joueur0,joueur1), listOf(Carte2ndeChance(),CarteNum(2), Carte2ndeChance(),CarteNum(10),
            Carte2ndeChance()))
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantPiocheUneCarte()
        assertDoesNotThrow { flip.joueurCourantPiocheUneCarte() }
        flip.joueurCourantPiocheUneCarte()
        assertDoesNotThrow { flip.joueurCourantPiocheUneCarte() }
    }



    /**
     * Entrées : Flip7 à 2 joueurs avec pioche de trois cartes numériques.
     *
     * Sorties : main du joueur courant contient 1 carte, défausse vide, JOUE_ENCORE, ATTENTE_CHOIX_JOUEUR, taillePioche à 2.
     *
     * Explication : Vérifier l'état de la partie après pioche d'une carte numérique.
     */
    @Test
    fun etatJeuPendantPiocheUneCarteNum() {
        val flip = genererFlip7(listOf(joueur0,joueur1),listOf(CarteNum(1),CarteNum(2),CarteNum(3)))
        val laCarte = flip.joueurCourantPiocheUneCarte()
        assertAll(
            {assertEquals(1,flip.main[0]!!.size)},
            {assertEquals(laCarte, flip.main[0]!!.last())},
            {assertEquals(0,flip.defausse.size)},
            {assertEquals(EtatJoueur.JOUE_ENCORE,flip.etatJoueur[0])},
            {assertEquals(EtatPartie.ATTENTE_CHOIX_JOUEUR,flip.etatPartie)},
            {assertEquals(2,flip.taillePioche)}
        )
    }

    /**
     * Entrées : Flip7 à 2 joueurs avec pioche commençant par une CarteBonusPlus(4).
     *
     * Sorties : main du joueur courant contient 1 carte bonus, défausse vide, JOUE_ENCORE, ATTENTE_CHOIX_JOUEUR, taillePioche à 2.
     *
     * Explication : Vérifier l'état de la partie après pioche d'une carte bonus.
     */
    @Test
    fun etatJeuPendantPiocheUneCarteBonus() {
        val flip = genererFlip7(listOf(joueur0,joueur1),listOf(CarteBonusPlus(4),CarteNum(2),CarteNum(3)))
        val laCarte = flip.joueurCourantPiocheUneCarte()
        assertAll(
            {assertEquals(1,flip.main[0]!!.size)},
            {assertEquals(laCarte, flip.main[0]!!.last())},
            {assertEquals(0,flip.defausse.size)},
            {assertEquals(EtatJoueur.JOUE_ENCORE,flip.etatJoueur[0])},
            {assertEquals(EtatPartie.ATTENTE_CHOIX_JOUEUR,flip.etatPartie)},
            {assertEquals(2,flip.taillePioche)}
        )
    }

    /**
     * Entrées : Flip7 à 2 joueurs avec pioche commençant par une Carte3aLaSuite.
     *
     * Sorties : main du joueur courant vide, défausse vide, JOUE_ENCORE, ATTENTE_CIBLE_3SUITE, taillePioche à 2.
     *
     * Explication : Vérifier l'état de la partie après pioche d'une carte 3 à la suite.
     */
    @Test
    fun etatJeuPendantPiocheUneCarte3aLaSuite() {
        val flip = genererFlip7(listOf(joueur0,joueur1),listOf(Carte3aLaSuite(),CarteNum(2),CarteNum(3)))
        val laCarte = flip.joueurCourantPiocheUneCarte()
        assertAll(
            {assertEquals(0,flip.main[0]!!.size)},
            {assertEquals(0,flip.defausse.size)},
            {assertEquals(EtatJoueur.JOUE_ENCORE,flip.etatJoueur[0])},
            {assertEquals(EtatPartie.ATTENTE_CIBLE_3SUITE,flip.etatPartie)},
            {assertEquals(2,flip.taillePioche)}
        )

    }

    /**
     * Entrées : Flip7 à 2 joueurs avec pioche commençant par une CarteStop.
     *
     * Sorties : main du joueur courant vide, défausse vide, JOUE_ENCORE, ATTENTE_CIBLE_STOP, taillePioche à 2.
     *
     * Explication : Vérifier l'état de la partie après pioche d'une carte Stop.
     */
    @Test
    fun etatJeuPendantPiocheUneCarteStop() {
        val flip = genererFlip7(listOf(joueur0,joueur1),listOf(CarteStop(),CarteNum(2),CarteNum(3)))
        flip.joueurCourantPiocheUneCarte()
        assertAll(
            {assertEquals(0,flip.main[0]!!.size)},
            {assertEquals(0,flip.defausse.size)},
            {assertEquals(EtatJoueur.JOUE_ENCORE,flip.etatJoueur[0])},
            {assertEquals(EtatPartie.ATTENTE_CIBLE_STOP,flip.etatPartie)},
            {assertEquals(2,flip.taillePioche)}
        )
    }

    /**
     * Entrées : Flip7 à 2 joueurs avec pioche commençant par une Carte2ndeChance.
     *
     * Sorties : main du joueur courant contient 1 carte, défausse vide, JOUE_ENCORE, ATTENTE_CHOIX_JOUEUR, taillePioche à 2.
     *
     * Explication : Vérifier l'état de la partie après pioche d'une carte 2nde chance.
     */
    @Test
    fun etatJeuPendantPiocheUneCarte2ndeChance() {
        val flip = genererFlip7(listOf(joueur0,joueur1),listOf(Carte2ndeChance(),CarteNum(2),CarteNum(3)))
        val laCarte = flip.joueurCourantPiocheUneCarte()
        assertAll(
            {assertEquals(1,flip.main[0]!!.size)},
            {assertEquals(laCarte, flip.main[0]!!.last())},
            {assertEquals(0,flip.defausse.size)},
            {assertEquals(EtatJoueur.JOUE_ENCORE,flip.etatJoueur[0])},
            {assertEquals(EtatPartie.ATTENTE_CHOIX_JOUEUR,flip.etatPartie)},
            {assertEquals(2,flip.taillePioche)}
        )
    }


    /**
     * Entrées : Flip7 à 2 joueurs avec pioche CarteNum(7), CarteNum(2), CarteNum(7), trois pioches successives du joueur 0.
     *
     * Sorties : main[0] contient 2 cartes, defausse vide, etatJoueur[0] = PERDU, etatPartie = ATTENTE_CHOIX_JOUEUR, pioche vide.
     *
     * Explication : Colonne « CarteNum() doublon » de la table joueurCourantPiocheUneCarte.
     */
    @Test
    fun etatJeuPendantPiocheUneCarteNumDoublon() {
        val flip = genererFlip7(listOf(joueur0,joueur1),listOf(CarteNum(7),CarteNum(2),CarteNum(7)))
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantPiocheUneCarte()
        assertAll(
            {assertEquals(2,flip.main[0]!!.size)},
            {assertEquals(0,flip.defausse.size)},
            {assertEquals(EtatJoueur.PERDU,flip.etatJoueur[0])},
            {assertEquals(EtatJoueur.JOUE_ENCORE,flip.etatJoueur[1])},
            {assertEquals(EtatPartie.ATTENTE_CHOIX_JOUEUR,flip.etatPartie)},
            {assertEquals(0,flip.taillePioche)}
        )
    }









}