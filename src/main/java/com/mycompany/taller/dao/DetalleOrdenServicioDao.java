package com.mycompany.taller.dao;

import com.mycompany.taller.config.Conexiontaller;
import com.mycompany.taller.modelo.DetalleOrdenServicio;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DetalleOrdenServicioDao {
    public List<DetalleOrdenServicio> listarPorOrden(int idOrden) {
        List<DetalleOrdenServicio> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_orden_servicios WHERE id_orden = ?";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idOrden);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new DetalleOrdenServicio(
                        rs.getInt("id_detalle_servicio"),
                        rs.getInt("id_orden"),
                        rs.getInt("id_servicio"),
                        rs.getDouble("costo_aplicado")
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean agregar(DetalleOrdenServicio dos) {
        String sql = "INSERT INTO detalle_orden_servicios(id_orden, id_servicio, costo_aplicado) VALUES(?,?,?)";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dos.getIdOrden());
            ps.setInt(2, dos.getIdServicio());
            ps.setDouble(3, dos.getCostoAplicado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
