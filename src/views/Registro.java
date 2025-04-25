package views;

import javax.swing.*;

import dao.UsuarioDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import model.Usuario;
import utils.PasswordUtils;
import utils.UIUtils;

public class Registro extends JFrame {

    private JTextField txtNombre, txtCorreo;
    private JPasswordField txtContraseña;
    private JButton btnRegistrar, btnSeleccionarImagen;
    private JCheckBox chkAdmin;
    private File imagenPerfil;
    private JPasswordField txtAdminPass;

    private UsuarioDAO usuarioDAO;

    public Registro() {
        usuarioDAO = new UsuarioDAO();

        setTitle("Registro de Usuario");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);

        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(null);
        panelCampos.setBounds(50, 100, 400, 350);
        panelCampos.setBackground(new Color(255, 255, 255, 180));

        JLabel lblTitulo = new JLabel("Crea tu cuenta");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBounds(50, 10, 200, 30);
        panelCampos.add(lblTitulo);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(50, 60, 100, 25);
        panelCampos.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(150, 60, 200, 25);
        panelCampos.add(txtNombre);

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setBounds(50, 100, 100, 25);
        panelCampos.add(lblCorreo);

        txtCorreo = new JTextField();
        txtCorreo.setBounds(150, 100, 200, 25);
        panelCampos.add(txtCorreo);

        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setBounds(50, 140, 100, 25);
        panelCampos.add(lblContraseña);

        txtContraseña = new JPasswordField();
        txtContraseña.setBounds(150, 140, 200, 25);
        panelCampos.add(txtContraseña);

        JLabel lblTipo = new JLabel("Administrador:");
        lblTipo.setBounds(50, 180, 100, 25);
        panelCampos.add(lblTipo);

        chkAdmin = new JCheckBox();
        chkAdmin.setBounds(150, 180, 20, 25);
        panelCampos.add(chkAdmin);

        JLabel lblAdminPass = new JLabel("Clave:");
        lblAdminPass.setBounds(180, 180, 120, 25);
        panelCampos.add(lblAdminPass);

        txtAdminPass = new JPasswordField();
        txtAdminPass.setBounds(230, 180, 120, 25);
        panelCampos.add(txtAdminPass);

        lblAdminPass.setVisible(false);
        txtAdminPass.setVisible(false);

        chkAdmin.addActionListener(e -> {
            boolean select = chkAdmin.isSelected();
            lblAdminPass.setVisible(select);
            txtAdminPass.setVisible(select);
        });

        btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.setBounds(50, 220, 300, 40);
        btnSeleccionarImagen.setBackground(new Color(52, 152, 219));
        btnSeleccionarImagen.setForeground(Color.WHITE);
        btnSeleccionarImagen.addActionListener(this::seleccionarImagen);
        panelCampos.add(btnSeleccionarImagen);

        btnRegistrar = new JButton("Registrarse");
        btnRegistrar.setBounds(50, 280, 300, 40);
        btnRegistrar.setBackground(new Color(46, 204, 113));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.addActionListener(this::registrarUsuario);
        panelCampos.add(btnRegistrar);

        getContentPane().add(panelCampos);
    }

    private void seleccionarImagen(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            imagenPerfil = fileChooser.getSelectedFile();
        }
    }

    private void registrarUsuario(ActionEvent e) {
        try {
            String nombre = txtNombre.getText().trim();
            String correo = txtCorreo.getText().trim();
            String contrasena = new String(txtContraseña.getPassword()).trim();
            String tipo = chkAdmin.isSelected() ? "admin" : "cliente";
            byte[] imagenBytes = null;

            if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!UIUtils.validarEmail(correo)) {
                JOptionPane.showMessageDialog(this, "Correo electrónico no válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!UIUtils.validarPasswordSegura(contrasena)) {
                JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos:\n-" +
                        "8 caracteres\n- 1 mayúscula\n- 1 minúscula\n- 2 números\n-" +
                        "1 carácter especial(@#$%^&*()_+\\-={}:;\"'|<>,.?/~)",
                        "Contraseña insegura", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (chkAdmin.isSelected()) {
                String adminPass = new String(txtAdminPass.getPassword()).trim();
                if (!adminPass.equals("admin")) {
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
