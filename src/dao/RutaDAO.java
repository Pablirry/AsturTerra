package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.ConexionDB;
import model.Ruta;

public class RutaDAO {

    public List<Ruta> listarRutas() throws ClassNotFoundException {
        List<Ruta> rutas = new ArrayList<>();
        String sql = "SELECT * FROM rutas ORDER BY nombre";

        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rutas.add(new Ruta(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getBytes("imagen"),
                        rs.getDouble("precio"),
                        rs.getString("dificultad")));
            }
        } catch (SQLException e) {
            System.err.println("Error listarRutas: " + e.getMessage());
        }
        return rutas;
    }

    public boolean agregarRuta(Ruta ruta) throws ClassNotFoundException {
        String sql = "INSERT INTO rutas (nombre, descripcion, imagen, precio, dificultad) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ruta.getNombre());
            ps.setString(2, ruta.getDescripcion());
            ps.setBytes(3, ruta.getImagen());
            ps.setDouble(4, ruta.getPrecio());
            ps.setString(5, ruta.getDificultad());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error agregarRuta: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarRuta(int idRuta) throws ClassNotFoundException {
        try (Connection con = ConexionDB.getConection()) {
            // Eliminar primero valoraciones asociadas
            String deleteValoraciones = "DELETE FROM valoraciones_rutas WHERE id_ruta = ?";
            try (PreparedStatement ps1 = con.prepareStatement(deleteValoraciones)) {
                ps1.setInt(1, idRuta);
                ps1.executeUpdate();
            }

            // Luego eliminar la ruta
            String deleteRuta = "DELETE FROM rutas WHERE id = ?";
            try (PreparedStatement ps2 = con.prepareStatement(deleteRuta)) {
                ps2.setInt(1, idRuta);
                return ps2.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error en eliminarRuta: " + e.getMessage());
            return false;
        }
    }

    public Ruta obtenerRutaPorId(int idRuta) throws ClassNotFoundException {
        String sql = "SELECT * FROM rutas WHERE id = ?";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idRuta);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Ruta(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getBytes("imagen"),
                        rs.getDouble("precio"),
                        rs.getString("dificultad"));
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerRutaPorId: " + e.getMessage());
        }
        return null;
    }

    public Ruta obtenerRutaPorNombre(String nombre) throws ClassNotFoundException {
        String sql = "SELECT * FROM rutas WHERE nombre = ?";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Ruta(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getBytes("imagen"),
                        rs.getDouble("precio"),
                        rs.getString("dificultad"));
            }

        } catch (SQLException e) {
            System.err.println("Error en obtenerRutaPorNombre: " + e.getMessage());
        }
        return null;
    }

}