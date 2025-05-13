package views;

import model.Restaurante;
import services.TurismoService;

import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;

public class AgregarRestaurante extends JDialog {
    private JTextField txtNombre, txtUbicacion;
    private JLabel lblImagen;
    private JButton btnSeleccionarImagen, btnGuardar, btnCancelar;
    private byte[] imagenBytes = null;
    private VistaRestaurantes padre;

    public AgregarRestaurante(VistaRestaurantes padre) {
        super(padre, "Agregar Restaurante", true);
        this.padre = padre;
        setSize(520, 370);
        setLocationRelativeTo(padre);
        setResizable(false);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color bg = dark ? new Color(44, 62, 80) : Color.WHITE;
        Color fg = dark ? Color.WHITE : Color.BLACK;
        Color fieldBg = dark ? new Color(44, 62, 80) : Color.WHITE;

        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        mainPanel.setBackground(bg);

        JLabel lblTitulo = new JLabel("Agregar Restaurante");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(41, 128, 185));
        lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.X_AXIS));

        JPanel panelCampos = new JPanel();
        panelCampos.setOpaque(false);
        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 18));

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtNombre.setMaximumSize(new Dimension(260, 32));
        txtNombre.setBackground(fieldBg);
        txtNombre.setForeground(fg);
        txtNombre.setCaretColor(fg);
        txtNombre.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185, 120), 2, true),
                "Nombre",
                0, 0, new Font("Segoe UI", Font.BOLD, 13), new Color(41, 128, 185)));

        txtUbicacion = new JTextField();
        txtUbicacion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtUbicacion.setMaximumSize(new Dimension(260, 32));
        txtUbicacion.setBackground(fieldBg);
        txtUbicacion.setForeground(fg);
        txtUbicacion.setCaretColor(fg);
        txtUbicacion.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185, 120), 2, true),
                "Ubicación",
                0, 0, new Font("Segoe UI", Font.BOLD, 13), new Color(41, 128, 185)));

        panelCampos.add(Box.createVerticalStrut(8));
        panelCampos.add(txtNombre);
        panelCampos.add(Box.createVerticalStrut(10));
        panelCampos.add(txtUbicacion);
        panelCampos.add(Box.createVerticalGlue());

        JPanel panelImagen = new JPanel();
        panelImagen.setOpaque(false);
        panelImagen.setLayout(new BoxLayout(panelImagen, BoxLayout.Y_AXIS));
        panelImagen.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        lblImagen = new JLabel("Sin imagen") {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(120, 120);
            }
        };
        lblImagen.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setForeground(new Color(120, 120, 120));

        btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSeleccionarImagen.setBackground(new Color(41, 128, 185));
        btnSeleccionarImagen.setForeground(Color.WHITE);
        btnSeleccionarImagen.setFocusPainted(false);
        btnSeleccionarImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSeleccionarImagen.setPreferredSize(new Dimension(150, 32));
        btnSeleccionarImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSeleccionarImagen.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnSeleccionarImagen.setBackground(new Color(31, 97, 141));
            }

            public void mouseExited(MouseEvent e) {
                btnSeleccionarImagen.setBackground(new Color(41, 128, 185));
            }
        });
        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());

        panelImagen.add(Box.createVerticalGlue());
        panelImagen.add(lblImagen);
        panelImagen.add(Box.createVerticalStrut(12));
        panelImagen.add(btnSeleccionarImagen);
        panelImagen.add(Box.createVerticalGlue());

        panelCentro.add(panelCampos);
        panelCentro.add(panelImagen);

        mainPanel.add(panelCentro, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 8));
        panelBotones.setOpaque(false);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(52, 152, 219));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(120, 36));
        btnGuardar.setBorder(BorderFactory.createEmptyBorder());
        btnGuardar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnGuardar.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(MouseEvent e) {
                btnGuardar.setBackground(new Color(52, 152, 219));
            }
        });
        btnGuardar.addActionListener(e -> guardarRestaurante());

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(110, 36));
        btnCancelar.setBorder(BorderFactory.createEmptyBorder());
        btnCancelar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnCancelar.setBackground(new Color(192, 57, 43));
            }

            public void mouseExited(MouseEvent e) {
                btnCancelar.setBackground(new Color(231, 76, 60));
            }
        });
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        mainPanel.add(panelBotones, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void seleccionarImagen() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                imagenBytes = Files.readAllBytes(file.toPath());
                lblImagen.setText("");
                lblImagen.setIcon(getCircularImageIcon(imagenBytes, 120));
            } catch (Exception ex) {
                lblImagen.setText("Sin imagen");
                lblImagen.setIcon(null);
                JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen.");
            }
        }
    }

    private ImageIcon getCircularImageIcon(byte[] imgBytes, int size) {
        try {
            java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(imgBytes);
            java.awt.image.BufferedImage original = javax.imageio.ImageIO.read(bais);
            Image img = original.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circleBuffer.createGraphics();
            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
            g2.drawImage(img, 0, 0, size, size, null);
            g2.dispose();
            return new ImageIcon(circleBuffer);
        } catch (Exception e) {
            return null;
        }
    }

    private void guardarRestaurante() {
        String nombre = txtNombre.getText().trim();
        String ubicacion = txtUbicacion.getText().trim();
        if (nombre.isEmpty() || ubicacion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.");
            return;
        }
        Restaurante restaurante = new Restaurante(0, nombre, ubicacion, imagenBytes);
        try {
            TurismoService.getInstance().agregarRestaurante(restaurante);
            padre.cargarRestaurantes();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar restaurante.");
        }
    }
}