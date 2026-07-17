package iut.info1.flip7

import Joueur
import iut.info1.flip7.TestFlip7Constructeur.Companion.genererPiocheSansDebug
import iut.info1.flip7.cartes.Carte2ndeChance
import iut.info1.flip7.cartes.Carte3aLaSuite
import iut.info1.flip7.cartes.CarteBonusMultiplie
import iut.info1.flip7.cartes.CarteBonusPlus
import iut.info1.flip7.cartes.CarteNum
import iut.info1.flip7.cartes.CarteStop
import iut.info1.flip7.etats.EtatJoueur
import iut.info1.flip7.etats.EtatPartie
import iut.info1.flip7.exceptions.CarteInvalideException
import iut.info1.flip7.exceptions.EtatPartieInvalideException
import iut.info1.flip7.exceptions.IndiceJoueurInvalideException
import iut.info1.flip7.exceptions.JoueurNonActifException
import iut.info1.flip7.exceptions.PiocheInvalideException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class TestFlip7Cible3aLaSuite {

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
     * Entrées : Flip7 à 2 joueurs avec pioche commençant par une Carte3aLaSuite.
     *
     * Sorties : etatPartie vaut ATTENTE_CIBLE_3SUITE après la pioche.
     *
     * Explication : Vérifier que l'état de la partie est correct après pioche d'une carte 3 à la suite.
     */
    @Test
    fun etatPartieCorrect() {
        val flip = Flip7(2, listOf(joueur0,joueur1), listOf(Carte3aLaSuite(), CarteNum(5),CarteNum(2),
            CarteBonusMultiplie()), true)
        flip.joueurCourantPiocheUneCarte()
        assertEquals(EtatPartie.ATTENTE_CIBLE_3SUITE, flip.etatPartie)
    }


    /**
     * Entrées : Flip7 à 2 joueurs, carte 3 à la suite piochée puis ciblée une première fois, tentative de second ciblage.
     *
     * Sorties : EtatPartieInvalideException lors du second appel à joueurCourantCible3aLaSuite().
     *
     * Explication : Vérifier qu'on ne peut pas cibler deux fois la même carte 3 à la suite.
     */
    @Test
    fun etatPartieIncorrect() {
        val flip = Flip7(2, listOf(joueur0,joueur1), listOf(Carte3aLaSuite(), CarteNum(5),CarteNum(2),
            CarteBonusMultiplie()), true)
        val carte = flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantCible3aLaSuite(carte,1)
        assertThrows<EtatPartieInvalideException> {flip.joueurCourantCible3aLaSuite(carte,2)}
    }


    /**
     * Entrées : Flip7 à 2 joueurs avec pioche ne contenant qu'une Carte3aLaSuite.
     *
     * Sorties : PiocheInvalideException lors du ciblage, faute de cartes suffisantes en pioche.
     *
     * Explication : Vérifier le comportement lorsqu'il n'y a pas assez de cartes à distribuer.
     */
    @Test
    fun testManqueDesCartes() {
        val flip = Flip7(2, listOf(joueur0,joueur1), listOf(Carte3aLaSuite()),true)
        val carte = flip.joueurCourantPiocheUneCarte()
        assertThrows<PiocheInvalideException> {flip.joueurCourantCible3aLaSuite(carte,1)}

    }

    /**
     * Entrées : etatPartie ATTENTE_CIBLE_3SUITE, Carte3aLaSuite piochée, appel joueurCourantCible3aLaSuite(carte, cible) avec cible invalide (Int.MIN_VALUE, -1, 2, 3, Int.MAX_VALUE).
     *
     * Sorties : IndiceJoueurInvalideException pour chaque cible incorrecte.
     *
     * Explication : Colonne « valeur de joueurCible fausse » de la table joueurCourantCible3aLaSuite.
     */
    @ParameterizedTest
    @MethodSource("generateurCiblesIncorrectes")
    fun cibleIncorrectes(cible : Int) {
        val flip = Flip7(2, listOf(joueur0,joueur1), listOf(Carte3aLaSuite(), CarteNum(5),CarteNum(2), CarteBonusMultiplie()), true)
        val carte = flip.joueurCourantPiocheUneCarte()
        assertThrows<IndiceJoueurInvalideException>{flip.joueurCourantCible3aLaSuite(carte,cible)}
    }

    /**
     * Entrées : Flip7 à 2 joueurs, carte 3 à la suite piochée puis une autre carte piochée avant le ciblage.
     *
     * Sorties : CarteInvalideException lors de joueurCourantCible3aLaSuite() avec la mauvaise carte.
     *
     * Explication : Vérifier qu'on ne peut cibler qu'avec la carte 3 à la suite effectivement piochée.
     */
    @Test
    fun cartePasseeIncorrecte() {
        val flip = Flip7(2, listOf(joueur0,joueur1), listOf(CarteNum(7), Carte3aLaSuite(), CarteNum(5),CarteNum(2)), true)
        val carte = flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantPiocheUneCarte()
        assertThrows<CarteInvalideException>{flip.joueurCourantCible3aLaSuite(carte,1)}
    }

    /**
     * Entrées : Flip7 à 2 joueurs, joueur cible arrêté par une CarteStop avant pioche d'une Carte3aLaSuite.
     *
     * Sorties : JoueurNonActifException lors du ciblage du joueur inactif.
     *
     * Explication : Vérifier qu'on ne peut pas cibler un joueur qui ne joue plus.
     */
    @Test
    fun joueurCibleNeJouePas() {
        val flip = Flip7(2, listOf(joueur0,joueur1), listOf(CarteStop(), Carte3aLaSuite(),CarteNum(2)), true)
        val carte = flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantCibleStop(carte,1)
        val carte2 = flip.joueurCourantPiocheUneCarte()
        assertThrows<JoueurNonActifException>{flip.joueurCourantCible3aLaSuite(carte2,1)}
    }

    /**
     * Entrées : Flip7 à 4 joueurs, pioche Carte3aLaSuite suivie de trois CarteNum, cible joueur 1.
     *
     * Sorties : ATTENTE_CHOIX_JOUEUR, tous les joueurs JOUE_ENCORE, 4 cartes ajoutées à la main du joueur 1, 3 cartes retirées de la pioche, retour listOf(CarteNum(0), CarteNum(1), CarteNum(2)).
     *
     * Explication : Cas nominal, distribution de trois cartes numériques consécutives au joueur cible.
     */
    @Test
    fun casNominal1() {
        val flip = Flip7(4,listOf(joueur0,joueur1,joueur2,joueur3), listOf(Carte3aLaSuite(),CarteNum(0),CarteNum(1),CarteNum(2)),debug = true)
        val carte = flip.joueurCourantPiocheUneCarte()
        val actualMainJoueur1 = flip.main[1]!!.size
        val actualTaillePioche = flip.taillePioche
        val sortie = flip.joueurCourantCible3aLaSuite(carte,1)
        assertAll(
            {assertEquals(EtatPartie.ATTENTE_CHOIX_JOUEUR,flip.etatPartie)},
            {for (nbJoueur in listOf(0,1,2,3)) {
                assertEquals(EtatJoueur.JOUE_ENCORE,flip.etatJoueur[nbJoueur])
            }},
            {assertEquals(4,flip.main[1]!!.size - actualMainJoueur1)},
            {assertEquals(3, actualTaillePioche - flip.taillePioche)},
            {assertEquals(listOf(CarteNum(0),CarteNum(1),CarteNum(2)), sortie)}

        )

    }


    /**
     * Entrées : Flip7 à 4 joueurs, pioche avec Carte3aLaSuite, CarteStop puis trois CarteNum, cible joueur 1.
     *
     * Sorties : ATTENTE_CHOIX_JOUEUR, tous JOUE_ENCORE, 4 cartes en main du joueur 1, 4 cartes retirées de la pioche, retour des trois CarteNum distribuées.
     *
     * Explication : Cas nominal avec une CarteStop ignorée lors de la distribution.
     */
    @Test
    fun casNominal2() {
        val flip = Flip7(4,listOf(joueur0,joueur1,joueur2,joueur3), listOf(Carte3aLaSuite(), CarteStop(),CarteNum(0),CarteNum(1),CarteNum(2)),debug = true)
        val carte = flip.joueurCourantPiocheUneCarte()
        val actualMainJoueur1 = flip.main[1]!!.size
        val actualTaillePioche = flip.taillePioche
        val sortie = flip.joueurCourantCible3aLaSuite(carte,1)
        assertAll(
            {assertEquals(EtatPartie.ATTENTE_CHOIX_JOUEUR,flip.etatPartie)},
            {for (nbJoueur in listOf(0,1,2,3)) {
                assertEquals(EtatJoueur.JOUE_ENCORE,flip.etatJoueur[nbJoueur])
            }},
            {assertEquals(4,flip.main[1]!!.size - actualMainJoueur1)},
            {assertEquals(4, actualTaillePioche - flip.taillePioche)},
            {assertEquals(listOf(CarteNum(0),CarteNum(1),CarteNum(2)), sortie)}

        )
    }


    /**
     * Entrées : Flip7 à 4 joueurs, pioche avec Carte3aLaSuite, deux CarteStop et trois CarteNum, cible joueur 1.
     *
     * Sorties : ATTENTE_CHOIX_JOUEUR, tous JOUE_ENCORE, 4 cartes en main du joueur 1, 5 cartes retirées de la pioche, retour des trois CarteNum distribuées.
     *
     * Explication : Cas nominal avec plusieurs CarteStop ignorées lors de la distribution.
     */
    @Test
    fun casNominal3() {
        val flip = Flip7(4,listOf(joueur0,joueur1,joueur2,joueur3), listOf(Carte3aLaSuite(), CarteStop(),CarteNum(0),CarteNum(1),CarteStop(),CarteNum(2)),debug = true)
        val carte = flip.joueurCourantPiocheUneCarte()
        val actualMainJoueur1 = flip.main[1]!!.size
        val actualTaillePioche = flip.taillePioche
        val sortie = flip.joueurCourantCible3aLaSuite(carte,1)
        assertAll(
            {assertEquals(EtatPartie.ATTENTE_CHOIX_JOUEUR,flip.etatPartie)},
            {for (nbJoueur in listOf(0,1,2,3)) {
                assertEquals(EtatJoueur.JOUE_ENCORE,flip.etatJoueur[nbJoueur])
            }},
            {assertEquals(4,flip.main[1]!!.size - actualMainJoueur1)},
            {assertEquals(5, actualTaillePioche - flip.taillePioche)},
            {assertEquals(listOf(CarteNum(0),CarteNum(1),CarteNum(2)), sortie)}

        )
    }


    /**
     * Entrées : Flip7 à 4 joueurs, pioche avec Carte3aLaSuite, doublon CarteNum(2) et CarteStop, cible joueur 1.
     *
     * Sorties : ATTENTE_CHOIX_JOUEUR, joueur 1 PERDU, 3 cartes en main, 2 cartes retirées de la pioche, retour listOf(CarteNum(2), CarteNum(2)).
     *
     * Explication : Cas nominal où un doublon fait perdre le joueur cible avant d'atteindre trois cartes.
     */
    @Test
    fun casNominal4() {
        val flip = Flip7(4,listOf(joueur0,joueur1,joueur2,joueur3), listOf(Carte3aLaSuite(), CarteNum(2),CarteNum(2),CarteNum(1),CarteStop(),CarteNum(8)),debug = true)
        val carte = flip.joueurCourantPiocheUneCarte()
        val actualTaillePioche = flip.taillePioche
        val sortie = flip.joueurCourantCible3aLaSuite(carte,1)
        assertAll(
            {assertEquals(EtatPartie.ATTENTE_CHOIX_JOUEUR,flip.etatPartie)},
            {assertEquals(EtatJoueur.PERDU, flip.etatJoueur[1])},
            {for (nbJoueur in listOf(0,2,3)) {
                assertEquals(EtatJoueur.JOUE_ENCORE,flip.etatJoueur[nbJoueur])
            }},
            {assertEquals(3,flip.main[1]!!.size)},
            {assertEquals(2, actualTaillePioche - flip.taillePioche)},
            {assertEquals(listOf(CarteNum(2),CarteNum(2)), sortie)}

        )
    }

    /**
     * Entrées : Flip7 à 4 joueurs, pioche avec Carte3aLaSuite, cartes bonus et CarteStop, cible joueur 1.
     *
     * Sorties : ATTENTE_CHOIX_JOUEUR, tous JOUE_ENCORE, 4 cartes en main du joueur 1, 3 cartes retirées de la pioche, retour des trois cartes bonus distribuées.
     *
     * Explication : Cas nominal avec distribution de cartes bonus.
     */
    @Test
    fun casNominal5() {
        val flip = Flip7(4,listOf(joueur0,joueur1,joueur2,joueur3), listOf(Carte3aLaSuite(), CarteBonusMultiplie(),
            CarteBonusPlus(6),CarteNum(1),CarteStop(),CarteNum(8)),debug = true)
        val carte = flip.joueurCourantPiocheUneCarte()
        val actualMainJoueur1 = flip.main[1]!!.size
        val actualTaillePioche = flip.taillePioche
        val sortie = flip.joueurCourantCible3aLaSuite(carte,1)
        assertAll(
            {assertEquals(EtatPartie.ATTENTE_CHOIX_JOUEUR,flip.etatPartie)},
            {for (nbJoueur in listOf(0,1,2,3)) {
                assertEquals(EtatJoueur.JOUE_ENCORE,flip.etatJoueur[nbJoueur])
            }},
            {assertEquals(4,flip.main[1]!!.size - actualMainJoueur1)},
            {assertEquals(3, actualTaillePioche - flip.taillePioche)},
            {assertEquals(listOf(CarteBonusMultiplie(),CarteBonusPlus(6),CarteNum(1)), sortie)}

        )
    }

    /**
     * Entrées : Flip7 à 4 joueurs, pioche avec Carte3aLaSuite, Carte2ndeChance et doublon CarteNum(5), cible joueur 1.
     *
     * Sorties : ATTENTE_CHOIX_JOUEUR, tous JOUE_ENCORE, 2 cartes ajoutées à la main du joueur 1, 3 cartes retirées de la pioche, retour listOf(CarteNum(5), Carte2ndeChance(), CarteNum(5)).
     *
     * Explication : Cas nominal avec cartes 2nde chance et doublon dans la distribution.
     */
    @Test
    fun casNominal6() {
        val flip = Flip7(4,listOf(joueur0,joueur1,joueur2,joueur3), listOf(Carte3aLaSuite(), CarteNum(5),
            Carte2ndeChance(),CarteNum(5),CarteStop(),CarteNum(8)),debug = true)
        val carte = flip.joueurCourantPiocheUneCarte()
        val actualMainJoueur1 = flip.main[1]!!.size
        val actualTaillePioche = flip.taillePioche
        val sortie = flip.joueurCourantCible3aLaSuite(carte,1)
        assertAll(
            {assertEquals(EtatPartie.ATTENTE_CHOIX_JOUEUR,flip.etatPartie)},
            {for (nbJoueur in listOf(0,1,2,3)) {
                assertEquals(EtatJoueur.JOUE_ENCORE,flip.etatJoueur[nbJoueur])
            }},
            {assertEquals(2,flip.main[1]!!.size - actualMainJoueur1)},
            {assertEquals(3, actualTaillePioche - flip.taillePioche)},
            {assertEquals(listOf(CarteNum(5), Carte2ndeChance(),CarteNum(5)), sortie)}
        )
    }

    /**
     * Entrées : Flip7 à 2 joueurs, cycle de manche puis pioche Carte3aLaSuite avec pioche reconstituée depuis la défausse, cible joueur 1.
     *
     * Sorties : défausse vide, 4 cartes en main du joueur 1, retour listOf(CarteNum(6), CarteNum(8), CarteNum(10)).
     *
     * Explication : Cas nominal où la pioche est reconstituée depuis la défausse avant la distribution.
     */
    @Test
    fun casNominalDepilageDefausse() {
        val flip = Flip7(2,listOf(joueur0,joueur1), listOf(CarteNum(10),CarteNum(11), Carte3aLaSuite(),
            CarteNum(6),CarteNum(8)),debug = true)
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantDitStop()
        flip.joueurCourantDitStop()
        flip.scoreManche()
        flip.nouvelleManche()
        val carte = flip.joueurCourantPiocheUneCarte()
        val sortie = flip.joueurCourantCible3aLaSuite(carte,1)
        assertEquals(0,flip.defausse.size)
        assertEquals(4,flip.main[1]!!.size)
        assertEquals(listOf(CarteNum(6),CarteNum(8),CarteNum(10)), sortie)

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