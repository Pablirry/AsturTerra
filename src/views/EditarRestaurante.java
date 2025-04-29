package views;

import model.Restaurante;
import services.TurismoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        setSize(480, 540);
        setLocationRelativeTo(null);
        setResizable(false);

        // Tema y colores
        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
        Color bgPanel = theme == ThemeManager.Theme.DARK ? new Color(44, 62, 80) : new Color(250, 252, 255);
        Color fgPanel = theme == ThemeManager.Theme.DARK ? Color.WHITE : new Color(44, 62, 80);
        Color borderColor = theme == ThemeManager.Theme.DARK ? new Color(52, 152, 219) : new Color(44, 62, 80);

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
        panel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        // Título
        JLabel lblTitulo = new JLabel("Editar Restaurante");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(fgPanel);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(20));

        // Panel de campos
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        lblNombre.setForeground(fgPanel);
        panelCampos.add(lblNombre, gbc);

        gbc.gridx = 1;
        txtNombre = new JTextField(restaurante.getNombre());
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 15));
        txtNombre.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        txtNombre.setForeground(fgPanel);
        txtNombre.setCaretColor(fgPanel);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtNombre.setPreferredSize(new Dimension(220, 36));
        panelCampos.add(txtNombre, gbc);

        // Ubicación
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setFont(new Font("Arial", Font.PLAIN, 16));
        lblUbicacion.setForeground(fgPanel);
        panelCampos.add(lblUbicacion, gbc);

        gbc.gridx = 1;
        txtUbicacion = new JTextField(restaurante.getUbicacion());
        txtUbicacion.setFont(new Font("Arial", Font.PLAIN, 15));
        txtUbicacion.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        txtUbicacion.setForeground(fgPanel);
        txtUbicacion.setCaretColor(fgPanel);
        txtUbicacion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtUbicacion.setPreferredSize(new Dimension(220, 36));
        panelCampos.add(txtUbicacion, gbc);

        // Imagen
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        lblImagen = new JLabel("Sin imagen", JLabel.CENTER);
        lblImagen.setPreferredSize(new Dimension(120, 120));
        lblImagen.setFont(new Font("Arial", Font.ITALIC, 14));
        lblImagen.setForeground(theme == ThemeManager.Theme.DARK ? new Color(120, 120, 120) : new Color(160, 160, 160));
        lblImagen.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        lblImagen.setOpaque(true);
        lblImagen.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        if (restaurante.getImagen() != null) {
            ImageIcon icon = new ImageIcon(restaurante.getImagen());
            lblImagen.setIcon(new ImageIcon(icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
            lblImagen.setText("");
        }
        panelCampos.add(lblImagen, gbc);

        // Botón cambiar imagen
        gbc.gridy++;
        btnImagen = new JButton("Cambiar Imagen");
        ThemeManager.setComponentTheme(btnImagen, theme);
        btnImagen.setFont(new Font("Arial", Font.BOLD, 14));
        setButtonEffects(btnImagen, new Color(52, 152, 219), new Color(41, 128, 185), new Color(31, 97, 141));
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
        panel.add(Box.createVerticalStrut(18));

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        Color panelBotonesBg = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK
                ? new Color(44, 62, 80)
                : new Color(220, 230, 241);
        panelBotones.setBackground(panelBotonesBg);
        panelBotones.setOpaque(false);

        btnGuardar = new JButton("Guardar");
        ThemeManager.setComponentTheme(btnGuardar, theme);
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 18));
        btnGuardar.setPreferredSize(new Dimension(170, 48));
        setButtonEffects(btnGuardar, new Color(46, 204, 113), new Color(39, 174, 96), new Color(30, 132, 73));
        btnGuardar.addActionListener(e -> guardarCambios());
        panelBotones.add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        ThemeManager.setComponentTheme(btnCancelar, theme);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 18));
        btnCancelar.setPreferredSize(new Dimension(170, 48));
        setButtonEffects(btnCancelar, new Color(231, 76, 60), new Color(192, 57, 43), new Color(155, 34, 38));
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnCancelar);

        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // Efectos visuales para los botones CRUD
    private void setButtonEffects(JButton btn, Color normal, Color hover, Color pressed) {
        btn.setBackground(normal);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(normal);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                btn.setBackground(pressed);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                if (p.x >= 0 && p.x < btn.getWidth() && p.y >= 0 && p.y < btn.getHeight()) {
                    btn.setBackground(hover);
                } else {
                    btn.setBackground(normal);
                }
            }
        });
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