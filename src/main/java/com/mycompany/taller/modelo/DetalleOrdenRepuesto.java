package com.mycompany.taller.modelo;

public class DetalleOrdenRepuesto {
    private int idDetalleRepuesto;
    private int idOrden;
    private int idRepuesto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public DetalleOrdenRepuesto() {}

    public DetalleOrdenRepuesto(int idDetalleRepuesto, int idOrden, int idRepuesto, int cantidad, double precioUnitario, double subtotal) {
        this.idDetalleRepuesto = idDetalleRepuesto;
        this.idOrden = idOrden;
        this.idRepuesto = idRepuesto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public int getIdDetalleRepuesto() { 
        return idDetalleRepuesto; }
    
    public void setIdDetalleRepuesto(int idDetalleRepuesto) { 
        this.idDetalleRepuesto = idDetalleRepuesto; }
    
    public int getIdOrden() { 
        return idOrden; }
    public void setIdOrden(int idOrden) { 
        this.idOrden = idOrden; }
    
    public int getIdRepuesto() { 
        return idRepuesto; }
    public void setIdRepuesto(int idRepuesto) { 
        this.idRepuesto = idRepuesto; }
    public int getCantidad() { return cantidad; }
    
    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad; }
    
    public double getPrecioUnitario() { 
        return precioUnitario; }
    
    public void setPrecioUnitario(double precioUnitario) { 
        this.precioUnitario = precioUnitario; }
    
    public double getSubtotal() { 
        return subtotal; }
    
    public void setSubtotal(double subtotal) { 
        this.subtotal = subtotal; }
}
