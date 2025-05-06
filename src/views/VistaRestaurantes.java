package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import model.Restaurante;
import model.Usuario;
import services.TurismoService;
import utils.I18n;
import utils.UIUtils;

public class VistaRestaurantes extends JFrame {
    private static VistaRestaurantes instance;

    private JPanel panelTarjetas;
    private JButton btnAgregar, btnVolver;
    private Usuario usuario;

    public static VistaRestaurantes getInstance(Usuario usuario) {
        if (instance == null || !instance.isVisible()) {
            instance = new VistaRestaurantes(usuario);
        }
        instance.toFront();
        return instance;
    }

    public VistaRestaurantes(Usuario usuario) {
        this.usuario = usuario;
        inicializarComponentes();
        cargarRestaurantes();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void inicializarComponentes() {
        setTitle(I18n.t("titulo.restaurantes"));
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(44, 62, 80));
        JLabel lblTitulo = new JLabel(I18n.t("titulo.restaurantes"));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel de tarjetas
        panelTarjetas = new JPanel();
        panelTarjetas.setLayout(new WrapLayout(FlowLayout.LEFT, 24, 24));
        panelTarjetas.setBackground(new Color(236, 240, 241));
        JScrollPane scroll = new JScrollPane(panelTarjetas);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(236, 240, 241));

