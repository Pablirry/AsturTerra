package views;

import javax.swing.*;
import dao.UsuarioDAO;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import model.Usuario;
import utils.PasswordUtils;
import utils.UIUtils;

public class Registro extends JFrame {

    private JTextField txtNombre, txtCorreo;
    private JPasswordField txtContraseña, txtAdminPass;
    private JButton btnRegistrar, btnSeleccionarImagen;
    private JCheckBox chkAdmin;
    private File imagenPerfil;
    private UsuarioDAO usuarioDAO;
    private JLabel lblImagenPreview;

    public Registro() {
        usuarioDAO = new UsuarioDAO();

        setTitle("Registro de Usuario");
        setMinimumSize(new Dimension(320, 540));
        setSize(420, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelFondo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image img = new ImageIcon("assets/asturias.jfif").getImage();
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception ex) {
                    g.setColor(new Color(52, 152, 219, 60));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panelFondo.setOpaque(true);

        // Logo en la esquina superior derecha
        JPanel panelLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        panelLogo.setOpaque(false);
        try {
            ImageIcon logoIcon = new ImageIcon("assets/LogoAsturTerra.png");
            Image logoImg = logoIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(logoImg));
            panelLogo.add(lblLogo);
        } catch (Exception ex) {
            // Si no hay logo, no pasa nada
        }
        panelFondo.add(panelLogo, BorderLayout.NORTH);

        // Panel central con BoxLayout para adaptabilidad
        JPanel panelCentral = new JPanel();
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        // Logo o imagen de usuario
        lblImagenPreview = new JLabel();
        lblImagenPreview.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblImagenPreview.setPreferredSize(new Dimension(100, 100));
        lblImagenPreview.setMaximumSize(new Dimension(120, 120));
        lblImagenPreview.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagenPreview.setVerticalAlignment(SwingConstants.CENTER);
        setImagenPreview(null);
        panelCentral.add(lblImagenPreview);
        panelCentral.add(Box.createVerticalStrut(10));

        JLabel lblTitulo = new JLabel("Crea tu cuenta");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(44, 62, 80));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(lblTitulo);
        panelCentral.add(Box.createVerticalStrut(18));

