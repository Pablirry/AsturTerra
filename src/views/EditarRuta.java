package views;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import model.Ruta;
import services.TurismoService;

public class EditarRuta extends JDialog {
    private JTextField txtNombre, txtPrecio;
    private JTextArea txtDescripcion;
    private JLabel lblImagen;
    private JButton btnSeleccionarImagen, btnGuardar, btnCancelar;
    private int dificultadSeleccionada = 0;
    private JLabel[] estrellas = new JLabel[5];
    private byte[] imagenBytes = null;
    private int idRuta;
    private VistaRutas rutas;

    public EditarRuta(VistaRutas rutas, Ruta ruta) {
        super(rutas, "Editar Ruta", true);
        this.rutas = rutas;
        this.idRuta = ruta.getId();
        setSize(700, 540);
        setLocationRelativeTo(rutas);
        setResizable(false);
        inicializarComponentes();
        cargarDatos(ruta);
    }

    private void inicializarComponentes() {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color bg = dark ? new Color(44, 62, 80) : Color.WHITE;
        Color fg = dark ? Color.WHITE : Color.BLACK;
        Color fieldBg = dark ? new Color(44, 62, 80) : Color.WHITE;

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // Panel de edición (izquierda)
        JPanel panelEdicion = new JPanel();
        panelEdicion.setOpaque(false);
        panelEdicion.setLayout(new BoxLayout(panelEdicion, BoxLayout.Y_AXIS));
        panelEdicion.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 24));

        JLabel lblTitulo = new JLabel("Editar Ruta");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(41, 128, 185));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(400, 2));
        separator.setForeground(new Color(41, 128, 185, 80));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Nombre
        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtNombre.setMaximumSize(new Dimension(360, 36));
        txtNombre.setBackground(fieldBg);
        txtNombre.setForeground(fg);
        txtNombre.setCaretColor(fg);
        txtNombre.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185, 90), 2, true),
            "Nombre",
            0, 0, new Font("Segoe UI", Font.PLAIN, 15), fg
        ));

        // Descripción (más grande) con scroll DENTRO del área de texto
        txtDescripcion = new JTextArea(7, 20);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setOpaque(true);
        txtDescripcion.setBackground(fieldBg);
        txtDescripcion.setForeground(fg);
        txtDescripcion.setCaretColor(fg);
        txtDescripcion.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185, 90), 2, true),
            "Descripción",
            0, 0, new Font("Segoe UI", Font.PLAIN, 15), fg
        ));

        // El JScrollPane solo para el área interna, no para el borde
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion) {
            @Override
            public void setBorder(Border border) {
                // No permitir cambiar el borde, lo mantiene el JTextArea
            }
        };
        scrollDesc.setBorder(null);
        scrollDesc.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollDesc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollDesc.setPreferredSize(new Dimension(360, 140));
        scrollDesc.setMaximumSize(new Dimension(360, 160));
        scrollDesc.getViewport().setOpaque(false);
        scrollDesc.setOpaque(false);

        // Precio
        txtPrecio = new JTextField();
        txtPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPrecio.setMaximumSize(new Dimension(360, 36));
        txtPrecio.setBackground(fieldBg);
        txtPrecio.setForeground(fg);
        txtPrecio.setCaretColor(fg);
        txtPrecio.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185, 90), 2, true),
            "Precio (€)",
            0, 0, new Font("Segoe UI", Font.PLAIN, 15), fg
        ));

        // Panel dificultad alineado a la izquierda
        JPanel panelDificultad = new JPanel();
        panelDificultad.setLayout(new BoxLayout(panelDificultad, BoxLayout.X_AXIS));
        panelDificultad.setOpaque(false);
        panelDificultad.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDificultad = new JLabel("Dificultad:");
        lblDificultad.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDificultad.setForeground(new Color(52, 152, 219));
        lblDificultad.setAlignmentY(Component.CENTER_ALIGNMENT);

        JPanel panelEstrellas = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        panelEstrellas.setOpaque(false);
        for (int i = 0; i < 5; i++) {
            estrellas[i] = new JLabel("☆");
            estrellas[i].setFont(new Font("Segoe UI Symbol", Font.BOLD, 26));
            final int estrellaIndex = i;
            estrellas[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            estrellas[i].setForeground(new Color(241, 196, 15));
            estrellas[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    dificultadSeleccionada = estrellaIndex + 1;
                    actualizarEstrellas();
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    for (int j = 0; j < 5; j++) {
                        estrellas[j].setText(j <= estrellaIndex ? "★" : "☆");
                        estrellas[j].setFont(new Font("Segoe UI Symbol", Font.BOLD, j <= estrellaIndex ? 30 : 26));
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    actualizarEstrellas();
                }
            });
            panelEstrellas.add(estrellas[i]);
        }
        panelDificultad.add(lblDificultad);
        panelDificultad.add(Box.createHorizontalStrut(10));
        panelDificultad.add(panelEstrellas);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));
        panelBotones.setOpaque(false);

        btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setBackground(new Color(52, 152, 219));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(160, 40));
        btnGuardar.setBorder(BorderFactory.createEmptyBorder());
        btnGuardar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnGuardar.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(MouseEvent e) {
                btnGuardar.setBackground(new Color(52, 152, 219));
            }
        });
        btnGuardar.addActionListener(e -> guardarCambios());

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(140, 40));
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

        // Añadir componentes al panel de edición
        panelEdicion.add(lblTitulo);
        panelEdicion.add(Box.createVerticalStrut(8));
        panelEdicion.add(separator);
        panelEdicion.add(Box.createVerticalStrut(16));
        panelEdicion.add(txtNombre);
        panelEdicion.add(Box.createVerticalStrut(10));
        panelEdicion.add(scrollDesc);
        panelEdicion.add(Box.createVerticalStrut(10));
        panelEdicion.add(txtPrecio);
        panelEdicion.add(Box.createVerticalStrut(10));
        panelEdicion.add(panelDificultad);
        panelEdicion.add(Box.createVerticalStrut(24));
        panelEdicion.add(panelBotones);

        // Panel de imagen (derecha)
        JPanel panelImagen = new JPanel();
        panelImagen.setOpaque(false);
        panelImagen.setLayout(new BoxLayout(panelImagen, BoxLayout.Y_AXIS));
        panelImagen.setPreferredSize(new Dimension(220, 1));
        panelImagen.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Sin margen derecho

        lblImagen = new JLabel("Sin imagen") {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(180, 180);
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        lblImagen.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        lblImagen.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblImagen.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // Mueve la imagen un poco a la izquierda

        btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSeleccionarImagen.setBackground(new Color(41, 128, 185));
        btnSeleccionarImagen.setForeground(Color.WHITE);
        btnSeleccionarImagen.setFocusPainted(false);
        btnSeleccionarImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSeleccionarImagen.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSeleccionarImagen.setPreferredSize(new Dimension(180, 38));
        btnSeleccionarImagen.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // Mueve el botón un poco a la izquierda
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
        panelImagen.add(Box.createVerticalStrut(18));
        panelImagen.add(btnSeleccionarImagen);
        panelImagen.add(Box.createVerticalGlue());

        // Añadir ambos paneles al mainPanel
        mainPanel.add(panelEdicion, BorderLayout.CENTER);
        mainPanel.add(panelImagen, BorderLayout.EAST);

        setContentPane(mainPanel);
    }

    private void actualizarEstrellas() {
        for (int i = 0; i < 5; i++) {
            estrellas[i].setText(i < dificultadSeleccionada ? "★" : "☆");
            estrellas[i].setFont(new Font("Segoe UI Symbol", Font.BOLD, i < dificultadSeleccionada ? 30 : 26));
        }
    }

    private void seleccionarImagen() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                imagenBytes = Files.readAllBytes(file.toPath());
                ImageIcon icon = getCircularImageIcon(imagenBytes, 160);
                lblImagen.setText("");
                lblImagen.setIcon(icon);
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

    private void cargarDatos(Ruta ruta) {
        if (ruta == null) {
            JOptionPane.showMessageDialog(this, "No se encontró la ruta.");
            dispose();
            return;
        }
        txtNombre.setText(ruta.getNombre());
        txtDescripcion.setText(ruta.getDescripcion());
        txtPrecio.setText(String.valueOf(ruta.getPrecio()));
        dificultadSeleccionada = ruta.getDificultad();
        actualizarEstrellas();
        imagenBytes = ruta.getImagen();
        if (imagenBytes != null) {
            lblImagen.setText("");
            lblImagen.setIcon(getCircularImageIcon(imagenBytes, 160));
        } else {
            lblImagen.setText("Sin imagen");
            lblImagen.setIcon(null);
        }
    }

    private void guardarCambios() {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty() || dificultadSeleccionada == 0) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos y seleccione dificultad.");
            return;
        }
        double precio;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Precio inválido.");
            return;
        }
        Ruta ruta = new Ruta(idRuta, nombre, descripcion, imagenBytes, precio, dificultadSeleccionada);
        try {
            TurismoService.getInstance().actualizarRuta(ruta);
            rutas.cargarRutas();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar cambios.");
        }
    }
}