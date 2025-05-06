package views;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;
import model.Ruta;
import model.Usuario;
import services.TurismoService;
import utils.I18n;
import utils.UIUtils;

public class VistaRutas extends JFrame {
    private static VistaRutas instance;

    private JPanel panelTarjetas;
    private JButton btnAgregar, btnVolver;
    private Usuario usuario;

    public static VistaRutas getInstance(Usuario usuario) {
        if (instance == null || !instance.isVisible()) {
            instance = new VistaRutas(usuario);
        }
        instance.toFront();
        return instance;
    }

    public VistaRutas(Usuario usuario) {
        this.usuario = usuario;
        inicializarComponentes();
        cargarRutas();
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                cargarRutas();
            }
        });
    }

    private void inicializarComponentes() {
        setTitle(I18n.t("titulo.rutas"));
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color bg = dark ? new Color(44, 62, 80) : new Color(236, 240, 241);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(44, 62, 80));
        JLabel lblTitulo = new JLabel(I18n.t("titulo.rutas"));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        panelTarjetas = new JPanel();
        panelTarjetas.setLayout(new GridBagLayout());
        panelTarjetas.setBackground(bg);

        JScrollPane scroll = new JScrollPane(panelTarjetas,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(bg);
        add(scroll, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(bg);

        btnAgregar = UIUtils.crearBoton(I18n.t("boton.agregar"), new Color(52, 152, 219));
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 18));
        btnAgregar.setPreferredSize(new Dimension(170, 48));
        btnAgregar.setVisible(usuario.isAdmin());
        btnAgregar.addActionListener(e -> new AgregarRuta(this).setVisible(true));
        panelBotones.add(btnAgregar);

        btnVolver = UIUtils.crearBoton(I18n.t("boton.volver"), new Color(52, 152, 219));
        btnVolver.setFont(new Font("Arial", Font.BOLD, 18));
        btnVolver.setPreferredSize(new Dimension(170, 48));
        btnVolver.addActionListener(e -> {
            new MenuPrincipal(usuario).setVisible(true);
            dispose();
        });
        panelBotones.add(btnVolver);

        add(panelBotones, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                instance = null;
            }
        });

        setVisible(true);
    }

    public void cargarRutas() {
        panelTarjetas.removeAll();
        try {
            List<Ruta> rutas = TurismoService.getInstance().obtenerRutas();
            int tarjetaAncho = 370;
            int panelWidth = panelTarjetas.getParent().getWidth();
            int tarjetasPorFila = Math.max(1, panelWidth / tarjetaAncho);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(12, 12, 12, 12);
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.NORTHWEST;

            int col = 0, row = 0;
            for (Ruta r : rutas) {
                JPanel tarjeta = crearTarjetaRuta(r);
                addClickListenerRecursivo(tarjeta, new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        mostrarDialogoDetalles(r);
                    }
                });
                gbc.gridx = col;
                gbc.gridy = row;
                panelTarjetas.add(tarjeta, gbc);
                col++;
                if (col >= tarjetasPorFila) {
                    col = 0;
                    row++;
                }
            }
            gbc.gridx = 0;
            gbc.gridy = row + 1;
            gbc.weighty = 1.0;
            panelTarjetas.add(Box.createVerticalGlue(), gbc);

        } catch (Exception ex) {
            UIUtils.mostrarError(this, "Error al cargar rutas: " + ex.getMessage());
        }
        panelTarjetas.revalidate();
        panelTarjetas.repaint();
    }

    private void addClickListenerRecursivo(Component comp, MouseListener listener) {
        comp.addMouseListener(listener);
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                addClickListenerRecursivo(child, listener);
            }
        }
    }

    private JPanel crearTarjetaRuta(Ruta ruta) {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        JPanel tarjeta = new JPanel();
        tarjeta.setPreferredSize(new Dimension(340, 240));
        tarjeta.setBackground(dark ? new Color(52, 73, 94) : Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new ThemeManager.RoundedBorder(new Color(52, 152, 219), 2, 24),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        tarjeta.setLayout(new BorderLayout(12, 0));
        tarjeta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(120, 120));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        if (ruta.getImagen() != null) {
            try {
                java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(ruta.getImagen());
                BufferedImage original = javax.imageio.ImageIO.read(bais);
                Image img = original.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                BufferedImage circleBuffer = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();
                g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 120, 120));
                g2.drawImage(img, 0, 0, 120, 120, null);
                g2.dispose();
                lblImagen.setIcon(new ImageIcon(circleBuffer));
            } catch (Exception ex) {
                lblImagen.setIcon(crearIconoSinImagen(dark));
            }
        } else {
            lblImagen.setIcon(crearIconoSinImagen(dark));
        }
        tarjeta.add(lblImagen, BorderLayout.WEST);

        JPanel panelInfo = new JPanel();
        panelInfo.setOpaque(false);
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblNombre = new JLabel(ruta.getNombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 20));
        lblNombre.setForeground(dark ? Color.WHITE : new Color(44, 62, 80));
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPrecio = new JLabel("Precio: $" + ruta.getPrecio());
        lblPrecio.setFont(new Font("Arial", Font.PLAIN, 15));
        lblPrecio.setForeground(dark ? Color.LIGHT_GRAY : new Color(100, 100, 100));
        lblPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDificultad = new JLabel("Dificultad: " + ruta.getDificultad());
        lblDificultad.setFont(new Font("Arial", Font.PLAIN, 15));
        lblDificultad.setForeground(new Color(52, 152, 219));
        lblDificultad.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea txtDescripcion = new JTextArea(ruta.getDescripcion());
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 13));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setEditable(false);
        txtDescripcion.setOpaque(false);
        txtDescripcion.setBorder(null);
        txtDescripcion.setForeground(dark ? Color.LIGHT_GRAY : Color.DARK_GRAY);
        txtDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelInfo.add(lblNombre);
        panelInfo.add(Box.createVerticalStrut(6));
        panelInfo.add(lblPrecio);
        panelInfo.add(lblDificultad);
        panelInfo.add(Box.createVerticalStrut(6));
        panelInfo.add(txtDescripcion);

        tarjeta.add(panelInfo, BorderLayout.CENTER);

        return tarjeta;
    }

    private Icon crearIconoSinImagen(boolean dark) {
        int size = 120;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(dark ? new Color(80, 80, 80) : new Color(220, 220, 220));
        g2.fillOval(0, 0, size, size);
        g2.setColor(dark ? new Color(120, 120, 120) : new Color(160, 160, 160));
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(1, 1, size - 2, size - 2);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        String texto = "Sin imagen";
        int textWidth = fm.stringWidth(texto);
        int textHeight = fm.getHeight();
        g2.setColor(dark ? new Color(180, 180, 180) : new Color(120, 120, 120));
        g2.drawString(texto, (size - textWidth) / 2, size / 2 + textHeight / 4);
        g2.dispose();
        return new ImageIcon(img);
    }

    private void mostrarDialogoDetalles(Ruta ruta) {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        JDialog dialogo = new JDialog(this, ruta.getNombre(), true);
        dialogo.setSize(700, 520);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        panel.setBackground(dark ? new Color(44, 62, 80) : Color.WHITE);

        JLabel lblImagen = new JLabel();
        lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblImagen.setPreferredSize(new Dimension(140, 140));
        if (ruta.getImagen() != null) {
            try {
                java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(ruta.getImagen());
                BufferedImage original = javax.imageio.ImageIO.read(bais);
                Image img = original.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
                BufferedImage circleBuffer = new BufferedImage(140, 140, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();
                g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 140, 140));
                g2.drawImage(img, 0, 0, 140, 140, null);
                g2.dispose();
                lblImagen.setIcon(new ImageIcon(circleBuffer));
            } catch (Exception ex) {
                lblImagen.setIcon(crearIconoSinImagen(dark));
            }
        } else {
            lblImagen.setIcon(crearIconoSinImagen(dark));
        }
        panel.add(lblImagen);
        panel.add(Box.createVerticalStrut(10));

        JLabel lblNombre = new JLabel(ruta.getNombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 22));
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNombre.setForeground(dark ? Color.WHITE : new Color(44, 62, 80));
        panel.add(lblNombre);

        JLabel lblPrecio = new JLabel("Precio: $" + ruta.getPrecio());
        lblPrecio.setFont(new Font("Arial", Font.PLAIN, 16));
        lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPrecio.setForeground(dark ? Color.LIGHT_GRAY : new Color(44, 62, 80));
        panel.add(lblPrecio);

        JLabel lblDificultad = new JLabel("Dificultad: " + ruta.getDificultad());
        lblDificultad.setFont(new Font("Arial", Font.PLAIN, 16));
        lblDificultad.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDificultad.setForeground(new Color(52, 152, 219));
        panel.add(lblDificultad);

        // Descripción centrada
        try {
            JTextPane txtPane = new JTextPane();
            txtPane.setText(ruta.getDescripcion());
            txtPane.setFont(new Font("Arial", Font.PLAIN, 15));
            txtPane.setEditable(false);
            txtPane.setOpaque(false);
            txtPane.setForeground(dark ? Color.LIGHT_GRAY : Color.DARK_GRAY);
            txtPane.setMaximumSize(new Dimension(500, 120));
            txtPane.setMinimumSize(new Dimension(300, 60));
            txtPane.setBackground(panel.getBackground());
            txtPane.setAlignmentX(Component.CENTER_ALIGNMENT);
            StyledDocument doc = txtPane.getStyledDocument();
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
            panel.add(Box.createVerticalStrut(12));
            panel.add(txtPane);
        } catch (Exception e) {
            JTextArea txtDescripcion = new JTextArea(ruta.getDescripcion());
            txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 15));
            txtDescripcion.setLineWrap(true);
            txtDescripcion.setWrapStyleWord(true);
            txtDescripcion.setEditable(false);
            txtDescripcion.setOpaque(false);
            txtDescripcion.setBorder(null);
            txtDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
            txtDescripcion.setForeground(dark ? Color.LIGHT_GRAY : Color.DARK_GRAY);
            txtDescripcion.setMaximumSize(new Dimension(500, 120));
            txtDescripcion.setMinimumSize(new Dimension(300, 60));
            txtDescripcion.setBackground(panel.getBackground());
            panel.add(Box.createVerticalStrut(12));
            panel.add(txtDescripcion);
        }

        panel.add(Box.createVerticalStrut(36));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        panelBotones.setOpaque(false);

        JButton btnValorar = new JButton(I18n.t("boton.valorar"));
        btnValorar.setBackground(new Color(241, 196, 15));
        btnValorar.setForeground(Color.WHITE);
        btnValorar.setFont(new Font("Arial", Font.BOLD, 16));
        btnValorar.setFocusPainted(false);
        btnValorar.setContentAreaFilled(true);
        btnValorar.setOpaque(true);
        btnValorar.addActionListener(e -> {
            dialogo.dispose();
            VistaValoraciones.getInstance(this, ruta.getId(), ruta.getNombre(), usuario);
        });
        panelBotones.add(btnValorar);

        if (usuario.isAdmin()) {
            JButton btnEditar = new JButton(I18n.t("boton.editar"));
            btnEditar.setBackground(new Color(52, 152, 219));
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setFont(new Font("Arial", Font.BOLD, 16));
            btnEditar.setFocusPainted(false);
            btnEditar.setContentAreaFilled(true);
            btnEditar.setOpaque(true);
            btnEditar.addActionListener(e -> {
                new EditarRuta(this, ruta).setVisible(true);
                dialogo.dispose();
            });
            panelBotones.add(btnEditar);

            JButton btnEliminar = new JButton(I18n.t("boton.eliminar"));
            btnEliminar.setBackground(new Color(231, 76, 60));
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setFont(new Font("Arial", Font.BOLD, 16));
            btnEliminar.setFocusPainted(false);
            btnEliminar.setContentAreaFilled(true);
            btnEliminar.setOpaque(true);
            btnEliminar.addActionListener(e -> {
                eliminarRuta(ruta);
                dialogo.dispose();
            });
            panelBotones.add(btnEliminar);
        }

        JButton btnCerrar = new JButton(I18n.t("boton.cancelar"));
        btnCerrar.setBackground(new Color(189, 195, 199));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setContentAreaFilled(true);
        btnCerrar.setOpaque(true);
        btnCerrar.addActionListener(e -> dialogo.dispose());
        panelBotones.add(btnCerrar);

        panel.add(panelBotones);

        JScrollPane scrollPanel = new JScrollPane(panel);
        scrollPanel.setBorder(null);
        scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        dialogo.add(scrollPanel, BorderLayout.CENTER);

        dialogo.setVisible(true);
    }

    private void eliminarRuta(Ruta ruta) {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar esta ruta?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION)
            return;
        try {
            boolean eliminado = TurismoService.getInstance().eliminarRuta(ruta.getId());
            if (eliminado) {
                cargarRutas();
                JOptionPane.showMessageDialog(this, "Ruta eliminada.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar la ruta.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}