package views;

import model.Restaurante;
import services.TurismoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.nio.file.Files;

public class AgregarRestaurante extends JFrame {
    private JTextField txtNombre, txtUbicacion;
    private JLabel lblImagen;
    private JButton btnGuardar, btnCancelar, btnImagen;
    private File imagenPerfil;
    private int valoracionSeleccionada = 0;
    private JLabel[] estrellas = new JLabel[5];

    public AgregarRestaurante() {
        setTitle("Agregar Restaurante");
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

        JLabel lblTitulo = new JLabel("Nuevo Restaurante");
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

        JLabel lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblUbicacion.setForeground(borderColor);
        lblUbicacion.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUbicacion = new JTextField();
        txtUbicacion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtUbicacion.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        txtUbicacion.setForeground(fgPanel);
        txtUbicacion.setCaretColor(fgPanel);
        txtUbicacion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtUbicacion.setMaximumSize(new Dimension(400, 36));
        txtUbicacion.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Valoración inicial
        JLabel lblValoracion = new JLabel("Valoración inicial:");
        lblValoracion.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblValoracion.setForeground(borderColor);
        lblValoracion.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel panelEstrellas = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelEstrellas.setOpaque(false);
        for (int i = 0; i < 5; i++) {
            estrellas[i] = new JLabel("☆");
            estrellas[i].setFont(new Font("Segoe UI Symbol", Font.BOLD, 28));
            estrellas[i].setForeground(new Color(241, 196, 15));
            final int estrellaIndex = i;
            estrellas[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            estrellas[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    valoracionSeleccionada = estrellaIndex + 1;
                    actualizarEstrellas();
                }
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    resaltarEstrellas(estrellaIndex + 1);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    actualizarEstrellas();
                }
            });
            panelEstrellas.add(estrellas[i]);
        }
        panelEstrellas.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelInfo.add(lblTitulo);
        panelInfo.add(Box.createVerticalStrut(2));
        panelInfo.add(separator);
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(lblNombre);
        panelInfo.add(txtNombre);
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(lblUbicacion);
        panelInfo.add(txtUbicacion);
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(lblValoracion);
        panelInfo.add(panelEstrellas);

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

        btnImagen = new JButton("Seleccionar Imagen");
        btnImagen.setBackground(borderColor);
        btnImagen.setForeground(Color.WHITE);
        btnImagen.setFocusPainted(false);
        btnImagen.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnImagen.addActionListener(e -> seleccionarImagen());

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

        btnGuardar = new JButton("Agregar");
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

        btnGuardar.addActionListener(e -> agregarRestaurante());
        btnCancelar.addActionListener(e -> dispose());

        // Responsividad: no dejar que los cuadros y campos se hagan demasiado pequeños
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = getWidth();
                int minWidth = 900;
                int minHeight = 550;
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
            imagenPerfil = fileChooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(imagenPerfil.getAbsolutePath());
            lblImagen.setIcon(new ImageIcon(icon.getImage().getScaledInstance(210, 210, Image.SCALE_SMOOTH)));
            lblImagen.setText("");
        }
    }

    private void actualizarEstrellas() {
        for (int i = 0; i < 5; i++) {
            estrellas[i].setText(i < valoracionSeleccionada ? "★" : "☆");
        }
    }

    private void resaltarEstrellas(int hasta) {
        for (int i = 0; i < 5; i++) {
            estrellas[i].setText(i < hasta ? "★" : "☆");
        }
    }

    private void agregarRestaurante() {
        try {
            String nombre = txtNombre.getText().trim();
            String ubicacion = txtUbicacion.getText().trim();

            if (nombre.isEmpty() || ubicacion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (valoracionSeleccionada == 0) {
                JOptionPane.showMessageDialog(this, "Selecciona una valoración inicial con estrellas.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (new dao.RestauranteDAO().obtenerRestaurantePorNombre(nombre) != null) {
                JOptionPane.showMessageDialog(this, "Ya existe un restaurante con ese nombre.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            byte[] imagenBytes = null;
            if (imagenPerfil != null) {
                imagenBytes = Files.readAllBytes(imagenPerfil.toPath());
            }

            Restaurante restaurante = new Restaurante(0, nombre, ubicacion, (float) valoracionSeleccionada,
                    imagenBytes);
            boolean guardado = TurismoService.getInstance().agregarRestaurante(restaurante);

            if (guardado) {
                Restaurante restInsertado = new dao.RestauranteDAO().obtenerRestaurantePorNombre(nombre);
                if (restInsertado != null) {
                    int idUsuario = 1;
                    dao.ValoracionDAO valoracionDAO = new dao.ValoracionDAO();
                    valoracionDAO.registrarValoracionRestaurante(
                            new model.ValoracionRestaurante(0, idUsuario, restInsertado.getId(), valoracionSeleccionada,
                                    "Valoración inicial"));
                }
                JOptionPane.showMessageDialog(this, "Restaurante agregado correctamente.");
                dispose();

            } else {
                JOptionPane.showMessageDialog(this, "No se pudo agregar el restaurante.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar restaurante: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}