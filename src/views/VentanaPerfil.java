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
        getContentPane().setLayout(null);
        setSize(459, 392);
        setLocationRelativeTo(parent);

        JLabel lbl1 = new JLabel("Nombre:");
        lbl1.setBounds(30, 30, 80, 25);
        getContentPane().add(lbl1);
        txtNombre = new JTextField(usuario.getNombre());
        txtNombre.setBounds(120, 30, 220, 25);
        txtNombre.setEnabled(false);
        getContentPane().add(txtNombre);

        JLabel lbl2 = new JLabel("Correo:");
        lbl2.setBounds(30, 70, 80, 25);
        getContentPane().add(lbl2);
        txtCorreo = new JTextField(usuario.getCorreo());
        txtCorreo.setBounds(120, 70, 220, 25);
        txtCorreo.setEnabled(false);
        getContentPane().add(txtCorreo);

        JLabel lbl3 = new JLabel("Contraseña:");
        lbl3.setBounds(30, 110, 80, 25);
        getContentPane().add(lbl3);
        txtContrasena = new JPasswordField(usuario.getContrasena());
        txtContrasena.setBounds(120, 110, 220, 25);
        txtContrasena.setEnabled(false);
        getContentPane().add(txtContrasena);

        lblImagen = new JLabel();
        lblImagen.setBounds(167, 146, 100, 100);
        setImagenPerfil(false);
        getContentPane().add(lblImagen);

        btnCambiarImagen = new JButton("Cambiar Imagen");
        btnCambiarImagen.setBounds(147, 257, 150, 25);
        btnCambiarImagen.addActionListener(e -> seleccionarImagen());
        getContentPane().add(btnCambiarImagen);

        btnEditar = new JButton("Editar");
        btnEditar.setBounds(104, 295, 100, 30);
        btnEditar.addActionListener(e -> setEditable(true));
        getContentPane().add(btnEditar);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(225, 295, 100, 30);
        btnGuardar.addActionListener(e -> guardarCambios());
        getContentPane().add(btnGuardar);
    }

    private void setEditable(boolean editable) {
        txtNombre.setEnabled(editable);
        txtCorreo.setEnabled(editable);
        txtContrasena.setEnabled(editable);
        btnCambiarImagen.setEnabled(editable);
        btnGuardar.setEnabled(editable);
        btnEditar.setEnabled(!editable);
    }

    private void setImagenPerfil(boolean editable) {
        Image img;
        try {
            byte[] imgBytes = usuario.getImagenPerfil();
            if (nuevaImagen != null) {
                imgBytes = Files.readAllBytes(nuevaImagen.toPath());
            }
            if (imgBytes != null) {
                // Convertir bytes a BufferedImage correctamente
                java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(imgBytes);
                BufferedImage bufferedImage = javax.imageio.ImageIO.read(bais);
                img = bufferedImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            } else {
                img = new ImageIcon("assets/LogoAsturTerra.png").getImage().getScaledInstance(100, 100,
                        Image.SCALE_SMOOTH);
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