package com.mycompany.taller.dao;

import com.mycompany.taller.config.Conexiontaller;
import com.mycompany.taller.modelo.Vehiculo;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VehiculoDao implements CrudDao<Vehiculo> {
    
    

    @Override
    public boolean agregar(Vehiculo v) {
        String sql = "INSERT INTO vehiculos(id_cliente, placa, marca, modelo, anio) VALUES(?,?,?,?,?)";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, v.getIdCliente());
            ps.setString(2, v.getPlaca());
            ps.setString(3, v.getMarca());
            ps.setString(4, v.getModelo());
            ps.setInt(5, v.getAnio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    
        @Override
    public Vehiculo buscarPorId(int id) {
        String sql = "SELECT * FROM vehiculos WHERE id_vehiculo = ?";
        Vehiculo vehiculo = null;

        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vehiculo = mapearVehiculo(rs);
                }
            }

            System.out.println("--- RESULTADO DE BUSQUEDA POR ID ---");

            if (vehiculo != null) {
                System.out.println("Vehiculo encontrado con exito");
                System.out.println("ID: " + vehiculo.getIdVehiculo());
                System.out.println("ID: " + vehiculo.getIdCliente());
                System.out.println("Placa: " + vehiculo.getPlaca());
                System.out.println("Marca: " + vehiculo.getMarca());
                System.out.println("Modelo: " + vehiculo.getModelo());
                System.out.println("Año: " + vehiculo.getAnio());
            } else {
                System.out.println("No se encontro ningun vehiculo con el ID " + id);
            }

            System.out.println("------------------------------------");

        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar vehiculo por ID: " + e.getMessage(), e);
        }

        return vehiculo;
    }
    


    @Override
    public boolean actualizar(Vehiculo v) {
        String sql = "UPDATE vehiculos SET id_cliente=?, placa=?, marca=?, modelo=?, anio=? WHERE id_vehiculo=?";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, v.getIdCliente());
            ps.setString(2, v.getPlaca());
            ps.setString(3, v.getMarca());
            ps.setString(4, v.getModelo());
            ps.setInt(5, v.getAnio());
            ps.setInt(6, v.getIdVehiculo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM vehiculos WHERE id_vehiculo=?";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    
    @Override
    public List<Vehiculo> listar() {
        List<Vehiculo> listaVehiculos = new ArrayList<>();
        String sql = "SELECT id_vehiculo, id_cliente, placa, marca, modelo, anio FROM vehiculos";
        
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                listaVehiculos.add(mapearVehiculo(rs));  
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return listaVehiculos;
    }
    
    private Vehiculo mapearVehiculo (ResultSet rs) throws SQLException {
           
        return new Vehiculo   ( 
                    rs.getInt("id_vehiculo"),
                    rs.getInt("id_cliente"),
                    rs.getString("placa"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getInt("anio") 
        );
    }
    
    
}
