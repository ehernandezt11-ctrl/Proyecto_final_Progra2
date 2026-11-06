package com.mycompany.taller.dao;

import com.mycompany.taller.config.Conexiontaller;
import com.mycompany.taller.modelo.Servicio;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ServicioDao implements CrudDao<Servicio> {
    
    @Override
    public boolean agregar(Servicio s) {
        String sql = "INSERT INTO servicios(nombre_servicio, descripcion, costo_mano_obra) VALUES(?,?,?)";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getNombreServicio());
            ps.setString(2, s.getDescripcion());
            ps.setDouble(3, s.getCostoManoObra());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    @Override
    public boolean actualizar(Servicio s) {
        String sql = "UPDATE servicios SET nombre_servicio=?, descripcion=?, costo_mano_obra=? WHERE id_servicio=?";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getNombreServicio());
            ps.setString(2, s.getDescripcion());
            ps.setDouble(3, s.getCostoManoObra());
            ps.setInt(4, s.getIdServicio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM servicios WHERE id_servicio=?";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    
    
     @Override
    public Servicio buscarPorId(int id) {
        String sql = "SELECT * FROM servicios WHERE id_servicio = ?";
        Servicio servicio = null;

        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    servicio =  mapearServicio(rs);
                }
            }

            System.out.println("--- RESULTADO DE BUSQUEDA POR ID ---");

            if (servicio != null) {
                System.out.println("Servicio encontrado con exito");
                System.out.println("ID: " + servicio.getIdServicio());
                System.out.println("Nombre Servicio:" + servicio.getNombreServicio());
                System.out.println("Descripcion: " + servicio.getDescripcion());
                System.out.println("Costo mano Obra: " + servicio.getCostoManoObra());
            } else {
                System.out.println("No se encontro ningun servicio con el ID " + id);
            }

            System.out.println("------------------------------------");

        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar servicio por ID: " + e.getMessage(), e);
        }

        return servicio;
    }
    
    
    @Override
    public List<Servicio> listar() {
        List<Servicio> listaServicio = new ArrayList<>();
        String sql = "SELECT id_servicio, nombre_servicio, descripcion, costo_mano_obra FROM servicios";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                listaServicio.add(mapearServicio(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return listaServicio;
    }

    private Servicio mapearServicio(ResultSet rs) throws SQLException {
        return new Servicio(
            rs.getInt("id_servicio"),
            rs.getString("nombre_servicio"),
            rs.getString("descripcion"),
            rs.getDouble("costo_mano_obra")
        );
    
       }
    


    
}
