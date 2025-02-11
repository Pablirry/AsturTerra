package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.ConexionDB;
import model.Usuario;

public class UsuarioDAO {

    public Usuario iniciarSesion(String correo, String contrasena) {
        try (Connection con = ConexionDB.getConection()) {
            String sql = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?";

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
                        rs.getBytes("imagen_perfil"));
                return usuario;
            }

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al iniciar sesión: " + e.getMessage());
        }
        return null;
    }

    public boolean registrarUsuario(Usuario usuario) {
        if (usuario.getNombre().isEmpty() || usuario.getCorreo().isEmpty() || usuario.getContrasena().isEmpty()) {
            return false;
        }

        try (Connection con = ConexionDB.getConection()) {
            String sql = "INSERT INTO usuarios (nombre, correo, contrasena, tipo, imagen_perfil) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getContrasena());
            ps.setString(4, usuario.getTipo());
            ps.setBytes(5, usuario.getImagenPerfil());

            return ps.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }
}