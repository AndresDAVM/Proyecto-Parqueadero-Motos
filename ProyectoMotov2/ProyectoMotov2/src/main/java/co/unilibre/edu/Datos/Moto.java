package co.unilibre.edu.Datos;

public class Moto {
    private final String placa;
    private final String marca;
    private final Propietario propietario;

    public Moto(String placa, String marca, Propietario propietario) {
        this.placa = placa;
        this.marca = marca;
        this.propietario = propietario;
    }

    public String getPlaca() { return placa; }
    public String getMarca() { return marca; }
    public Propietario getPropietario() { return propietario; }
}
