package co.unilibre.edu.Gestion;

import co.unilibre.edu.Pagos.Pago;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReporteDiario {
    private final LocalDate dia;
    private final List<Pago> pagosDelDia = new ArrayList<>();
    private int totalIngresado;

    public ReporteDiario(LocalDate dia, List<Pago> pagos) {
        this.dia = dia;
        for (Pago p : pagos) {
            if (p.getFecha().toLocalDate().equals(dia)) {
                pagosDelDia.add(p);
                totalIngresado += p.getValor();
            }
        }
    }

    public LocalDate getDia() { return dia; }
    public int getCantidadMotos() { return pagosDelDia.size(); }
    public int getTotalIngresado() { return totalIngresado; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== REPORTE DEL DIA ").append(dia).append(" =====\n");
        sb.append("Motos atendidas: ").append(getCantidadMotos()).append("\n");
        sb.append("Total ingresado: ").append(String.format("$%,d", totalIngresado)).append("\n");
        for (Pago p : pagosDelDia) {
            sb.append(String.format("  %s - %s - $%,d%n",
                    p.getMoto().getPlaca(), p.getTipoPago(), p.getValor()));
        }
        return sb.toString();
    }
}
