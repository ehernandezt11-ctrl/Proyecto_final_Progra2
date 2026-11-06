package com.mycompany.taller.modelo;

public class Mecanico extends Persona{
    private String especialidad;

    public Mecanico() {
    super ();
    }

    public Mecanico(int id, String nombres, String apellidos, String telefono, String especialidad) {
        super(id,nombres,apellidos,telefono);
        this.especialidad = especialidad;
    }

    public int getIdMecanico() { 
        return id; 
    }
    public void setIdMecanico(int idMecanico) { 
        this.id = idMecanico; }
    
    public String getEspecialidad() { 
        return especialidad; }
    
    public void setEspecialidad(String especialidad) { 
        this.especialidad = especialidad; }

    @Override
    public String obtenerInformacionDetallada() {
        return "Mecanico  [ID:"  + id + "]  " + nombres +  "  "  +  apellidos + " - Especialidad:  " + especialidad;
    }
}
