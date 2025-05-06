package views;

import model.Ruta;
import services.TurismoService;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.io.File;
import java.awt.image.BufferedImage;

public class EditarRuta extends JDialog {
    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JTextField txtPrecio;
    private JComboBox<String> cmbDificultad;
    private JLabel lblImagen;
    private JButton btnGuardar, btnCancelar, btnImagen;
    private File imagenRuta;
    private Ruta rutaOriginal;
    private VistaRutas parent;
    private byte[] imagenActual;

    public EditarRuta(VistaRutas parent, Ruta ruta) {
        super((Frame) null, "Editar " + ruta.getNombre(), true);
        this.parent = parent;
        this.rutaOriginal = ruta;
        this.imagenActual = ruta.getImagen();
        setSize(700, 800);
        setMinimumSize(new Dimension(500, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
        Color bgPanel = theme == ThemeManager.Theme.DARK ? new Color(44, 62, 80) : new Color(245, 247, 250);
        Color fgPanel = theme == ThemeManager.Theme.DARK ? Color.WHITE : new Color(44, 62, 80);
        Color borderColor = new Color(52, 152, 219);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgPanel);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitulo = new JLabel("Editar: " + ruta.getNombre());
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(borderColor);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(10));

        // Imagen circular y centrada JUSTO debajo del título
        lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(140, 140));
        lblImagen.setMaximumSize(new Dimension(140, 140));
        lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setVerticalAlignment(SwingConstants.CENTER);
        panel.add(lblImagen);

        // Forzar repintado y actualización del icono tras añadir el JLabel
        SwingUtilities.invokeLater(() -> setImagenCircular(imagenActual));

        panel.add(Box.createVerticalStrut(24)); // Espacio mayor antes del botón

