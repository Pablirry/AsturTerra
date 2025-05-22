package views;

import model.Restaurante;
import services.TurismoService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;

public class EditarRestaurante extends JDialog {
    private JTextField txtNombre, txtUbicacion, txtEspecialidadOtra;
    private JComboBox<String> comboEspecialidad;
    private JLabel lblImagen;
    private JTextArea txtDescripcion;
    private JButton btnSeleccionarImagen, btnGuardar, btnCancelar;
    private byte[] imagenBytes = null;
    private int idRestaurante;
    private VistaRestaurantes padre;

    public EditarRestaurante(VistaRestaurantes padre, int idRestaurante) {
        super(padre, "Editar Restaurante", true);
        this.padre = padre;
        this.idRestaurante = idRestaurante;
        setSize(570, 450);
        setLocationRelativeTo(padre);
        setResizable(false);
        inicializarComponentes();
        cargarDatos();
    }

    private void inicializarComponentes() {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color bg = dark ? new Color(44, 62, 80) : Color.WHITE;
        Color fg = dark ? Color.WHITE : Color.BLACK;
        Color fieldBg = dark ? new Color(44, 62, 80) : Color.WHITE;

        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        mainPanel.setBackground(bg);

        JLabel lblTitulo = new JLabel("Editar Restaurante");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(41, 128, 185));
        lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.X_AXIS));

        JPanel panelCampos = new JPanel();
        panelCampos.setOpaque(false);
        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 18));

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtNombre.setMaximumSize(new Dimension(260, 32));
        txtNombre.setBackground(fieldBg);
        txtNombre.setForeground(fg);
        txtNombre.setCaretColor(fg);
        txtNombre.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185, 120), 2, true),
                "Nombre",
                0, 0, new Font("Segoe UI", Font.BOLD, 13), new Color(41, 128, 185)));

        txtUbicacion = new JTextField();
        txtUbicacion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtUbicacion.setMaximumSize(new Dimension(260, 32));
        txtUbicacion.setBackground(fieldBg);
        txtUbicacion.setForeground(fg);
        txtUbicacion.setCaretColor(fg);
        txtUbicacion.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185, 120), 2, true),
                "Ubicación",
                0, 0, new Font("Segoe UI", Font.BOLD, 13), new Color(41, 128, 185)));

        comboEspecialidad = new JComboBox<>(new String[] {
                "Asturiano", "Americano", "Español", "Italiano", "Mexicano", "Otro"
        });
        comboEspecialidad.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        comboEspecialidad.setMaximumSize(new Dimension(260, 32));
        comboEspecialidad.setBackground(fieldBg);
        comboEspecialidad.setForeground(fg);
        comboEspecialidad.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185, 120), 2, true),
                "Especialidad",
                0, 0, new Font("Segoe UI", Font.BOLD, 13), new Color(41, 128, 185)));

        // Renderer para fondo personalizado
        comboEspecialidad.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setBackground(isSelected ? new Color(41, 128, 185) : fieldBg);
                c.setForeground(fg);
                if (c instanceof JComponent) {
                    ((JComponent) c).setOpaque(true);
                }
                return c;
            }
        });

        txtEspecialidadOtra = new JTextField();
        txtEspecialidadOtra.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtEspecialidadOtra.setMaximumSize(new Dimension(260, 32));
        txtEspecialidadOtra.setBackground(fieldBg);
        txtEspecialidadOtra.setForeground(fg);
        txtEspecialidadOtra.setCaretColor(fg);
        txtEspecialidadOtra.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185, 120), 2, true),
                "Especifique cuál",
                0, 0, new Font("Segoe UI", Font.BOLD, 13), new Color(41, 128, 185)));
        txtEspecialidadOtra.setVisible(false);

        comboEspecialidad.addActionListener(e -> {
            boolean esOtro = "Otro".equals(comboEspecialidad.getSelectedItem());
            txtEspecialidadOtra.setVisible(esOtro);
            if (esOtro) {
                txtEspecialidadOtra.requestFocus();
            }
            comboEspecialidad.getParent().revalidate();
            comboEspecialidad.getParent().repaint();
        });

        JPanel panelDescripcion = new JPanel(new BorderLayout());
        panelDescripcion.setOpaque(false);
        panelDescripcion.setMaximumSize(new Dimension(260, 80));
        panelDescripcion.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185, 120), 2, true),
                "Descripción",
                0, 0, new Font("Segoe UI", Font.BOLD, 13), new Color(41, 128, 185)));

        txtDescripcion = new JTextArea(3, 20);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
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
        scrollDesc.setPreferredSize(new Dimension(240, 60));
        scrollDesc.setMaximumSize(new Dimension(240, 60));
        scrollDesc.getViewport().setOpaque(false);
        scrollDesc.setOpaque(false);

        panelDescripcion.add(scrollDesc, BorderLayout.CENTER);

        panelCampos.add(Box.createVerticalStrut(8));
        panelCampos.add(txtNombre);
        panelCampos.add(Box.createVerticalStrut(10));
        panelCampos.add(txtUbicacion);
        panelCampos.add(Box.createVerticalStrut(10));
        panelCampos.add(comboEspecialidad);
        panelCampos.add(txtEspecialidadOtra);
        panelCampos.add(Box.createVerticalGlue());
        panelCampos.add(Box.createVerticalStrut(10));
        panelCampos.add(panelDescripcion);
        panelCampos.add(Box.createVerticalGlue());

        JPanel panelImagen = new JPanel();
        panelImagen.setOpaque(false);
        panelImagen.setLayout(new BoxLayout(panelImagen, BoxLayout.Y_AXIS));
        panelImagen.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        lblImagen = new JLabel("Sin imagen") {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(120, 120);
            }
        };
        lblImagen.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setForeground(new Color(120, 120, 120));

        btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSeleccionarImagen.setBackground(new Color(41, 128, 185));
        btnSeleccionarImagen.setForeground(Color.WHITE);
        btnSeleccionarImagen.setFocusPainted(false);
        btnSeleccionarImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSeleccionarImagen.setPreferredSize(new Dimension(150, 32));
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
        panelImagen.add(Box.createVerticalStrut(12));
        panelImagen.add(btnSeleccionarImagen);
        panelImagen.add(Box.createVerticalGlue());

        panelCentro.add(panelCampos);
        panelCentro.add(panelImagen);

        mainPanel.add(panelCentro, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 8));
        panelBotones.setOpaque(false);

        btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setBackground(new Color(52, 152, 219));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(150, 36));
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
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(110, 36));
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

    private void seleccionarImagen() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                imagenBytes = Files.readAllBytes(file.toPath());
                lblImagen.setText("");
                lblImagen.setIcon(getCircularImageIcon(imagenBytes, 120));
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
            BufferedImage original = javax.imageio.ImageIO.read(bais);
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

    private void cargarDatos() {
        try {
            Restaurante restaurante = TurismoService.getInstance().obtenerRestaurantePorId(idRestaurante);
            if (restaurante == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el restaurante.");
                dispose();
                return;
            }
            txtNombre.setText(restaurante.getNombre());
            txtUbicacion.setText(restaurante.getUbicacion());
            txtDescripcion.setText(restaurante.getDescripcion());

            boolean found = false;
            for (int i = 0; i < comboEspecialidad.getItemCount(); i++) {
                if (restaurante.getEspecialidad().equals(comboEspecialidad.getItemAt(i))) {
                    comboEspecialidad.setSelectedIndex(i);
                    found = true;
                    break;
                }
            }
            if (!found) {
                comboEspecialidad.setSelectedItem("Otro");
                txtEspecialidadOtra.setVisible(true);
                txtEspecialidadOtra.setText(restaurante.getEspecialidad());
            } else {
                txtEspecialidadOtra.setVisible(false);
                txtEspecialidadOtra.setText("");
            }

            imagenBytes = restaurante.getImagen();
            if (imagenBytes != null) {
                lblImagen.setText("");
                lblImagen.setIcon(getCircularImageIcon(imagenBytes, 120));
            } else {
                lblImagen.setText("Sin imagen");
                lblImagen.setIcon(null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos.");
            dispose();
        }
    }

    private void guardarCambios() {
        String nombre = txtNombre.getText().trim();
        String ubicacion = txtUbicacion.getText().trim();
        String especialidad = (String) comboEspecialidad.getSelectedItem();
        String descripcion = txtDescripcion.getText().trim();
        if (nombre.isEmpty() || ubicacion.isEmpty() || especialidad == null) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.");
            return;
        }
        if ("Otro".equals(especialidad)) {
            especialidad = txtEspecialidadOtra.getText().trim();
        }
        Restaurante restaurante = new Restaurante(idRestaurante, nombre, ubicacion, imagenBytes, especialidad, descripcion);
        try {
            TurismoService.getInstance().actualizarRestaurante(restaurante);
            padre.cargarRestaurantes();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar cambios.");
        }
    }
}