
package com.mycompany.taller.modelo;

public class Cliente extends Persona {
    private String correo;
    private String direccion;

    public Cliente() {
        super();
    }

    public Cliente(int id, String nombres, String apellidos, String telefono, String correo, String direccion) {
        super(id,nombres, apellidos, telefono);
        this.correo = correo;
        this.direccion = direccion;
    }

    public int getIdCliente() { 
        return id; 
    }
    public void setIdCliente(int idCliente) { 
        this.id = idCliente; 
    }

    public String getCorreo() { 
        return correo; 
    }
    public void setCorreo(String correo) { 
        this.correo = correo; 
    }

    public String getDireccion() { 
        return direccion; 
    }
    public void setDireccion(String direccion) { 
        this.direccion = direccion; 
    }
    
    @Override
    public String obtenerInformacionDetallada() {
        return "Cliente [ID: "  +  id  + "]  " +  nombres  + "  " +  apellidos + " - Tel:  " +  telefono;
    }
}
