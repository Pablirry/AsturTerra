package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.ConexionDB;
import modelo.Usuario;

public class UsuarioDAO {

    public Usuario iniciarSesion(String correo, String contrasena) {
        try (Connection con = ConexionDB.getConection()) {
            String sql = "SELECT * FROM usuario WHERE correo = ? AND contrasena = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            ps.setString(2, contrasena);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario(rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getString("tipo"),
                        rs.getBytes("imagenPerfil"));
                return usuario;
            }

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al iniciar sesión");
        }
        return null;
    }

    public boolean registrarUsuario(Usuario usuario) {
        try (Connection con = ConexionDB.getConection()) {
            String sql = "INSERT INTO usuario (nombre, correo, contrasena, tipo, imagenPerfil) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getContrasena());
            ps.setString(4, usuario.getTipo());
            ps.setBytes(5, usuario.getImagenPerfil());

            return ps.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al registrar usuario");
            return false;
        }
    }

}