package com.franchesco.taxicliente.Clases;

/**
 * Created by HP on 06/12/2017.
 */

public class PedidoListaCJ {

    private String ReferenciaOrigen;
    private String NumPersonas;
    private String Hora;
    private String DniChofer;

    public PedidoListaCJ(String referenciaOrigen,String numPersonas, String hora,String dniChofer) {
        ReferenciaOrigen = referenciaOrigen;
        NumPersonas = numPersonas;
        Hora = hora;
        DniChofer = dniChofer;
    }

    public String getReferenciaOrigen() {
        return ReferenciaOrigen;
    }

    public void setReferenciaOrigen(String referenciaOrigen) {
        ReferenciaOrigen = referenciaOrigen;
    }

    public String getNumPersonas() {
        return NumPersonas;
    }

    public void setNumPersonas(String numPersonas) {
        NumPersonas = numPersonas;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getDniChofer() {
        return DniChofer;
    }

    public void setDniChofer(String dniChofer) {
        DniChofer = dniChofer;
    }
}
