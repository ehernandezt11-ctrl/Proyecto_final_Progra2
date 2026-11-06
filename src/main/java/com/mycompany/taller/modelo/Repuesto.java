package com.mycompany.taller.modelo;

public class Repuesto {
    private int idRepuesto;
    private String nombre;
    private String descripcion;
    private double precio;
    private int existencia;

    public Repuesto() {}

    public Repuesto(int idRepuesto, String nombre, String descripcion, double precio, int existencia) {
        this.idRepuesto = idRepuesto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.existencia = existencia;
    }

    public int getIdRepuesto() { 
        return idRepuesto; }
    
    public void setIdRepuesto(int idRepuesto) { 
        this.idRepuesto = idRepuesto; }
    
    public String getNombre() { 
        return nombre; }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre; }
    
    public String getDescripcion() { 
        return descripcion; }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion; }
    
    public double getPrecio() { 
        return precio; }
    
    public void setPrecio(double precio) { 
        this.precio = precio; }
    
    public int getExistencia() { 
        return existencia; }
    
    public void setExistencia(int existencia) { 
        this.existencia = existencia; }
}
