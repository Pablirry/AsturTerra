package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import config.ConexionDB;
import model.ValoracionRuta;
import model.ValoracionRestaurante;

public class ValoracionDAO {

    public boolean registrarValoracion(ValoracionRestaurante valoracion) {
        try (Connection con = ConexionDB.getConection()) {
            String sql = "INSERT INTO valoraciones (id_usuario, id_restaurante, puntuacion, comentario) VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, valoracion.getIdUsuario());
            ps.setInt(2, valoracion.getIdRestaurante());
            ps.setInt(3, valoracion.getPuntuacion());
            ps.setString(4, valoracion.getComentario());

            return ps.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al registrar valoración: " + e.getMessage());
            return false;
        }
    }

    public boolean valorarRuta(int idUsuario, int idRuta, int puntuacion, String comentario) {
        try (Connection con = ConexionDB.getConection()) {
            String sql = "INSERT INTO valoraciones (id_usuario, id_ruta, puntuacion, comentario) VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idRuta);
            ps.setInt(3, puntuacion);
            ps.setString(4, comentario);

            return ps.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al valorar ruta: " + e.getMessage());
            return false;
        }
    }

    public boolean registrarValoracionRuta(ValoracionRuta valoracion) {
        String sql = "INSERT INTO valoraciones_rutas (id_usuario, id_ruta, puntuacion, comentario) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, valoracion.getIdUsuario());
            ps.setInt(2, valoracion.getIdRuta());
            ps.setInt(3, valoracion.getPuntuacion());
            ps.setString(4, valoracion.getComentario());

            return ps.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error en registrarValoracionRuta: " + e.getMessage());
            return false;
        }
    }

    public boolean registrarValoracionRestaurante(ValoracionRestaurante valoracion) {
        String sql = "INSERT INTO valoraciones_restaurantes (id_usuario, id_restaurante, puntuacion, comentario) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, valoracion.getIdUsuario());
            ps.setInt(2, valoracion.getIdRestaurante());
            ps.setInt(3, valoracion.getPuntuacion());
            ps.setString(4, valoracion.getComentario());

            return ps.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error en registrarValoracionRestaurante: " + e.getMessage());
            return false;
        }
    }
}
