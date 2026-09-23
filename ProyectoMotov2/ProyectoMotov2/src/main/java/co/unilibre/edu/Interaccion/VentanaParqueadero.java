package co.unilibre.edu.Interaccion;

import co.unilibre.edu.Datos.Moto;
import co.unilibre.edu.Datos.Propietario;
import co.unilibre.edu.Gestion.GestorParqueadero;
import co.unilibre.edu.Pagos.TipoPago;

import javax.swing.*;
import java.awt.*;

public class VentanaParqueadero extends JFrame {
    private final GestorParqueadero gestor;
    private String placaPendiente;

    private final JTextField txtPlaca = new JTextField();
    private final JTextField txtMarca = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtIdentificacion = new JTextField();
    private final JTextField txtPlacaSalida = new JTextField();
    private final JComboBox<TipoPago> cmbTipoPago = new JComboBox<>(TipoPago.values());
    private final JButton btnPago = new JButton("Registrar pago");
    private final JLabel lblEspacios = new JLabel();
    private final JTextArea areaMensajes = new JTextArea(10, 45);

    public VentanaParqueadero(GestorParqueadero gestor) {
        this.gestor = gestor;
        setTitle("Parqueadero de Motos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        lblEspacios.setFont(lblEspacios.getFont().deriveFont(Font.BOLD, 14f));
        lblEspacios.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblEspacios, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(1, 2, 8, 8));
        centro.add(crearPanelIngreso());
        centro.add(crearPanelSalida());
        add(centro, BorderLayout.CENTER);

        areaMensajes.setEditable(false);
        areaMensajes.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JButton btnReporte = new JButton("Generar reporte del día");
        btnReporte.addActionListener(e -> mostrar(gestor.generarReporte().toString()));
        JPanel sur = new JPanel(new BorderLayout(4, 4));
        sur.add(btnReporte, BorderLayout.NORTH);
        sur.add(new JScrollPane(areaMensajes), BorderLayout.CENTER);
        add(sur, BorderLayout.SOUTH);

        habilitarPago(false);
        actualizarEspacios();
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel crearPanelIngreso() {
        JPanel p = new JPanel(new GridLayout(0, 2, 6, 6));
        p.setBorder(BorderFactory.createTitledBorder("Registrar ingreso"));
        p.add(new JLabel("Placa:"));             p.add(txtPlaca);
        p.add(new JLabel("Marca:"));             p.add(txtMarca);
        p.add(new JLabel("Propietario:"));       p.add(txtNombre);
        p.add(new JLabel("Nº identificación:")); p.add(txtIdentificacion);
        JButton btn = new JButton("Registrar ingreso");
        btn.addActionListener(e -> ingresar());
        p.add(new JLabel());
        p.add(btn);
        return p;
    }

    private JPanel crearPanelSalida() {
        JPanel p = new JPanel(new GridLayout(0, 2, 6, 6));
        p.setBorder(BorderFactory.createTitledBorder("Salida y pago"));
        p.add(new JLabel("Placa:"));
        p.add(txtPlacaSalida);
        JButton btnSalida = new JButton("Dar salida");
        btnSalida.addActionListener(e -> salir());
        p.add(new JLabel());
        p.add(btnSalida);
        p.add(new JLabel("Tipo de pago:"));
        p.add(cmbTipoPago);
        btnPago.addActionListener(e -> pagar());
        p.add(new JLabel());
        p.add(btnPago);
        return p;
    }

    private void ingresar() {
        String placa = txtPlaca.getText().trim().toUpperCase();
        String marca = txtMarca.getText().trim();
        String nombre = txtNombre.getText().trim();
        String id = txtIdentificacion.getText().trim();

        if (placa.isEmpty() || marca.isEmpty() || nombre.isEmpty() || id.isEmpty()) {
            mostrar("Complete todos los campos del ingreso.");
            return;
        }
        if (!id.matches("\\d+")) {
            mostrar("El número de identificación solo debe tener dígitos.");
            return;
        }
        boolean ok = gestor.registrarIngreso(new Moto(placa, marca, new Propietario(nombre, id)));
        if (ok) {
            mostrar("Ingreso registrado: " + placa + " (" + marca + ") - " + nombre);
            txtPlaca.setText("");
            txtMarca.setText("");
            txtNombre.setText("");
            txtIdentificacion.setText("");
        } else {
            mostrar("No se pudo registrar " + placa + ": parqueadero lleno, la placa ya está dentro o tiene un pago pendiente.");
        }
        actualizarEspacios();
    }

    private void salir() {
        String placa = txtPlacaSalida.getText().trim().toUpperCase();
        if (placaPendiente != null) {
            mostrar("Primero registre el pago de " + placaPendiente + ".");
            return;
        }
        if (placa.isEmpty()) {
            mostrar("Escriba la placa de la moto.");
            return;
        }
        int valor = gestor.darSalida(placa);
        if (valor < 0) {
            mostrar("La placa " + placa + " no está en el parqueadero.");
            return;
        }
        placaPendiente = placa;
        mostrar("Salida de " + placa + ". VALOR A PAGAR: " + String.format("$%,d", valor)
                + ". Seleccione el tipo de pago.");
        habilitarPago(true);
        actualizarEspacios();
    }

    private void pagar() {
        if (placaPendiente == null) {
            mostrar("No hay ningún pago pendiente.");
            return;
        }
        TipoPago tipo = (TipoPago) cmbTipoPago.getSelectedItem();
        if (gestor.registrarPago(placaPendiente, tipo)) {
            mostrar("Pago registrado: " + placaPendiente + " - " + tipo);
            placaPendiente = null;
            txtPlacaSalida.setText("");
            habilitarPago(false);
        }
    }

    private void habilitarPago(boolean habilitar) {
        btnPago.setEnabled(habilitar);
        cmbTipoPago.setEnabled(habilitar);
    }

    private void actualizarEspacios() {
        lblEspacios.setText("Espacios disponibles: " + gestor.getEspaciosDisponibles()
                + " de " + GestorParqueadero.MAX_ESPACIOS);
    }

    private void mostrar(String texto) {
        areaMensajes.append(texto + "\n\n");
        areaMensajes.setCaretPosition(areaMensajes.getDocument().getLength());
    }
}
