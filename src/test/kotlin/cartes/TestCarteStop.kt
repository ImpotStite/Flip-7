package cartes
import iut.info1.flip7.cartes.Carte2ndeChance
import iut.info1.flip7.cartes.Carte3aLaSuite
import iut.info1.flip7.cartes.CarteStop
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


/**
 * Tests unitaires pour la classe CarteStop.
 * Vérifie le comportement du constructeur, la représentation textuelle (toString)
 * et la logique d'égalité (equals).
 */
class TestCarteStop {

    @Test
    fun cas1Constructeur() {
        val carteStop = CarteStop()

        assertEquals(0, carteStop.valeur)
    }

    @Test
    fun casString() {
        val carteStop = CarteStop()

        assertEquals("[Carte Stop]", carteStop.toString())
    }

    //Test sur la fonction equals : Test sur un objet et lui même,
    //Sur deux instances de la même carte
    //Sur deux cartes différentes
    //Sur une carte et un type différent ou null
    @Test
    fun cas3Equals() {
        val carteA = CarteStop()
        val carteB = CarteStop()
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