package co.unilibre.edu.Pagos;

import co.unilibre.edu.Datos.Moto;

import java.time.LocalDateTime;

public class Pago {
    private final Moto moto;
    private final int valor;
    private final LocalDateTime fecha;
    private TipoPago tipoPago;

    public Pago(Moto moto, int valor, LocalDateTime fecha) {
        this.moto = moto;
        this.valor = valor;
        this.fecha = fecha;
    }

    public Moto getMoto() { return moto; }
    public int getValor() { return valor; }
    public LocalDateTime getFecha() { return fecha; }
    public TipoPago getTipoPago() { return tipoPago; }
    public void setTipoPago(TipoPago tipoPago) { this.tipoPago = tipoPago; }
}
