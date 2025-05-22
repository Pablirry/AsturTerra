package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import config.ConexionDB;
import model.Evento;

public class EventoDAO {

    public List<Evento> listarEventos() {

        List<Evento> eventos = new ArrayList<>();

        String sql = "SELECT * FROM eventos";

        try (Connection con = ConexionDB.getConection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                eventos.add(new Evento(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("ubicacion"),
                        rs.getString("tipo"),
                        rs.getDouble("precio"),
                        rs.getBytes("imagen")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return eventos;
    }

    public boolean agregarEvento(Evento evento) {
        String sql = "INSERT INTO eventos (nombre, descripcion, ubicacion, tipo, precio, imagen) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, evento.getNombre());
            ps.setString(2, evento.getDescripcion());
            ps.setString(3, evento.getUbicacion());
            ps.setString(4, evento.getTipo());
            ps.setDouble(5, evento.getPrecio());
            ps.setBytes(6, evento.getImagen());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarEvento(int idEvento) {
        String sql = "DELETE FROM eventos WHERE id = ?";

        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEvento);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean actualizarEvento(Evento evento) {
        String sql = "UPDATE eventos SET nombre = ?, descripcion = ?, ubicacion = ?, tipo = ?, precio = ?, imagen = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, evento.getNombre());
            ps.setString(2, evento.getDescripcion());
            ps.setString(3, evento.getUbicacion());
            ps.setString(4, evento.getTipo());
            ps.setDouble(5, evento.getPrecio());
            ps.setBytes(6, evento.getImagen());
            ps.setInt(7, evento.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
