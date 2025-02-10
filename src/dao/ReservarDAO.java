package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import config.ConexionDB;
import model.Reserva;

public class ReservarDAO {

    public boolean reservarRuta(int idUsuario, int idRuta, Date fecha) throws ClassNotFoundException {
        Connection con = ConexionDB.getConection();
        String sql = "INSERT INTO reservas (id_usuario, id_ruta, fecha) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idRuta);
            ps.setDate(3, new java.sql.Date(fecha.getTime()));
            ps.executeUpdate();
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en reservar Ruta: " + e.getMessage());
            return false;
        }
    }

    public List<Reserva> obtenerReservasUsuario(int idUsuario) throws ClassNotFoundException {
        List<Reserva> reservas = new ArrayList<>();
        Connection con = ConexionDB.getConection();
        String sql = "SELECT * FROM reservas WHERE id_usuario = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reservas.add(new Reserva(
                    rs.getInt("id"),
                    rs.getInt("id_usuario"),
                    rs.getInt("id_ruta"),
                    rs.getDate("fecha")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservas;
    }

    public void cancelarReserva(int idReserva) throws ClassNotFoundException {
        Connection con = ConexionDB.getConection();
        String sql = "DELETE FROM reservas WHERE id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idReserva);
            ps.executeUpdate();
            System.out.println("Reserva cancelada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
