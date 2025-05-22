package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.ConexionDB;
import model.Usuario;
import utils.PasswordUtils;

public class UsuarioDAO {

    public Usuario iniciarSesion(String correo, String contrasena) throws ClassNotFoundException {
        try (Connection con = ConexionDB.getConection()) {
            String sql = "SELECT * FROM usuarios WHERE correo = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
    
            if (rs.next()) {
                String hashAlmacenado = rs.getString("contrasena");
                String hashIngresado = PasswordUtils.hash(contrasena);
            if (hashAlmacenado.equals(hashIngresado)){
                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getString("tipo"),
                        rs.getBytes("imagen_perfil")
                );
            } else {
                System.out.println("Contraseña incorrecta");
            }
            } else {
                System.out.println("Usuario no encontrado");
        }
        } catch (SQLException e) {
            System.err.println("Error iniciarSesion: " + e.getMessage());
        }
        return null;
    }

    public boolean registrarUsuario(Usuario usuario) throws ClassNotFoundException {
        try (Connection con = ConexionDB.getConection()) {
            String sql = "INSERT INTO usuarios (nombre, correo, contrasena, tipo, imagen_perfil) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getContrasena());
            ps.setString(4, usuario.getTipo());
            ps.setBytes(5, usuario.getImagenPerfil());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error registrarUsuario: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarUsuario(Usuario usuario) throws ClassNotFoundException {
        try (Connection con = ConexionDB.getConection()) {
            String sql = "UPDATE usuarios SET nombre = ?, correo = ?, contrasena = ?, tipo = ?, imagen_perfil = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getContrasena());
            ps.setString(4, usuario.getTipo());
            ps.setBytes(5, usuario.getImagenPerfil());
            ps.setInt(6, usuario.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizarUsuario: " + e.getMessage());
            return false;
        }
    }

    public Usuario obtenerUsuarioPorId(int id) throws ClassNotFoundException {
        try (Connection con = ConexionDB.getConection()) {
            String sql = "SELECT * FROM usuarios WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getString("tipo"),
                        rs.getBytes("imagen_perfil")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerUsuarioPorId: " + e.getMessage());
        }
        return null;
    }

    public Usuario obtenerUsuarioPorCorreo(String correo) throws ClassNotFoundException{
        try (Connection con = ConexionDB.getConection()) {
            String sql = "SELECT * FROM usuarios WHERE correo = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("contrasena"),
                    rs.getString("tipo"),
                    rs.getBytes("imagen_perfil")
                );
            }
        } catch (SQLException ex) {
            System.err.println("Error al obtener usuario por correo " + ex.getMessage());
        }
        return null;
    }

        public boolean eliminarUsuarioPorCorreo(String correo) throws Exception {
        String sql = "DELETE FROM usuarios WHERE correo = ?";
        try (Connection con = ConexionDB.getConection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            return ps.executeUpdate() > 0;
        }
    }
}