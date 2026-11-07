
package com.mycompany.taller.controlador;

import com.mycompany.taller.dao.DetalleOrdenServicioDao;
import com.mycompany.taller.modelo.DetalleOrdenServicio;
import java.util.List;

public class DetalleOrdenServicioControlador {

    private final DetalleOrdenServicioDao detalleDao;

    public DetalleOrdenServicioControlador() {
        this.detalleDao = new DetalleOrdenServicioDao();
    }

    public boolean registrarDetalle(DetalleOrdenServicio detalle) {
        validarDetalle(detalle);
        return detalleDao.insertar(detalle);
    }

    public DetalleOrdenServicio buscarDetalle(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero");
        }

        return detalleDao.buscarPorId(id);
    }

    public boolean actualizarDetalle(DetalleOrdenServicio detalle) {
        if (detalle == null || detalle.getIdDetalleServicio() <= 0) {
            throw new IllegalArgumentException("El detalle o su ID no son validos");
        }

        validarDetalle(detalle);
        return detalleDao.actualizar(detalle);
    }

    public boolean eliminarDetalle(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero");
        }

        return detalleDao.eliminar(id);
    }

    public List<DetalleOrdenServicio> listarDetalles() {
        return detalleDao.listarTodos();
    }

    public List<DetalleOrdenServicio> listarDetallesPorOrden(int idOrden) {
        if (idOrden <= 0) {
            throw new IllegalArgumentException("El ID de la orden debe ser mayor que cero");
        }

        return detalleDao.listarPorOrden(idOrden);
    }

    private void validarDetalle(DetalleOrdenServicio detalle) {
        if (detalle == null) {
            throw new IllegalArgumentException("El detalle no puede ser nulo");
        }

        if (detalle.getIdOrden() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una orden de trabajo");
        }

        if (detalle.getServicio() == null
                || detalle.getServicio().getIdServicio() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un servicio valido");
        }

        if (detalle.getCostoAplicado() < 0) {
            throw new IllegalArgumentException("El costo aplicado no puede ser negativo");
        }
    }
}
