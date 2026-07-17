package iut.info1.flip7

import Joueur
import io.mockk.mockk
import io.mockk.verify
import iut.info1.flip7.cartes.Carte
import iut.info1.flip7.cartes.Carte2ndeChance
import iut.info1.flip7.cartes.Carte3aLaSuite
import iut.info1.flip7.cartes.CarteBonusMultiplie
import iut.info1.flip7.cartes.CarteBonusPlus
import iut.info1.flip7.cartes.CarteNum
import iut.info1.flip7.cartes.CarteStop
import iut.info1.flip7.cartes.IOutilsCarte
import iut.info1.flip7.exceptions.ListeJoueursInvalideException
import iut.info1.flip7.exceptions.NombreJoueursInvalideException
import iut.info1.flip7.exceptions.ScoreFinPartieInvalideException
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import iut.info1.flip7.etats.EtatJoueur
import iut.info1.flip7.etats.EtatPartie
import iut.info1.flip7.exceptions.PiocheInvalideException
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream


class TestFlip7Constructeur {

    // Le bloc ci-dessous contient des objets Joueur, Carte, outilsCarte etc... destinés à servir aux nombreux tests

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
     * Entrées : nbJoueurs = -3, liste de 4 joueurs, pioche valide (sans debug).
     *
     * Sorties : NombreJoueursInvalideException levée.
     *
     * Explication : Vérifie que le constructeur rejette un nombre de joueurs négatif. Partie tests du constructeur.
     */
    @Test
    fun testNombreJoueursInvalides1() {
        assertThrows<NombreJoueursInvalideException> {
            val flip7 = Flip7(-3, listOf(joueur0, joueur1, joueur2, joueur3), genererPiocheSansDebug())
        }
    }

    /**
     * Entrées : nbJoueurs = Int.MAX_VALUE, liste de 4 joueurs, pioche valide.
     *
     * Sorties : NombreJoueursInvalideException levée.
     *
     * Explication : Vérifie que le constructeur rejette un nombre de joueurs supérieur à la limite autorisée.
     */
    @Test
    fun testNombreJoueursInvalides2() {
        assertThrows<NombreJoueursInvalideException> {
            val flip7 = Flip7(Int.MAX_VALUE, listOf(joueur0, joueur1, joueur2, joueur3), genererPiocheSansDebug())
        }
    }

    /**
     * Entrées : scoreFinPartie invalide (paramètre du générateur), 4 joueurs, pioche valide.
     *
     * Sorties : ScoreFinPartieInvalideException levée.
     *
     * Explication : Vérifie que le constructeur rejette les scores de fin de partie hors de l'intervalle autorisé [50, 200].
     */
    @ParameterizedTest
    @MethodSource("generateurScoresInvalides")
    fun testScoresFinPartieInvalide(score: Int) {
        assertThrows<ScoreFinPartieInvalideException> {
            val flip7 =
                Flip7(4, listOf(joueur0, joueur1, joueur2, joueur3), genererPiocheSansDebug(), scoreFinPartie = score)
        }
    }


    /**
     * Entrées : scoreFinPartie valide (paramètre du générateur), 2 joueurs, pioche valide.
     *
     * Sorties : Aucune exception levée.
     *
     * Explication : Vérifie que le constructeur accepte les scores de fin de partie dans l'intervalle autorisé.
     */
    @ParameterizedTest
    @MethodSource("generateurScoresValides")
    fun testScoresFinPartieValides(score: Int) {
        assertDoesNotThrow {
            val flip7 = Flip7(2, listOf(joueur0, joueur1), genererPiocheSansDebug(), scoreFinPartie = score)
        }
    }

    /**
     * Entrées : nbJoueurs = 2, liste de 3 joueurs, pioche valide.
     *
     * Sorties : ListeJoueursInvalideException levée.
     *
     * Explication : Vérifie que le constructeur rejette une incohérence entre nbJoueurs et la taille de la liste de joueurs.
     */
    @Test
    fun testIncoherenceNbJoueursEtTailleJoueurs1() {
        assertThrows<ListeJoueursInvalideException> {
            val flip7 = Flip7(2, listOf(joueur0, joueur1, joueur2), genererPiocheSansDebug())
        }
    }

    /**
     * Entrées : debug = true, pioche minimale, mock IOutilsCarte.
     *
     * Sorties : verifiePiocheInitiale n'est jamais appelé.
     *
     * Explication : En mode debug, la vérification de la pioche initiale ne doit pas être effectuée.
     */
    @Test
    fun testDebugTrueOutilsCarteNonAppele() {
        val outilsCarteMock = mockk<IOutilsCarte>(relaxed=true)
        val flip7 = Flip7(3, listOf(joueur0, joueur1, joueur2), listOf(CarteNum(1)),debug = true, outilsCarte = outilsCarteMock)
        verify(exactly=0) {
            outilsCarteMock.verifiePiocheInitiale(any())
        }
    }

    /**
     * Entrées : nbJoueurs = 2, liste de joueurs vide, pioche valide.
     *
     * Sorties : ListeJoueursInvalideException levée.
     *
     * Explication : Vérifie que le constructeur rejette une liste de joueurs vide.
     */
    @Test
    fun testListeJoueursVide() {
        assertThrows<ListeJoueursInvalideException> {
            val flip7 = Flip7(2, listOf(), genererPiocheSansDebug())
        }
    }

