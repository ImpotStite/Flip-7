package iut.info1.flip7

import Joueur
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import iut.info1.flip7.TestFlip7Constructeur.Companion.genererPiocheSansDebug
import iut.info1.flip7.cartes.Carte
import iut.info1.flip7.cartes.CarteNum
import iut.info1.flip7.cartes.CarteStop
import iut.info1.flip7.etats.EtatJoueur
import iut.info1.flip7.etats.EtatPartie
import iut.info1.flip7.exceptions.EtatPartieInvalideException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class TestJoueurCourantDitStop {

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
     * Entrées : Partie en ATTENTE_CHOIX_JOUEUR.
     *
     * Sorties : Aucune exception levée.
     *
     * Explication : Vérifie que joueurCourantDitStop() est appelable dans le bon état.
     */
    @Test
    fun etatPartieCorrect() {
        val flip = genererFlip7(listOf(joueur0,joueur1), listOf(CarteStop()))
        assertEquals(EtatPartie.ATTENTE_CHOIX_JOUEUR, flip.etatPartie)
        assertDoesNotThrow {flip.joueurCourantDitStop()}
    }

    /**
     * Entrées : Manche terminée et scorée (après scoreManche).
     *
     * Sorties : EtatPartieInvalideException levée.
     *
     * Explication : Vérifie qu'on ne peut plus dire Stop après la fin de la manche.
     */
    @Test
    fun etatPartieIncorrect() {
        val flip = genererFlip7(listOf(joueur0,joueur1), listOf(CarteStop()))
        flip.joueurCourantDitStop()
        flip.joueurCourantDitStop()
        flip.scoreManche()
        assertThrows<EtatPartieInvalideException> {flip.joueurCourantDitStop()}
    }

    /**
     * Entrées : Joueur courant (0) dit Stop.
     *
     * Sorties : etatJoueur[0] = STOP, etatJoueur[1] = JOUE_ENCORE, etatPartie inchangé.
     *
     * Explication : Cas nominal d'un joueur qui s'arrête volontairement.
     */
    @Test
    fun casNominal() {
        val flip = genererFlip7(listOf(joueur0,joueur1), listOf(CarteNum(1), CarteNum(2)))
        flip.joueurCourantDitStop()
        assertAll(
            {assertEquals(EtatJoueur.JOUE_ENCORE, flip.etatJoueur[1])},
            {assertEquals(EtatJoueur.STOP, flip.etatJoueur[0])},
            {assertEquals(EtatPartie.ATTENTE_CHOIX_JOUEUR, flip.etatPartie)}
        )
    }


    /**
     * Entrées : Partie à 4 joueurs, joueurs 1/2/3 disent Stop successivement.
     *
     * Sorties : joueurCourant revient à 0, joueur 0 encore actif.
     *
     * Explication : Vérifie le retour à l'index 0 quand tous les joueurs sauf le courant ont dit Stop.
     */
    @Test
    fun casNominalIndexReset() {
        val flip = Flip7(4,listOf(joueur0,joueur1,joueur2,joueur3), genererPiocheSansDebug())
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantDitStop()
        flip.joueurCourantDitStop()
        flip.joueurCourantDitStop()

        assertAll(
            {assertEquals(EtatJoueur.JOUE_ENCORE, flip.etatJoueur[0])},
            { for (idJoueur in listOf(1,2,3)) {
                assertEquals(EtatJoueur.STOP, flip.etatJoueur[idJoueur])
            }},
            {assertEquals(EtatPartie.ATTENTE_CHOIX_JOUEUR, flip.etatPartie)},
            {assertEquals(0,flip.joueurCourant)}
        )
    }


    /**
     * Entrées : Partie à 4 joueurs, séquence Stop/Pioche/Stop.
     *
     * Sorties : joueurCourant passe de 0 à 3 après le dernier Stop.
     *
     * Explication : Vérifie l'avancement de l'index joueur courant vers le prochain joueur actif.
     */
    @Test
    fun casNominalIndexDe0Vers3() {
        val flip = Flip7(4,listOf(joueur0,joueur1,joueur2,joueur3), genererPiocheSansDebug())
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantDitStop()
        flip.joueurCourantDitStop()
        flip.joueurCourantPiocheUneCarte()

        assertEquals(0,flip.joueurCourant)
        flip.joueurCourantDitStop()
        assertAll(
            {assertEquals(EtatJoueur.JOUE_ENCORE, flip.etatJoueur[3])},
            { for (idJoueur in listOf(1,2,0)) {
                assertEquals(EtatJoueur.STOP, flip.etatJoueur[idJoueur])
            }},
            {assertEquals(EtatPartie.ATTENTE_CHOIX_JOUEUR, flip.etatPartie)},
            {assertEquals(3,flip.joueurCourant)}
        )
    }

    /**
     * Entrées : Partie à 4 joueurs avec un joueur PERDU, séquence de pioches et Stop.
     *
     * Sorties : joueurCourant passe de 0 à 3 en sautant les joueurs inactifs.
     *
     * Explication : Vérifie que les joueurs PERDU ou STOP sont ignorés lors de l'avancement de l'index.
     */
    @Test
    fun casNominalAvecUnPERDU() {
        val flip = Flip7(4,listOf(joueur0,joueur1,joueur2,joueur3), listOf(
            CarteNum(2),
            CarteNum(3),
            CarteNum(4),
            CarteNum(5),
            CarteNum(6),
            CarteNum(3),
            CarteNum(7),
            CarteNum(8)
        ), debug = true)

        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantPiocheUneCarte()

        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantDitStop()
        flip.joueurCourantPiocheUneCarte()

        assertEquals(0,flip.joueurCourant)
        flip.joueurCourantDitStop()
        assertEquals(3,flip.joueurCourant)

    }

    /**
     * Entrées : Partie à 4 joueurs, dernier joueur actif dit Stop.
     *
     * Sorties : etatPartie = MANCHE_TERMINEE, tous les joueurs en STOP.
     *
     * Explication : Vérifie la transition vers MANCHE_TERMINEE lorsque le dernier joueur actif s'arrête.
     */
    @Test
    fun casNominalDernierJoueurDitStop() {
        val flip = Flip7(4,listOf(joueur0,joueur1,joueur2,joueur3),listOf(CarteNum(1)),debug = true)
        flip.joueurCourantDitStop()
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantDitStop()
        flip.joueurCourantDitStop()

        assertEquals(EtatPartie.ATTENTE_CHOIX_JOUEUR,flip.etatPartie)
        flip.joueurCourantDitStop()
        assertEquals(EtatPartie.MANCHE_TERMINEE,flip.etatPartie)
        for (i in 0..3) {
            assertEquals(EtatJoueur.STOP,flip.etatJoueur[i])
        }
        assertEquals(1,flip.joueurCourant)

    }


















}