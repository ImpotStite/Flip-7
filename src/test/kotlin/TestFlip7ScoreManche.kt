package iut.info1.flip7

import Joueur
import iut.info1.flip7.TestFlip7Constructeur.Companion.genererPiocheSansDebug
import iut.info1.flip7.cartes.Carte
import iut.info1.flip7.cartes.CarteBonusMultiplie
import iut.info1.flip7.cartes.CarteBonusPlus
import iut.info1.flip7.cartes.CarteNum
import iut.info1.flip7.etats.EtatJoueur
import iut.info1.flip7.etats.EtatPartie
import iut.info1.flip7.exceptions.EtatPartieInvalideException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class TestFlip7ScoreManche {


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

        fun genererFlip7(deuxJoueurs: List<IJoueur>, cartes: List<Carte>): Flip7 {
            return Flip7(2, deuxJoueurs, cartes, true)
        }

    }

    /**
     * Entrées : Les deux joueurs ont dit Stop (MANCHE_TERMINEE).
     *
     * Sorties : Aucune exception levée lors de scoreManche().
     *
     * Explication : Vérifie que scoreManche() est appelable lorsque la manche est terminée.
     */
    @Test
    fun testEtatPartieCorrect() {
        val flip = Flip7(2,listOf(joueur0,joueur1), genererPiocheSansDebug())
        flip.joueurCourantDitStop()
        flip.joueurCourantDitStop()
        assertDoesNotThrow {
            flip.scoreManche()
        }
    }

    /**
     * Entrées : Un seul joueur a dit Stop.
     *
     * Sorties : EtatPartieInvalideException levée.
     *
     * Explication : Vérifie que scoreManche() ne peut pas être appelée avant la fin de la manche.
     */
    @Test
    fun testEtatPartieIncorrect() {
        val flip = Flip7(2,listOf(joueur0,joueur1), genererPiocheSansDebug())
        flip.joueurCourantDitStop()
        assertThrows<EtatPartieInvalideException> {
            flip.scoreManche()
        }
    }

    /**
     * Entrées : Joueur 0 pioche 12, 3, 2 puis Stop ; joueur 1 pioche puis Stop.
     *
     * Sorties : scoreManche = {0: 14, 1: 3}, etatPartie NOUVELLE_MANCHE, scores cumulés mis à jour.
     *
     * Explication : Cas nominal où les deux joueurs s'arrêtent sans perdre, calcul correct des scores de manche.
     */
    @Test
    fun testCasNominalJoueursStop() {
        val flip = genererFlip7(listOf(joueur0,joueur1),listOf(CarteNum(12),CarteNum(3),CarteNum(2)))
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantDitStop()
        flip.joueurCourantDitStop()
        assertEquals(mapOf(0 to 14, 1 to 3),flip.scoreManche())
        assertAll(
            {assertEquals(EtatPartie.NOUVELLE_MANCHE,flip.etatPartie)},
            {assertEquals(EtatJoueur.STOP,flip.etatJoueur[0])},
            {assertEquals(EtatJoueur.STOP,flip.etatJoueur[1])},
            {assertEquals(mapOf(0 to 14, 1 to 3) ,flip.score)}
        )
    }

    /**
     * Entrées : Partie avec scoreFinPartie = 50, deux manches jouées (scores intermédiaires : joueur 0 = 46, joueur 1 = 6).
     *
     * Sorties : Seconde manche : score {0:7, 1:0}, etatPartie PARTIE_TERMINEE, scores cumulés {0:53, 1:6}.
     *
     * Explication : Vérifie le calcul de score et la détection de fin de partie lorsque le seuil est atteint.
     */
    @Test
    fun testCasNominalJoueursStopEtFinPartie() {
        val flip = Flip7(2,listOf(joueur0,joueur1), listOf(
            CarteNum(12),
            CarteNum(1),
            CarteNum(11),
            CarteNum(2),
            CarteBonusMultiplie(),
            CarteNum(3),
            CarteNum(7),
            CarteNum(4),
            CarteNum(5)
        ), scoreFinPartie = 50,debug=true)
        for (i in 0 .. 5) {
            flip.joueurCourantPiocheUneCarte()
        }
        flip.joueurCourantDitStop()
        flip.joueurCourantDitStop()
        flip.scoreManche()
        flip.nouvelleManche()
        //Scores :  O = 46 ; 1 = 6
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantDitStop()
        flip.joueurCourantDitStop()
        assertEquals(mapOf(0 to 7, 1 to 0),flip.scoreManche())
        assertAll(
            {assertEquals(EtatPartie.PARTIE_TERMINEE,flip.etatPartie)},
            {assertEquals(EtatJoueur.STOP,flip.etatJoueur[0])},
            {assertEquals(EtatJoueur.STOP,flip.etatJoueur[1])},
            {assertEquals(mapOf(0 to 53, 1 to 6) ,flip.score)}
        )
    }

    /**
     * Entrées : Joueur 0 pioche 12, 3, 12 (perdu par doublon) ; joueur 1 Stop.
     *
     * Sorties : scoreManche = {0:0, 1:3}, joueur 0 PERDU.
     *
     * Explication : Cas nominal avec un joueur perdant (doublon) et un joueur stop.
     */
    @Test
    fun testCasNominalJoueurStopEtJoueurPerdu() {
        val flip = genererFlip7(listOf(joueur0,joueur1),listOf(CarteNum(12),CarteNum(3),CarteNum(12)))
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantDitStop()
        assertEquals(mapOf(0 to 0, 1 to 3),flip.scoreManche())
        assertAll(
            {assertEquals(EtatPartie.NOUVELLE_MANCHE,flip.etatPartie)},
            {assertEquals(EtatJoueur.PERDU,flip.etatJoueur[0])},
            {assertEquals(EtatJoueur.STOP,flip.etatJoueur[1])},
            {assertEquals(mapOf(0 to 0, 1 to 3) ,flip.score)}
        )
    }

    /**
     * Entrées : Joueur 0 pioche 1 puis Stop, joueur 1 pioche 2 à 7 (Flip 7).
     *
     * Sorties : score[0] = 43 (bonus Flip 7 de 15 par rapport à la somme 1+2+…+7), score[1] = 0, etatJoueur[1] = STOP.
     *
     * Explication : Colonne « au moins un joueur fait un Flip 7 » de la table scoreManche.
     */
    @Test
    fun testCasNominalBonusPointsFlip7() {
        val flip = genererFlip7(listOf(joueur0,joueur1),listOf(
            CarteNum(1),
            CarteNum(2),
            CarteNum(3),
            CarteNum(4),
            CarteNum(5),
            CarteNum(6),
            CarteNum(7)))
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantDitStop()
        for (i in 0 .. 5) {
            flip.joueurCourantPiocheUneCarte()
        }
        flip.scoreManche()
        assertEquals(15, flip.score[0]?.minus((1+2+3+4+5+6+7)) ?: 0 )
        assertAll(
            {assertEquals(EtatPartie.NOUVELLE_MANCHE,flip.etatPartie)},
            {assertEquals(EtatJoueur.JOUE_ENCORE,flip.etatJoueur[0])},
            {assertEquals(EtatJoueur.STOP,flip.etatJoueur[1])},
            {assertEquals(mapOf(0 to 43, 1 to 0) ,flip.score)}
        )
    }

    /**
     * Entrées : Joueur 0 pioche 1 puis Stop, joueur 1 enchaîne six pioches (2, 3, 4, 5, 6, 1).
     *
     * Sorties : scoreManche()[0] = 0, etatJoueur[0] = PERDU, etatJoueur[1] = STOP, scores cumulés {0:0, 1:0}.
     *
     * Explication : Colonne « un joueur fait un Flip 7 et a PERDU » de la table scoreManche.
     */
    @Test
    fun testBonusPointsFlip7etPerdu() {
        val flip = genererFlip7(listOf(joueur0,joueur1),listOf(
            CarteNum(1),
            CarteNum(2),
            CarteNum(3),
            CarteNum(4),
            CarteNum(5),
            CarteNum(6),
            CarteNum(1)))
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantDitStop()
        for (i in 0 .. 5) {
            flip.joueurCourantPiocheUneCarte()
        }
        assertEquals(0, flip.scoreManche()[0])
        assertAll(
            {assertEquals(EtatPartie.NOUVELLE_MANCHE,flip.etatPartie)},
            {assertEquals(EtatJoueur.PERDU,flip.etatJoueur[0])},
            {assertEquals(EtatJoueur.STOP,flip.etatJoueur[1])},
            {assertEquals(mapOf(0 to 0, 1 to 0) ,flip.score)}
        )
    }

    /**
     * Entrées : Les deux joueurs piochent chacun un doublon (12 puis 11).
     *
     * Sorties : scoreManche()[0] = 0, scoreManche()[1] = 0, etatJoueur[0] = PERDU, etatJoueur[1] = PERDU, scores cumulés {0:0, 1:0}.
     *
     * Explication : Colonne « tous les joueurs ont perdu » de la table scoreManche, aucun point marqué.
     */
    @Test
    fun testCasNominalTousLesJoueursPerdu() {
        val flip = Flip7(2,listOf(joueur0,joueur1), listOf(
            CarteNum(12),
            CarteNum(11),
            CarteNum(12),
            CarteNum(11)
        ),debug=true)
        for (i in 0 .. 3) {
            flip.joueurCourantPiocheUneCarte()
        }
        assertEquals(mapOf(0 to 0, 1 to 0), flip.scoreManche())
        assertAll(
            {assertEquals(EtatPartie.NOUVELLE_MANCHE,flip.etatPartie)},
            {assertEquals(EtatJoueur.PERDU,flip.etatJoueur[0])},
            {assertEquals(EtatJoueur.PERDU,flip.etatJoueur[1])},
            {assertEquals(mapOf(0 to 0, 1 to 0) ,flip.score)}
        )
    }





}