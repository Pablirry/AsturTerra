package views;

import javax.swing.*;

import dao.UsuarioDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import model.Usuario;

public class Registro extends JFrame {

    private JTextField txtNombre, txtCorreo;
    private JPasswordField txtContraseña;
    private JButton btnRegistrar, btnSeleccionarImagen;
    private JCheckBox chkAdmin;
    private File imagenPerfil;

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
        chkAdmin.setBounds(150, 180, 200, 25);
        panelCampos.add(chkAdmin);

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

            if (imagenPerfil != null) {
                imagenBytes = Files.readAllBytes(Paths.get(imagenPerfil.getAbsolutePath()));
            }

            Usuario nuevoUsuario = new Usuario(0, nombre, correo, contrasena, tipo, imagenBytes);
            boolean registrado = usuarioDAO.registrarUsuario(nuevoUsuario);

            if (registrado) {
                JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente.");
                dispose();
                new Login().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar usuario. Verifique los datos.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
