package com.example.franchesco.taxichofer.Clases;

import java.io.Serializable;

/**
 * Created by HP on 11/12/2017.
 */

public class Pedidos implements Serializable {

    private String IdPedido;
    private String ReferenciaOrigen;
    private String NumPersonas;
    private String Hora;

    public Pedidos(String idPedido, String referenciaOrigen, String numPersonas, String hora) {
        IdPedido = idPedido;
        ReferenciaOrigen = referenciaOrigen;
        NumPersonas = numPersonas;
        Hora = hora;
    }

    public String getIdPedido() {
        return IdPedido;
    }

    public void setIdPedido(String idPedido) {
        IdPedido = idPedido;
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
}
