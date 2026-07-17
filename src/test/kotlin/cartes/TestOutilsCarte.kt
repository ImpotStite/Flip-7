package cartes
import iut.info1.flip7.cartes.Carte
import iut.info1.flip7.cartes.Carte2ndeChance
import iut.info1.flip7.cartes.Carte3aLaSuite
import iut.info1.flip7.cartes.CarteBonus
import iut.info1.flip7.cartes.CarteBonusMultiplie
import iut.info1.flip7.cartes.CarteBonusPlus
import iut.info1.flip7.cartes.CarteNum
import iut.info1.flip7.cartes.CarteSpeciale
import iut.info1.flip7.cartes.CarteStop
import iut.info1.flip7.cartes.OutilsCarte
import iut.info1.flip7.exceptions.MainInvalideException
import iut.info1.flip7.exceptions.PiocheInvalideException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream


class TestOutilsCarte {

    val outil = OutilsCarte()

    companion object {
        @JvmStatic
        fun genereListesDePiochesInvalides(): Stream<Arguments> {
            var liste1 = mutableListOf<Carte>()
            var liste2 = mutableListOf<Carte>()
            var liste3 = mutableListOf<Carte>()
            var liste4 = mutableListOf<Carte>()
            var liste5 = mutableListOf<Carte>()
            var liste6 = mutableListOf<Carte>()
            var liste7 = mutableListOf<Carte>()
            var liste8 = mutableListOf<Carte>()
            var liste9 = mutableListOf<Carte>()

            for (i in 0 until 94){
                liste1.add(CarteNum(12))
            }
            for (i in 0 until 94){
                liste2.add(CarteBonusMultiplie())
            }
            for (i in 0 until 94){
                liste3.add(CarteBonusPlus(2))
            }
            for (i in 0 until 94){
                liste4.add(CarteStop())
            }
            for (i in 12 downTo 1){
                for (j in i .. 12){
                    liste5.add(CarteNum(i))
                }
            }
            for (i in 1 until 6){
                liste5.add(CarteBonusPlus(i*2))
            }
            liste5.add(CarteBonusMultiplie())
            liste5.add(CarteNum(0))
            for (i in 1 .. 3){
                liste5.add(CarteStop())
                liste5.add(Carte2ndeChance())
                liste5.add(Carte3aLaSuite())
            }
            for (i in 1 .. 12){
                for (j in 1 .. i){
                    liste6.add(CarteNum(i))
                }
            }
            for (i in 1 until 6){
                liste6.add(CarteBonusPlus(i*2))
            }
            liste6.add(CarteBonusMultiplie())
            liste6.add(CarteNum(0))
            for (i in 1 .. 9){
                liste6.add(CarteStop())
            }
            for (i in 1 .. 12){
                for (j in 1 .. i){
                    liste7.add(CarteNum(i))
                }
            }
            for (i in 1 until 6){
                liste7.add(CarteBonusPlus(i*2))
            }
            liste7.add(CarteBonusMultiplie())
            liste7.add(CarteNum(0))
            for (i in 1 .. 9){
                liste7.add(Carte2ndeChance())
            }
            for (i in 1 .. 12){
                for (j in 1 .. i){
                    liste8.add(CarteNum(i))
                }
            }
            for (i in 1 until 6){
                liste8.add(CarteBonusPlus(i*2))
            }
            liste8.add(CarteBonusMultiplie())
            liste8.add(CarteNum(0))
            for (i in 1 .. 9){
                liste8.add(Carte3aLaSuite())
            }

            for (i in 1 .. 12){
                for (j in 1 .. i){
                    liste9.add(CarteNum(i))
                }
            }
            for (i in 1 .. 5){
                liste9.add(CarteBonusPlus(i*2))
            }
            liste9.add(CarteBonusPlus(2))
            liste9.add(CarteNum(0))
            for (i in 1 .. 3){
                liste9.add(CarteStop())
                liste9.add(Carte2ndeChance())
                liste9.add(Carte3aLaSuite())
            }

            return Stream.of(
                // Une liste vide
                Arguments.of(listOf<Carte>()),
                // Une liste avec 1 seule carte
                Arguments.of(listOf<Carte>(CarteNum(0))),
                // Une liste avec 94 cartes num
                Arguments.of(liste1),
                // Une liste avec 94 cartes bonus multiplier
                Arguments.of(liste2),
                // Une liste avec 94 cartes bonus plus
                Arguments.of(liste3),
                // Une liste avec 94 cartes speciale (stop)
                Arguments.of(liste4),
                // Une liste avec 79 cartes numeros ordre inversé + 6 cartes bonus correctes + 9 cartes speciales correctes
                Arguments.of(liste5),
                // Une liste avec 79 cartes numeros correctes + 6 cartes bonus corrctes + 9 cartes stop
                Arguments.of(liste6),
                // Une liste avec 79 cartes numeros correctes + 6 cartes bonus corrctes + 9 cartes 2nde chance
                Arguments.of(liste7),
                // Une liste avec 79 cartes numeros correctes + 6 cartes bonus corrctes + 9 cartes 3 a la suite
                Arguments.of(liste8),
                // Une liste avec 79 cartes numeros correctes + 6 cartes bonus sans multiplier + 9 cartes speciale correctes
                Arguments.of(liste9)
            )
        }
        @JvmStatic
        fun genereListeDePiochesValide(): Stream<Arguments> {
            var liste = mutableListOf<Carte>()
            for (i in 0..12) {
                for (j in 0 until i) {
                    liste.add(CarteNum(i))
                }
            }
            for (i in 1 until 6) {
                liste.add(CarteBonusPlus(i * 2))
            }
            liste.add(CarteBonusMultiplie())
            liste.add(CarteNum(0))
            for (i in 1..3) {
                liste.add(CarteStop())
                liste.add(Carte2ndeChance())
                liste.add(Carte3aLaSuite())
            }
            return Stream.of(Arguments.of(liste))
        }
    }
    fun separeStringEnList(st1: String?,st2: String?,st3: String?):List<Carte>{
        var listeNum = listOf<String>()
        var listeBonus = listOf<String>()
        var listeSpeciale = listOf<String>()

        if (st1 != null) {
            listeNum = st1.split(",")
        }
        if (st2 != null) {
            listeBonus = st2.split(",")
        }
        if (st3 != null) {
            listeSpeciale = st3.split(",")
        }
        var cartes = mutableListOf<Carte>()


        for (valeur in listeNum){
            if (valeur.isNotEmpty()) {
                cartes.add(CarteNum(valeur.toInt()))
            }
        }
        for (valeur in listeBonus){
            if (valeur.isNotEmpty()) {
                if (valeur == "0") {
                    cartes.add(CarteBonusMultiplie())
                } else {
                    cartes.add(CarteBonusPlus(2*valeur.toInt()))
                }
            }
        }
        for (valeur in listeSpeciale){
            if (valeur.isNotEmpty()) {
                when (valeur.toInt()) {
                    1 -> cartes.add(CarteStop())
                    2 -> cartes.add(Carte2ndeChance())
                    3 -> cartes.add(Carte3aLaSuite())
                }
            }
        }
        return cartes.toList()
    }

