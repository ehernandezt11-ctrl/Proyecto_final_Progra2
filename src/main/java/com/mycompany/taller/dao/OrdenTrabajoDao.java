package com.mycompany.taller.dao;

import com.mycompany.taller.config.Conexiontaller;
import com.mycompany.taller.modelo.OrdenTrabajo;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrdenTrabajoDao implements CrudDao<OrdenTrabajo> {

    @Override
    public boolean agregar(OrdenTrabajo o) {
        String sql = "INSERT INTO ordenes_trabajo (id_vehiculo, id_mecanico, fecha_ingreso, descripcion_problema, estado, costo_total) VALUES (?, ?, NOW(), ?, 'PENDIENTE', 0.0)";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, o.getIdVehiculo());
            ps.setInt(2, o.getIdMecanico());
            ps.setString(3, o.getDescripcionProblema());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean guardar(OrdenTrabajo o) {
        return agregar(o);
    }

    @Override
    public boolean actualizar(OrdenTrabajo o) {
        String sql = "UPDATE ordenes_trabajo SET id_vehiculo=?, id_mecanico=?, descripcion_problema=?, estado=?, costo_total=? WHERE id_orden=?";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, o.getIdVehiculo());
            ps.setInt(2, o.getIdMecanico());
            ps.setString(3, o.getDescripcionProblema());
            ps.setString(4, o.getEstado());
            ps.setDouble(5, o.getCostoTotal());
            ps.setInt(6, o.getIdOrden());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM ordenes_trabajo WHERE id_orden=?";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public OrdenTrabajo buscarPorId(int id) {
        String sql = "SELECT * FROM ordenes_trabajo WHERE id_orden = ?";
        OrdenTrabajo orden = null;

        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    orden = mapearOrden(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orden;
    }

    @Override
    public List<OrdenTrabajo> listar() {
        List<OrdenTrabajo> lista = new ArrayList<>();
        String sql = "SELECT * FROM ordenes_trabajo";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearOrden(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    
    public boolean iniciarReparacion(int idOrden) {
        String sql = "UPDATE ordenes_trabajo SET estado = 'EN PROCESO' WHERE id_orden = ? AND estado = 'PENDIENTE'";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idOrden);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

  
    public boolean asignarRepuesto(int idOrden, int idRepuesto, int cantidad) {
        String sqlCheckStock = "SELECT existencia FROM repuestos WHERE id_repuesto = ?";
        String sqlDescontarStock = "UPDATE repuestos SET existencia = existencia - ? WHERE id_repuesto = ? AND existencia >= ?";
        String sqlInsertDetalle = "INSERT INTO detalle_orden_repuestos (id_orden, id_repuesto, cantidad) VALUES (?, ?, ?)";

        Connection con = null;
        try {
            con = Conexiontaller.getConexion();
            con.setAutoCommit(false); 

 
            
            try (PreparedStatement psCheck = con.prepareStatement(sqlCheckStock)) {
                psCheck.setInt(1, idRepuesto);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        int stockActual = rs.getInt("existencia");
                        if (stockActual < cantidad) {
                            con.rollback();
                            return false; 
                        }
                    } else {
                        con.rollback();
                        return false;
                    }
                }
            }

         
            
            try (PreparedStatement psUpdate = con.prepareStatement(sqlDescontarStock)) {
                psUpdate.setInt(1, cantidad);
                psUpdate.setInt(2, idRepuesto);
                psUpdate.setInt(3, cantidad);
                int affectedRows = psUpdate.executeUpdate();
                if (affectedRows == 0) {
                    con.rollback();
                    return false;
                }
            }


            try (PreparedStatement psInsert = con.prepareStatement(sqlInsertDetalle)) {
                psInsert.setInt(1, idOrden);
                psInsert.setInt(2, idRepuesto);
                psInsert.setInt(3, cantidad);
                psInsert.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }


    public boolean finalizarOrden(int idOrden) {
        String sqlCheckServicios = "SELECT COUNT(*) FROM detalle_orden_servicios WHERE id_orden = ?";
        String sqlCalcServicios = "SELECT COALESCE(SUM(s.costo_mano_obra), 0) FROM detalle_orden_servicios dos JOIN servicios s ON dos.id_servicio = s.id_servicio WHERE dos.id_orden = ?";
        String sqlCalcRepuestos = "SELECT COALESCE(SUM(dor.cantidad * r.precio), 0) FROM detalle_orden_repuestos dor JOIN repuestos r ON dor.id_repuesto = r.id_repuesto WHERE dor.id_orden = ?";
        String sqlUpdateOrden = "UPDATE ordenes_trabajo SET estado = 'FINALIZADA', fecha_entrega = NOW(), costo_total = ? WHERE id_orden = ?";

        Connection con = null;
        try {
            con = Conexiontaller.getConexion();
            con.setAutoCommit(false);

         
            try (PreparedStatement psServ = con.prepareStatement(sqlCheckServicios)) {
                psServ.setInt(1, idOrden);
                try (ResultSet rs = psServ.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        con.rollback();
                        return false; 
                    }
                }
            }

           
            double totalManoObra = 0;
            try (PreparedStatement psMano = con.prepareStatement(sqlCalcServicios)) {
                psMano.setInt(1, idOrden);
                try (ResultSet rs = psMano.executeQuery()) {
                    if (rs.next()) totalManoObra = rs.getDouble(1);
                }
            }

            double totalRepuestos = 0;
            try (PreparedStatement psRep = con.prepareStatement(sqlCalcRepuestos)) {
                psRep.setInt(1, idOrden);
                try (ResultSet rs = psRep.executeQuery()) {
                    if (rs.next()) totalRepuestos = rs.getDouble(1);
                }
            }

            double costoTotal = totalManoObra + totalRepuestos;

            
            try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdateOrden)) {
                psUpdate.setDouble(1, costoTotal);
                psUpdate.setInt(2, idOrden);
                psUpdate.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) { try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) { try { con.setAutoCommit(true); con.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
    }

    
    public boolean cancelarOrden(int idOrden) {
        String sqlGetRepuestos = "SELECT id_repuesto, cantidad FROM detalle_orden_repuestos WHERE id_orden = ?";
        String sqlDevolverStock = "UPDATE repuestos SET existencia = existencia + ? WHERE id_repuesto = ?";
        String sqlUpdateEstado = "UPDATE ordenes_trabajo SET estado = 'CANCELADA' WHERE id_orden = ? AND estado IN ('PENDIENTE', 'EN PROCESO')";

        Connection con = null;
        try {
            con = Conexiontaller.getConexion();
            con.setAutoCommit(false);

            
            try (PreparedStatement psGet = con.prepareStatement(sqlGetRepuestos)) {
                psGet.setInt(1, idOrden);
                try (ResultSet rs = psGet.executeQuery()) {
                    while (rs.next()) {
                        int idRepuesto = rs.getInt("id_repuesto");
                        int cantidad = rs.getInt("cantidad");

                      
                        try (PreparedStatement psDev = con.prepareStatement(sqlDevolverStock)) {
                            psDev.setInt(1, cantidad);
                            psDev.setInt(2, idRepuesto);
                            psDev.executeUpdate();
                        }
                    }
                }
            }

           
            try (PreparedStatement psUp = con.prepareStatement(sqlUpdateEstado)) {
                psUp.setInt(1, idOrden);
                int rows = psUp.executeUpdate();
                if (rows == 0) {
                    con.rollback();
                    return false; 
                }
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) { try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) { try { con.setAutoCommit(true); con.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
    }
    
    public boolean actualizarEstado(int idOrden, String nuevoEstado) {
        String sql = "UPDATE ordenes_trabajo SET estado = ? WHERE id_orden = ?";
        try (Connection con = Conexiontaller.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idOrden);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private OrdenTrabajo mapearOrden(ResultSet rs) throws SQLException {
        return new OrdenTrabajo(
            rs.getInt("id_orden"),
            rs.getInt("id_vehiculo"),
            rs.getInt("id_mecanico"),
            rs.getString("fecha_ingreso"),
            rs.getString("fecha_entrega"),
            rs.getString("descripcion_problema"),
            rs.getString("estado"),
            rs.getDouble("costo_total")
        );
    }
}