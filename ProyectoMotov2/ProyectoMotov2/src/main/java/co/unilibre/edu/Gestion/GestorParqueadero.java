package co.unilibre.edu.Gestion;

import co.unilibre.edu.Datos.Moto;
import co.unilibre.edu.Datos.Registro;
import co.unilibre.edu.Pagos.CalculadoraTarifa;
import co.unilibre.edu.Pagos.Pago;
import co.unilibre.edu.Pagos.TipoPago;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorParqueadero {
    public static final int MAX_ESPACIOS = 23;

    private final List<Registro> registrosActivos = new ArrayList<>();
    private final Map<String, Pago> pagosPendientes = new HashMap<>();
    private final List<Pago> pagos = new ArrayList<>();

    // ---------- RQ01: ingreso ----------
    public boolean registrarIngreso(Moto moto) {
        return registrarIngreso(moto, LocalDateTime.now());
    }

    /** false si el parqueadero está lleno, la placa ya está dentro o tiene un pago pendiente. */
    public boolean registrarIngreso(Moto moto, LocalDateTime hora) {
        if (moto == null) {
            throw new IllegalArgumentException("La moto no puede ser nula");
        }
        if (registrosActivos.size() >= MAX_ESPACIOS) return false;
        if (buscarActivo(moto.getPlaca()) != null) return false;
        if (pagosPendientes.containsKey(normalizar(moto.getPlaca()))) return false;
        registrosActivos.add(new Registro(moto, hora));
        return true;
    }

    // ---------- RQ02: salida ----------
    public int darSalida(String placa) {
        return darSalida(placa, LocalDateTime.now());
    }

    /** Devuelve el valor a pagar (40 pesos por minuto) o -1 si la placa no está dentro. */
    public int darSalida(String placa, LocalDateTime hora) {
        Registro registro = buscarActivo(placa);
        if (registro == null) return -1;

        int minutos = CalculadoraTarifa.minutosCobrados(registro.getHoraEntrada(), hora);
        int valor = CalculadoraTarifa.calcularValor(minutos);

        registro.setHoraSalida(hora);
        registrosActivos.remove(registro);
        pagosPendientes.put(normalizar(placa), new Pago(registro.getMoto(), valor, hora));
        return valor;
    }

    // ---------- RQ03: pago ----------
    /** false si esa placa no tiene un pago pendiente. */
    public boolean registrarPago(String placa, TipoPago tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Debe indicar el tipo de pago");
        }
        Pago pago = pagosPendientes.remove(normalizar(placa));
        if (pago == null) return false;
        pago.setTipoPago(tipo);
        pagos.add(pago);
        return true;
    }

    public boolean tienePagoPendiente(String placa) {
        return pagosPendientes.containsKey(normalizar(placa));
    }

    // ---------- RQ04: reporte ----------
    public ReporteDiario generarReporte() {
        return generarReporte(LocalDate.now());
    }

    public ReporteDiario generarReporte(LocalDate dia) {
        return new ReporteDiario(dia, pagos);
    }

    // ---------- consultas ----------
    public int getMotosDentro() { return registrosActivos.size(); }
    public int getEspaciosDisponibles() { return MAX_ESPACIOS - registrosActivos.size(); }

    private Registro buscarActivo(String placa) {
        String buscada = normalizar(placa);
        for (Registro r : registrosActivos) {
            if (normalizar(r.getMoto().getPlaca()).equals(buscada)) return r;
        }
        return null;
    }

    private static String normalizar(String placa) {
        return placa == null ? "" : placa.trim().toUpperCase();
    }
}
