package views;

import model.Evento;
import services.TurismoService;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;

public class EditarEvento extends JDialog {
    private JTextField txtNombre, txtUbicacion, txtPrecio, txtTipoOtro;
    private JTextArea txtDescripcion;
    private JLabel lblImagen;
    private JButton btnSeleccionarImagen, btnGuardar, btnCancelar;
    private byte[] imagenBytes = null;
    private int idEvento;
    private VistaEventos padre;

    private JComboBox<String> comboTipo;

    public EditarEvento(VistaEventos padre, Evento evento) {
        super(padre, "Editar Evento", true);
        this.padre = padre;
        this.idEvento = evento.getId();
        setSize(700, 680);
        setLocationRelativeTo(padre);
        setResizable(false);
        inicializarComponentes();
        cargarDatos(evento);
    }

    private void inicializarComponentes() {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color bg = dark ? new Color(44, 62, 80) : Color.WHITE;
        Color fg = dark ? Color.WHITE : Color.BLACK;
        Color borderColor = new Color(41, 128, 185, 120);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        mainPanel.setBackground(bg);

        JLabel lblTitulo = new JLabel("Editar Evento");
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
        txtNombre.setBackground(bg);
        txtNombre.setForeground(fg);
        txtNombre.setCaretColor(fg);
        txtNombre.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor, 2, true),
                "Nombre", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(41, 128, 185)));

        txtDescripcion = new JTextArea(4, 20);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBackground(bg);
        txtDescripcion.setForeground(fg);
        txtDescripcion.setCaretColor(fg);
        txtDescripcion.setBorder(null);

        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor, 2, true),
                "Descripción", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(41, 128, 185)));
        scrollDesc.setMaximumSize(new Dimension(500, 120));
        scrollDesc.getViewport().setBackground(bg);
        scrollDesc.setBackground(bg);
        scrollDesc.getVerticalScrollBar().setBackground(bg);
        scrollDesc.getHorizontalScrollBar().setBackground(bg);

        txtUbicacion = new JTextField();
        txtUbicacion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtUbicacion.setMaximumSize(new Dimension(500, 36));
        txtUbicacion.setBackground(bg);
        txtUbicacion.setForeground(fg);
        txtUbicacion.setCaretColor(fg);
        txtUbicacion.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor, 2, true),
                "Ubicación", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(41, 128, 185)));

        comboTipo = new JComboBox<>(new String[] { "Ocio", "Religioso", "Cultural", "Otro" });
        comboTipo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        comboTipo.setMaximumSize(new Dimension(260, 32));
        comboTipo.setBackground(bg);
        comboTipo.setForeground(fg);
        comboTipo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185, 120), 2, true),
                "Tipo",
                0, 0, new Font("Segoe UI", Font.BOLD, 13), new Color(41, 128, 185)));

        comboTipo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setBackground(isSelected ? new Color(41, 128, 185) : bg);
                c.setForeground(fg);
                if (c instanceof JComponent) {
                    ((JComponent) c).setOpaque(true);
                }
                return c;
            }
        });

        JPanel panelTipoOtro = new JPanel();
        panelTipoOtro.setOpaque(false);
        panelTipoOtro.setLayout(new BoxLayout(panelTipoOtro, BoxLayout.Y_AXIS));
        panelTipoOtro.setMaximumSize(new Dimension(260, 40));

        txtTipoOtro = new JTextField();
        txtTipoOtro.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtTipoOtro.setMaximumSize(new Dimension(260, 45));
        txtTipoOtro.setBackground(bg);
        txtTipoOtro.setForeground(fg);
        txtTipoOtro.setCaretColor(fg);
        txtTipoOtro.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(41, 128, 185, 120), 2, true),
                "Especifique cuál",
                0, 0, new Font("Segoe UI", Font.BOLD, 13), new Color(41, 128, 185)));
        txtTipoOtro.setVisible(false);

        panelTipoOtro.add(txtTipoOtro);

        comboTipo.addActionListener(e -> {
            boolean esOtro = "Otro".equals(comboTipo.getSelectedItem());
            txtTipoOtro.setVisible(esOtro);
            if (esOtro) {
                txtTipoOtro.requestFocus();
            }
            panelTipoOtro.revalidate();
            panelTipoOtro.repaint();
        });

        txtPrecio = new JTextField();
        txtPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPrecio.setMaximumSize(new Dimension(500, 36));
        txtPrecio.setBackground(bg);
        txtPrecio.setForeground(fg);
        txtPrecio.setCaretColor(fg);
        txtPrecio.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(borderColor, 2, true),
                "Precio (€)", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(41, 128, 185)));

        panelCampos.add(Box.createVerticalStrut(8));
        panelCampos.add(txtNombre);
        panelCampos.add(Box.createVerticalStrut(10));
        panelCampos.add(scrollDesc);
        panelCampos.add(Box.createVerticalStrut(10));
        panelCampos.add(txtUbicacion);
        panelCampos.add(Box.createVerticalStrut(10));
        panelCampos.add(comboTipo);
        panelCampos.add(panelTipoOtro);
        panelCampos.add(Box.createVerticalStrut(10));
        panelCampos.add(txtPrecio);
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
        lblImagen.setBackground(bg);
        lblImagen.setOpaque(true);

        btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSeleccionarImagen.setBackground(new Color(41, 128, 185));
        btnSeleccionarImagen.setForeground(Color.WHITE);
        btnSeleccionarImagen.setFocusPainted(false);
        btnSeleccionarImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSeleccionarImagen.setPreferredSize(new Dimension(160, 36));
        btnSeleccionarImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
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

        btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setBackground(new Color(52, 152, 219));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(170, 40));
        btnGuardar.addActionListener(e -> guardarCambios());

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(120, 40));
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
                setCircularImage(imagenBytes);
            } catch (Exception ex) {
                imagenBytes = null;
                lblImagen.setText("Sin imagen");
                lblImagen.setIcon(null);
                JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen.");
            }
        }
    }

    private void setCircularImage(byte[] imgBytes) {
        try {
            java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(imgBytes);
            BufferedImage original = javax.imageio.ImageIO.read(bais);
            Image img = original.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
            BufferedImage circleBuffer = new BufferedImage(140, 140, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circleBuffer.createGraphics();
            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 140, 140));
            g2.drawImage(img, 0, 0, 140, 140, null);
            g2.dispose();
            lblImagen.setText("");
            lblImagen.setIcon(new ImageIcon(circleBuffer));
        } catch (Exception e) {
            lblImagen.setText("Sin imagen");
            lblImagen.setIcon(null);
        }
    }

    private void cargarDatos(Evento evento) {
        txtNombre.setText(evento.getNombre());
        txtDescripcion.setText(evento.getDescripcion());
        txtUbicacion.setText(evento.getUbicacion());
        String tipo = evento.getTipo();
        if ("Ocio".equalsIgnoreCase(tipo)) {
            comboTipo.setSelectedItem("Ocio");
            txtTipoOtro.setVisible(false);
        } else if ("Religioso".equalsIgnoreCase(tipo)) {
            comboTipo.setSelectedItem("Religioso");
            txtTipoOtro.setVisible(false);
        } else if ("Cultural".equalsIgnoreCase(tipo)) {
            comboTipo.setSelectedItem("Cultural");
            txtTipoOtro.setVisible(false);
        } else {
            comboTipo.setSelectedItem("Otro");
            txtTipoOtro.setVisible(true);
            txtTipoOtro.setText(tipo);
        }
        txtPrecio.setText(String.valueOf(evento.getPrecio()));
        imagenBytes = evento.getImagen();
        if (imagenBytes != null) {
            setCircularImage(imagenBytes);
        } else {
            lblImagen.setText("Sin imagen");
            lblImagen.setIcon(null);
        }
    }

    private void guardarCambios() {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String ubicacion = txtUbicacion.getText().trim();
        String tipo;
        if ("Otro".equals(comboTipo.getSelectedItem())) {
            tipo = txtTipoOtro.getText().trim();
        } else {
            tipo = (String) comboTipo.getSelectedItem();
        }
        String precioStr = txtPrecio.getText().trim();
        if (nombre.isEmpty() || descripcion.isEmpty() || ubicacion.isEmpty() || tipo.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.");
            return;
        }
        double precio;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Precio inválido.");
            return;
        }
        Evento evento = new Evento(idEvento, nombre, descripcion, ubicacion, tipo, precio, imagenBytes);
        try {
            TurismoService.getInstance().actualizarEvento(evento);
            padre.cargarEventos();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar cambios.");
        }
    }
}