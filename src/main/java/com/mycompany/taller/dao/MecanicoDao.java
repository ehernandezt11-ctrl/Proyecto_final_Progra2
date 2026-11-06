package com.mycompany.taller.dao;

import com.mycompany.taller.config.Conexiontaller;
import com.mycompany.taller.modelo.Mecanico;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MecanicoDao implements CrudDao<Mecanico> {
    
    @Override
    public boolean agregar(Mecanico m) {
        String sql = "INSERT INTO mecanicos(nombres, apellidos, telefono, especialidad) VALUES(?,?,?,?)";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNombres());
            ps.setString(2, m.getApellidos());
            ps.setString(3, m.getTelefono());
            ps.setString(4, m.getEspecialidad());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    @Override
    public boolean actualizar(Mecanico m) {
        String sql = "UPDATE mecanicos SET nombres=?, apellidos=?, telefono=?, especialidad=? WHERE id_mecanico=?";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNombres());
            ps.setString(2, m.getApellidos());
            ps.setString(3, m.getTelefono());
            ps.setString(4, m.getEspecialidad());
            ps.setInt(5, m.getIdMecanico());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

     @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM mecanicos WHERE id_mecanico=?";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    
    @Override
    public Mecanico buscarPorId(int id) {
        String sql = "SELECT * FROM mecanicos WHERE id_mecanico = ?";
        Mecanico mecanico = null;

        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                  mecanico = mapearMecanico(rs);
                }
            }

            System.out.println("--- RESULTADO DE BUSQUEDA POR ID ---");
            if (mecanico != null) {
                System.out.println("Mecanico encontrado con exito");
                System.out.println("ID: " + mecanico.getIdMecanico());
                System.out.println("Nombre Completo: "+ mecanico.getNombres() + " "+ mecanico.getApellidos());
                System.out.println("Telefono: " + mecanico.getTelefono());
                System.out.println("Especialidad: " + mecanico.getEspecialidad());
            } else {
                System.out.println("No se encontro ningun mecanico con el ID " + id);
            }
            System.out.println("------------------------------------");

        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar mecanico por ID: " + e.getMessage(), e);
        }

        return mecanico;
    }
    
    
    @Override
    public List<Mecanico> listar() {
        List<Mecanico> listaMecanicos = new ArrayList<>();
        String sql = "SELECT id_mecanico, nombres, apellidos, telefono, especialidad FROM mecanicos";
        
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
               listaMecanicos.add(mapearMecanico(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return listaMecanicos;
    }
    
    
     private Mecanico mapearMecanico(ResultSet rs) throws SQLException {
     
        return new Mecanico   ( 
                rs.getInt("id_mecanico"),
                    rs.getString("nombres"),
                    rs.getString("apellidos"),
                    rs.getString("telefono"),
                    rs.getString("especialidad")
        );
    }
    
    
}
