package views;

import model.Restaurante;
import services.TurismoService;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.io.File;

public class EditarRestaurante extends JDialog {
    private JTextField txtNombre, txtUbicacion;
    private JLabel lblImagen;
    private JButton btnGuardar, btnCancelar, btnImagen;
    private File imagenRestaurante;
    private Restaurante restauranteOriginal;

    public EditarRestaurante(Restaurante restaurante) {
        super((Frame) null, "Editar Restaurante", true);
        this.restauranteOriginal = restaurante;
        setSize(440, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        // Tema y colores
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
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Título
        JLabel lblTitulo = new JLabel("Editar Restaurante");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(fgPanel);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(18));

        JPanel panelCampos = new JPanel();
        panelCampos.setOpaque(false);
        panelCampos.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        lblNombre.setForeground(fgPanel);
        panelCampos.add(lblNombre, gbc);

        gbc.gridy++;
        txtNombre = new JTextField(restaurante.getNombre());
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 15));
        txtNombre.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        txtNombre.setForeground(fgPanel);
        txtNombre.setCaretColor(fgPanel);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtNombre.setPreferredSize(new Dimension(250, 36));
        panelCampos.add(txtNombre, gbc);

        // Ubicación
        gbc.gridy++;
        JLabel lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setFont(new Font("Arial", Font.PLAIN, 16));
        lblUbicacion.setForeground(fgPanel);
        panelCampos.add(lblUbicacion, gbc);

        gbc.gridy++;
        txtUbicacion = new JTextField(restaurante.getUbicacion());
        txtUbicacion.setFont(new Font("Arial", Font.PLAIN, 15));
        txtUbicacion.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        txtUbicacion.setForeground(fgPanel);
        txtUbicacion.setCaretColor(fgPanel);
        txtUbicacion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtUbicacion.setPreferredSize(new Dimension(250, 36));
        panelCampos.add(txtUbicacion, gbc);

        // Imagen
        gbc.gridy++;
        lblImagen = new JLabel("Sin imagen", JLabel.CENTER);
        lblImagen.setPreferredSize(new Dimension(120, 120));
        lblImagen.setFont(new Font("Arial", Font.ITALIC, 14));
        lblImagen.setForeground(theme == ThemeManager.Theme.DARK ? new Color(120, 120, 120) : new Color(160, 160, 160));
        lblImagen.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        if (restaurante.getImagen() != null) {
            ImageIcon icon = new ImageIcon(restaurante.getImagen());
            lblImagen.setIcon(new ImageIcon(icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
            lblImagen.setText("");
        }
        panelCampos.add(lblImagen, gbc);

        gbc.gridy++;
        btnImagen = new JButton("Cambiar Imagen");
        btnImagen.setBackground(borderColor);
        btnImagen.setForeground(Color.WHITE);
        btnImagen.setFocusPainted(false);
        btnImagen.setFont(new Font("Arial", Font.BOLD, 14));
        btnImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnImagen.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                imagenRestaurante = fc.getSelectedFile();
                ImageIcon icon = new ImageIcon(imagenRestaurante.getAbsolutePath());
                lblImagen.setIcon(new ImageIcon(icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
                lblImagen.setText("");
            }
        });
        panelCampos.add(btnImagen, gbc);

        panel.add(panelCampos);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setOpaque(false);
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 15));
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 15));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void guardarCambios() {
        try {
            String nombre = txtNombre.getText().trim();
            String ubicacion = txtUbicacion.getText().trim();

            if (nombre.isEmpty() || ubicacion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
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
                restauranteOriginal.getValoracion(), // Mantén la valoración
                imagenBytes
            );

            boolean actualizado = TurismoService.getInstance().actualizarRestaurante(restauranteEditado);
            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Restaurante actualizado correctamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el restaurante.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar restaurante: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}