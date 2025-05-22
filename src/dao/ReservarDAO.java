package dao;

import java.sql.*;
import java.util.*;
import config.ConexionDB;
import model.Reserva;

public class ReservarDAO {

    public List<Reserva> listarReservas() throws ClassNotFoundException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reservas";
        try (Connection con = ConexionDB.getConection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                reservas.add(new Reserva(
                    rs.getInt("id"),
                    rs.getInt("id_usuario"),
                    rs.getInt("id_ruta"),
                    rs.getTimestamp("fecha"),
                    rs.getBoolean("confirmada")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservas;
    }

    public boolean eliminarReserva(int idReserva) throws ClassNotFoundException {
        String sql = "DELETE FROM reservas WHERE id = ?";
        try (Connection con = ConexionDB.getConection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean confirmarReserva(int idReserva) throws ClassNotFoundException {
        String sql = "UPDATE reservas SET confirmada = TRUE WHERE id = ?";
        try (Connection con = ConexionDB.getConection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean agregarReserva(Reserva reserva) throws Exception {
    String sql = "INSERT INTO reservas (id_usuario, id_ruta, fecha, confirmada) VALUES (?, ?, ?, ?)";
    try (Connection con = ConexionDB.getConection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, reserva.getIdUsuario());
        ps.setInt(2, reserva.getIdRuta());
        ps.setTimestamp(3, new java.sql.Timestamp(reserva.getFecha().getTime()));
        ps.setBoolean(4, reserva.isConfirmada());
        return ps.executeUpdate() > 0;
    }
}
}