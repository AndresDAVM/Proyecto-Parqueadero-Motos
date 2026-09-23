package co.unilibre.edu.Datos;

import java.time.LocalDateTime;

public class Registro {
    private final Moto moto;
    private final LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;

    public Registro(Moto moto, LocalDateTime horaEntrada) {
        this.moto = moto;
        this.horaEntrada = horaEntrada;
    }

    public Moto getMoto() { return moto; }
    public LocalDateTime getHoraEntrada() { return horaEntrada; }
    public LocalDateTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalDateTime horaSalida) { this.horaSalida = horaSalida; }
}
