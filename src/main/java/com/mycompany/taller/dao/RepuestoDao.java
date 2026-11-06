package com.mycompany.taller.dao;

import com.mycompany.taller.config.Conexiontaller;
import com.mycompany.taller.modelo.Repuesto;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RepuestoDao implements CrudDao<Repuesto> {
    
    @Override
    public boolean agregar(Repuesto r) {
        String sql = "INSERT INTO repuestos(nombre, descripcion, precio, existencia) VALUES(?,?,?,?)";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, r.getNombre());
            ps.setString(2, r.getDescripcion());
            ps.setDouble(3, r.getPrecio());
            ps.setInt(4, r.getExistencia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    } 

    @Override
    public boolean actualizar(Repuesto r) {
        String sql = "UPDATE repuestos SET nombre=?, descripcion=?, precio=?, existencia=? WHERE id_repuesto=?";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, r.getNombre());
            ps.setString(2, r.getDescripcion());
            ps.setDouble(3, r.getPrecio());
            ps.setInt(4, r.getExistencia());
            ps.setInt(5, r.getIdRepuesto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM repuestos WHERE id_repuesto=?";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    
    @Override
    public Repuesto buscarPorId(int id) {
        String sql = "SELECT * FROM repuestos WHERE id_repuesto = ?";
        Repuesto repuesto = null;

        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    repuesto = mapearRepuesto(rs);
                }
            }

            System.out.println("--- RESULTADO DE BUSQUEDA POR ID ---");

            if (repuesto != null) {
                System.out.println("Repuesto encontrado con exito");
                System.out.println("ID: " + repuesto.getIdRepuesto());
                System.out.println("Nombre: " + repuesto.getNombre());
                System.out.println("Descripcion: " + repuesto.getDescripcion());
                System.out.println("Precio: " + repuesto.getPrecio());
                System.out.println("Existencia: " + repuesto.getExistencia());
            } else {
                System.out.println("No se encontro ningun repuesto con el ID " + id);
            }

            System.out.println("------------------------------------");

        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar repuesto por ID: " + e.getMessage(), e);
        }

        return repuesto;
    }
    
        
    @Override
    public List<Repuesto> listar() {
        List<Repuesto> listaRepuesto = new ArrayList<>();
        String sql = "SELECT id_repuesto, nombre, descripcion, precio, existencia FROM repuestos";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                listaRepuesto.add(mapearRepuesto(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return listaRepuesto;
    }
    
    
    private Repuesto mapearRepuesto(ResultSet rs) throws SQLException {
            
        return new Repuesto   ( 
                    rs.getInt("id_repuesto"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("existencia")
        );
    }
    
    
    public boolean abastecer(int idRepuesto, int agrCantidad) {
        String sql = "UPDATE repuestos SET existencia = existencia + ? WHERE id_repuesto = ?";
        try (Connection con = Conexiontaller.getConexion();
            PreparedStatement ps = con.prepareStatement(sql))
                {
                ps.setInt(1, agrCantidad);
                ps.setInt(2, idRepuesto);
                return ps.executeUpdate() > 0;
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Repuesto> statusInventario(int limitExistencia ) {
        List<Repuesto> lista = new ArrayList<>();
        String sql = "SELECT id_repuesto, nombre, descripcion, precio, existencia FROM repuestos WHERE existencia <=?";
        try (Connection  con = Conexiontaller.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limitExistencia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearRepuesto(rs));
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    
    
    
}
