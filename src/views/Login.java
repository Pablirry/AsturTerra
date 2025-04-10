package views;

import javax.swing.*;

import dao.UsuarioDAO;
import model.Usuario;

import java.awt.*;

public class Login extends JFrame {

    private JTextField txtCorreo;
    private JPasswordField txtContraseña;
    private JButton btnLogin;
    private JButton btnRegistro;
    private JPanel panelFondo;
    private UsuarioDAO usuarioDAO;

    public Login() {
        setTitle("Inicio de Sesión");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        usuarioDAO = new UsuarioDAO();

        panelFondo = new JPanel();
        panelFondo.setBackground(new Color(44, 62, 80));
        panelFondo.setBounds(0, 0, 400, 500);
        panelFondo.setLayout(null);

        JLabel lblTitulo = new JLabel("ASTURTERRA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(100, 50, 200, 40);
        panelFondo.add(lblTitulo);

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setForeground(Color.WHITE);
        lblCorreo.setBounds(50, 120, 100, 25);
        panelFondo.add(lblCorreo);

        txtCorreo = new JTextField();
        txtCorreo.setBounds(50, 150, 300, 30);
        panelFondo.add(txtCorreo);

        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setForeground(Color.WHITE);
        lblContraseña.setBounds(50, 200, 100, 25);
        panelFondo.add(lblContraseña);

        txtContraseña = new JPasswordField();
        txtContraseña.setBounds(50, 230, 300, 30);
        panelFondo.add(txtContraseña);

        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBounds(50, 300, 300, 40);
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(Color.WHITE);
        panelFondo.add(btnLogin);

        btnRegistro = new JButton("Registrarse");
        btnRegistro.setBounds(50, 360, 300, 40);
        btnRegistro.setBackground(new Color(46, 204, 113));
        btnRegistro.setForeground(Color.WHITE);
        panelFondo.add(btnRegistro);

        btnLogin.addActionListener(e -> iniciarSesion());
        btnRegistro.addActionListener(e -> {
            new Registro().setVisible(true);
            dispose();
        });

        add(panelFondo);
        setVisible(true);
    }

    private void iniciarSesion() {
        try {
            String correo = txtCorreo.getText();
            String contrasena = new String(txtContraseña.getPassword());

            Usuario usuario = usuarioDAO.iniciarSesion(correo, contrasena);
            if (usuario != null) {
                TurismoService.getInstance().registrarActividad(usuario.getId(), "Inicio de sesion")
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso");
                MenuPrincipal.getInstance(usuario).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al iniciar sesión: " + ex.getMessage());
        }
    }
}