        btnImagen = new JButton("Cambiar Imagen");
        btnImagen.setBackground(borderColor);
        btnImagen.setForeground(Color.WHITE);
        btnImagen.setFocusPainted(false);
        btnImagen.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnImagen.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                imagenRuta = fc.getSelectedFile();
                try {
                    imagenActual = Files.readAllBytes(imagenRuta.toPath());
                    setImagenCircular(imagenActual);
                } catch (Exception ex) {
                    imagenActual = null;
                    setImagenCircular(null);
                }
            }
        });
        panel.add(btnImagen);
        panel.add(Box.createVerticalStrut(14));

        JPanel panelCampos = new JPanel();
        panelCampos.setOpaque(false);
        panelCampos.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        int fieldWidth = 320;
        int fieldHeight = 32;
        int areaWidth = 400;
        int areaHeight = 100;
        int comboWidth = 180;
        int comboHeight = 32;

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblNombre.setForeground(fgPanel);
        panelCampos.add(lblNombre, gbc);

        gbc.gridy++;
        txtNombre = new JTextField(ruta.getNombre());
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtNombre.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        txtNombre.setForeground(fgPanel);
        txtNombre.setCaretColor(fgPanel);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        txtNombre.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
        txtNombre.setMaximumSize(new Dimension(fieldWidth, fieldHeight));
        txtNombre.setMinimumSize(new Dimension(fieldWidth, fieldHeight));
        panelCampos.add(txtNombre, gbc);

        // Descripción
        gbc.gridy++;
        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblDescripcion.setForeground(fgPanel);
        panelCampos.add(lblDescripcion, gbc);

        gbc.gridy++;
        txtDescripcion = new JTextArea(ruta.getDescripcion());
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        txtDescripcion.setForeground(fgPanel);
        txtDescripcion.setCaretColor(fgPanel);
        txtDescripcion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        txtDescripcion.setPreferredSize(new Dimension(areaWidth, areaHeight));
        txtDescripcion.setMaximumSize(new Dimension(areaWidth, areaHeight));
        txtDescripcion.setMinimumSize(new Dimension(areaWidth, areaHeight));
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setPreferredSize(new Dimension(areaWidth, areaHeight));
        scrollDesc.setMaximumSize(new Dimension(areaWidth, areaHeight));
        scrollDesc.setMinimumSize(new Dimension(areaWidth, areaHeight));
        panelCampos.add(scrollDesc, gbc);

        // Precio
        gbc.gridy++;
        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblPrecio.setForeground(fgPanel);
        panelCampos.add(lblPrecio, gbc);

        gbc.gridy++;
        txtPrecio = new JTextField(String.valueOf(ruta.getPrecio()));
        txtPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtPrecio.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        txtPrecio.setForeground(fgPanel);
        txtPrecio.setCaretColor(fgPanel);
        txtPrecio.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        txtPrecio.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
        txtPrecio.setMaximumSize(new Dimension(fieldWidth, fieldHeight));
        txtPrecio.setMinimumSize(new Dimension(fieldWidth, fieldHeight));
        panelCampos.add(txtPrecio, gbc);

        // Dificultad
        gbc.gridy++;
        JLabel lblDificultad = new JLabel("Dificultad:");
        lblDificultad.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblDificultad.setForeground(fgPanel);
        panelCampos.add(lblDificultad, gbc);

        gbc.gridy++;
        cmbDificultad = new JComboBox<>(new String[] { "Fácil", "Media", "Difícil" });
        cmbDificultad.setSelectedItem(ruta.getDificultad());
        cmbDificultad.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        cmbDificultad.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        cmbDificultad.setForeground(fgPanel);
        cmbDificultad.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        cmbDificultad.setPreferredSize(new Dimension(comboWidth, comboHeight));
        cmbDificultad.setMaximumSize(new Dimension(comboWidth, comboHeight));
        cmbDificultad.setMinimumSize(new Dimension(comboWidth, comboHeight));
        panelCampos.add(cmbDificultad, gbc);

        panelCampos.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(panelCampos);

        panel.add(Box.createVerticalStrut(14));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setOpaque(false);
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void setImagenCircular(byte[] imagenBytes) {
        int size = 140;
        BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();

        if (imagenBytes != null && imagenBytes.length > 0) {
            try {
                ImageIcon icon = new ImageIcon(imagenBytes);
                Image img = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
                g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
                g2.drawImage(img, 0, 0, size, size, null);
            } catch (Exception ex) {
                drawSinImagen(g2, size, theme);
            }
        } else {
            drawSinImagen(g2, size, theme);
        }
        g2.dispose();
        lblImagen.setIcon(new ImageIcon(circleBuffer));
        lblImagen.setText("");
    }

    private void drawSinImagen(Graphics2D g2, int size, ThemeManager.Theme theme) {
        Color circleColor = theme == ThemeManager.Theme.DARK ? new Color(80, 80, 80) : new Color(220, 220, 220);
        Color borderColor = theme == ThemeManager.Theme.DARK ? new Color(120, 120, 120) : new Color(160, 160, 160);
        Color textColor = theme == ThemeManager.Theme.DARK ? new Color(180, 180, 180) : new Color(120, 120, 120);

        g2.setColor(circleColor);
        g2.fillOval(0, 0, size, size);
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(1, 1, size - 2, size - 2);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        FontMetrics fm = g2.getFontMetrics();
        String texto = "Sin imagen";
        int textWidth = fm.stringWidth(texto);
        int textHeight = fm.getHeight();
        g2.setColor(textColor);
        g2.drawString(texto, (size - textWidth) / 2, size / 2 + textHeight / 4);
    }

    private void guardarCambios() {
        try {
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            String precioStr = txtPrecio.getText().trim();
            String dificultad = (String) cmbDificultad.getSelectedItem();

            if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty() || dificultad == null) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            double precio;
            try {
                precio = Double.parseDouble(precioStr);
                if (precio < 0)
                    throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Precio no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            byte[] imagenBytes = imagenActual;

            Ruta rutaEditada = new Ruta(
                    rutaOriginal.getId(),
                    nombre,
                    descripcion,
                    imagenBytes,
                    precio,
                    dificultad);

            boolean actualizado = TurismoService.getInstance().actualizarRuta(rutaEditada);
            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Ruta actualizada correctamente.");
                if (parent != null) parent.cargarRutas();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la ruta.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar ruta: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}