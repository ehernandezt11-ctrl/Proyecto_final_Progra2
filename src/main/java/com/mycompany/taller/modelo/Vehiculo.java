package com.mycompany.taller.modelo;

public class Vehiculo {
    private Integer idVehiculo;
    private Integer idCliente;
    private String placa;
    private String marca;
    private String modelo;
    private Integer anio;

    public Vehiculo() {}

    public Vehiculo(Integer idVehiculo, Integer idCliente, String placa, String marca, String modelo, Integer anio) {
        this.idVehiculo = idVehiculo;
        this.idCliente = idCliente;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
    }

    public Integer getIdVehiculo() { 
        return idVehiculo; 
    }
    public void setIdVehiculo(Integer idVehiculo) { 
        this.idVehiculo = idVehiculo; 
    }
    
    public Integer getIdCliente() { 
        return idCliente; 
    }
    public void setIdCliente(Integer idCliente) { 
        this.idCliente = idCliente; 
    }
    
    public String getPlaca() { 
        return placa; 
    }
    public void setPlaca(String placa) { 
        this.placa = placa; 
    }
    
    public String getMarca() { 
        return marca; 
    }
    public void setMarca(String marca) { 
        this.marca = marca; 
    }
    
    public String getModelo() { 
        return modelo; 
    }
    public void setModelo(String modelo) { 
        this.modelo = modelo; 
    }
    
    public Integer getAnio() { 
        return anio; 
    }
    public void setAnio(Integer anio) { 
        this.anio = anio; 
    }
}