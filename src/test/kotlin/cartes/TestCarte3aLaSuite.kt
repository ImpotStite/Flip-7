package cartes
import iut.info1.flip7.cartes.Carte2ndeChance
import iut.info1.flip7.cartes.Carte3aLaSuite
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


/**
 * Tests unitaires pour la classe Carte3aLaSuite.
 * Vérifie le comportement du constructeur, la représentation textuelle (toString)
 * et la logique d'égalité (equals).
 */
class TestCarte3aLaSuite {
    @Test
    fun cas1Constructeur() {
        val carte3aLaSuite = Carte3aLaSuite()

        assertEquals(0, carte3aLaSuite.valeur)
    }

    @Test
    fun cas2String() {
        val carte = Carte3aLaSuite()

        assertEquals("[Carte 3 à la suite]", carte.toString())
    }

    //Test sur la fonction equals : Test sur un objet et lui même,
    //Sur deux instances de la même carte
    //Sur deux cartes différentes
    //Sur une carte et un type différent ou null
    @Test
    fun cas3Equals() {
        val carteA = Carte3aLaSuite()
        val carteB = Carte3aLaSuite()
        val carteC = Carte2ndeChance()
        val autreObjet = "Une chaîne de caractères"

        assertTrue(carteA.equals(carteA))

        assertTrue(carteA.equals(carteB))
        assertTrue(carteB.equals(carteA))

        assertFalse(carteA.equals(carteC))
        assertFalse(carteA.equals(autreObjet))
        assertFalse(carteA.equals(null))
    }
}