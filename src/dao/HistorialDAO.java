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

public class HistorialDAO {

    public boolean registrarActividad(Historial historial) throws ClassNotFoundException {
        String sql = "INSERT INTO historial (id_usuario, accion, fecha) VALUES (?, ?, ?)";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, historial.getIdUsuario());
            ps.setString(2, historial.getAccion());
            ps.setTimestamp(3, new Timestamp(historial.getFecha().getTime()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error registrarActividad: " + e.getMessage());
            return false;
        }
    }

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
            System.err.println("Error obtenerHistorialUsuario: " + e.getMessage());
        }
        return historial;
    }

}
