package views;

import model.Ruta;
import services.TurismoService;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.io.File;

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

        // Colores principales según tema
        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
        Color bgGlobal = theme == ThemeManager.Theme.DARK
                ? new Color(34, 45, 65)
                : new Color(235, 241, 250);
        Color panelCamposColor = theme == ThemeManager.Theme.DARK
                ? new Color(44, 62, 80)
                : new Color(255, 255, 255);
        Color fgPanel = theme == ThemeManager.Theme.DARK
                ? Color.WHITE
                : new Color(44, 62, 80);
        Color borderColor = new Color(52, 152, 219);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 700;
        int height = 920;
        setSize(width, height);
        setMinimumSize(new Dimension(500, 500));
        setLocation(screen.width / 2 - width / 2, 40);
        setLayout(new BorderLayout());

        // Panel global que cubre toda la ventana (fondo liso)
        JPanel panelFondo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(bgGlobal);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelFondo.setOpaque(true);

        // Panel principal con esquinas redondeadas, mismo color que el fondo global
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgGlobal);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JPanel panelTitulo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(bgGlobal);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelTitulo.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0)); // Cambia a FlowLayout centrado
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel lblIcono;
        File iconFile = new File("assets/editar.png");
        if (iconFile.exists()) {
            ImageIcon iconoTitulo = new ImageIcon(iconFile.getAbsolutePath());
            Image imgEscala = iconoTitulo.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            lblIcono = new JLabel(new ImageIcon(imgEscala));
        } else {
            lblIcono = new JLabel();
        }
        lblIcono.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        JLabel lblTitulo = new JLabel("Ruta: " + ruta.getNombre());
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(borderColor);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setVerticalAlignment(SwingConstants.CENTER);

        // Añadir icono a la izquierda y título a la derecha, ambos centrados
        panelTitulo.add(lblIcono);
        panelTitulo.add(lblTitulo);

        panel.add(panelTitulo);
        panel.add(Box.createVerticalStrut(10));

        // Imagen rectangular y centrada con borde y sombra
        JPanel imgPanel = new JPanel();
        imgPanel.setOpaque(false);
        imgPanel.setLayout(new BoxLayout(imgPanel, BoxLayout.Y_AXIS));
        imgPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(200, 200));
        lblImagen.setMaximumSize(new Dimension(200, 200));
        lblImagen.setMinimumSize(new Dimension(200, 200));
        lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setVerticalAlignment(SwingConstants.CENTER);
        lblImagen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 3, true),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        imgPanel.add(lblImagen);

        setImagen(imagenActual);

        imgPanel.add(Box.createVerticalStrut(10));

        btnImagen = new JButton("Cambiar Imagen");
        btnImagen.setBackground(borderColor);
        btnImagen.setForeground(Color.WHITE);
        btnImagen.setFocusPainted(false);
        btnImagen.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnImagen.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        btnImagen.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                imagenRuta = fc.getSelectedFile();
                try {
                    imagenActual = Files.readAllBytes(imagenRuta.toPath());
                    setImagen(imagenActual);
                } catch (Exception ex) {
                    imagenActual = null;
                    setImagen(null);
                }
            }
        });
        imgPanel.add(btnImagen);

        panel.add(imgPanel);
        panel.add(Box.createVerticalStrut(10));

        // Panel de campos con fondo y borde redondeado (solo este es diferente)
        JPanel panelCampos = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(panelCamposColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
            }
        };
        panelCampos.setOpaque(false);
        panelCampos.setLayout(new GridBagLayout());
        panelCampos.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        int fieldWidth = 340;
        int fieldHeight = 36;
        int areaWidth = 380;
        int areaHeight = 90;
        int comboWidth = 200;
        int comboHeight = 36;

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNombre.setForeground(borderColor);
        panelCampos.add(lblNombre, gbc);

        gbc.gridy++;
        txtNombre = new JTextField(ruta.getNombre());
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtNombre.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        txtNombre.setForeground(fgPanel);
        txtNombre.setCaretColor(fgPanel);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtNombre.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
        txtNombre.setMaximumSize(new Dimension(fieldWidth, fieldHeight));
        txtNombre.setMinimumSize(new Dimension(fieldWidth, fieldHeight));
        panelCampos.add(txtNombre, gbc);

        // Descripción
        gbc.gridy++;
        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDescripcion.setForeground(borderColor);
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
                BorderFactory.createLineBorder(borderColor, 2, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        txtDescripcion.setPreferredSize(new Dimension(areaWidth, areaHeight));
        txtDescripcion.setMaximumSize(new Dimension(areaWidth, areaHeight));
        txtDescripcion.setMinimumSize(new Dimension(areaWidth, areaHeight));
        txtDescripcion.setEditable(true);
        txtDescripcion.setFocusable(true);

        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setPreferredSize(new Dimension(areaWidth, areaHeight));
        scrollDesc.setMaximumSize(new Dimension(areaWidth, areaHeight));
        scrollDesc.setMinimumSize(new Dimension(areaWidth, areaHeight));
        scrollDesc.setBorder(BorderFactory.createEmptyBorder());
        panelCampos.add(scrollDesc, gbc);

        // Precio
        gbc.gridy++;
        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPrecio.setForeground(borderColor);
        panelCampos.add(lblPrecio, gbc);

        gbc.gridy++;
        txtPrecio = new JTextField(String.valueOf(ruta.getPrecio()));
        txtPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPrecio.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        txtPrecio.setForeground(fgPanel);
        txtPrecio.setCaretColor(fgPanel);
        txtPrecio.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtPrecio.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
        txtPrecio.setMaximumSize(new Dimension(fieldWidth, fieldHeight));
        txtPrecio.setMinimumSize(new Dimension(fieldWidth, fieldHeight));
        panelCampos.add(txtPrecio, gbc);

        // Dificultad
        gbc.gridy++;
        JLabel lblDificultad = new JLabel("Dificultad:");
        lblDificultad.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDificultad.setForeground(borderColor);
        panelCampos.add(lblDificultad, gbc);

        gbc.gridy++;
        cmbDificultad = new JComboBox<>(new String[] { "Fácil", "Media", "Difícil" });
        cmbDificultad.setSelectedItem(ruta.getDificultad());
        cmbDificultad.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cmbDificultad.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        cmbDificultad.setForeground(fgPanel);
        cmbDificultad.setBorder(BorderFactory.createLineBorder(borderColor, 2, true));
        cmbDificultad.setPreferredSize(new Dimension(comboWidth, comboHeight));
        cmbDificultad.setMaximumSize(new Dimension(comboWidth, comboHeight));
        cmbDificultad.setMinimumSize(new Dimension(comboWidth, comboHeight));
        panelCampos.add(cmbDificultad, gbc);

        panelCampos.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(panelCampos);

        panel.add(Box.createVerticalStrut(10));

        // Botones con fondo igual al global
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(bgGlobal);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelBotones.setOpaque(false);

        btnGuardar = new JButton("💾 Guardar");
        btnGuardar.setBackground(theme == ThemeManager.Theme.DARK
                ? new Color(46, 204, 113)
                : new Color(52, 152, 219));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("Dialog", Font.BOLD, 18));
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(12, 36, 12, 36));

        btnCancelar = new JButton("✖ Cancelar");
        btnCancelar.setBackground(theme == ThemeManager.Theme.DARK
                ? new Color(231, 76, 60)
                : new Color(200, 60, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Dialog", Font.BOLD, 18));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(12, 36, 12, 36));

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        // Añadir paneles al fondo
        panelFondo.add(panel, BorderLayout.CENTER);
        panelFondo.add(panelBotones, BorderLayout.SOUTH);

        setContentPane(panelFondo);

        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void setImagen(byte[] imagenBytes) {
        if (imagenBytes != null && imagenBytes.length > 0) {
            try {
                Image img = Toolkit.getDefaultToolkit().createImage(imagenBytes);
                MediaTracker tracker = new MediaTracker(new Component() {
                });
                tracker.addImage(img, 0);
                tracker.waitForAll();

                if (!tracker.isErrorAny() && img.getWidth(null) > 0 && img.getHeight(null) > 0) {
                    ImageIcon icon = new ImageIcon(img.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
                    lblImagen.setIcon(icon);
                    lblImagen.setText("");
                    return;
                }
            } catch (Exception ex) {
                // Si hay error, sigue abajo y muestra "Sin imagen"
            }
        }
        // Si no hay imagen o hay error, muestra solo el texto "Sin imagen" centrado en
        // el cuadrado
        lblImagen.setIcon(null);
        lblImagen.setText("Sin imagen");
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setVerticalAlignment(SwingConstants.CENTER);
        lblImagen.setFont(new Font("Segoe UI", Font.BOLD, 18));
        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
        lblImagen.setForeground(theme == ThemeManager.Theme.DARK ? new Color(180, 180, 180) : new Color(120, 120, 120));
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
                if (parent != null)
                    parent.cargarRutas();
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