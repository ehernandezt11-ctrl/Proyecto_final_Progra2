
package com.mycompany.taller.controlador;

import com.mycompany.taller.dao.ServicioDao;
import com.mycompany.taller.modelo.Servicio;
import java.util.List;

public class ServicioControlador {

    private final ServicioDao servicioDao;

    public ServicioControlador() {
        this.servicioDao = new ServicioDao();
    }

    public boolean registrarServicio(Servicio servicio) {
        validarServicio(servicio);
        return servicioDao.insertar(servicio);
    }

    public Servicio buscarServicio(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero.");
        }

        return servicioDao.buscarPorId(id);
    }

    public boolean actualizarServicio(Servicio servicio) {
        if (servicio == null || servicio.getIdServicio() <= 0) {
            throw new IllegalArgumentException("El servicio o su ID no son válidos.");
        }

        validarServicio(servicio);
        return servicioDao.actualizar(servicio);
    }

    public boolean eliminarServicio(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero.");
        }

        return servicioDao.eliminar(id);
    }

    public List<Servicio> listarServicios() {
        return servicioDao.listarTodos();
    }

    private void validarServicio(Servicio servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException("El servicio no puede ser nulo.");
        }

        if (servicio.getNombreServicio() == null
                || servicio.getNombreServicio().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del servicio es obligatorio.");
        }

        if (servicio.getCostoManoObra() < 0) {
            throw new IllegalArgumentException("El costo de mano de obra no puede ser negativo.");
        }
    }
}
