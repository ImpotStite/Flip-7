package controls

import javafx.event.EventHandler
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import view.Table.Accueil
import java.awt.Desktop
import java.io.File

class ControleurAccueilRegle(val view : Accueil) : EventHandler<MouseEvent> {

    override fun handle(event: MouseEvent?) {
        val imgRegle = Image("/assets/boutons/Regle.png", 250.0, 62.5, false, true)
        if (event?.eventType == MouseEvent.MOUSE_PRESSED) {
            val imgRegleClic = Image("/assets/boutons/RegleClick.png", 250.0, 62.5, false, true)
            view.btRegle.graphic = ImageView(imgRegleClic)
        }
        if (event?.eventType == MouseEvent.MOUSE_RELEASED) {
            view.btRegle.graphic = ImageView(imgRegle)
            val pdfFile = File("dokkahtml-flip7-1.3/html/flip7_regles_fr.pdf")
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            } else  {
                println("Le fichier des règles est introuvable !")
            }
        }
    }}