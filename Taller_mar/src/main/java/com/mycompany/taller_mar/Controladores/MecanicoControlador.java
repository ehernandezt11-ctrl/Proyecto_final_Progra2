
package com.mycompany.taller.controlador;

import com.mycompany.taller.dao.MecanicoDao;
import com.mycompany.taller.modelo.Mecanico;
import java.util.List;

public class MecanicoControlador {

    private final MecanicoDao mecanicoDao;

    public MecanicoControlador() {
        this.mecanicoDao = new MecanicoDao();
    }

    public boolean registrarMecanico(Mecanico mecanico) {
        validarMecanico(mecanico);
        return mecanicoDao.insertar(mecanico);
    }

    public Mecanico buscarMecanico(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero");
        }

        return mecanicoDao.buscarPorId(id);
    }

    public boolean actualizarMecanico(Mecanico mecanico) {
        if (mecanico == null || mecanico.getId() <= 0) {
            throw new IllegalArgumentException("El mecanico o su ID no son validos");
        }

        validarMecanico(mecanico);
        return mecanicoDao.actualizar(mecanico);
    }

    public boolean eliminarMecanico(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero");
        }

        return mecanicoDao.eliminar(id);
    }

    public List<Mecanico> listarMecanicos() {
        return mecanicoDao.listarTodos();
    }

    private void validarMecanico(Mecanico mecanico) {
        if (mecanico == null) {
            throw new IllegalArgumentException("El mecanico no puede ser nulo");
        }

        if (mecanico.getNombres() == null
                || mecanico.getNombres().trim().isEmpty()) {
            throw new IllegalArgumentException("Los nombres son obligatorios");
        }

        if (mecanico.getApellidos() == null
                || mecanico.getApellidos().trim().isEmpty()) {
            throw new IllegalArgumentException("Los apellidos son obligatorios");
        }

        if (mecanico.getEspecialidad() == null
                || mecanico.getEspecialidad().trim().isEmpty()) {
            throw new IllegalArgumentException("La especialidad es obligatoria");
        }
    }
}
