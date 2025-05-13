package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.awt.image.BufferedImage;
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
        setSize(820, 480);
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

        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        mainPanel.setBackground(bg);

        // Título arriba
        JLabel lblTitulo = new JLabel("Editar Ruta");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(41, 128, 185));
        lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        // Panel central con dos columnas
        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.X_AXIS));

        // Panel izquierdo: campos
        JPanel panelCampos = new JPanel();
        panelCampos.setOpaque(false);
        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 24));

        // Campo nombre
        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtNombre.setMaximumSize(new Dimension(520, 36));
        txtNombre.setBackground(fieldBg);
        txtNombre.setForeground(fg);
        txtNombre.setCaretColor(fg);
        txtNombre.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185, 120), 2, true),
            "Nombre",
            0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(41, 128, 185)
        ));

        // Campo descripción con borde fijo y scroll interno
        JPanel panelDescripcion = new JPanel(new BorderLayout());
        panelDescripcion.setOpaque(false);
        panelDescripcion.setMaximumSize(new Dimension(520, 120));
        panelDescripcion.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185, 120), 2, true),
            "Descripción",
            0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(41, 128, 185)
        ));

        txtDescripcion = new JTextArea(6, 20);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setOpaque(true);
        txtDescripcion.setBackground(fieldBg);
        txtDescripcion.setForeground(fg);
        txtDescripcion.setCaretColor(fg);
        txtDescripcion.setBorder(null);

        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setBorder(null);
        scrollDesc.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollDesc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollDesc.setPreferredSize(new Dimension(500, 100));
        scrollDesc.setMaximumSize(new Dimension(500, 120));
        scrollDesc.getViewport().setOpaque(false);
        scrollDesc.setOpaque(false);

        panelDescripcion.add(scrollDesc, BorderLayout.CENTER);

        // Campo precio
        txtPrecio = new JTextField();
        txtPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPrecio.setMaximumSize(new Dimension(520, 36));
        txtPrecio.setBackground(fieldBg);
        txtPrecio.setForeground(fg);
        txtPrecio.setCaretColor(fg);
        txtPrecio.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185, 120), 2, true),
            "Precio (€)",
            0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(41, 128, 185)
        ));

        // Panel horizontal para dificultad y estrellas alineados
        JPanel panelDificultad = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelDificultad.setOpaque(false);
        panelDificultad.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDificultad.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0)); // Más a la izquierda

        JLabel lblDificultad = new JLabel("Dificultad:");
        lblDificultad.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDificultad.setForeground(new Color(52, 152, 219));
        lblDificultad.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Estrellas
        JPanel panelEstrellas = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        panelEstrellas.setOpaque(false);
        panelEstrellas.setAlignmentY(Component.CENTER_ALIGNMENT);
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
        panelDificultad.add(panelEstrellas);

        // Añadir al panel de campos
        panelCampos.add(Box.createVerticalStrut(8));
        panelCampos.add(txtNombre);
        panelCampos.add(Box.createVerticalStrut(10));
        panelCampos.add(panelDescripcion);
        panelCampos.add(Box.createVerticalStrut(10));
        panelCampos.add(txtPrecio);
        panelCampos.add(Box.createVerticalStrut(14));
        panelCampos.add(panelDificultad);
        panelCampos.add(Box.createVerticalGlue());

        // Panel derecho: imagen y botón
        JPanel panelImagen = new JPanel();
        panelImagen.setOpaque(false);
        panelImagen.setLayout(new BoxLayout(panelImagen, BoxLayout.Y_AXIS));
        panelImagen.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));

        lblImagen = new JLabel("Sin imagen") {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(140, 140);
            }
        };
        lblImagen.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setForeground(new Color(120, 120, 120));

        btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSeleccionarImagen.setBackground(new Color(41, 128, 185));
        btnSeleccionarImagen.setForeground(Color.WHITE);
        btnSeleccionarImagen.setFocusPainted(false);
        btnSeleccionarImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSeleccionarImagen.setPreferredSize(new Dimension(160, 36));
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
        panelImagen.add(Box.createVerticalStrut(16));
        panelImagen.add(btnSeleccionarImagen);
        panelImagen.add(Box.createVerticalGlue());

        // Añadir ambos paneles al centro
        panelCentro.add(panelCampos);
        panelCentro.add(panelImagen);

        mainPanel.add(panelCentro, BorderLayout.CENTER);

        // Panel de botones abajo centrado
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 10));
        panelBotones.setOpaque(false);

        btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setBackground(new Color(52, 152, 219));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(150, 40));
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
        btnCancelar.setPreferredSize(new Dimension(120, 40));
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

    private void actualizarEstrellas() {
        for (int i = 0; i < 5; i++) {
            estrellas[i].setText(i < dificultadSeleccionada ? "★" : "☆");
            estrellas[i].setFont(new Font("Segoe UI Symbol", Font.BOLD, 26));
        }
    }

    private void seleccionarImagen() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                imagenBytes = Files.readAllBytes(file.toPath());
                ImageIcon icon = getCircularImageIcon(imagenBytes, 140);
                lblImagen.setText("");
                lblImagen.setIcon(icon);
            } catch (Exception ex) {
                imagenBytes = null;
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
            lblImagen.setIcon(getCircularImageIcon(imagenBytes, 140));
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