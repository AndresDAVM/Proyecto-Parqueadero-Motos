package co.unilibre.edu.Pagos;

import java.time.Duration;
import java.time.LocalDateTime;

public final class CalculadoraTarifa {
    public static final int TARIFA_MINUTO = 40;

    private CalculadoraTarifa() {
    }

    /** Minutos a cobrar: minuto iniciado, con un mínimo de 1. */
    public static int minutosCobrados(LocalDateTime entrada, LocalDateTime salida) {
        long segundos = Duration.between(entrada, salida).getSeconds();
        if (segundos < 0) {
            throw new IllegalArgumentException("La salida no puede ser anterior al ingreso");
        }
        return (int) Math.max(1, (segundos + 59) / 60);
    }

    public static int calcularValor(int minutos) {
        return minutos * TARIFA_MINUTO;
    }
}
