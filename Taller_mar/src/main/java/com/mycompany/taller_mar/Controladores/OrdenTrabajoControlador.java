
package com.mycompany.taller.controlador;

import com.mycompany.taller.dao.OrdenTrabajoDao;
import com.mycompany.taller.modelo.OrdenTrabajo;
import java.util.Arrays;
import java.util.List;

public class OrdenTrabajoControlador {

    private final OrdenTrabajoDao ordenDao;

    public OrdenTrabajoControlador() {
        this.ordenDao = new OrdenTrabajoDao();
    }

    public boolean registrarOrden(OrdenTrabajo orden) {
        validarOrden(orden);

        if (!"PENDIENTE".equals(orden.getEstado())) {
            throw new IllegalArgumentException("Una orden nueva debe estar PENDIENTE.");
        }

        return ordenDao.insertar(orden);
    }

    public OrdenTrabajo buscarOrden(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero.");
        }

        return ordenDao.buscarPorId(id);
    }

    public List<OrdenTrabajo> listarOrdenes() {
        return ordenDao.listarTodos();
    }

    public List<OrdenTrabajo> listarOrdenesPorVehiculo(int idVehiculo) {
        if (idVehiculo <= 0) {
            throw new IllegalArgumentException("El ID del vehículo debe ser mayor que cero.");
        }

        return ordenDao.listarPorVehiculo(idVehiculo);
    }

    public boolean actualizarOrden(OrdenTrabajo orden) {
        if (orden == null || orden.getIdOrden() <= 0) {
            throw new IllegalArgumentException("La orden o su ID no son válidos.");
        }

        validarOrden(orden);

        if (orden.getFechaIngreso() == null) {
            throw new IllegalArgumentException("La fecha de ingreso es obligatoria.");
        }

        if (orden.getEstado() == null
                || !Arrays.asList("PENDIENTE", "EN PROCESO", "FINALIZADA", "CANCELADA")
                        .contains(orden.getEstado())) {
            throw new IllegalArgumentException("El estado de la orden no es válido.");
        }

        return ordenDao.actualizar(orden);
    }

    public boolean eliminarOrden(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero.");
        }

        return ordenDao.eliminar(id);
    }

    private void validarOrden(OrdenTrabajo orden) {
        if (orden == null) {
            throw new IllegalArgumentException("La orden no puede ser nula.");
        }

        if (orden.getVehiculo() == null
                || orden.getVehiculo().getIdVehiculo() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un vehículo válido.");
        }

        if (orden.getMecanico() == null
                || orden.getMecanico().getId() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un mecánico válido.");
        }

        if (orden.getdescripcionProblema() == null
                || orden.getdescripcionProblema().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del problema es obligatoria.");
        }
    }
}
