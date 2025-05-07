package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.nio.file.Files;
import model.Ruta;
import services.TurismoService;

public class AgregarRuta extends JFrame {

    private JTextField txtNombre, txtPrecio;
    private JTextArea txtDescripcion;
    private JComboBox<String> cmbDificultad;
    private JButton btnSeleccionarImagen, btnAgregar, btnCancelar;
    private File imagenRuta;
    private JLabel lblImagen;
    private VistaRutas parent;

    public AgregarRuta(VistaRutas parent) {
        this.parent = parent;
        setTitle("Agregar Ruta");
        setSize(1100, 750);
        setMinimumSize(new Dimension(900, 550));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
        Color bgGlobal = theme == ThemeManager.Theme.DARK ? new Color(34, 45, 65) : new Color(235, 241, 250);
        Color fgPanel = theme == ThemeManager.Theme.DARK ? Color.WHITE : new Color(44, 62, 80);
        Color borderColor = new Color(52, 152, 219);

        JPanel panelFondo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(bgGlobal);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelFondo.setOpaque(true);

        // Panel principal con esquinas redondeadas
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
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(32, 48, 32, 48));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.BOTH;

        // Panel info izquierda
        JPanel panelInfo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(theme == ThemeManager.Theme.DARK ? new Color(44, 62, 80) : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
            }
        };
        panelInfo.setOpaque(false);
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setAlignmentY(Component.TOP_ALIGNMENT);
        panelInfo.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JLabel lblTitulo = new JLabel("Nueva Ruta");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitulo.setForeground(borderColor);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(260, 2));
        separator.setForeground(borderColor);
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNombre.setForeground(borderColor);
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtNombre.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        txtNombre.setForeground(fgPanel);
        txtNombre.setCaretColor(fgPanel);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtNombre.setMaximumSize(new Dimension(400, 36));
        txtNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDescripcion.setForeground(borderColor);
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtDescripcion = new JTextArea(3, 30);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        txtDescripcion.setForeground(fgPanel);
        txtDescripcion.setCaretColor(fgPanel);
        txtDescripcion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 2, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        txtDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setPreferredSize(new Dimension(400, 60));
        scrollDesc.setMaximumSize(new Dimension(Short.MAX_VALUE, 100));
        scrollDesc.setBorder(BorderFactory.createEmptyBorder());
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollDesc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollDesc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPrecio.setForeground(borderColor);
        lblPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPrecio = new JTextField();
        txtPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPrecio.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        txtPrecio.setForeground(fgPanel);
        txtPrecio.setCaretColor(fgPanel);
        txtPrecio.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtPrecio.setMaximumSize(new Dimension(200, 36));
        txtPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDificultad = new JLabel("Dificultad:");
        lblDificultad.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDificultad.setForeground(borderColor);
        lblDificultad.setAlignmentX(Component.LEFT_ALIGNMENT);

        cmbDificultad = new JComboBox<>(new String[] { "Fácil", "Media", "Difícil" });
        cmbDificultad.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cmbDificultad.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        cmbDificultad.setForeground(fgPanel);
        cmbDificultad.setBorder(BorderFactory.createLineBorder(borderColor, 2, true));
        cmbDificultad.setMaximumSize(new Dimension(200, 36));
        cmbDificultad.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelInfo.add(lblTitulo);
        panelInfo.add(Box.createVerticalStrut(2));
        panelInfo.add(separator);
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(lblNombre);
        panelInfo.add(txtNombre);
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(lblDescripcion);
        panelInfo.add(scrollDesc);
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(lblPrecio);
        panelInfo.add(txtPrecio);
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(lblDificultad);
        panelInfo.add(cmbDificultad);

        // Imagen a la derecha en panel redondeado
        JPanel panelImagen = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(theme == ThemeManager.Theme.DARK ? new Color(44, 62, 80) : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
            }
        };
        panelImagen.setOpaque(false);
        panelImagen.setLayout(new BoxLayout(panelImagen, BoxLayout.Y_AXIS));
        panelImagen.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        lblImagen = new JLabel("Sin imagen", JLabel.CENTER);
        lblImagen.setPreferredSize(new Dimension(210, 210));
        lblImagen.setFont(new Font("Arial", Font.ITALIC, 16));
        lblImagen.setForeground(theme == ThemeManager.Theme.DARK ? new Color(120, 120, 120) : new Color(160, 160, 160));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setVerticalAlignment(SwingConstants.CENTER);
        lblImagen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 3, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.setBackground(borderColor);
        btnSeleccionarImagen.setForeground(Color.WHITE);
        btnSeleccionarImagen.setFocusPainted(false);
        btnSeleccionarImagen.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSeleccionarImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSeleccionarImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());

        panelImagen.add(lblImagen);
        panelImagen.add(Box.createVerticalStrut(10));
        panelImagen.add(btnSeleccionarImagen);

        // Añadir a grid
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        gbc.weighty = 1.0;
        panel.add(panelInfo, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.4;
        panel.add(panelImagen, gbc);

        // Botones abajo
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(bgGlobal);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelBotones.setOpaque(false);

        btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new Color(46, 204, 113));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAgregar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAgregar.setPreferredSize(new Dimension(220, 38));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(220, 38));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(32, 0, 0, 0);
        panel.add(panelBotones, gbc);

        panelFondo.add(panel, BorderLayout.CENTER);
        setContentPane(panelFondo);

        btnAgregar.addActionListener(e -> agregarRuta());
        btnCancelar.addActionListener(e -> dispose());

        // Responsividad: no dejar que los cuadros y campos se hagan demasiado pequeños
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = getWidth();
                int minWidth = 800;
                int minHeight = 500;
                if (w < minWidth) setSize(minWidth, getHeight());
                if (getHeight() < minHeight) setSize(getWidth(), minHeight);
            }
        });

        setVisible(true);
    }

    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            imagenRuta = fileChooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(imagenRuta.getAbsolutePath());
            lblImagen.setIcon(new ImageIcon(icon.getImage().getScaledInstance(210, 210, Image.SCALE_SMOOTH)));
            lblImagen.setText("");
        }
    }

    private void agregarRuta() {
        try {
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            String precioStr = txtPrecio.getText().trim();
            String dificultad = (String) cmbDificultad.getSelectedItem();

            if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error",
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

            if (new dao.RutaDAO().obtenerRutaPorNombre(nombre) != null) {
                JOptionPane.showMessageDialog(this, "Ya existe una ruta con ese nombre.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            byte[] imagenBytes = null;
            if (imagenRuta != null) {
                imagenBytes = Files.readAllBytes(imagenRuta.toPath());
            }

            Ruta ruta = new Ruta(0, nombre, descripcion, imagenBytes, precio, dificultad);
            boolean guardado = TurismoService.getInstance().agregarRuta(ruta);

            if (guardado) {
                JOptionPane.showMessageDialog(this, "Ruta agregada correctamente.");
                if (parent != null) parent.cargarRutas();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo agregar la ruta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar ruta: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}