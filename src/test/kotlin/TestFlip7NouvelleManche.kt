package iut.info1.flip7

import Joueur
import iut.info1.flip7.cartes.Carte
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import iut.info1.flip7.TestFlip7Constructeur.Companion.genererPiocheSansDebug
import iut.info1.flip7.cartes.CarteBonusMultiplie
import iut.info1.flip7.cartes.CarteNum
import iut.info1.flip7.etats.EtatJoueur
import iut.info1.flip7.exceptions.EtatPartieInvalideException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class TestFlip7NouvelleManche {

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
     * Entrées : Partie où les deux joueurs ont dit Stop et scoreManche a été appelé (état NOUVELLE_MANCHE).
     *
     * Sorties : Aucune exception levée lors de nouvelleManche().
     *
     * Explication : Vérifie que nouvelleManche() est appelable dans le bon état de partie.
     */
    @Test
    fun etatPartieCorrect() {
        val flip = genererFlip7(listOf(joueur0,joueur1),genererPiocheSansDebug())
        flip.joueurCourantDitStop()
        flip.joueurCourantDitStop()
        flip.scoreManche()
        assertDoesNotThrow {
            flip.nouvelleManche()
        }
    }

    /**
     * Entrées : Partie où un seul joueur a dit Stop (état incorrect).
     *
     * Sorties : EtatPartieInvalideException levée.
     *
     * Explication : Vérifie que nouvelleManche() ne peut pas être appelée avant la fin de la manche.
     */
    @Test
    fun etatPartieIncorrect() {
        val flip = genererFlip7(listOf(joueur0,joueur1),genererPiocheSansDebug())
        flip.joueurCourantDitStop()
        assertThrows<EtatPartieInvalideException> {
            flip.nouvelleManche()
        }
    }

    /**
     * Entrées : Manche jouée avec un joueur PERDU et un STOP, puis nouvelleManche().
     *
     * Sorties : Tous les joueurs repassent en JOUE_ENCORE.
     *
     * Explication : Vérifie la réinitialisation des états joueur entre deux manches.
     */
    @Test
    fun testResetEtatJoueur() {
        val flip = Flip7(2,listOf(joueur0,joueur1),listOf(CarteNum(12),CarteNum(12)),debug = true)
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantDitStop()
        flip.joueurCourantPiocheUneCarte()
        flip.scoreManche()
        assertEquals(EtatJoueur.PERDU,flip.etatJoueur[0])
        assertEquals(EtatJoueur.STOP,flip.etatJoueur[1])
        flip.nouvelleManche()
        assertEquals(EtatJoueur.JOUE_ENCORE,flip.etatJoueur[0])
        assertEquals(EtatJoueur.JOUE_ENCORE,flip.etatJoueur[1])
    }


    /**
     * Entrées : Partie à 3 joueurs, manche terminée avec joueurCourant = 2.
     *
     * Sorties : Après nouvelleManche(), joueurCourant = 0 (round-robin).
     *
     * Explication : Vérifie que l'index du joueur courant revient à 0 au début d'une nouvelle manche.
     */
    @Test
    fun testindexRoundRobin() {
        val flip = Flip7(3,listOf(joueur0,joueur1,joueur2),listOf(CarteNum(12),CarteNum(12)),debug = true)
        flip.joueurCourantDitStop()
        flip.joueurCourantDitStop()
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantDitStop()
        flip.scoreManche()
        assertEquals(2,flip.joueurCourant)
        flip.nouvelleManche()
        assertEquals(0,flip.joueurCourant)

    }

    /**
     * Entrées : Manche avec cartes en main des joueurs, puis scoreManche() et nouvelleManche().
     *
     * Sorties : Mains vidées (1 carte en moins pour joueur 0), défausse enrichie de 2 cartes.
     *
     * Explication : Vérifie que les cartes des mains sont déplacées vers la défausse en début de nouvelle manche.
     */
    @Test
    fun testVidageMainsDansDefausse() {
        val flip = genererFlip7(listOf(joueur0,joueur1),listOf(
            CarteNum(12),
            CarteNum(11),
            CarteNum(10),
            CarteBonusMultiplie()
        ))
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantPiocheUneCarte()
        flip.joueurCourantDitStop()
        flip.joueurCourantDitStop()
        val mainsJoueur0AvantFin = flip.main[0]!!.size
        val tailleDefausseAvantFin = flip.defausse.size
        flip.scoreManche()
        flip.nouvelleManche()
        assertEquals(1, mainsJoueur0AvantFin - flip.main[0]!!.size)
        assertEquals(2,flip.defausse.size - tailleDefausseAvantFin)

    }






}