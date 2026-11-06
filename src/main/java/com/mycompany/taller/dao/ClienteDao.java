package com.mycompany.taller.dao;

import com.mycompany.taller.config.Conexiontaller;
import com.mycompany.taller.modelo.Cliente;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ClienteDao implements CrudDao<Cliente> {
    
    
    @Override
    public boolean agregar(Cliente c) {
        String sql = "INSERT INTO clientes(nombres, apellidos, telefono, correo, direccion) VALUES(?,?,?,?,?)";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNombres());
            ps.setString(2, c.getApellidos());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getCorreo());
            ps.setString(5, c.getDireccion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
        
    }
    
    
    @Override
    public boolean actualizar(Cliente c) {
        String sql = "UPDATE clientes SET nombres=?, apellidos=?, telefono=?, correo=?, direccion=? WHERE id_cliente=?";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNombres());
            ps.setString(2, c.getApellidos());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getCorreo());
            ps.setString(5, c.getDireccion());
            ps.setInt(6, c.getIdCliente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM clientes WHERE id_cliente=?";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    

  @Override
    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";
        Cliente cliente = null;

        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cliente = mapearCliente(rs);
                }
            }

            System.out.println("--- RESULTADO DE BUSQUEDA POR ID ---");

            if (cliente != null) {
                System.out.println("Cliente encontrado con exito");
                System.out.println("ID: " + cliente.getIdCliente());
                System.out.println("Nombre Completo: "
                        + cliente.getNombres() + " "
                        + cliente.getApellidos());
                System.out.println("Telefono: " + cliente.getTelefono());
                System.out.println("Direccion: " + cliente.getDireccion());
                System.out.println("Correo: " + cliente.getCorreo());
            } else {
                System.out.println("No se encontro ningun cliente con el ID " + id);
            }

            System.out.println("------------------------------------");

        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar cliente por ID: " + e.getMessage(), e);
        }

        return cliente;
    }


        @Override
    public List<Cliente> listar() {
        List<Cliente> listaClientes = new ArrayList<>();
        String sql = "SELECT id_cliente, nombres, apellidos, telefono, correo, direccion FROM clientes";
        
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                listaClientes.add(mapearCliente(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return listaClientes;
    }
    
    
    
     private Cliente mapearCliente(ResultSet rs) throws SQLException {
        return new Cliente   ( 
                rs.getInt("id_cliente"),
                rs.getString("nombres") ,
                rs.getString("apellidos") ,
                rs.getString("telefono") ,
                rs.getString("correo"),
                rs.getString("direccion") 
        );
    }
    

    
}


