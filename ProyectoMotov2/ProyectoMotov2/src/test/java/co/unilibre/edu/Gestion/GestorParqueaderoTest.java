package co.unilibre.edu.Gestion;

import co.unilibre.edu.Datos.Moto;
import co.unilibre.edu.Datos.Propietario;
import co.unilibre.edu.Pagos.TipoPago;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GestorParqueaderoTest {
    private static final LocalDateTime T0 = LocalDateTime.of(2026, 9, 23, 9, 0, 0);

    private GestorParqueadero gestor;

    @BeforeEach
    void preparar() {
        gestor = new GestorParqueadero();
    }

    private Moto moto(String placa) {
        return new Moto(placa, "Yamaha", new Propietario("Andres", "123456"));
    }

    @Test
    void ingresoRegistraLaMotoYDescuentaUnEspacio() {
        assertTrue(gestor.registrarIngreso(moto("ABC123"), T0));
        assertEquals(1, gestor.getMotosDentro());
        assertEquals(22, gestor.getEspaciosDisponibles());
    }

    @Test
    void noPermiteMasDe23Motos() {
        for (int i = 1; i <= 23; i++) {
            assertTrue(gestor.registrarIngreso(moto(String.format("MOT%03d", i)), T0));
        }
        assertEquals(0, gestor.getEspaciosDisponibles());
        assertFalse(gestor.registrarIngreso(moto("MOT999"), T0));
    }

    @Test
    void noPermitePlacaRepetidaSinImportarMayusculas() {
        assertTrue(gestor.registrarIngreso(moto("ABC123"), T0));
        assertFalse(gestor.registrarIngreso(moto("abc123"), T0));
        assertEquals(1, gestor.getMotosDentro());
    }

    @Test
    void salidaCobra40PesosPorMinuto() {
        gestor.registrarIngreso(moto("ABC123"), T0);
        assertEquals(400, gestor.darSalida("ABC123", T0.plusMinutes(10)));
    }

    @Test
    void salidaCobraMinutoIniciado() {
        gestor.registrarIngreso(moto("ABC123"), T0);
        assertEquals(80, gestor.darSalida("ABC123", T0.plusSeconds(90)));
    }

    @Test
    void salidaCobraMinimoUnMinuto() {
        gestor.registrarIngreso(moto("ABC123"), T0);
        assertEquals(40, gestor.darSalida("ABC123", T0));
    }

    @Test
    void salidaConPlacaInexistenteDevuelveMenosUno() {
        assertEquals(-1, gestor.darSalida("NOEXISTE", T0));
    }

    @Test
    void salidaLiberaElEspacio() {
        gestor.registrarIngreso(moto("ABC123"), T0);
        gestor.darSalida("ABC123", T0.plusMinutes(1));
        assertEquals(0, gestor.getMotosDentro());
        assertEquals(23, gestor.getEspaciosDisponibles());
    }

    @Test
    void noSePuedePagarSinHaberDadoSalida() {
        gestor.registrarIngreso(moto("ABC123"), T0);
        assertFalse(gestor.registrarPago("ABC123", TipoPago.EFECTIVO));
    }

    @Test
    void motoConPagoPendienteNoPuedeVolverAIngresar() {
        gestor.registrarIngreso(moto("ABC123"), T0);
        gestor.darSalida("ABC123", T0.plusMinutes(2));
        assertTrue(gestor.tienePagoPendiente("ABC123"));
        assertFalse(gestor.registrarIngreso(moto("ABC123"), T0.plusMinutes(3)));
    }

    @Test
    void reporteSumaMotosYValorIngresado() {
        gestor.registrarIngreso(moto("ABC123"), T0);
        gestor.registrarIngreso(moto("XYZ789"), T0);
        assertEquals(400, gestor.darSalida("ABC123", T0.plusMinutes(10)));
        assertEquals(200, gestor.darSalida("XYZ789", T0.plusMinutes(5)));
        assertTrue(gestor.registrarPago("ABC123", TipoPago.EFECTIVO));
        assertTrue(gestor.registrarPago("XYZ789", TipoPago.NEQUI));

        ReporteDiario reporte = gestor.generarReporte(T0.toLocalDate());
        assertEquals(2, reporte.getCantidadMotos());
        assertEquals(600, reporte.getTotalIngresado());
    }

    @Test
    void pagoPendienteNoCuentaEnElReporte() {
        gestor.registrarIngreso(moto("ABC123"), T0);
        gestor.darSalida("ABC123", T0.plusMinutes(10));

        ReporteDiario reporte = gestor.generarReporte(T0.toLocalDate());
        assertEquals(0, reporte.getCantidadMotos());
        assertEquals(0, reporte.getTotalIngresado());
    }
}
