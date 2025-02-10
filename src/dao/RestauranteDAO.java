package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.ConexionDB;
import modelo.Restaurante;

public class RestauranteDAO {

    public List<Restaurante> listarRestaurantes() throws ClassNotFoundException {
        List<Restaurante> restaurantes = new ArrayList<>();
        String sql = "SELECT * FROM restaurantes ORDER BY valoracion DESC";

        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                restaurantes.add(new Restaurante(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("ubicacion"),
                        rs.getFloat("valoracion"),
                        rs.getBytes("imagen")));
            }
        } catch (SQLException e) {
            System.err.println("Error en listarRestaurantes: " + e.getMessage());
        }
        return restaurantes;
    }

    public boolean agregarRestaurante(Restaurante restaurante) throws ClassNotFoundException {
        String sql = "INSERT INTO restaurantes (nombre, ubicacion, valoracion, imagen) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, restaurante.getNombre());
            ps.setString(2, restaurante.getUbicacion());
            ps.setFloat(3, restaurante.getValoracion());
            ps.setBytes(4, restaurante.getImagen());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en agregarRestaurante: " + e.getMessage());
            return false;
        }
    }

}
