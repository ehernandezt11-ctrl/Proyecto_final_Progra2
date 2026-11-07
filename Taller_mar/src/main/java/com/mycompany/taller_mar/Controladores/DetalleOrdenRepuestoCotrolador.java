
package com.mycompany.taller.controlador;

import com.mycompany.taller.dao.DetalleOrdenRepuestoDao;
import com.mycompany.taller.modelo.DetalleOrdenRepuesto;
import java.util.List;

public class DetalleOrdenRepuestoControlador {

    private final DetalleOrdenRepuestoDao detalleDao;

    public DetalleOrdenRepuestoControlador() {
        this.detalleDao = new DetalleOrdenRepuestoDao();
    }

    public boolean registrarDetalle(DetalleOrdenRepuesto detalle) {
        validarDetalle(detalle);
        return detalleDao.insertar(detalle);
    }

    public DetalleOrdenRepuesto buscarDetalle(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero");
        }

        return detalleDao.buscarPorId(id);
    }

    public boolean actualizarDetalle(DetalleOrdenRepuesto detalle) {
        if (detalle == null || detalle.getIdDetalleRepuesto() <= 0) {
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

    public List<DetalleOrdenRepuesto> listarDetalles() {
        return detalleDao.listarTodos();
    }

    public List<DetalleOrdenRepuesto> listarDetallesPorOrden(int idOrden) {
        if (idOrden <= 0) {
            throw new IllegalArgumentException("El ID de la orden debe ser mayor que cero");
        }

        return detalleDao.listarPorOrden(idOrden);
    }

    private void validarDetalle(DetalleOrdenRepuesto detalle) {
        if (detalle == null) {
            throw new IllegalArgumentException("El detalle no puede ser nulo");
        }

        if (detalle.getIdOrden() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una orden de trabajo");
        }

        if (detalle.getRepuesto() == null
                || detalle.getRepuesto().getIdRepuesto() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un repuesto valido");
        }

        if (detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        if (detalle.getPrecioUnitario() < 0) {
            throw new IllegalArgumentException("El precio unitario no puede ser negativo");
        }
    }
}
