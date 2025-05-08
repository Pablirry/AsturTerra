package views;

import model.Usuario;
import utils.I18n;
import utils.PasswordUtils;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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
        super(parent, I18n.t("menu.perfil"), true);
        this.usuario = usuario;

        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;

        String contrasenaOriginal = usuario.getContrasena();
        if (PasswordUtils.hash(contrasenaOriginal).isEmpty()) {
            contrasenaOriginal = "";
        }
        JPanel panelMain = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(dark ? new Color(44, 62, 80) : new Color(245, 247, 250));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
            }
        };
        panelMain.setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2, true));
        setContentPane(panelMain);

        setSize(480, 430);
        setResizable(false);
        setLocationRelativeTo(parent);

        // Título con icono
        JLabel lblTitulo = new JLabel(I18n.t("menu.perfil"), JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(52, 152, 219));
        lblTitulo.setBounds(0, 18, 480, 32);
        panelMain.add(lblTitulo);

        // Imagen de perfil
        lblImagen = new JLabel();
        lblImagen.setBounds(190, 60, 100, 100);
        setImagenPerfil(false);
        panelMain.add(lblImagen);

        btnCambiarImagen = new JButton(I18n.t("boton.cambiar.Imagen"));
        btnCambiarImagen.setBounds(165, 165, 150, 28);
        btnCambiarImagen.setBackground(new Color(52, 152, 219));
        btnCambiarImagen.setForeground(Color.WHITE);
        btnCambiarImagen.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCambiarImagen.setFocusPainted(false);
        btnCambiarImagen.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnCambiarImagen.addActionListener(e -> seleccionarImagen());
        panelMain.add(btnCambiarImagen);

        // Etiquetas y campos
        JLabel lblNombre = new JLabel(I18n.t("label.nombre") + ":");
        lblNombre.setBounds(60, 210, 80, 25);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNombre.setForeground(dark ? Color.WHITE : new Color(44, 62, 80));
        panelMain.add(lblNombre);
        txtNombre = new JTextField(usuario.getNombre());
        txtNombre.setBounds(150, 210, 250, 28);
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtNombre.setEnabled(false);
        txtNombre.setBackground(dark ? new Color(52, 73, 94) : Color.WHITE);
        txtNombre.setForeground(dark ? Color.WHITE : Color.BLACK);
        txtNombre.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        panelMain.add(txtNombre);

        JLabel lblCorreo = new JLabel(I18n.t("label.correo") + ":");
        lblCorreo.setBounds(60, 250, 80, 25);
        lblCorreo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblCorreo.setForeground(dark ? Color.WHITE : new Color(44, 62, 80));
        panelMain.add(lblCorreo);
        txtCorreo = new JTextField(usuario.getCorreo());
        txtCorreo.setBounds(150, 250, 250, 28);
        txtCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtCorreo.setEnabled(false);
        txtCorreo.setBackground(dark ? new Color(52, 73, 94) : Color.WHITE);
        txtCorreo.setForeground(dark ? Color.WHITE : Color.BLACK);
        txtCorreo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        panelMain.add(txtCorreo);

        JLabel lblPass = new JLabel(I18n.t("label.Ncontraseña") + ":");
        lblPass.setBounds(35, 290, 130, 25);
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblPass.setForeground(dark ? Color.WHITE : new Color(44, 62, 80));
        panelMain.add(lblPass);

        int xCampo = 180;
        int anchoCampo = 210;
        txtContrasena = new JPasswordField();
        txtContrasena.setBounds(xCampo, 290, anchoCampo, 28);
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtContrasena.setEnabled(false);
        txtContrasena.setBackground(dark ? new Color(52, 73, 94) : Color.WHITE);
        txtContrasena.setForeground(dark ? Color.WHITE : Color.BLACK);
        txtContrasena.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        panelMain.add(txtContrasena);

        JCheckBox chkMostrar = new JCheckBox();
        chkMostrar.setBounds(xCampo + anchoCampo + 10, 290, 28, 28); // ojo a la derecha del campo
        chkMostrar.setOpaque(false);
        chkMostrar.setFocusable(false);
        ImageIcon iconOjo = new ImageIcon("assets/view.png");
        ImageIcon iconOjoCerrado = new ImageIcon("assets/hide.png");

        Image imgOjo = iconOjo.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
        Image imgOjoCerrado = iconOjoCerrado.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);

        chkMostrar.setIcon(new ImageIcon(imgOjo));
        chkMostrar.setSelectedIcon(new ImageIcon(imgOjoCerrado));

        chkMostrar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                chkMostrar.setBackground(new Color(220, 220, 220, 80));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                chkMostrar.setBackground(new Color(0, 0, 0, 0));
            }
        });

        chkMostrar.addActionListener(e -> {
            if (chkMostrar.isSelected()) {
                txtContrasena.setEchoChar((char) 0);
            } else {
                txtContrasena.setEchoChar('•');
            }
        });
        panelMain.add(chkMostrar);

        // Etiqueta para mostrar errores de validación
        JLabel lblErrorPass = new JLabel();
        lblErrorPass.setBounds(xCampo, 318, 250, 18);
        lblErrorPass.setForeground(Color.RED);
        lblErrorPass.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelMain.add(lblErrorPass);

        txtContrasena.getDocument().addDocumentListener(new DocumentListener() {
            void validar() {
                String pass = new String(txtContrasena.getPassword());
                if (!pass.isEmpty() && !UIUtils.validarPasswordSegura(pass)) {
                    lblErrorPass.setText(I18n.t("error.password.formato"));
                } else {
                    lblErrorPass.setText("");
                }
            }

            public void insertUpdate(DocumentEvent e) {
                validar();
            }

            public void removeUpdate(DocumentEvent e) {
                validar();
            }

            public void changedUpdate(DocumentEvent e) {
                validar();
            }
        });

        // Botones
        btnEditar = new JButton(I18n.t("boton.editar"));
        btnEditar.setBounds(110, 340, 110, 36);
        btnEditar.setBackground(new Color(52, 152, 219));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnEditar.setFocusPainted(false);
        btnEditar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnEditar.addActionListener(e -> setEditable(true));
        panelMain.add(btnEditar);

        btnGuardar = new JButton(I18n.t("boton.guardar"));
        btnGuardar.setBounds(260, 340, 110, 36);
        btnGuardar.setBackground(new Color(39, 174, 96));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnGuardar.addActionListener(e -> guardarCambios());
        btnGuardar.setEnabled(true);
        panelMain.add(btnGuardar);
    }

    private void setEditable(boolean editable) {
        txtNombre.setEnabled(editable);
        txtCorreo.setEnabled(editable);
        txtContrasena.setEnabled(editable);
        btnCambiarImagen.setEnabled(editable);
        btnEditar.setEnabled(!editable);
        setImagenPerfil(editable);

        if (editable && !PasswordUtils.hash(usuario.getContrasena()).isEmpty()) {
            txtContrasena.setText("");
        }
    }

    private void setImagenPerfil(boolean editable) {
        Image img;
        try {
            byte[] imgBytes = usuario.getImagenPerfil();
            if (nuevaImagen != null) {
                imgBytes = Files.readAllBytes(nuevaImagen.toPath());
            }
            if (imgBytes != null) {
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
        int size = 100;
        BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
        g2.drawImage(img, 0, 0, 100, 100, null);
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
            String nuevaContrasena = new String(txtContrasena.getPassword()).trim();

            if (!nuevaContrasena.isEmpty() && !UIUtils.validarPasswordSegura(nuevaContrasena)) {
                JOptionPane.showMessageDialog(this, I18n.t("error.password.formato"));
                return;
            }

            if (!nuevaContrasena.isEmpty()) {
                usuario.setContrasena(PasswordUtils.hash(nuevaContrasena));
            }

            if (nuevaImagen != null) {
                usuario.setImagenPerfil(Files.readAllBytes(nuevaImagen.toPath()));
            }
            new dao.UsuarioDAO().actualizarUsuario(usuario);
            JOptionPane.showMessageDialog(this, utils.I18n.t("mensaje.usuario.actualizado"));
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, utils.I18n.t("mensaje.error.generico") + ": " + ex.getMessage());
        }
    }
}