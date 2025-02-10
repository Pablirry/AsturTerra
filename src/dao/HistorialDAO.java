package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.ConexionDB;
import model.Historial;

public class HistorialDAO {

    public boolean registrarAccion(int idUsuario, String accion) throws ClassNotFoundException {
        String sql = "INSERT INTO historial (id_usuario, accion) VALUES (?, ?)";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setString(2, accion);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en registrarAccion: " + e.getMessage());
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
            System.err.println("Error en obtenerHistorialUsuario: " + e.getMessage());
        }
        return historial;
    }

}
