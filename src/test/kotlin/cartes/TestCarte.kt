package cartes

import iut.info1.flip7.cartes.Carte
import iut.info1.flip7.cartes.Carte2ndeChance
import iut.info1.flip7.cartes.Carte3aLaSuite
import iut.info1.flip7.cartes.CarteBonusMultiplie
import iut.info1.flip7.cartes.CarteBonusPlus
import iut.info1.flip7.cartes.CarteNum
import iut.info1.flip7.cartes.CarteStop
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

/**
 * Tests unitaires pour la classe Carte.
 * Vérifie le comportement de reconnaissance des types de cartes.
 */
class TestCarte() {

 @ParameterizedTest
    @MethodSource("generateurTypeCarte")
    fun estCarte2ndeChance(carte : Carte) {
        val attendu = carte is Carte2ndeChance
        assertEquals(attendu, carte.estCarte2ndeChance())

        val attendu2 = carte is CarteStop
        assertEquals(attendu2, carte.estCarteStop())

        val attendu3 = carte is CarteNum
        assertEquals(attendu3, carte.estCarteNum())

        val attendu4 = carte is CarteBonusMultiplie
        assertEquals(attendu4, carte.estCarteBonusMultiplie())

        val attendu5 = carte is CarteBonusPlus
        assertEquals(attendu5, carte.estCarteBonusPlus())
    }


    companion object{

        val carte3 = Carte3aLaSuite()
        val carteStop = CarteStop()
        val carteNum = CarteNum(3)
        val carte2ndeChance = Carte2ndeChance()
        val carteBonusMultiple = CarteBonusMultiplie()
        val carteBonusPlus = CarteBonusPlus(2)

        @JvmStatic
        fun generateurTypeCarte(): Stream<Carte> {
            return Stream.of(
                carte3,
                carteStop,
                carteNum,
                carte2ndeChance,
                carteBonusPlus,
                carteBonusMultiple
            )
        }
    }

}