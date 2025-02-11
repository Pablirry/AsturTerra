package controllers;

import dao.UsuarioDAO;
import dao.HistorialDAO;
import model.Usuario;
import views.Login;
import views.MenuPrincipal;
import views.Registro;

import javax.swing.*;

public class UsuarioController {
    private UsuarioDAO usuarioDAO;
    private HistorialDAO historialDAO;
    private Login loginVista;

    public UsuarioController(Login loginVista) {
        this.usuarioDAO = new UsuarioDAO();
        this.historialDAO = new HistorialDAO();
        this.loginVista = loginVista;
        agregarEventos();
    }

    private void agregarEventos() {
        loginVista.getBtnLogin().addActionListener(e -> iniciarSesion());
        loginVista.getBtnRegistro().addActionListener(e -> abrirRegistro());
    }

    public void iniciarSesion() {
        try {
            String correo = loginVista.getTxtCorreo().getText();
            String contraseña = new String(loginVista.getTxtContraseña().getPassword());

            if (correo.isEmpty() || contraseña.isEmpty()) {
                JOptionPane.showMessageDialog(loginVista, "Los campos no pueden estar vacíos.");
                return;
            }

            Usuario usuario = usuarioDAO.iniciarSesion(correo, contraseña);
            if (usuario != null) {
                historialDAO.registrarAccion(usuario.getId(), "Inicio de sesión");
                JOptionPane.showMessageDialog(loginVista, "Inicio de sesión exitoso.");
                loginVista.dispose();
                MenuPrincipal.getInstance().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(loginVista, "Credenciales incorrectas.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(loginVista, "Error en inicio de sesión: " + e.getMessage());
        }
    }

    private void abrirRegistro() {
        new Registro().setVisible(true);
    }

    public boolean registrarUsuario(Usuario usuario) {
        return usuarioDAO.registrarUsuario(usuario);
    }
}
