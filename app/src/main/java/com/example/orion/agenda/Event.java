package com.example.orion.agenda;

/**
 * Created by Orion on 14/04/2017.
 */

public class Event {

    private String idEvento;
    private String evento;
    private String fecha;
    private String hora;
    private String descripcion;
    private String contacto;
    private String tel;

    public Event() {

    }

    public Event(String evento, String fecha, String hora, String descripcion, String tel) {
        this.evento=evento;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = descripcion;
        this.tel = tel;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getContacto(){
        return contacto;
    }

    public void setContacto(String nombre){
        contacto = nombre;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }
}
