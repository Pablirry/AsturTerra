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
        setSize(700, 550);
        setLocationRelativeTo(padre);
        setResizable(false);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color bg = dark ? new Color(44, 62, 80) : Color.WHITE;
        Color fg = dark ? Color.WHITE : Color.BLACK;
        Color fieldBg = dark ? new Color(44, 62, 80) : Color.WHITE;

        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        mainPanel.setBackground(bg);

        JLabel lblTitulo = new JLabel("Agregar Ruta");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(41, 128, 185));
        lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.X_AXIS));

        JPanel panelCampos = new JPanel();
        panelCampos.setOpaque(false);
        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 24));

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtNombre.setMaximumSize(new Dimension(500, 36));
        txtNombre.setBackground(fieldBg);
        txtNombre.setForeground(fg);
        txtNombre.setCaretColor(fg);
        txtNombre.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185, 120), 2, true),
                "Nombre",
                0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(41, 128, 185)));

        JPanel panelDescripcion = new JPanel(new BorderLayout());
        panelDescripcion.setOpaque(false);
        panelDescripcion.setMaximumSize(new Dimension(500, 120));
        panelDescripcion.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185, 120), 2, true),
                "Descripción",
                0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(41, 128, 185)));

        txtDescripcion = new JTextArea(4, 20);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setOpaque(true);
        txtDescripcion.setBackground(fieldBg);
        txtDescripcion.setForeground(fg);
        txtDescripcion.setCaretColor(fg);
        txtDescripcion.setBorder(null);

        JScrollPane scrollDesc = new JScrollPane(txtDescripcion) {
            @Override
            public void setBorder(Border border) {
            }
        };
        scrollDesc.setBorder(null);
        scrollDesc.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollDesc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollDesc.setPreferredSize(new Dimension(400, 100));
        scrollDesc.setMaximumSize(new Dimension(400, 120));
        scrollDesc.getViewport().setOpaque(false);
        scrollDesc.setOpaque(false);

        panelDescripcion.add(scrollDesc, BorderLayout.CENTER);

        txtPrecio = new JTextField();
        txtPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPrecio.setMaximumSize(new Dimension(500, 36));
        txtPrecio.setBackground(fieldBg);
        txtPrecio.setForeground(fg);
        txtPrecio.setCaretColor(fg);
        txtPrecio.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185, 120), 2, true),
                "Precio (€)",
                0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(41, 128, 185)));

        JPanel panelDificultad = new JPanel();
        panelDificultad.setOpaque(false);
        panelDificultad.setLayout(new BoxLayout(panelDificultad, BoxLayout.X_AXIS));
        panelDificultad.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDificultad.add(new JLabel("Dificultad: "));
        panelDificultad.add(Box.createHorizontalStrut(8));

        for (int i = 0; i < 5; i++) {
            final int idx = i + 1;
            estrellas[i] = new JLabel("☆");
            estrellas[i].setFont(new Font("Segoe UI Symbol", Font.BOLD, 26));
            estrellas[i].setForeground(new Color(241, 196, 15));
            estrellas[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            estrellas[i].addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    dificultadSeleccionada = idx;
                    actualizarEstrellas();
                }
                public void mouseEntered(MouseEvent e) {
                    colorearEstrellas(idx);
                }
                public void mouseExited(MouseEvent e) {
                    actualizarEstrellas();
                }
            });
            panelDificultad.add(estrellas[i]);
        }

        panelCampos.add(Box.createVerticalStrut(8));
        panelCampos.add(txtNombre);
        panelCampos.add(Box.createVerticalStrut(10));
        panelCampos.add(panelDescripcion);
        panelCampos.add(Box.createVerticalStrut(10));
        panelCampos.add(txtPrecio);
        panelCampos.add(Box.createVerticalStrut(10));
        panelCampos.add(panelDificultad);
        panelCampos.add(Box.createVerticalGlue());

        JPanel panelImagen = new JPanel();
        panelImagen.setOpaque(false);
        panelImagen.setLayout(new BoxLayout(panelImagen, BoxLayout.Y_AXIS));
        panelImagen.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

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

        panelCentro.add(panelCampos);
        panelCentro.add(panelImagen);

        mainPanel.add(panelCentro, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 10));
        panelBotones.setOpaque(false);

        btnGuardar = new JButton("Guardar");
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
        btnGuardar.addActionListener(e -> guardarRuta());

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

    private void colorearEstrellas(int hasta) {
        for (int i = 0; i < 5; i++) {
            estrellas[i].setText(i < hasta ? "★" : "☆");
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