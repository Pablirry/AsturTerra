package views;

import model.Usuario;
import dao.UsuarioDAO;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.awt.image.BufferedImage;
import java.nio.file.Files;

public class VentanaPerfil extends JDialog {
    private Usuario usuario;
    private JTextField txtNombre, txtCorreo;
    private JPasswordField txtContrasena;
    private JLabel lblImagen;
    private JButton btnCambiarImagen, btnGuardar, btnEditar;
    private File nuevaImagen = null;

    public VentanaPerfil(JFrame parent, Usuario usuario) {
        super(parent, "Perfil de Usuario", true);
        this.usuario = usuario;
        setLayout(null);
        setSize(400, 350);
        setLocationRelativeTo(parent);

        JLabel lbl1 = new JLabel("Nombre:");
        lbl1.setBounds(30, 30, 80, 25);
        add(lbl1);
        txtNombre = new JTextField(usuario.getNombre());
        txtNombre.setBounds(120, 30, 220, 25);
        add(txtNombre);

        JLabel lbl2 = new JLabel("Correo:");
        lbl2.setBounds(30, 70, 80, 25);
        add(lbl2);
        txtCorreo = new JTextField(usuario.getCorreo());
        txtCorreo.setBounds(120, 70, 220, 25);
        add(txtCorreo);

        JLabel lbl3 = new JLabel("Contraseña:");
        lbl3.setBounds(30, 110, 80, 25);
        add(lbl3);
        txtContrasena = new JPasswordField(usuario.getContrasena());
        txtContrasena.setBounds(120, 110, 220, 25);
        add(txtContrasena);

        lblImagen = new JLabel();
        lblImagen.setBounds(150, 150, 100, 100);
        setImagenPerfil(false);
        add(lblImagen);

        btnCambiarImagen = new JButton("Cambiar Imagen");
        btnCambiarImagen.setBounds(120, 260, 150, 25);
        btnCambiarImagen.addActionListener(e -> seleccionarImagen());
        add(btnCambiarImagen);

        btnEditar = new JButton("Editar");
        btnEditar.setBounds(80, 300, 100, 30);
        btnEditar.addActionListener(e -> setEditable(true));
        add(btnEditar);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(150, 295, 100, 30);
        btnGuardar.addActionListener(e -> guardarCambios());
        add(btnGuardar);
    }

    private void setEditable(boolean editable) {
        txtNombre.setEnabled(editable);
        txtCorreo.setEnabled(editable);
        txtContrasena.setEnabled(editable);
        btnCambiarImagen.setEnabled(editable);
        btnGuardar.setEnabled(editable);
        btnEditar.setEnabled(editable);
    }

    private void setImagenPerfil(boolean editable) {
        Image img;
        try {
            byte[] imgBytes = usuario.getImagenPerfil();
            if (nuevaImagen != null) {
                imgBytes = Files.readAllBytes(nuevaImagen.toPath());
            }
            if (imgBytes != null) {
                img = new ImageIcon(imgBytes).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            } else {
                img = new ImageIcon("assets/LogoAsturTerra.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            }
        } catch (Exception ex) {
            img = new ImageIcon("assets/LogoAsturTerra.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        }
        // Circular con borde
        int size = 100;
        BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
        g2.drawImage(img, 0, 0, 100, 100, null);
        // Borde redondeado
        g2.setClip(null);
        g2.setStroke(new BasicStroke(4));
        g2.setColor(editable ? new Color(52, 152, 219) : Color.GRAY);
        g2.drawOval(2, 2, size - 4, size - 4);
        g2.dispose();
        lblImagen.setIcon(new ImageIcon(circleBuffer));
    }

    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            nuevaImagen = fileChooser.getSelectedFile();
            setImagenPerfil(true);
        }
    }

    private void guardarCambios() {
        try {
            usuario.setNombre(txtNombre.getText().trim());
            usuario.setCorreo(txtCorreo.getText().trim());
            usuario.setContrasena(new String(txtContrasena.getPassword()).trim());
            if (nuevaImagen != null) {
                usuario.setImagenPerfil(Files.readAllBytes(nuevaImagen.toPath()));
            }
            new UsuarioDAO().actualizarUsuario(usuario);
            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }
}