package iut.info1.flip7

import Joueur
import iut.info1.flip7.cartes.Carte
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import iut.info1.flip7.TestFlip7Constructeur.Companion.genererPiocheSansDebug
import iut.info1.flip7.cartes.CarteNum
import iut.info1.flip7.cartes.CarteStop
import iut.info1.flip7.etats.EtatJoueur
import iut.info1.flip7.etats.EtatPartie
import iut.info1.flip7.exceptions.CarteInvalideException
import iut.info1.flip7.exceptions.EtatPartieInvalideException
import iut.info1.flip7.exceptions.IndiceJoueurInvalideException
import iut.info1.flip7.exceptions.JoueurNonActifException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class TestFlip7JoueurCourantCibleStop {

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




    /**
     * Entrées : Partie à 2 joueurs en debug, pioche avec CarteStop, joueur courant pioche puis cible un joueur avec la carte Stop.
     *
     * Sorties : Aucune exception levée.
     *
     * Explication : Vérifie que joueurCourantCibleStop est appelable lorsque la partie est dans l'état ATTENTE_CIBLE_STOP.
     */
    @Test
    fun etatPartieCorrect() {
        val flip = Flip7(2, listOf(joueur0,joueur1), listOf(CarteStop(), CarteNum(5),CarteNum(2)), true)
        val carte = flip.joueurCourantPiocheUneCarte()

        assertDoesNotThrow { flip.joueurCourantCibleStop(carte,1) }
    }


    /**
     * Entrées : Même configuration, premier ciblage réussi, second appel alors que etatPartie n'est plus ATTENTE_CIBLE_STOP.
     *
     * Sorties : EtatPartieInvalideException levée au second appel.
     *
     * Explication : Colonne « etatPartie faux » de la table joueurCourantCibleStop.
     */
    @Test
    fun etatPartieIncorrect() {
        val flip = Flip7(2, listOf(joueur0,joueur1), listOf(CarteStop(), CarteNum(5),CarteNum(2)), true)
        val carte = flip.joueurCourantPiocheUneCarte()

        flip.joueurCourantCibleStop(carte,1)
        assertThrows<EtatPartieInvalideException> { flip.joueurCourantCibleStop(carte,1) }
    }

    /**
     * Entrées : Partie à 2 joueurs, carte Stop piocée, indice cible invalide (paramètre).
     *
     * Sorties : IndiceJoueurInvalideException levée.
     *
     * Explication : Vérifie que joueurCourantCibleStop rejette les indices de joueur cible hors limites.
     */
    @ParameterizedTest
    @MethodSource("generateurCiblesIncorrectes")
    fun cibleIncorrectes(cible : Int) {
        val flip = Flip7(2, listOf(joueur0,joueur1), listOf(CarteStop(), CarteNum(5),CarteNum(2)), true)
        val carte = flip.joueurCourantPiocheUneCarte()
        assertThrows<IndiceJoueurInvalideException>{flip.joueurCourantCibleStop(carte,cible)}
    }

    /**
     * Entrées : Joueur pioche CarteNum(6) puis CarteStop, tente de cibler avec la première carte (pas la Stop).
     *
     * Sorties : CarteInvalideException levée.
     *
     * Explication : Vérifie que seule la carte Stop actuellement piocée peut être utilisée pour cibler.
     */
    @Test
    fun cartePasseeIncorrecte() {
        val flip = Flip7(2, listOf(joueur0,joueur1), listOf(CarteNum(6),CarteStop(), CarteNum(5),CarteNum(2)), true)
        val carte = flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantPiocheUneCarte()
        assertThrows<CarteInvalideException>{flip.joueurCourantCibleStop(carte,1)}
    }


    /**
     * Entrées : Joueur 0 cible joueur 1 avec Stop (joueur 1 passe en STOP), joueur 0 pioche une nouvelle Stop et tente de cibler à nouveau le joueur 1.
     *
     * Sorties : JoueurNonActifException levée.
     *
     * Explication : Vérifie qu'on ne peut pas cibler un joueur qui n'est plus en état JOUE_ENCORE.
     */
    @Test
    fun joueurCibleNeJouePas() {
        val flip = Flip7(2, listOf(joueur0,joueur1), listOf(CarteStop(), CarteStop(),CarteNum(2)), true)
        val carte = flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantCibleStop(carte,1)
        val carte2 = flip.joueurCourantPiocheUneCarte()
        assertThrows<JoueurNonActifException>{flip.joueurCourantCibleStop(carte2,1)}
    }

    /**
     * Entrées : Pioche d'une seule CarteStop, joueur 0 pioche et se cible lui-même (cible = 0).
     *
     * Sorties : etatPartie = ATTENTE_CHOIX_JOUEUR, etatJoueur[0] = STOP, etatJoueur[1] = JOUE_ENCORE.
     *
     * Explication : Vérifie le comportement d'auto-Stop lorsqu'un joueur se cible lui-même.
     */
    @Test
    fun autoStop() {
        val flip = Flip7(2, listOf(joueur0,joueur1), listOf(CarteStop()), true)
        val carte = flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantCibleStop(carte,0)
        assertAll(
            {assertEquals(EtatPartie.ATTENTE_CHOIX_JOUEUR,flip.etatPartie)},
            {assertEquals(EtatJoueur.STOP,flip.etatJoueur[0])},
            {assertEquals(EtatJoueur.JOUE_ENCORE,flip.etatJoueur[1])},
        )
    }

    /**
     * Entrées : Partie à 4 joueurs, joueur 0 pioche CarteStop et cible joueur 1.
     *
     * Sorties : etatPartie correct, joueur 1 en STOP avec la carte en main, joueurs 0/2/3 encore actifs.
     *
     * Explication : Vérifie l'état complet de la partie après un ciblage Stop nominal.
     */
    @Test
    fun etatJeuApresAppelMethode() {
        val flip = Flip7(4, listOf(joueur0,joueur1,joueur2,joueur3), listOf(CarteStop(), CarteStop(),CarteNum(2)), true)
        val carte = flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantCibleStop(carte,1)
        assertAll(
            {assertEquals(EtatPartie.ATTENTE_CHOIX_JOUEUR,flip.etatPartie)},
            { for (idJoueur in listOf(flip.joueurCourant,2,3)){
                assertEquals(EtatJoueur.JOUE_ENCORE,flip.etatJoueur[idJoueur])
            }
            },
            {assertEquals(EtatJoueur.STOP,flip.etatJoueur[1])},
            {assertTrue(flip.main[1]!!.contains(carte))},
            {assertFalse(flip.main[flip.joueurCourant]!!.contains(carte))}
        )
    }

    companion object {
        @JvmStatic
        fun generateurCiblesIncorrectes(): Stream<Int> {
            return Stream.of(
                Int.MIN_VALUE,
                -1,
                2,
                3,
                Int.MAX_VALUE
            )
        }
    }
}













