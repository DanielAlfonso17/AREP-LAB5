package edu.escuelaing.arep.docker;


import org.mongodb.morphia.annotations.Entity;


import java.util.Date;

@Entity
public class Datos {

    private String dato;
    private Date fecha;

    public Datos(){

    }

    public Datos(String dato){
        this.dato = dato;
        this.fecha = new Date();
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