    /**
     * Entrées : 2 joueurs, pioche valide.
     *
     * Sorties : flip7.joueurs[0] === joueur0 (même référence).
     *
     * Explication : Vérifie que les joueurs passés au constructeur sont conservés par référence.
     */
    @Test
    fun testPassageParReference() {
        val flip7 = Flip7(2, listOf(joueur0, joueur1), genererPiocheSansDebug())
        assertTrue(flip7.joueurs[0] === joueur0)
    }

    /**
     * Entrées : debug = true, pioche de 4 cartes (CarteNum(2), CarteNum(4), CarteNum(7), CarteStop).
     *
     * Sorties : Aucune exception levée.
     *
     * Explication : Vérifie qu'en mode debug une pioche de 4 cartes minimum est acceptée sans validation complète.
     */
    @Test
    fun debugTruePioche4Cartes() {
        assertDoesNotThrow {
            val flip7 = Flip7(
                2,
                listOf(joueur2, joueur3),
                cartes = listOf<Carte>(CarteNum(2), CarteNum(4), CarteNum(7), CarteStop()),
                debug = true
            )
        }
    }

    /**
     * Entrées : debug = false, pioche invalide (paramètre du générateur).
     *
     * Sorties : PiocheInvalideException levée.
     *
     * Explication : Vérifie que le constructeur rejette les pioches invalides hors mode debug.
     */
    @ParameterizedTest
    @MethodSource("generateurPiochesInvalides")
    fun debugFalsePiochesInvalides(mauvaisesPioches: List<Carte>) {
        assertThrows<PiocheInvalideException> {
            val flip7 = Flip7(2, listOf(joueur2, joueur3), cartes = mauvaisesPioches)
        }
    }


    /**
     * Entrées : 3 joueurs, pioche standard (94 cartes), debug = false.
     *
     * Sorties : Flip7 correctement initialisé (nbJoueurs, debug, scoreFinPartie, taillePioche, score, main, etatPartie, etatJoueur, joueurCourant, defausse).
     *
     * Explication : Cas nominal d'instanciation sans mode debug avec vérification de tous les attributs initiaux.
     */
    @Test
    fun casNominalInstanciationFlip7debugFalse() {
        val flip7 = Flip7(3, listOf(joueur0, joueur1, joueur2), genererPiocheSansDebug())

        assertAll(
            { assertEquals(3, flip7.nbJoueurs) },
            { assertEquals(false, flip7.debug) },
            { assertEquals(200, flip7.scoreFinPartie) },
            { assertEquals(94, flip7.taillePioche) },
            { assertEquals(mapOf(
                0 to 0,
                1 to 0,
                2 to 0
            ), flip7.score) },

            {
                assertEquals(
                    mapOf(
                        0 to listOf<Carte>(),
                        1 to listOf<Carte>(),
                        2 to listOf<Carte>(),
                    ), flip7.main
                )
            },

            { assertEquals(EtatPartie.ATTENTE_CHOIX_JOUEUR, flip7.etatPartie) },
            { assertTrue(flip7.etatJoueur.values.all { it == EtatJoueur.JOUE_ENCORE }) },
            { assertEquals(0, flip7.joueurCourant) },
            { assertEquals(listOf<Carte>(), flip7.defausse) },
        )
    }

    /**
     * Entrées : 2 joueurs, pioche triple (282 cartes), debug = true, scoreFinPartie = 80.
     *
     * Sorties : Flip7 correctement initialisé (nbJoueurs, debug, taillePioche, scoreFinPartie).
     *
     * Explication : Cas nominal d'instanciation en mode debug avec pioche personnalisée.
     */
    @Test
    fun casNominalVerificationsDebugTrue() {
        val flip7 = Flip7(
            2,
            listOf(joueur0, joueur1),
            genererPiocheSansDebug() + genererPiocheSansDebug() + genererPiocheSansDebug(),
            debug = true,
            scoreFinPartie = 80
        )

        assertAll(
            { assertEquals(2, flip7.nbJoueurs) },
            { assertEquals(true, flip7.debug) },
            { assertEquals(282, flip7.taillePioche) },
            { assertEquals(80, flip7.scoreFinPartie) },
        )
    }





    companion object {
        fun genererPiocheSansDebug(): List<Carte> {
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

        @JvmStatic
        fun generateurPiochesInvalides(): Stream<List<Carte>> {
            return Stream.of(
                emptyList(),
                listOf(CarteNum(1)),
                listOf(CarteStop()),
                genererPiocheSansDebug().toMutableList().dropLast(1),
                genererPiocheSansDebug() + Carte3aLaSuite()
            )

        }

        fun donneCarteNum(valeur : Map<Int,Int>) : MutableList<Carte> {
            val dico = valeur
            var res = mutableListOf<Carte>()
            for (i in dico.keys) {
                res.add(CarteNum(i))
            }
            return res
        }

        @JvmStatic
        fun generateurScoresValides(): Stream<Int> {
            return Stream.of(
                50,
                51,
                100,
                199,
                200
            )

        }

        @JvmStatic
        fun generateurScoresInvalides(): Stream<Int> {
            return Stream.of(
                Int.MIN_VALUE,
                -1,
                0,
                49,
                201,
                Int.MAX_VALUE
            )
        }
    }
}






