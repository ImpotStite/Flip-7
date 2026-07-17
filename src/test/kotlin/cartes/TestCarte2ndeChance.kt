package cartes
import iut.info1.flip7.cartes.Carte
import iut.info1.flip7.cartes.Carte2ndeChance
import iut.info1.flip7.cartes.Carte3aLaSuite
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Tests unitaires pour la classe Carte2ndeChance.
 * Vérifie le comportement du constructeur, la représentation textuelle (toString)
 * et la logique d'égalité (equals).
 */
class TestCarte2ndeChance {

    @Test
    fun cas1Constructeur() {
        val carteChance = Carte2ndeChance()

        assertEquals(0, carteChance.valeur)
    }

    @Test
    fun cas2String() {
        val carte = Carte2ndeChance()

        assertEquals("[Carte 2nde chance]", carte.toString())
    }


    //Test sur la fonction equals : Test sur un objet et lui même,
    //Sur deux instances de la même carte
    //Sur deux cartes différentes
    //Sur une carte et un type différent ou null
    @Test
    fun cas3Equals() {
        val carteA = Carte2ndeChance()
        val carteB = Carte2ndeChance()
        val carteC = Carte3aLaSuite()
        val autreObjet = "Une chaîne de caractères"

        assertTrue(carteA.equals(carteA))

        assertTrue(carteA.equals(carteB))
        assertTrue(carteB.equals(carteA))


        assertFalse(carteB.equals(carteC))
        assertFalse(carteA.equals(autreObjet))
        assertFalse(carteA.equals(null))
    }

}