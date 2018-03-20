package com.example.franchesco.taxichofer.Clases;

/**
 * Created by HP on 11/12/2017.
 */

public class Carreras {

    private String IdPedido;
    private String ReferenciaOrigen;
    private String NumPersonas;
    private String Hora;
    private String DirOrigen;
    private String DirDestino;

    public Carreras(String idPedido, String referenciaOrigen, String numPersonas, String hora,String dirOrigen,String dirDestino) {
        IdPedido = idPedido;
        ReferenciaOrigen = referenciaOrigen;
        NumPersonas = numPersonas;
        Hora = hora;
        DirOrigen = dirOrigen;
        DirDestino = dirDestino;
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

    public String getDirOrigen() {
        return DirOrigen;
    }

    public void setDirOrigen(String dirOrigen) {
        DirOrigen = dirOrigen;
    }

    public String getDirDestino() {
        return DirDestino;
    }

    public void setDirDestino(String dirDestino) {
        DirDestino = dirDestino;
    }
}
