package com.mycompany.taller.modelo;

public class DetalleOrdenServicio {
    private int idDetalleServicio;
    private int idOrden;
    private int idServicio;
    private double costoAplicado;

    public DetalleOrdenServicio() {}

    public DetalleOrdenServicio(int idDetalleServicio, int idOrden, int idServicio, double costoAplicado) {
        this.idDetalleServicio = idDetalleServicio;
        this.idOrden = idOrden;
        this.idServicio = idServicio;
        this.costoAplicado = costoAplicado;
    }

    public int getIdDetalleServicio() { 
        return idDetalleServicio; }
    
    public void setIdDetalleServicio(int idDetalleServicio) {
        this.idDetalleServicio = idDetalleServicio; }
    
    public int getIdOrden() { 
        return idOrden; }
    
    public void setIdOrden(int idOrden) { 
        this.idOrden = idOrden; }
    
    public int getIdServicio() { 
        return idServicio; }
    
    public void setIdServicio(int idServicio) { 
        this.idServicio = idServicio; }
    
    public double getCostoAplicado() { 
        return costoAplicado; }
    
    public void setCostoAplicado(double costoAplicado) { 
        this.costoAplicado = costoAplicado; }
}