        btnAgregar = UIUtils.crearBoton(I18n.t("boton.agregar"), new Color(52, 152, 219));
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 18));
        btnAgregar.setPreferredSize(new Dimension(170, 48));
        btnAgregar.setVisible(usuario.isAdmin());
        btnAgregar.addActionListener(e -> new AgregarRestaurante().setVisible(true));
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

    private void cargarRestaurantes() {
        panelTarjetas.removeAll();
        try {
            List<Restaurante> restaurantes = TurismoService.getInstance().obtenerRestaurantes();
            for (Restaurante r : restaurantes) {
                panelTarjetas.add(crearTarjetaRestaurante(r));
            }
        } catch (Exception ex) {
            UIUtils.mostrarError(this, "Error al cargar restaurantes: " + ex.getMessage());
        }
        panelTarjetas.revalidate();
        panelTarjetas.repaint();
    }

    private JPanel crearTarjetaRestaurante(Restaurante restaurante) {
        JPanel tarjeta = new JPanel();
        tarjeta.setPreferredSize(new Dimension(320, 220));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new ThemeManager.RoundedBorder(new Color(52, 152, 219), 2, 24),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        tarjeta.setLayout(new BorderLayout(12, 0));
        tarjeta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Imagen circular o "Sin imagen"
        JLabel lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(100, 100));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        if (restaurante.getImagen() != null) {
            try {
                java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(restaurante.getImagen());
                BufferedImage original = javax.imageio.ImageIO.read(bais);
                Image img = original.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                BufferedImage circleBuffer = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();
                g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 100, 100));
                g2.drawImage(img, 0, 0, 100, 100, null);
                g2.dispose();
                lblImagen.setIcon(new ImageIcon(circleBuffer));
            } catch (Exception ex) {
                lblImagen.setIcon(crearIconoSinImagen());
            }
        } else {
            lblImagen.setIcon(crearIconoSinImagen());
        }
        tarjeta.add(lblImagen, BorderLayout.WEST);

        // Info
        JPanel panelInfo = new JPanel();
        panelInfo.setOpaque(false);
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        JLabel lblNombre = new JLabel(restaurante.getNombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 20));
        lblNombre.setForeground(new Color(44, 62, 80));
        JLabel lblUbicacion = new JLabel("Ubicación: " + restaurante.getUbicacion());
        lblUbicacion.setFont(new Font("Arial", Font.PLAIN, 15));
        lblUbicacion.setForeground(new Color(100, 100, 100));

        // Valoración con estrella
        JLabel lblValoracion = new JLabel("Valoración: " + String.format("%.1f", restaurante.getValoracion()));
        lblValoracion.setFont(new Font("Arial", Font.BOLD, 16));
        lblValoracion.setForeground(new Color(241, 196, 15));
        JLabel lblEstrella = new JLabel("★");
        lblEstrella.setFont(new Font("Segoe UI Symbol", Font.BOLD, 18));
        lblEstrella.setForeground(new Color(241, 196, 15));
        JPanel panelValoracion = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        panelValoracion.setOpaque(false);
        panelValoracion.add(lblValoracion);
        panelValoracion.add(lblEstrella);

        panelInfo.add(lblNombre);
        panelInfo.add(Box.createVerticalStrut(8));
        panelInfo.add(lblUbicacion);
        panelInfo.add(Box.createVerticalStrut(8));
        panelInfo.add(panelValoracion);

        tarjeta.add(panelInfo, BorderLayout.CENTER);

        // Evento click para mostrar detalles y botones
        tarjeta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mostrarDialogoDetalles(restaurante);
            }
        });

        return tarjeta;
    }

    // Icono circular "Sin imagen"
    private Icon crearIconoSinImagen() {
        int size = 100;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(220, 220, 220));
        g2.fillOval(0, 0, size, size);
        g2.setColor(new Color(160, 160, 160));
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(1, 1, size - 2, size - 2);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        String texto = "Sin imagen";
        int textWidth = fm.stringWidth(texto);
        int textHeight = fm.getHeight();
        g2.setColor(new Color(120, 120, 120));
        g2.drawString(texto, (size - textWidth) / 2, size / 2 + textHeight / 4);
        g2.dispose();
        return new ImageIcon(img);
    }

    // Diálogo de detalles con botones
    private void mostrarDialogoDetalles(Restaurante restaurante) {
        JDialog dialogo = new JDialog(this, restaurante.getNombre(), true);
        dialogo.setSize(420, 340);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        panel.setBackground(Color.WHITE);

        // Imagen
        JLabel lblImagen = new JLabel();
        lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblImagen.setPreferredSize(new Dimension(120, 120));
        if (restaurante.getImagen() != null) {
            try {
                java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(restaurante.getImagen());
                BufferedImage original = javax.imageio.ImageIO.read(bais);
                Image img = original.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                BufferedImage circleBuffer = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();
                g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 120, 120));
                g2.drawImage(img, 0, 0, 120, 120, null);
                g2.dispose();
                lblImagen.setIcon(new ImageIcon(circleBuffer));
            } catch (Exception ex) {
                lblImagen.setIcon(crearIconoSinImagen());
            }
        } else {
            lblImagen.setIcon(crearIconoSinImagen());
        }
        panel.add(lblImagen);
        panel.add(Box.createVerticalStrut(10));

        JLabel lblNombre = new JLabel(restaurante.getNombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 22));
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblNombre);

        JLabel lblUbicacion = new JLabel("Ubicación: " + restaurante.getUbicacion());
        lblUbicacion.setFont(new Font("Arial", Font.PLAIN, 16));
        lblUbicacion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblUbicacion);

        // Valoración con estrella
        JPanel panelValoracion = new JPanel();
        panelValoracion.setOpaque(false);
        panelValoracion.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 0));
        JLabel lblValoracion = new JLabel("Valoración: " + String.format("%.1f", restaurante.getValoracion()));
        lblValoracion.setFont(new Font("Arial", Font.BOLD, 18));
        lblValoracion.setForeground(new Color(241, 196, 15));
        JLabel lblEstrella = new JLabel("★");
        lblEstrella.setFont(new Font("Segoe UI Symbol", Font.BOLD, 20));
        lblEstrella.setForeground(new Color(241, 196, 15));
        panelValoracion.add(lblValoracion);
        panelValoracion.add(lblEstrella);
        panelValoracion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(panelValoracion);

        panel.add(Box.createVerticalStrut(18));

        // Botones de acción
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        panelBotones.setOpaque(false);

        JButton btnDetalles = new JButton(I18n.t("boton.detalles"));
        btnDetalles.setBackground(new Color(46, 204, 113));
        btnDetalles.setForeground(Color.WHITE);
        btnDetalles.setFont(new Font("Arial", Font.BOLD, 16));
        btnDetalles.setFocusPainted(false);
        btnDetalles.setContentAreaFilled(true);
        btnDetalles.setOpaque(true);
        btnDetalles.addActionListener(e -> {
            JOptionPane.showMessageDialog(dialogo,
                    restaurante.getNombre() + "\nUbicación: " + restaurante.getUbicacion() +
                            "\nValoración: " + restaurante.getValoracion(),
                    "Detalles del Restaurante", JOptionPane.INFORMATION_MESSAGE);
        });
        panelBotones.add(btnDetalles);

        JButton btnValorar = new JButton(I18n.t("boton.valorar"));
        btnValorar.setBackground(new Color(241, 196, 15));
        btnValorar.setForeground(Color.WHITE);
        btnValorar.setFont(new Font("Arial", Font.BOLD, 16));
        btnValorar.setFocusPainted(false);
        btnValorar.setContentAreaFilled(true);
        btnValorar.setOpaque(true);
        btnValorar.addActionListener(e -> new ValorarRestaurantes(restaurante.getNombre(), usuario));
        panelBotones.add(btnValorar);

        if (usuario.isAdmin()) {
            JButton btnEditar = new JButton(I18n.t("boton.editar"));
            btnEditar.setBackground(new Color(52, 152, 219));
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setFont(new Font("Arial", Font.BOLD, 16));
            btnEditar.setFocusPainted(false);
            btnEditar.setContentAreaFilled(true);
            btnEditar.setOpaque(true);
            btnEditar.addActionListener(e -> new EditarRestaurante(restaurante).setVisible(true));
            panelBotones.add(btnEditar);

            JButton btnEliminar = new JButton(I18n.t("boton.eliminar"));
            btnEliminar.setBackground(new Color(231, 76, 60));
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setFont(new Font("Arial", Font.BOLD, 16));
            btnEliminar.setFocusPainted(false);
            btnEliminar.setContentAreaFilled(true);
            btnEliminar.setOpaque(true);
            btnEliminar.addActionListener(e -> {
                eliminarRestaurante(restaurante);
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

        dialogo.add(panel, BorderLayout.CENTER);
        dialogo.setVisible(true);
    }

    private void eliminarRestaurante(Restaurante restaurante) {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar este restaurante?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION)
            return;
        try {
            boolean eliminado = TurismoService.getInstance().eliminarRestaurante(restaurante.getId());
            if (eliminado) {
                cargarRestaurantes();
                JOptionPane.showMessageDialog(this, "Restaurante eliminado.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el restaurante.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}

// Clase auxiliar para layout tipo "wrap" (tarjetas responsivas)
class WrapLayout extends FlowLayout {
    public WrapLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }
    @Override
    public Dimension preferredLayoutSize(Container target) {
        return layoutSize(target, true);
    }
    @Override
    public Dimension minimumLayoutSize(Container target) {
        Dimension minimum = layoutSize(target, false);
        minimum.width -= (getHgap() + 1);
        return minimum;
    }
    private Dimension layoutSize(Container target, boolean preferred) {
        synchronized (target.getTreeLock()) {
            int targetWidth = target.getWidth();
            if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;
            int hgap = getHgap();
            int vgap = getVgap();
            Insets insets = target.getInsets();
            int maxWidth = targetWidth - (insets.left + insets.right + hgap * 2);
            int x = 0, y = insets.top + vgap, rowHeight = 0;
            int nmembers = target.getComponentCount();
            for (int i = 0; i < nmembers; i++) {
                Component m = target.getComponent(i);
                if (!m.isVisible()) continue;
                Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                if ((x == 0) || ((x + d.width) <= maxWidth)) {
                    if (x > 0) x += hgap;
                    x += d.width;
                    rowHeight = Math.max(rowHeight, d.height);
                } else {
                    x = d.width;
                    y += vgap + rowHeight;
                    rowHeight = d.height;
                }
            }
            y += rowHeight;
            y += insets.bottom;
            return new Dimension(targetWidth, y);
        }
    }
}