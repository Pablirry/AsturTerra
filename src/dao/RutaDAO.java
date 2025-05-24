package dao;

import java.sql.*;
import java.util.*;
import config.ConexionDB;
import model.Ruta;

/**
 * @author Pablo
 */


public class RutaDAO {

    /**
     * Método para listar todas las rutas
     * @return : list<Ruta>
     * @throws ClassNotFoundException
     */

    public List<Ruta> listarRutas() throws ClassNotFoundException {
        List<Ruta> rutas = new ArrayList<>();
        String sql = "SELECT * FROM rutas";
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
                    rs.getInt("dificultad")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rutas;
    }

    /**
     * Método para agregar una ruta
     * @param ruta : Ruta
     * @return : boolean
     * @throws ClassNotFoundException
     */

    public boolean agregarRuta(Ruta ruta) throws ClassNotFoundException {
        String sql = "INSERT INTO rutas (nombre, descripcion, imagen, precio, dificultad) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ruta.getNombre());
            ps.setString(2, ruta.getDescripcion());
            ps.setBytes(3, ruta.getImagen());
            ps.setDouble(4, ruta.getPrecio());
            ps.setInt(5, ruta.getDificultad());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método para eliminar una ruta
     * @param idRuta : entero
     * @return : boolean
     * @throws ClassNotFoundException
     */

    public boolean eliminarRuta(int idRuta) throws ClassNotFoundException {
        String sql = "DELETE FROM rutas WHERE id = ?";
        try (Connection con = ConexionDB.getConection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRuta);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método para obtener una ruta por su id
     * @param idRuta : entero
     * @return : Ruta
     * @throws ClassNotFoundException
     */

    public Ruta obtenerRutaPorId(int idRuta) throws ClassNotFoundException {
        String sql = "SELECT * FROM rutas WHERE id = ?";
        try (Connection con = ConexionDB.getConection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRuta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Ruta(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getBytes("imagen"),
                        rs.getDouble("precio"),
                        rs.getInt("dificultad")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Método para actualizar una ruta
     * @param ruta : Ruta
     * @return : boolean
     * @throws ClassNotFoundException
     */

    public boolean actualizarRuta(Ruta ruta) throws ClassNotFoundException {
        String sql = "UPDATE rutas SET nombre=?, descripcion=?, imagen=?, precio=?, dificultad=? WHERE id=?";
        try (Connection con = ConexionDB.getConection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ruta.getNombre());
            ps.setString(2, ruta.getDescripcion());
            ps.setBytes(3, ruta.getImagen());
            ps.setDouble(4, ruta.getPrecio());
            ps.setInt(5, ruta.getDificultad());
            ps.setInt(6, ruta.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}