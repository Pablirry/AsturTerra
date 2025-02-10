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

    public List<Ruta> listarRutas() throws ClassNotFoundException, SQLException {
        List<Ruta> rutas = new ArrayList<>();
        try (Connection con = ConexionDB.getConection()) {
            String sql = "SELECT * FROM rutas";

            try {
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    byte[] imagenBytes = rs.getBytes("imagen");
                    rutas.add(new Ruta(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            imagenBytes,
                            rs.getDouble("precio"),
                            rs.getString("dificultad")));
                }
            } catch (SQLException e) {
                System.out.println("Error al listar rutas");
            }

            return rutas;
        }

    }

    public void agregarRuta(Ruta ruta) throws ClassNotFoundException {
        Connection con = ConexionDB.getConection();
        String sql = "INSERT INTO rutas (nombre, descripcion, imagen, precio, dificultad) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, ruta.getNombre());
            ps.setString(2, ruta.getDescripcion());
            ps.setBytes(3, ruta.getImagen());
            ps.setDouble(4, ruta.getPrecio());
            ps.setString(5, ruta.getDificultad());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al agregar ruta");
        }
    }

    public void eliminarRuta(int id) throws ClassNotFoundException {
        Connection con = ConexionDB.getConection();
        String sql = "DELETE FROM rutas WHERE id = ?";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar ruta");
        }
    }

}
