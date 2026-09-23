package co.unilibre.edu;

import co.unilibre.edu.Gestion.GestorParqueadero;
import co.unilibre.edu.Interaccion.VentanaParqueadero;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaParqueadero(new GestorParqueadero()).setVisible(true));
    }
}
