package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.ConexionDB;
import model.ValoracionRuta;
import model.ValoracionRestaurante;

public class ValoracionDAO {

    public boolean registrarValoracionRuta(ValoracionRuta valoracion) throws ClassNotFoundException {
        String sql = "INSERT INTO valoraciones_rutas (id_usuario, id_ruta, puntuacion, comentario) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, valoracion.getIdUsuario());
            ps.setInt(2, valoracion.getIdRuta());
            ps.setInt(3, valoracion.getPuntuacion());
            ps.setString(4, valoracion.getComentario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error registrarValoracionRuta: " + e.getMessage());
            return false;
        }
    }

    public boolean registrarValoracionRestaurante(ValoracionRestaurante valoracion) throws ClassNotFoundException {
        String sql = "INSERT INTO valoraciones_restaurantes (id_usuario, id_restaurante, puntuacion, comentario) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, valoracion.getIdUsuario());
            ps.setInt(2, valoracion.getIdRestaurante());
            ps.setInt(3, valoracion.getPuntuacion());
            ps.setString(4, valoracion.getComentario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error registrarValoracionRestaurante: " + e.getMessage());
            return false;
        }
    }

    public List<ValoracionRuta> obtenerValoracionesRuta(int idRuta) throws ClassNotFoundException {
        List<ValoracionRuta> lista = new ArrayList<>();
        String sql = "SELECT * FROM valoraciones_rutas WHERE id_ruta = ?";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRuta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new ValoracionRuta(
                        rs.getInt("id"),
                        rs.getInt("id_usuario"),
                        rs.getInt("id_ruta"),
                        rs.getInt("puntuacion"),
                        rs.getString("comentario")));
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerValoracionesRuta: " + e.getMessage());
        }
        return lista;
    }

    public List<ValoracionRestaurante> obtenerValoracionesRestaurante(int idRestaurante) throws ClassNotFoundException {
        List<ValoracionRestaurante> lista = new ArrayList<>();
        String sql = "SELECT * FROM valoraciones_restaurantes WHERE id_restaurante = ?";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRestaurante);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new ValoracionRestaurante(
                        rs.getInt("id"),
                        rs.getInt("id_usuario"),
                        rs.getInt("id_restaurante"),
                        rs.getInt("puntuacion"),
                        rs.getString("comentario")));
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerValoracionesRestaurante: " + e.getMessage());
        }
        return lista;
    }

    public double obtenerValoracionMediaRuta(int idRuta) throws ClassNotFoundException {
        String sql = "SELECT AVG(puntuacion) AS media FROM valoraciones_rutas WHERE id_ruta = ?";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRuta);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("media");
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerValoracionMediaRuta: " + e.getMessage());
        }
        return 0.0;
    }

    public double obtenerValoracionMediaRestaurante(int idRestaurante) throws ClassNotFoundException {
        String sql = "SELECT AVG(puntuacion) AS media FROM valoraciones_restaurantes WHERE id_restaurante = ?";
        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRestaurante);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("media");
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerValoracionMediaRestaurante: " + e.getMessage());
        }
        return 0.0;
    }
}
