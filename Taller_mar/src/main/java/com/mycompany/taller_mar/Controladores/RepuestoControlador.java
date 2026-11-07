
package com.mycompany.taller.controlador;

import com.mycompany.taller.dao.RepuestoDao;
import com.mycompany.taller.modelo.Repuesto;
import java.util.List;

public class RepuestoControlador {

    private final RepuestoDao repuestoDao;

    public RepuestoControlador() {
        this.repuestoDao = new RepuestoDao();
    }

    public boolean registrarRepuesto(Repuesto repuesto) {
        validarRepuesto(repuesto);
        return repuestoDao.insertar(repuesto);
    }

    public Repuesto buscarRepuesto(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero.");
        }

        return repuestoDao.buscarPorId(id);
    }

    public boolean actualizarRepuesto(Repuesto repuesto) {
        if (repuesto == null || repuesto.getIdRepuesto() <= 0) {
            throw new IllegalArgumentException("El repuesto o su ID no son válidos.");
        }

        validarRepuesto(repuesto);
        return repuestoDao.actualizar(repuesto);
    }

    public boolean eliminarRepuesto(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero.");
        }

        return repuestoDao.eliminar(id);
    }

    public List<Repuesto> listarRepuestos() {
        return repuestoDao.listarTodos();
    }

    public boolean abastecerRepuesto(int id, int cantidad) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero.");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }

        return repuestoDao.abastecer(id, cantidad);
    }

    private void validarRepuesto(Repuesto repuesto) {
        if (repuesto == null) {
            throw new IllegalArgumentException("El repuesto no puede ser nulo.");
        }

        if (repuesto.getNombre() == null
                || repuesto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del repuesto es obligatorio.");
        }

        if (repuesto.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }

        if (repuesto.getExistencia() < 0) {
            throw new IllegalArgumentException("La existencia no puede ser negativa.");
        }
    }
}
