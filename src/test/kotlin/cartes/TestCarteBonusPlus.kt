package cartes
import iut.info1.flip7.cartes.Carte3aLaSuite
import iut.info1.flip7.cartes.CarteBonusPlus
import iut.info1.flip7.cartes.CarteNum
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


/**
 * Tests unitaires pour la classe CarteBonusPlus.
 * Vérifie le comportement du constructeur, la représentation textuelle (toString)
 * et la logique d'égalité (equals).
 */
class TestCarteBonusPlus {

    @Test
    fun cas1Constructeur() {
        val carteBonus = CarteBonusPlus(2)

        assertEquals(2, carteBonus.valeur)
    }

    @Test
    fun casValeurImpaireConstructeur() {
        assertThrows<IllegalArgumentException>{CarteBonusPlus(3)}
    }

    @Test
    fun casValeurMaxConstructeur() {
        assertThrows<IllegalArgumentException>{CarteBonusPlus(Int.MAX_VALUE )}
    }

    @Test
    fun casValeurMinConstructeur() {
        assertThrows<IllegalArgumentException>{CarteBonusPlus(Int.MIN_VALUE )}
    }

    @Test
    fun casValeurTropGrandeConstructeur() {
        assertThrows<IllegalArgumentException>{CarteBonusPlus(14)}
    }

    @Test
    fun casValeurNegativeConstructeur() {
        assertThrows<IllegalArgumentException>{CarteBonusPlus(-2)}
    }

    @Test
    fun cas2String() {
        val carte = CarteBonusPlus(2)

        assertEquals("[Carte Bonus +2]", carte.toString())
    }


    //Test sur la fonction equals : Test sur un objet et lui même,
    //Sur deux instances de la même carte
    //Sur deux cartes BonusPlus avec des valeurs différentes
    //Sur deux cartes différentes
    //Sur une carte et un type différent ou null
    @Test
    fun cas3Equals() {
        val carteA = CarteBonusPlus(2)
        val carteB = CarteBonusPlus(2)
        val carteC = Carte3aLaSuite()
        val carteD = CarteBonusPlus(4)
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