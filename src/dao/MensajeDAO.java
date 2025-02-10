package dao;

import config.ConexionDB;
import modelo.Mensaje;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MensajeDAO {

    public boolean enviarMensaje(int idUsuario, String mensaje) throws ClassNotFoundException {
        String sql = "INSERT INTO mensajes (id_usuario, mensaje) VALUES (?, ?)";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setString(2, mensaje);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en enviarMensaje: " + e.getMessage());
            return false;
        }
    }

    public List<Mensaje> obtenerMensajes(int idUsuario) throws ClassNotFoundException {
        List<Mensaje> mensajes = new ArrayList<>();
        String sql = "SELECT * FROM mensajes WHERE id_usuario = ? ORDER BY fecha DESC";

        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                mensajes.add(new Mensaje(
                        rs.getInt("id"),
                        rs.getInt("id_usuario"),
                        rs.getString("mensaje"),
                        rs.getString("respuesta"),
                        rs.getTimestamp("fecha")));
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerMensajes: " + e.getMessage());
        }
        return mensajes;
    }
}
