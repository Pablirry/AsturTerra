package views;

import javax.swing.*;


import services.TurismoService;
import dao.UsuarioDAO;
import model.Usuario;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

public class Login extends JFrame {

    private JTextField txtCorreo;
    private JPasswordField txtContraseña;
    private JButton btnLogin;
    private JButton btnRegistro;
    private JPanel panelFondo;
    private JLabel lblLogo;
    private UsuarioDAO usuarioDAO;
    public static Usuario usuarioActual;

    public Login() {
        setTitle("Inicio de Sesión");
        setMinimumSize(new Dimension(400, 500));
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        usuarioDAO = new UsuarioDAO();

        panelFondo = new JPanel();
        panelFondo.setBackground(new Color(44, 62, 80));
        panelFondo.setLayout(new BorderLayout());

        // Panel central con BoxLayout vertical
        JPanel panelCentral = new JPanel();
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        // Logo responsive
        lblLogo = new JLabel();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        actualizarLogo();

        // Campos y botones
        panelCentral.add(Box.createVerticalStrut(30));
        panelCentral.add(lblLogo);
        panelCentral.add(Box.createVerticalStrut(30));

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setForeground(Color.WHITE);
        lblCorreo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(lblCorreo);

        txtCorreo = new JTextField();
        txtCorreo.setMaximumSize(new Dimension(300, 32));
        txtCorreo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(txtCorreo);

        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setForeground(Color.WHITE);
        lblContraseña.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(lblContraseña);

        txtContraseña = new JPasswordField();
        txtContraseña.setMaximumSize(new Dimension(300, 32));
        txtContraseña.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(txtContraseña);

        panelCentral.add(Box.createVerticalStrut(20));

        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(btnLogin);

        panelCentral.add(Box.createVerticalStrut(10));

        btnRegistro = new JButton("Registrarse");
        btnRegistro.setBackground(new Color(46, 204, 113));
        btnRegistro.setForeground(Color.WHITE);
        btnRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(btnRegistro);

        panelFondo.add(panelCentral, BorderLayout.CENTER);
        add(panelFondo);

        btnLogin.addActionListener(e -> iniciarSesion());
        btnRegistro.addActionListener(e -> {
            new Registro().setVisible(true);
            dispose();
        });

        // Responsive: actualizar logo al cambiar tamaño
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                actualizarLogo();
            }
        });

        setVisible(true);
    }

    private void actualizarLogo() {
        int ancho = Math.max(100, getWidth() / 3);
        int alto = ancho;
        try {
            BufferedImage img = javax.imageio.ImageIO.read(new java.io.File("assets/LogoAsturTerra.png"));
            Image scaled = img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(scaled));
        } catch (Exception ex) {
            lblLogo.setText("ASTURTERRA");
            lblLogo.setFont(new Font("Arial", Font.BOLD, 28));
            lblLogo.setForeground(Color.WHITE);
        }
    }

    private void iniciarSesion() {
        try {
            String correo = txtCorreo.getText().trim();
            String contrasena = new String(txtContraseña.getPassword()).trim();
    
            System.out.println("Correo: " + correo + ", Contraseña: " + contrasena);
    
            if (correo.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Introduce correo y contraseña.");
                return;
            }
    
            Usuario usuario = usuarioDAO.iniciarSesion(correo, contrasena);
            if (usuario != null) {
                TurismoService.getInstance().registrarActividad(usuario.getId(), "Inicio de sesion");
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso");
                new MenuPrincipal(usuario).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al iniciar sesión: " + ex.getMessage());
        }
    }
}