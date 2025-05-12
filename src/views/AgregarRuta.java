package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import model.Ruta;
import services.TurismoService;

/**
 * Diálogo para agregar una nueva ruta turística.
 */
public class AgregarRuta extends JDialog {
    private JTextField txtNombre, txtPrecio;
    private JTextArea txtDescripcion;
    private JLabel lblImagen;
    private JButton btnSeleccionarImagen, btnGuardar, btnCancelar;
    private int dificultadSeleccionada = 0;
    private JLabel[] estrellas = new JLabel[5];
    private byte[] imagenBytes = null;
    private VistaRutas padre;

    public AgregarRuta(VistaRutas padre) {
        super(padre, "Agregar Ruta", true);
        this.padre = padre;
        setSize(480, 600);
        setLocationRelativeTo(padre);
        setResizable(false);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color bg = dark ? new Color(44, 62, 80) : Color.WHITE;

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JLabel lblTitulo = new JLabel("Agregar Ruta");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(41, 128, 185));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(400, 2));
        separator.setForeground(new Color(41, 128, 185, 80));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtNombre.setMaximumSize(new Dimension(360, 32));
        txtNombre.setBorder(BorderFactory.createTitledBorder("Nombre"));

        // Panel para la descripción con borde
        JPanel panelDescripcion = new JPanel(new BorderLayout());
        panelDescripcion.setOpaque(false);
        panelDescripcion.setBorder(BorderFactory.createTitledBorder("Descripción"));

        txtDescripcion = new JTextArea(3, 20);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setOpaque(true);
        txtDescripcion.setBackground(dark ? new Color(52, 73, 94) : new Color(245, 245, 250));
        txtDescripcion.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setPreferredSize(new Dimension(360, 70));
        scrollDesc.setMaximumSize(new Dimension(360, 90));
        scrollDesc.setBorder(BorderFactory.createEmptyBorder());
        scrollDesc.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        // Cambia la política para que el scroll siempre esté visible
        scrollDesc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // Personalización visual del scroll
        JScrollBar verticalBar = scrollDesc.getVerticalScrollBar();
        verticalBar.setUnitIncrement(16);
        verticalBar.setOpaque(false);
        verticalBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            private final Dimension d = new Dimension(10, 40);

            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(52, 152, 219, 220);
                this.trackColor = new Color(236, 240, 241, 120);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                button.setVisible(false);
                return button;
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                button.setVisible(false);
                return button;
            }

            @Override
            protected Dimension getMaximumThumbSize() {
                return d;
            }

            @Override
            protected Dimension getMinimumThumbSize() {
                return d;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (!c.isEnabled() || thumbBounds.width > thumbBounds.height) return;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 12, 12);
                g2.setColor(new Color(41, 128, 185, 180));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width-1, thumbBounds.height-1, 12, 12);
                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(trackColor);
                g2.fillRoundRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height, 12, 12);
                g2.dispose();
            }
        });

        panelDescripcion.add(scrollDesc, BorderLayout.CENTER);

        txtPrecio = new JTextField();
        txtPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPrecio.setMaximumSize(new Dimension(360, 32));
        txtPrecio.setBorder(BorderFactory.createTitledBorder("Precio (€)"));

        // Panel dificultad
        JPanel panelDificultad = new JPanel();
        panelDificultad.setLayout(new BoxLayout(panelDificultad, BoxLayout.X_AXIS));
        panelDificultad.setOpaque(false);
        panelDificultad.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDificultad = new JLabel("Dificultad:");
        lblDificultad.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDificultad.setForeground(new Color(52, 152, 219));
        lblDificultad.setAlignmentY(Component.CENTER_ALIGNMENT);

        JPanel panelEstrellas = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        panelEstrellas.setOpaque(false);
        for (int i = 0; i < 5; i++) {
            estrellas[i] = new JLabel("☆");
            estrellas[i].setFont(new Font("Segoe UI Symbol", Font.BOLD, 18));
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
        panelDificultad.add(Box.createHorizontalGlue());
        panelDificultad.add(lblDificultad);
        panelDificultad.add(Box.createHorizontalStrut(10));
        panelDificultad.add(panelEstrellas);
        panelDificultad.add(Box.createHorizontalGlue());

        // Imagen circular
        lblImagen = new JLabel("Sin imagen") {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(90, 90);
            }
        };
        lblImagen.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSeleccionarImagen.setBackground(new Color(41, 128, 185));
        btnSeleccionarImagen.setForeground(Color.WHITE);
        btnSeleccionarImagen.setFocusPainted(false);
        btnSeleccionarImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSeleccionarImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 10));
        panelBotones.setOpaque(false);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(52, 152, 219));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(120, 38));
        btnGuardar.addActionListener(e -> guardarRuta());

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(120, 38));
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        // Añadir componentes al panel
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(8));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(16));
        panel.add(txtNombre);
        panel.add(Box.createVerticalStrut(10));
        panel.add(panelDescripcion);
        panel.add(Box.createVerticalStrut(10));
        panel.add(txtPrecio);
        panel.add(Box.createVerticalStrut(10));
        panel.add(panelDificultad);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblImagen);
        panel.add(Box.createVerticalStrut(6));
        panel.add(btnSeleccionarImagen);
        panel.add(Box.createVerticalStrut(18));
        panel.add(panelBotones);

        setContentPane(panel);
    }

    private void actualizarEstrellas() {
        for (int i = 0; i < 5; i++)
            estrellas[i].setText(i < dificultadSeleccionada ? "★" : "☆");
    }

    private void seleccionarImagen() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                imagenBytes = Files.readAllBytes(file.toPath());
                // Mostrar la imagen seleccionada en círculo
                ImageIcon icon = getCircularImageIcon(imagenBytes, 90);
                lblImagen.setText("");
                lblImagen.setIcon(icon);
            } catch (Exception ex) {
                lblImagen.setText("Sin imagen");
                lblImagen.setIcon(null);
                JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen.");
            }
        }
    }

    // Utilidad para mostrar imagen circular
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

    private void guardarRuta() {
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
        Ruta ruta = new Ruta(0, nombre, descripcion, imagenBytes, precio, dificultadSeleccionada);
        try {
            TurismoService.getInstance().agregarRuta(ruta);
            padre.cargarRutas();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar ruta.");
        }
    }
}