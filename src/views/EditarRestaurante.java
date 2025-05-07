package views;

import model.Restaurante;
import services.TurismoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.nio.file.Files;

public class EditarRestaurante extends JDialog {
    private JTextField txtNombre, txtUbicacion;
    private JLabel lblImagen;
    private JButton btnGuardar, btnCancelar, btnImagen;
    private File imagenRestaurante;
    private Restaurante restauranteOriginal;

    public EditarRestaurante(JFrame parent, Restaurante restaurante) {
        super(parent, "Editar Restaurante", true);
        this.restauranteOriginal = restaurante;
        setSize(900, 600);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(parent);
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

        JLabel lblTitulo = new JLabel("Editar Restaurante");
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

        txtNombre = new JTextField(restauranteOriginal.getNombre());
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtNombre.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        txtNombre.setForeground(fgPanel);
        txtNombre.setCaretColor(fgPanel);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtNombre.setMaximumSize(new Dimension(400, 36));
        txtNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblUbicacion.setForeground(borderColor);
        lblUbicacion.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUbicacion = new JTextField(restauranteOriginal.getUbicacion());
        txtUbicacion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtUbicacion.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        txtUbicacion.setForeground(fgPanel);
        txtUbicacion.setCaretColor(fgPanel);
        txtUbicacion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtUbicacion.setMaximumSize(new Dimension(400, 36));
        txtUbicacion.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelInfo.add(lblTitulo);
        panelInfo.add(Box.createVerticalStrut(2));
        panelInfo.add(separator);
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(lblNombre);
        panelInfo.add(txtNombre);
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(lblUbicacion);
        panelInfo.add(txtUbicacion);

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

        if (restauranteOriginal.getImagen() != null) {
            ImageIcon icon = new ImageIcon(restauranteOriginal.getImagen());
            lblImagen.setIcon(new ImageIcon(icon.getImage().getScaledInstance(210, 210, Image.SCALE_SMOOTH)));
            lblImagen.setText("");
        }

        btnImagen = new JButton("Seleccionar Imagen");
        btnImagen.setBackground(borderColor);
        btnImagen.setForeground(Color.WHITE);
        btnImagen.setFocusPainted(false);
        btnImagen.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnImagen.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                imagenRestaurante = fc.getSelectedFile();
                ImageIcon icon = new ImageIcon(imagenRestaurante.getAbsolutePath());
                lblImagen.setIcon(new ImageIcon(icon.getImage().getScaledInstance(210, 210, Image.SCALE_SMOOTH)));
                lblImagen.setText("");
            }
        });

        panelImagen.add(lblImagen);
        panelImagen.add(Box.createVerticalStrut(10));
        panelImagen.add(btnImagen);

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

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(220, 38));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(220, 38));

        panelBotones.add(btnGuardar);
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

        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> dispose());

        // Responsividad
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = getWidth();
                int minWidth = 700;
                int minHeight = 500;
                if (w < minWidth) setSize(minWidth, getHeight());
                if (getHeight() < minHeight) setSize(getWidth(), minHeight);
            }
        });

        setVisible(true);
    }

    private void guardarCambios() {
        try {
            String nombre = txtNombre.getText().trim();
            String ubicacion = txtUbicacion.getText().trim();

            if (nombre.isEmpty() || ubicacion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            byte[] imagenBytes = restauranteOriginal.getImagen();
            if (imagenRestaurante != null) {
                imagenBytes = Files.readAllBytes(imagenRestaurante.toPath());
            }

            Restaurante restauranteEditado = new Restaurante(
                    restauranteOriginal.getId(),
                    nombre,
                    ubicacion,
                    restauranteOriginal.getValoracion(),
                    imagenBytes);

            boolean actualizado = TurismoService.getInstance().actualizarRestaurante(restauranteEditado);
            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Restaurante actualizado correctamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el restaurante.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar restaurante: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}