package views;

import javax.swing.*;
import java.awt.*;
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

    public AgregarRuta() {
        setTitle("Agregar Ruta");
        setSize(650, 700); // Ventana más grande para imagen y campos
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Colores y tema
        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
        Color bgPanel = theme == ThemeManager.Theme.DARK ? new Color(44, 62, 80) : new Color(245, 247, 250);
        Color fgPanel = theme == ThemeManager.Theme.DARK ? Color.WHITE : new Color(44, 62, 80);
        Color borderColor = new Color(52, 152, 219);

        // Panel principal con fondo y borde redondeado
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
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Título
        JLabel lblTitulo = new JLabel("Nueva Ruta");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(fgPanel);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(18));

        // Panel campos
        JPanel panelCampos = new JPanel();
        panelCampos.setOpaque(false);
        panelCampos.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        panelCampos.add(lblNombre, gbc);

        gbc.gridy++;
        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 15));
        txtNombre.setPreferredSize(new Dimension(500, 36)); // MÁS ANCHO
        panelCampos.add(txtNombre, gbc);

        // Descripción
        gbc.gridy++;
        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 16));
        panelCampos.add(lblDescripcion, gbc);

        gbc.gridy++;
        txtDescripcion = new JTextArea(10, 30); // MÁS ANCHO
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 15));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setPreferredSize(new Dimension(500, 100)); // MÁS ANCHO
        panelCampos.add(scrollDesc, gbc);

        // Precio
        gbc.gridy++;
        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setFont(new Font("Arial", Font.PLAIN, 16));
        lblPrecio.setForeground(fgPanel);
        panelCampos.add(lblPrecio, gbc);

        gbc.gridy++;
        txtPrecio = new JTextField();
        txtPrecio.setFont(new Font("Arial", Font.PLAIN, 15));
        txtPrecio.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        txtPrecio.setForeground(fgPanel);
        txtPrecio.setCaretColor(fgPanel);
        txtPrecio.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtPrecio.setPreferredSize(new Dimension(120, 36)); // Más pequeño
        panelCampos.add(txtPrecio, gbc);

        // Dificultad
        gbc.gridy++;
        JLabel lblDificultad = new JLabel("Dificultad:");
        lblDificultad.setFont(new Font("Arial", Font.PLAIN, 16));
        lblDificultad.setForeground(fgPanel);
        panelCampos.add(lblDificultad, gbc);

        gbc.gridy++;
        cmbDificultad = new JComboBox<>(new String[] { "Fácil", "Media", "Difícil" });
        cmbDificultad.setFont(new Font("Arial", Font.PLAIN, 15));
        cmbDificultad.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        cmbDificultad.setForeground(fgPanel);
        cmbDificultad.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        cmbDificultad.setPreferredSize(new Dimension(250, 36));
        panelCampos.add(cmbDificultad, gbc);

        panelCampos.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(panelCampos);
        panel.add(Box.createVerticalStrut(18));

        // Imagen
        lblImagen = new JLabel("Sin imagen", JLabel.CENTER);
        lblImagen.setPreferredSize(new Dimension(320, 180));
        lblImagen.setFont(new Font("Arial", Font.ITALIC, 14));
        lblImagen.setForeground(theme == ThemeManager.Theme.DARK ? new Color(120, 120, 120) : new Color(160, 160, 160));
        lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblImagen.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        panel.add(lblImagen);

        btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.setBackground(borderColor);
        btnSeleccionarImagen.setForeground(Color.WHITE);
        btnSeleccionarImagen.setFocusPainted(false);
        btnSeleccionarImagen.setFont(new Font("Arial", Font.BOLD, 14));
        btnSeleccionarImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSeleccionarImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnSeleccionarImagen);
        panel.add(Box.createVerticalStrut(18));

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setOpaque(false);
        btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new Color(46, 204, 113));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 15));
        btnAgregar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 15));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnCancelar);

        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregarRuta());
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            imagenRuta = fileChooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(imagenRuta.getAbsolutePath());
            lblImagen.setIcon(new ImageIcon(icon.getImage().getScaledInstance(320, 180, Image.SCALE_SMOOTH)));
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

            // Evitar duplicados por nombre
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