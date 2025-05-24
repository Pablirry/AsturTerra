package dao;

import java.sql.*;
import java.util.*;
import config.ConexionDB;
import model.Reserva;
import model.ReservaEvento;

/**
 * @author Pablo
 */

public class ReservarDAO {

    /**
     * Método para listar todas las reservas
     * 
     * @return : list<Reserva>
     * @throws ClassNotFoundException
     */

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
                        rs.getBoolean("confirmada")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservas;
    }

    /**
     * Método para eliminar una reserva
     * 
     * @param idReserva : entero
     * @return : boolean
     * @throws ClassNotFoundException
     */

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

    /**
     * Método para confirmar una reserva
     * 
     * @param idReserva : entero
     * @return : boolean
     * @throws ClassNotFoundException
     */

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

    /**
     * Método para agregar una reserva
     * 
     * @param reserva : Reserva
     * @return : boolean
     * @throws Exception
     */

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

    /**
     * Método para listar todas las reservas de eventos
     * 
     * @return : list<ReservaEvento>
     * @throws ClassNotFoundException
     */

    public List<ReservaEvento> listarReservasEvento() throws ClassNotFoundException {
        List<ReservaEvento> reservas = new ArrayList<>();
        String sql = "SELECT * FROM reservas_eventos";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                reservas.add(new ReservaEvento(
                        rs.getInt("id"),
                        rs.getInt("usuario_id"),
                        rs.getInt("evento_id"),
                        rs.getDate("fecha_reserva"),
                        rs.getBoolean("confirmada")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservas;
    }

    /**
     * Método para agregar una reserva de evento
     * 
     * @param reserva : ReservaEvento
     * @return : boolean
     * @throws Exception
     */

    public boolean agregarReservaEvento(ReservaEvento reserva) throws Exception {
        String sql = "INSERT INTO reservas_eventos (usuario_id, evento_id, fecha_reserva, confirmada) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, reserva.getUsuarioId());
            ps.setInt(2, reserva.getEventoId());
            ps.setTimestamp(3, new java.sql.Timestamp(reserva.getFechaReserva().getTime()));
            ps.setBoolean(4, reserva.isConfirmada());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Método para eliminar una reserva de evento
     * 
     * @param idReserva : entero
     * @return : boolean
     * @throws ClassNotFoundException
     */

    public boolean eliminarReservaEvento(int idReserva) throws ClassNotFoundException {
        String sql = "DELETE FROM reservas_eventos WHERE id = ?";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método para confirmar una reserva de evento
     * 
     * @param idReserva : entero
     * @return : boolean
     * @throws ClassNotFoundException
     */

    public boolean confirmarReservaEvento(int idReserva) throws ClassNotFoundException {
        String sql = "UPDATE reservas_eventos SET confirmada = TRUE WHERE id = ?";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}