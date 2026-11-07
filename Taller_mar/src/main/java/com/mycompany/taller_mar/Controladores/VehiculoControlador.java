
package com.mycompany.taller.controlador;

import com.mycompany.taller.dao.VehiculoDao;
import com.mycompany.taller.modelo.Vehiculo;
import java.util.List;

public class VehiculoControlador {

    private final VehiculoDao vehiculoDao;

    public VehiculoControlador() {
        this.vehiculoDao = new VehiculoDao();
    }

    public boolean registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehiculo no puede ser nulo");
        }

        if (vehiculo.getPlaca() == null || vehiculo.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("La placa es obligatoria");
        }

        if (vehiculo.getMarca() == null || vehiculo.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("La marca es obligatoria");
        }

        if (vehiculo.getModelo() == null || vehiculo.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo es obligatorio");
        }

        if (vehiculo.getAnio() <= 0) {
            throw new IllegalArgumentException("El año del vehiculo no es valido");
        }

        if (vehiculo.getClientePropietario() == null
                || vehiculo.getClientePropietario().getId() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un cliente existente");
        }

        return vehiculoDao.insertar(vehiculo);
    }

    public Vehiculo buscarVehiculo(int idVehiculo) {
        if (idVehiculo <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero");
        }

        return vehiculoDao.buscarPorId(idVehiculo);
    }

    public boolean actualizarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null || vehiculo.getIdVehiculo() <= 0) {
            throw new IllegalArgumentException("El vehiculo o su ID no son validos");
        }

        if (vehiculo.getPlaca() == null || vehiculo.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("La placa es obligatoria");
        }

        if (vehiculo.getMarca() == null || vehiculo.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("La marca es obligatoria");
        }

        if (vehiculo.getModelo() == null || vehiculo.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo es obligatorio");
        }

        if (vehiculo.getAnio() <= 0) {
            throw new IllegalArgumentException("El año del vehiculo no es valido");
        }

        if (vehiculo.getClientePropietario() == null
                || vehiculo.getClientePropietario().getId() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un cliente existente");
        }

        return vehiculoDao.actualizar(vehiculo);
    }

    public boolean eliminarVehiculo(int idVehiculo) {
        if (idVehiculo <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero");
        }

        return vehiculoDao.eliminar(idVehiculo);
    }

    public List<Vehiculo> listarVehiculos() {
        return vehiculoDao.listarTodos();
    }
}