    @ParameterizedTest
    @MethodSource("genereListesDePiochesInvalides")
    fun testPiocheInvalide(deck: List<Carte>){
        assertThrows<PiocheInvalideException>{ outil.verifiePiocheInitiale(deck) }
    }

    @ParameterizedTest
    @MethodSource("genereListeDePiochesValide")
    fun testPiocheValide(deck: List<Carte>){
        assertDoesNotThrow{
            outil.verifiePiocheInitiale(deck)
        }
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';',value=
        [
            // pas 2 0 ou 2 1 dans le jeu
        "0,0;;",
        "1,1;;",
            // un doublon oui mais pas deux
        "2,2,2;;",
        "3,3,3;;",
        "4,4,4;;",
        "5,5,5;;",
        "6,6,6;;",
        "7,7,7;;",
        "8,8,8;;",
        "9,9,9;;",
        "10,10,10;;",
        "11,11,11;;",
        "12,12,12;;",
            // pas 4 exemplaire de cartes special et 1 seul stop par main
        ";;3,3,3,3",
        ";;2,2,2,2",
        ";;1,1",
            // pas deux cartes bonus identiques
        ";0,0;",
        ";1,1;",
        ";2,2;",
        ";3,3;",
        ";4,4;",
        ";5,5;",
            // pas de main avec 8 cartes numeros
        "0,8,1,6,7,3,4,9;;",
            // pas de main avec un doublon et une seconde chance
        "5,5;;2",
            // pas de main avec un doublon et un stop
        "5,5;;1",
            // pas de main avec un flip7 et un stop (bug signalé pas résolu)
        //"0,1,2,3,4,5,6;;1"
        ]
    )
    fun testMainsInvalides(st1: String?,st2: String?,st3: String?) {
        val deck = separeStringEnList(st1,st2,st3)
        assertThrows<MainInvalideException>{ outil.verifieMainCorrecte(deck) }
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';', value =
        [
            // la plus grande main possible : 7 numeros + 6 bonus + 7 speciales
            "0,1,2,3,4,5,6;0,1,2,3,4,5;1,2,2,2,3,3,3",
            // main vide
            ";;",
            // main avec un doublon autorisée
            "2,2;;",
            "3,3;;",
            "4,4;;",
            "5,5;;",
            "6,6;;",
            "7,7;;",
            "8,8;;",
            "9,9;;",
            "10,10;;",
            "11,11;;",
            "12,12;;",
            // main valide aleatoire avec un peut de tout
            "8,4,6,2;2,3;3,2,2,1"
        ]
    )
    fun testMainsValides(st1: String?,st2: String?,st3: String?){
        val deck = separeStringEnList(st1,st2,st3)
        assertDoesNotThrow{ outil.verifieMainCorrecte(deck) }
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';', value =
        [
            // la plus grande main possible : 7 numeros + 6 bonus + 6 speciales
            "12,11,10,9,8,7,6;0,1,2,3,4,5;2,2,2,3,3,3;171",
            ";1;;2",
            "1,5;0;;12",
            "2,3;2,0;;14",
            // main vide
            ";;;0"
        ]
    )
    fun testCalculScore(st1: String?,st2: String?,st3: String?,attendu: Int){
        val deck = separeStringEnList(st1,st2,st3)
        val res = outil.calculScore(deck)
        assertEquals(attendu,res)
    }


    @ParameterizedTest
    @CsvSource(delimiter = ';', value =
        [
            // la plus grande main possible : 7 numeros + 6 bonus + 6 speciales
            "12,11,10,9,8,7,6;0,1,2,3,4,5;2,2,2,3,3,3;true",
            // main vide
            ";;;false",
            // un flip 7 quelconque
            "1,5,3,4,8,2,9;;;true",
            // une main avec 7 cartes mais pas flip7
            ";0,1,2,3,4,5;1;false"
        ]
    )
    fun testEstFlip7(st1: String?,st2: String?,st3: String?,attendu: Boolean){
        val deck = separeStringEnList(st1,st2,st3)
        val res = outil.estFlip7(deck)
        assertEquals(attendu,res)
    }

}