        txtNombre = new JTextField("Nombre completo");
        txtNombre.setForeground(new Color(150, 150, 150, 120));
        txtNombre.setMaximumSize(new Dimension(600, 38));
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 18));
        txtNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtNombre.addFocusListener(placeHolderListener(txtNombre, "Nombre completo"));
        panelCentral.add(txtNombre);
        panelCentral.add(Box.createVerticalStrut(10));

        txtCorreo = new JTextField("Correo electrónico");
        txtCorreo.setForeground(new Color(150, 150, 150, 120));
        txtCorreo.setMaximumSize(new Dimension(600, 38));
        txtCorreo.setFont(new Font("Arial", Font.PLAIN, 18));
        txtCorreo.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtCorreo.addFocusListener(placeHolderListener(txtCorreo, "Correo electrónico"));
        panelCentral.add(txtCorreo);
        panelCentral.add(Box.createVerticalStrut(10));

        txtContraseña = new JPasswordField("Contraseña");
        txtContraseña.setForeground(new Color(150, 150, 150, 120));
        txtContraseña.setMaximumSize(new Dimension(600, 38));
        txtContraseña.setFont(new Font("Arial", Font.PLAIN, 18));
        txtContraseña.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtContraseña.setEchoChar((char) 0);
        txtContraseña.addFocusListener(passwordPlaceHolderListener(txtContraseña, "Contraseña"));
        panelCentral.add(txtContraseña);
        panelCentral.add(Box.createVerticalStrut(10));

        JPanel panelAdmin = new JPanel();
        panelAdmin.setOpaque(false);
        panelAdmin.setLayout(new BoxLayout(panelAdmin, BoxLayout.X_AXIS));
        panelAdmin.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Espaciador para centrar
        panelAdmin.add(Box.createHorizontalGlue());

        // Cambia el color del texto del checkbox de administrador a oscuro
        chkAdmin = new JCheckBox("Administrador");
        chkAdmin.setOpaque(false);
        chkAdmin.setForeground(Color.WHITE);
        panelAdmin.add(chkAdmin);
        panelAdmin.add(Box.createHorizontalStrut(10));

        txtAdminPass = new JPasswordField("Clave admin");
        txtAdminPass.setForeground(new Color(150, 150, 150, 120));
        txtAdminPass.setPreferredSize(new Dimension(140, 32));
        txtAdminPass.setFont(new Font("Arial", Font.PLAIN, 16));
        txtAdminPass.setEchoChar((char) 0);
        txtAdminPass.addFocusListener(passwordPlaceHolderListener(txtAdminPass, "Clave admin"));
        txtAdminPass.setVisible(false);
        panelAdmin.add(txtAdminPass);

        // Espaciador para centrar
        panelAdmin.add(Box.createHorizontalGlue());

        panelCentral.add(panelAdmin);
        panelCentral.add(Box.createVerticalStrut(10));

        chkAdmin.addActionListener(e -> {
            txtAdminPass.setVisible(chkAdmin.isSelected());
            panelAdmin.revalidate();
            panelAdmin.repaint();
        });

        btnSeleccionarImagen = new JButton("Seleccionar Imagen de Perfil");
        btnSeleccionarImagen.setBackground(new Color(52, 152, 219));
        btnSeleccionarImagen.setForeground(Color.WHITE);
        btnSeleccionarImagen.setFont(new Font("Arial", Font.BOLD, 16));
        btnSeleccionarImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSeleccionarImagen.addActionListener(this::seleccionarImagen);
        panelCentral.add(btnSeleccionarImagen);
        panelCentral.add(Box.createVerticalStrut(16));

        btnRegistrar = new JButton("Registrarse");
        btnRegistrar.setBackground(new Color(46, 204, 113));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 18));
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        getRootPane().setDefaultButton(btnRegistrar);
        btnRegistrar.addActionListener(this::registrarUsuario);
        panelCentral.add(btnRegistrar);

        JPanel panelLogin = new JPanel();
        panelLogin.setOpaque(false);
        panelLogin.setLayout(new BoxLayout(panelLogin, BoxLayout.X_AXIS));
        panelLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblYaCuenta = new JLabel("¿Ya tienes cuenta? ");
        lblYaCuenta.setFont(new Font("Arial", Font.PLAIN, 14));
        lblYaCuenta.setForeground(Color.WHITE);
        JLabel lblLogin = new JLabel("<html><u>Inicia sesión</u></html>");
        lblLogin.setFont(new Font("Arial", Font.PLAIN, 14));
        lblLogin.setForeground(new Color(102, 204, 255));
        lblLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new Login().setVisible(true);
            }
        });

        panelLogin.add(lblYaCuenta);
        panelLogin.add(lblLogin);

        panelCentral.add(Box.createVerticalStrut(12));
        panelCentral.add(panelLogin);

        panelFondo.add(panelCentral, BorderLayout.CENTER);
        setContentPane(panelFondo);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarComponentes();
            }
        });
        ajustarComponentes();
    }

    private void setImagenPreview(File file) {
        try {
            Image img;
            if (file != null) {
                img = new ImageIcon(file.getAbsolutePath()).getImage();
            } else {
                img = new ImageIcon("assets/usuario.png").getImage();
            }
            Image scaled = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblImagenPreview.setIcon(new ImageIcon(scaled));
        } catch (Exception ex) {
            lblImagenPreview.setIcon(null);
        }
    }

    private FocusListener placeHolderListener(JTextField field, String placeholder) {
        Color placeholderColor = new Color(150, 150, 150, 120);
        return new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(placeholderColor);
                }
            }
        };
    }

    private FocusListener passwordPlaceHolderListener(JPasswordField field, String placeholder) {
        Color placeholderColor = new Color(150, 150, 150, 120);
        return new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String pwd = new String(field.getPassword());
                if (pwd.equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    field.setEchoChar('•');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String pwd = new String(field.getPassword());
                if (pwd.isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(placeholderColor);
                    field.setEchoChar((char) 0);
                }
            }
        };
    }

    private void seleccionarImagen(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            imagenPerfil = fileChooser.getSelectedFile();
            setImagenPreview(imagenPerfil);
        }
    }

    private void ajustarComponentes() {
        int h = getHeight();
        int w = getWidth();
        int fontSize = Math.max(16, h / 32);
        int fieldHeight = Math.max(32, h / 22);
        int btnHeight = Math.max(40, h / 16);
        int btnWidth = Math.max(180, Math.min(400, w - 64));

        txtNombre.setFont(new Font("Arial", Font.PLAIN, fontSize));
        txtNombre.setPreferredSize(new Dimension(btnWidth, fieldHeight));
        txtNombre.setMaximumSize(new Dimension(btnWidth, fieldHeight));

        txtCorreo.setFont(new Font("Arial", Font.PLAIN, fontSize));
        txtCorreo.setPreferredSize(new Dimension(btnWidth, fieldHeight));
        txtCorreo.setMaximumSize(new Dimension(btnWidth, fieldHeight));

        txtContraseña.setFont(new Font("Arial", Font.PLAIN, fontSize));
        txtContraseña.setPreferredSize(new Dimension(btnWidth, fieldHeight));
        txtContraseña.setMaximumSize(new Dimension(btnWidth, fieldHeight));

        txtAdminPass.setFont(new Font("Arial", Font.PLAIN, fontSize - 2));
        txtAdminPass.setPreferredSize(new Dimension(Math.max(120, btnWidth / 2), fieldHeight));
        txtAdminPass.setMaximumSize(new Dimension(Math.max(120, btnWidth / 2), fieldHeight));

        btnRegistrar.setFont(new Font("Arial", Font.BOLD, fontSize + 2));
        btnRegistrar.setPreferredSize(new Dimension(btnWidth, btnHeight));

        btnSeleccionarImagen.setFont(new Font("Arial", Font.BOLD, fontSize));
        btnSeleccionarImagen.setPreferredSize(new Dimension(btnWidth, btnHeight));
    }

    private void registrarUsuario(ActionEvent e) {
        try {
            String nombre = txtNombre.getText().trim();
            String correo = txtCorreo.getText().trim();
            String contrasena = new String(txtContraseña.getPassword()).trim();
            String tipo = chkAdmin.isSelected() ? "admin" : "cliente";
            byte[] imagenBytes = null;

            if (nombre.isEmpty() || nombre.equals("Nombre completo") ||
                    correo.isEmpty() || correo.equals("Correo electrónico") ||
                    contrasena.isEmpty() || contrasena.equals("Contraseña")) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!UIUtils.validarEmail(correo)) {
                UIUtils.mostrarError(this, "Correo electrónico no válido");
                return;
            }

            if (!UIUtils.validarPasswordSegura(contrasena)) {
                UIUtils.mostrarError(this, "La contraseña debe tener al menos:\n-" +
                        "8 caracteres\n- 1 mayúscula\n- 1 minúscula\n- 2 números\n-" +
                        "1 carácter especial(@#$%^&*()_+\\-={}:;\"'|<>,.?/~)");
                return;
            }

            if (chkAdmin.isSelected()) {
                String adminPass = new String(txtAdminPass.getPassword()).trim();
                if (adminPass.isEmpty() || adminPass.equals("Clave admin") || !adminPass.equals("admin")) {
                    JOptionPane.showMessageDialog(this, "Clave de administrador incorrecta.");
                    return;
                }
            }

            if (imagenPerfil != null) {
                imagenBytes = Files.readAllBytes(Paths.get(imagenPerfil.getAbsolutePath()));
            }

            // Evitar duplicados correo
            if (new dao.UsuarioDAO().obtenerUsuarioPorCorreo(correo) != null) {
                JOptionPane.showMessageDialog(this, "El correo ya está registrado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String hash = PasswordUtils.hash(contrasena);
            Usuario nuevoUsuario = new Usuario(0, nombre, correo, hash, tipo, imagenBytes);
            boolean registrado = usuarioDAO.registrarUsuario(nuevoUsuario);

            if (registrado) {
                UIUtils.mostrarInfo(this, "Usuario registrado exitosamente.");
                dispose();
                new Login().setVisible(true);
            } else {
                UIUtils.mostrarError(this, "Error al registrar usuario. Verifique los datos.");
            }

        } catch (Exception ex) {
            UIUtils.mostrarError(this, "Error: " + ex.getMessage());
        }
    }
}