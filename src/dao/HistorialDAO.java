package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import config.ConexionDB;
import model.Historial;
import utils.UIUtils;

/**
 * @author Pablo
 */


public class HistorialDAO {

    /**
     * Método para registrar una actividad en el historial
     * @param historial : Historial
     * @return : boolean
     * @throws ClassNotFoundException
     */

    public boolean registrarActividad(Historial historial) throws ClassNotFoundException {
        String sql = "INSERT INTO historial (id_usuario, accion, fecha) VALUES (?, ?, ?)";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, historial.getIdUsuario());
            ps.setString(2, historial.getAccion());
            ps.setTimestamp(3, new Timestamp(historial.getFecha().getTime()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            UIUtils.mostrarError(null, "Error al registrar actividad: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para obtener el historial de un usuario
     * @param idUsuario : entero
     * @return : list<Historial>
     * @throws ClassNotFoundException
     */

    public List<Historial> obtenerHistorialUsuario(int idUsuario) throws ClassNotFoundException {
        List<Historial> historial = new ArrayList<>();
        String sql = "SELECT * FROM historial WHERE id_usuario = ? ORDER BY fecha DESC";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                historial.add(new Historial(
                        rs.getInt("id"),
                        rs.getInt("id_usuario"),
                        rs.getString("accion"),
                        rs.getTimestamp("fecha")));
            }
        } catch (SQLException e) {
            UIUtils.mostrarError(null, "Error al obtener historial: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            UIUtils.mostrarError(null, "Error de conexión a la base de datos: " + e.getMessage());
        }
        return historial;
    }

}
