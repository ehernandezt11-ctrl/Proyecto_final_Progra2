
package com.mycompany.taller.controlador;

import com.mycompany.taller.dao.ClienteDao;
import com.mycompany.taller.modelo.Cliente;
import java.util.List;

public class ClienteControlador {

    private final ClienteDao clienteDao;

    public ClienteControlador() {
        this.clienteDao = new ClienteDao();
    }

    public boolean registrarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }

        if (cliente.getNombres() == null || cliente.getNombres().trim().isEmpty()) {
            throw new IllegalArgumentException("Los nombres son obligatorios");
        }

        if (cliente.getApellidos() == null || cliente.getApellidos().trim().isEmpty()) {
            throw new IllegalArgumentException("Los apellidos son obligatorios");
        }

        return clienteDao.insertar(cliente);
    }

    public Cliente buscarCliente(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero");
        }

        return clienteDao.buscarPorId(id);
    }

    public boolean actualizarCliente(Cliente cliente) {
        if (cliente == null || cliente.getId() <= 0) {
            throw new IllegalArgumentException("El cliente o su ID no son válidos");
        }

        if (cliente.getNombres() == null || cliente.getNombres().trim().isEmpty()) {
            throw new IllegalArgumentException("Los nombres son obligatorios");
        }

        if (cliente.getApellidos() == null || cliente.getApellidos().trim().isEmpty()) {
            throw new IllegalArgumentException("Los apellidos son obligatorios");
        }

        return clienteDao.actualizar(cliente);
    }

    public boolean eliminarCliente(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que cero");
        }

        return clienteDao.eliminar(id);
    }

    public List<Cliente> listarClientes() {
        return clienteDao.listarTodos();
    }
}
