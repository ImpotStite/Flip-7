package cartes
import iut.info1.flip7.cartes.Carte3aLaSuite
import iut.info1.flip7.cartes.CarteNum
import iut.info1.flip7.cartes.CarteStop
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


/**
 * Tests unitaires pour la classe CarteNum.
 * Vérifie le comportement du constructeur, la représentation textuelle (toString)
 * et la logique d'égalité (equals).
 */
class TestCarteNum {
    @Test
    fun cas1Constructeur() {
        val carteNum = CarteNum(2)

        assertEquals(2, carteNum.valeur)
    }

    @Test
    fun casValeurImpaireGrandeConstructeur() {
        val carteNum = CarteNum(3)

        assertEquals(3, carteNum.valeur)
    }

    @Test
    fun casValeurMinConstructeur() {
        assertThrows<IllegalArgumentException>{CarteNum(Int.MIN_VALUE)}
    }

    @Test
    fun casValeurMaxConstructeur() {
        assertThrows<IllegalArgumentException>{CarteNum(Int.MAX_VALUE)}
    }

    @Test
    fun casValeurTropGrandeConstructeur() {
        assertThrows<IllegalArgumentException>{CarteNum(14)}
    }

    @Test
    fun casValeurNegativeConstructeur() {
        assertThrows<IllegalArgumentException>{CarteNum(-2)}
    }

    @Test
    fun casString() {
        val carte = CarteNum(3)

        assertEquals("[Carte n°3]", carte.toString())
    }

    @Test
    fun cas2String() {
        val carte = CarteNum(12)

        assertEquals("[Carte n°12]", carte.toString())
    }

    //Test sur la fonction equals : Test sur un objet et lui même,
    //Sur deux instances de la même carte
    //Sur deux cartes Num avec des valeurs différentes
    //Sur deux cartes différentes
    //Sur une carte et un type différent ou null
    @Test
    fun cas3Equals() {
        val carteA = CarteNum(12)
        val carteB = CarteNum(12)
        val carteC = Carte3aLaSuite()
        val carteD = CarteNum(1)
        val autreObjet = "Une chaîne de caractères"

        assertTrue(carteA.equals(carteA))

        assertTrue(carteA.equals(carteB))
        assertTrue(carteB.equals(carteA))

        assertFalse(carteB.equals(carteD))
        assertFalse(carteB.equals(carteC))
        assertFalse(carteA.equals(autreObjet))
        assertFalse(carteA.equals(null))
    }
}