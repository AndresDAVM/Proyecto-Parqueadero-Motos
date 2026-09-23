package co.unilibre.edu.Datos;

public class Propietario {
    private final String nombre;
    private final String identificacion;

    public Propietario(String nombre, String identificacion) {
        this.nombre = nombre;
        this.identificacion = identificacion;
    }

    public String getNombre() { return nombre; }
    public String getIdentificacion() { return identificacion; }
}
