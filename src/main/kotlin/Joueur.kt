import iut.info1.flip7.IJoueur


class Joueur(private val nom : String) : IJoueur {

    override fun donneNom(): String {
        return this.nom
    }
}