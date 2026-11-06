package com.mycompany.taller.dao;

import com.mycompany.taller.config.Conexiontaller;
import com.mycompany.taller.modelo.DetalleOrdenRepuesto;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DetalleOrdenRepuestoDao {
    public List<DetalleOrdenRepuesto> listarPorOrden(int idOrden) {
        List<DetalleOrdenRepuesto> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_orden_repuestos WHERE id_orden = ?";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idOrden);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new DetalleOrdenRepuesto(
                        rs.getInt("id_detalle_repuesto"),
                        rs.getInt("id_orden"),
                        rs.getInt("id_repuesto"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio_unitario"),
                        rs.getDouble("subtotal")
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean agregar(DetalleOrdenRepuesto dor) {
        String sql = "INSERT INTO detalle_orden_repuestos(id_orden, id_repuesto, cantidad, precio_unitario, subtotal) VALUES(?,?,?,?,?)";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dor.getIdOrden());
            ps.setInt(2, dor.getIdRepuesto());
            ps.setInt(3, dor.getCantidad());
            ps.setDouble(4, dor.getPrecioUnitario());
            ps.setDouble(5, dor.getSubtotal());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
