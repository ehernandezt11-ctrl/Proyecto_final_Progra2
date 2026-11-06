package com.mycompany.taller.modelo;

public class Servicio {
    private int idServicio;
    private String nombreServicio;
    private String descripcion;
    private double costoManoObra;

    public Servicio() {}

    public Servicio(int idServicio, String nombreServicio, String descripcion, double costoManoObra) {
        this.idServicio = idServicio;
        this.nombreServicio = nombreServicio;
        this.descripcion = descripcion;
        this.costoManoObra = costoManoObra;
    }

    public int getIdServicio() { 
        return idServicio; }
    public void setIdServicio(int idServicio) { 
        this.idServicio = idServicio; }
    
    public String getNombreServicio() { 
        return nombreServicio; }
    public void setNombreServicio(String nombreServicio) { 
        this.nombreServicio = nombreServicio; }
    
    public String getDescripcion() { 
        return descripcion; }
    public void setDescripcion(String descripcion) { 
        this.descripcion = descripcion; }
    
    public double getCostoManoObra() { 
        return costoManoObra; }
    public void setCostoManoObra(double costoManoObra) { 
        this.costoManoObra = costoManoObra; }
}
