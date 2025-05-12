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
        setSize(520, 670);
        setLocationRelativeTo(padre);
        setResizable(false);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color bg = dark ? new Color(44, 62, 80) : Color.WHITE;
        Color fg = dark ? Color.WHITE : Color.BLACK;
        Color fieldBg = dark ? new Color(44, 62, 80) : Color.WHITE;

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
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(41, 128, 185));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(400, 2));
        separator.setForeground(new Color(41, 128, 185, 80));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        // Imagen circular
        lblImagen = new JLabel("Sin imagen") {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(100, 100);
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
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
        btnSeleccionarImagen.setPreferredSize(new Dimension(180, 38));
        btnSeleccionarImagen.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnSeleccionarImagen.setBackground(new Color(31, 97, 141));
            }
            public void mouseExited(MouseEvent e) {
                btnSeleccionarImagen.setBackground(new Color(41, 128, 185));
            }
        });
        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());

        // Botones modernos
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 10));
        panelBotones.setOpaque(false);

        btnGuardar = new JButton("Guardar");
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
        btnGuardar.addActionListener(e -> guardarRuta());

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

        // Añadir componentes al panel
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(8));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(16));
        panel.add(txtNombre);
        panel.add(Box.createVerticalStrut(10));
        panel.add(scrollDesc);
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