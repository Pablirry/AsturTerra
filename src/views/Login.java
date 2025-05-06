package views;

import javax.swing.*;
import services.TurismoService;
import dao.UsuarioDAO;
import model.Usuario;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.prefs.Preferences;

public class Login extends JFrame {

    private JTextField txtCorreo;
    private JPasswordField txtContraseña;
    private JButton btnLogin;
    private JButton btnRegistro;
    private JPanel panelFondo;
    private JLabel lblLogo;
    private UsuarioDAO usuarioDAO;
    public static Usuario usuarioActual;
    private JCheckBox chkMostrarContrasena;
    private JCheckBox chkRecordar;

    public Login() {
        setTitle("Inicio de Sesión");
        setMinimumSize(new Dimension(320, 480));
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        usuarioDAO = new UsuarioDAO();

        panelFondo = new JPanel();
        panelFondo.setBackground(new Color(44, 62, 80));
        panelFondo.setLayout(new BorderLayout());

        JPanel panelCentral = new JPanel();
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        int margenLateral = 24;
        JPanel panelMargen = new JPanel(new BorderLayout());
        panelMargen.setOpaque(false);
        panelMargen.setBorder(BorderFactory.createEmptyBorder(0, margenLateral, 0, margenLateral));
        panelMargen.add(panelCentral, BorderLayout.CENTER);

        panelFondo.add(panelMargen, BorderLayout.CENTER);
        add(panelFondo);

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

        Color placeHolderColor = new Color(150, 150, 150, 120);

        txtCorreo = new JTextField("Correo electrónico");
        txtCorreo.setForeground(placeHolderColor);
        txtCorreo.setMaximumSize(new Dimension(600, 40));
        txtCorreo.setPreferredSize(new Dimension(300, 32));
        txtCorreo.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtCorreo.setFont(new Font("Arial", Font.PLAIN, 18));
        txtCorreo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtCorreo.getText().equals("Correo electrónico")) {
                    txtCorreo.setText("");
                    txtCorreo.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtCorreo.getText().isEmpty()) {
                    txtCorreo.setText("Correo electrónico");
                    txtCorreo.setForeground(placeHolderColor);
                }
            }
        });
        panelCentral.add(txtCorreo);

        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setForeground(Color.WHITE);
        lblContraseña.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(lblContraseña);

        txtContraseña = new JPasswordField("Contraseña");
        txtContraseña.setForeground(placeHolderColor);
        txtContraseña.setMaximumSize(new Dimension(600, 40));
        txtContraseña.setPreferredSize(new Dimension(300, 32));
        txtContraseña.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtContraseña.setFont(new Font("Arial", Font.PLAIN, 18));
        txtContraseña.setEchoChar((char) 0);
        txtContraseña.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String pwd = new String(txtContraseña.getPassword());
                if (pwd.equals("Contraseña")) {
                    txtContraseña.setText("");
                    txtContraseña.setForeground(Color.BLACK);
                    txtContraseña.setEchoChar('•');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String pwd = new String(txtContraseña.getPassword());
                if (pwd.isEmpty()) {
                    txtContraseña.setText("Contraseña");
                    txtContraseña.setForeground(placeHolderColor);
                    txtContraseña.setEchoChar((char) 0);
                }
            }
        });
        panelCentral.add(txtContraseña);

        // Mostrar/ocultar contraseña
        chkMostrarContrasena = new JCheckBox("Mostrar contraseña");
        chkMostrarContrasena.setOpaque(false);
        chkMostrarContrasena.setForeground(Color.WHITE);
        chkMostrarContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);
        chkMostrarContrasena.addActionListener(e -> {
            String pwd = new String(txtContraseña.getPassword());
            if (chkMostrarContrasena.isSelected()) {
                txtContraseña.setEchoChar((char) 0);
            } else {
                if (!pwd.equals("Contraseña")) {
                    txtContraseña.setEchoChar('•');
                }
            }
        });
        panelCentral.add(chkMostrarContrasena);

        // Recordar credenciales
        chkRecordar = new JCheckBox("Recordar credenciales");
        chkRecordar.setOpaque(false);
        chkRecordar.setForeground(Color.WHITE);
        chkRecordar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(chkRecordar);

        panelCentral.add(Box.createVerticalStrut(20));

        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 20));
        getRootPane().setDefaultButton(btnLogin);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(220, 48));
        panelCentral.add(btnLogin);

        panelCentral.add(Box.createVerticalStrut(10));

        btnRegistro = new JButton("Registrarse");
        btnRegistro.setBackground(new Color(46, 204, 113));
        btnRegistro.setForeground(Color.WHITE);
        btnRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistro.setFont(new Font("Arial", Font.BOLD, 20));
        btnRegistro.setFocusPainted(false);
        btnRegistro.setPreferredSize(new Dimension(220, 48));
        panelCentral.add(btnRegistro);

        panelFondo.add(panelCentral, BorderLayout.CENTER);
        add(panelFondo);

        btnLogin.addActionListener(e -> iniciarSesion());
        btnRegistro.addActionListener(e -> {
            new Registro().setVisible(true);
            dispose();
        });

        // Cargar credenciales si existen
        cargarCredenciales();

        // Responsive: actualizar logo y componentes al cambiar tamaño
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                actualizarLogo();
                ajustarComponentes();
            }
        });

        setVisible(true);
        ajustarComponentes();
    }

    private void actualizarLogo() {
        int ancho = Math.max(80, getWidth() / 3);
        int alto = ancho;
        try {
            BufferedImage img = javax.imageio.ImageIO.read(new java.io.File("assets/LogoAsturTerra.png"));
            Image scaled = img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(scaled));
            lblLogo.setText("");
        } catch (Exception ex) {
            lblLogo.setText("ASTURTERRA");
            lblLogo.setFont(new Font("Arial", Font.BOLD, 28));
            lblLogo.setForeground(Color.WHITE);
        }
    }

    private void ajustarComponentes() {
        int h = getHeight();
        int w = getWidth();
        int fontSize = Math.max(16, h / 30);
        int fieldHeight = Math.max(32, h / 18);
        int btnHeight = Math.max(40, h / 15);
        int btnWidth = Math.max(180, Math.min(400, w - 64)); // margen total 2*32

        txtCorreo.setFont(new Font("Arial", Font.PLAIN, fontSize));
        txtCorreo.setPreferredSize(new Dimension(btnWidth, fieldHeight));
        txtCorreo.setMaximumSize(new Dimension(btnWidth, fieldHeight));

        txtContraseña.setFont(new Font("Arial", Font.PLAIN, fontSize));
        txtContraseña.setPreferredSize(new Dimension(btnWidth, fieldHeight));
        txtContraseña.setMaximumSize(new Dimension(btnWidth, fieldHeight));

        btnLogin.setFont(new Font("Arial", Font.BOLD, fontSize + 2));
        btnLogin.setPreferredSize(new Dimension(btnWidth, btnHeight));

        btnRegistro.setFont(new Font("Arial", Font.BOLD, fontSize + 2));
        btnRegistro.setPreferredSize(new Dimension(btnWidth, btnHeight));
    }

    private void iniciarSesion() {
        try {
            String correo = txtCorreo.getText().trim();
            String contrasena = new String(txtContraseña.getPassword()).trim();

            if (correo.isEmpty() || contrasena.isEmpty() ||
                correo.equals("Correo electrónico") || contrasena.equals("Contraseña")) {
                JOptionPane.showMessageDialog(this, "Introduce correo y contraseña.");
                return;
            }

            Usuario usuario = usuarioDAO.iniciarSesion(correo, contrasena);
            if (usuario != null) {
                if (chkRecordar.isSelected()) {
                    guardarCredenciales(correo, contrasena);
                } else {
                    borrarCredenciales();
                }
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

    private void guardarCredenciales(String correo, String contrasena) {
        Preferences prefs = Preferences.userRoot().node("turismoapp");
        prefs.put("correo", correo);
        prefs.put("contrasena", contrasena);
    }

    private void cargarCredenciales() {
        Preferences prefs = Preferences.userRoot().node("turismoapp");
        String correo = prefs.get("correo", "");
        String contrasena = prefs.get("contrasena", "");
        if (!correo.isEmpty()) {
            txtCorreo.setText(correo);
            txtCorreo.setForeground(Color.BLACK);
        }
        if (!contrasena.isEmpty()) {
            txtContraseña.setText(contrasena);
            txtContraseña.setForeground(Color.BLACK);
            txtContraseña.setEchoChar('•');
        }
        chkRecordar.setSelected(!correo.isEmpty() && !contrasena.isEmpty());
    }

    private void borrarCredenciales() {
        Preferences prefs = Preferences.userRoot().node("turismoapp");
        prefs.remove("correo");
        prefs.remove("contrasena");
    }
}