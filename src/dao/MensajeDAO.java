package dao;

import config.ConexionDB;
import model.Mensaje;
import utils.UIUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pablo
 */


public class MensajeDAO {

    /**
     * Envia un mensaje a un usuario.
     * @param idUsuario : entero
     * @param mensaje : Mensaje
     * @return : boolean
     * @throws ClassNotFoundException
     */

    public boolean enviarMensaje(int idUsuario, Mensaje mensaje) throws ClassNotFoundException {
        String sql = "INSERT INTO mensajes (id_usuario, mensaje, respuesta, fecha) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, mensaje.getMensaje());
            ps.setString(3, mensaje.getRespuesta());
            ps.setTimestamp(4, new Timestamp(mensaje.getFecha().getTime()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            UIUtils.mostrarError(null, "Error al enviar mensaje: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene los mensajes de un usuario.
     * @param idUsuario : entero
     * @return : List<Mensaje>
     * @throws ClassNotFoundException
     */

    public List<Mensaje> obtenerMensajesUsuario(int idUsuario) throws ClassNotFoundException {
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
                        rs.getString("usuarioNombre"),
                        rs.getString("mensaje"),
                        rs.getString("respuesta"),
                        rs.getTimestamp("fecha")));
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerMensajesUsuario: " + e.getMessage());
        }
        return mensajes;
    }

    /**
     * Obtiene la conversación entre dos usuarios.
     * @param idEmisor : entero
     * @param idReceptor : entero
     * @return : List<Mensaje>
     * @throws ClassNotFoundException
     */

    public List<Mensaje> obtenerConversacion(int idEmisor, int idReceptor) throws ClassNotFoundException {
        List<Mensaje> conversacion = new ArrayList<>();
        String sql = "SELECT * FROM mensajes WHERE id_usuario IN (?, ?) ORDER BY fecha ASC";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEmisor);
            ps.setInt(2, idReceptor);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                conversacion.add(new Mensaje(
                        rs.getInt("id"),
                        rs.getInt("id_usuario"),
                        rs.getString("usuarioNombre"),
                        rs.getString("mensaje"),
                        rs.getString("respuesta"),
                        rs.getTimestamp("fecha")));
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerConversacion: " + e.getMessage());
        }
        return conversacion;
    }

    /**
     * Marca un mensaje como leído.
     * @param idMensaje : entero
     * @return : boolean
     * @throws ClassNotFoundException
     */

    public boolean marcarMensajeComoLeido(int idMensaje) throws ClassNotFoundException {
        String sql = "UPDATE mensajes SET respuesta = 'LEÍDO' WHERE id = ?";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMensaje);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error marcarMensajeComoLeido: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene los mensajes sin respuesta.
     * @return : list<Mensaje>
     * @throws ClassNotFoundException
     */

    public List<Mensaje> obtenerMensajesSinRespuesta() throws ClassNotFoundException {
        List<Mensaje> mensajes = new ArrayList<>();
        String sql = "SELECT * FROM mensajes WHERE respuesta IS NULL OR respuesta = '' ORDER BY fecha ASC";
        try (Connection con = ConexionDB.getConection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                mensajes.add(new Mensaje(
                        rs.getInt("id"),
                        rs.getInt("id_usuario"),
                        rs.getString("usuarioNombre"),
                        rs.getString("mensaje"),
                        rs.getString("respuesta"),
                        rs.getTimestamp("fecha")));
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerMensajesSinRespuesta: " + e.getMessage());
        }
        return mensajes;
    }

    /**
     * Responde a un mensaje.
     * @param idMensaje : entero
     * @param respuesta : String
     * @return : boolean
     * @throws ClassNotFoundException
     */

    public boolean responderMensaje(int idMensaje, String respuesta) throws ClassNotFoundException {
        String sql = "UPDATE mensajes SET respuesta = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, respuesta);
            ps.setInt(2, idMensaje);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error responderMensaje: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los mensajes.
     * @return : list<Mensaje>
     * @throws ClassNotFoundException
     */

    public List<Mensaje> obtenerTodosMensajes() throws ClassNotFoundException {
        List<Mensaje> mensajes = new ArrayList<>();
        String sql = "SELECT * FROM mensajes ORDER BY fecha ASC";
        try (Connection con = ConexionDB.getConection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                mensajes.add(new Mensaje(
                        rs.getInt("id"),
                        rs.getInt("id_usuario"),
                        rs.getString("usuarioNombre"),
                        rs.getString("mensaje"),
                        rs.getString("respuesta"),
                        rs.getTimestamp("fecha")));
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerTodosMensajes: " + e.getMessage());
        }
        return mensajes;
    }

    /**
     * Obtiene los mensajes de un usuario específico.
     * @param idUsuario : entero
     * @return : List<Mensaje>
     * @throws ClassNotFoundException
     */

    public List<Mensaje> obtenerMensajesPorUsuario(Integer idUsuario) {
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
                        rs.getString("usuarioNombre"),
                        rs.getString("mensaje"),
                        rs.getString("respuesta"),
                        rs.getTimestamp("fecha")));
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerMensajesPorUsuario: " + e.getMessage());
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        return mensajes;
    }
}
