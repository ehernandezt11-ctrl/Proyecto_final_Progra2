package com.mycompany.taller.modelo;

public class OrdenTrabajo {
    private int idOrden;
    private Integer idVehiculo;
    private Integer idMecanico;
    private String fechaIngreso;
    private String fechaEntrega;
    private String descripcionProblema;
    private String estado;
    private double costoTotal;

    public OrdenTrabajo() {}

    public OrdenTrabajo(int idOrden, int idVehiculo, int idMecanico, String fechaIngreso, String fechaEntrega, String descripcionProblema, String estado, double costoTotal) {
        this.idOrden = idOrden;
        this.idVehiculo = idVehiculo;
        this.idMecanico = idMecanico;
        this.fechaIngreso = fechaIngreso;
        this.fechaEntrega = fechaEntrega;
        this.descripcionProblema = descripcionProblema;
        this.estado = estado;
        this.costoTotal = costoTotal;
    }

    public int getIdOrden() { 
        return idOrden; }
    public void setIdOrden(int idOrden) { 
        this.idOrden = idOrden; }
    
    public Integer getIdVehiculo() { 
        return idVehiculo; }
    
    public void setIdVehiculo(Integer idVehiculo) { 
        this.idVehiculo = idVehiculo; }
    
    public Integer getIdMecanico() { 
        return idMecanico; }
    
    public void setIdMecanico(Integer idMecanico) { 
        this.idMecanico = idMecanico; }
    
    public String getFechaIngreso() { 
        return fechaIngreso; }
    
    public void setFechaIngreso(String fechaIngreso) { 
        this.fechaIngreso = fechaIngreso; }
    
    public String getFechaEntrega() { 
        return fechaEntrega; }
    
    public void setFechaEntrega(String fechaEntrega) { 
        this.fechaEntrega = fechaEntrega; }
    
    public String getDescripcionProblema() { 
        return descripcionProblema; }
    
    public void setDescripcionProblema(String descripcionProblema) { 
        this.descripcionProblema = descripcionProblema; }
    
    public String getEstado() { 
        return estado; }
    
    public void setEstado(String estado) { 
        this.estado = estado; }
    
    public double getCostoTotal() {
        return costoTotal; }
    
    public void setCostoTotal(double costoTotal) { 
        this.costoTotal = costoTotal; }
}
