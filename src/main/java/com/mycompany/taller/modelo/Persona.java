package com.mycompany.taller.modelo;

public abstract class Persona {
    protected int id;
    protected String nombres;
    protected String apellidos;
    protected String telefono;

    public Persona() {}

    public Persona(int id, String nombres, String apellidos, String telefono) {
        this.id= id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
    }

    // Getters y Setters
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }    
    
    public String getNombres() { 
        return nombres; 
    }
    public void setNombres(String nombres) { 
        this.nombres = nombres; 
    }

    public String getApellidos() { 
        return apellidos; 
    }
    public void setApellidos(String apellidos) { 
        this.apellidos = apellidos; 
    }

    public String getTelefono() { 
        return telefono; 
    }
    public void setTelefono(String telefono) { 
        this.telefono = telefono; 
    }
    
    public abstract String obtenerInformacionDetallada();
    
}