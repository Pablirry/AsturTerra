package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.ConexionDB;
import model.Restaurante;

/**
 * @author Pablo
 */


public class RestauranteDAO {

    /**
     * Método para listar todos los restaurantes
     * @return : list<Restaurante>
     * @throws ClassNotFoundException
     */

    public List<Restaurante> listarRestaurantes() throws ClassNotFoundException {
        List<Restaurante> restaurantes = new ArrayList<>();
        String sql = "SELECT * FROM restaurantes ORDER BY nombre";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                restaurantes.add(new Restaurante(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("ubicacion"),
                        rs.getBytes("imagen"),
                        rs.getString("especialidad"),
                        rs.getString("descripcion")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error listarRestaurantes: " + e.getMessage());
        }
        return restaurantes;
    }

    /**
     * Método para agregar un restaurante
     * @param restaurante : Restaurante
     * @return : boolean
     * @throws ClassNotFoundException
     */

    public boolean agregarRestaurante(Restaurante restaurante) throws ClassNotFoundException {
        String sql = "INSERT INTO restaurantes (nombre, ubicacion, especialidad, descripcion, imagen) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, restaurante.getNombre());
            ps.setString(2, restaurante.getUbicacion());
            ps.setString(3, restaurante.getEspecialidad());
            ps.setString(4, restaurante.getDescripcion());
            ps.setBytes(5, restaurante.getImagen());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error agregarRestaurante: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para eliminar un restaurante
     * @param idRestaurante : entero
     * @return : boolean
     * @throws ClassNotFoundException
     */

    public boolean eliminarRestaurante(int idRestaurante) throws ClassNotFoundException {
        String sql = "DELETE FROM restaurantes WHERE id = ?";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRestaurante);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminarRestaurante: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para obtener un restaurante por su id
     * @param id : entero
     * @return : Restaurante
     * @throws ClassNotFoundException
     */

    public Restaurante obtenerRestaurantePorId(int id) throws ClassNotFoundException {
        String sql = "SELECT * FROM restaurantes WHERE id = ?";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Restaurante(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("ubicacion"),
                        rs.getBytes("imagen"),
                        rs.getString("especialidad"),
                        rs.getString("descripcion")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerRestaurantePorId: " + e.getMessage());
        }
        return null;
    }

    /**
     * Método para obtener un restaurante por su nombre
     * @param nombre : String
     * @return : Restaurante
     * @throws ClassNotFoundException
     */

    public Restaurante obtenerRestaurantePorNombre(String nombre) throws ClassNotFoundException {
        Restaurante restaurante = null;
        String sql = "SELECT * FROM restaurantes WHERE nombre = ?";
        try (Connection con = ConexionDB.getConection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    restaurante = new Restaurante(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("ubicacion"),
                            rs.getBytes("imagen"),
                            rs.getString("especialidad"),
                            rs.getString("descripcion")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerRestaurantePorNombre: " + e.getMessage());
        }
        return restaurante;
    }

    /**
     * Método para actualizar un restaurante
     * @param restaurante : Restaurante
     * @return : boolean
     * @throws ClassNotFoundException
     */

    public boolean actualizarRestaurante(Restaurante restaurante) throws ClassNotFoundException {
        String sql = "UPDATE restaurantes SET nombre = ?, ubicacion = ?, especialidad = ?, descripcion = ?, imagen = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, restaurante.getNombre());
            ps.setString(2, restaurante.getUbicacion());
            ps.setString(3, restaurante.getEspecialidad());
            ps.setString(4, restaurante.getDescripcion());
            ps.setBytes(5, restaurante.getImagen());
            ps.setInt(6, restaurante.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizarRestaurante: " + e.getMessage());
            return false;
        }
    }
}
