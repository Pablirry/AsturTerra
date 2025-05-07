package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import model.Ruta;
import model.Usuario;
import services.TurismoService;
import utils.I18n;
import utils.UIUtils;
import utils.WrapLayout;

public class VistaRutas extends JFrame {
    private static VistaRutas instance;

    private JPanel panelTarjetas;
    private JButton btnAgregar, btnVolver;
    private Usuario usuario;
    private JPanel contenedorCentral;
    private final int margenLateral = 60;

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

        contenedorCentral = new JPanel(new BorderLayout());
        contenedorCentral.setBackground(bg);
        contenedorCentral.setBorder(BorderFactory.createEmptyBorder(0, margenLateral, 0, margenLateral));

        panelTarjetas = new JPanel();
        panelTarjetas.setBackground(bg);
        panelTarjetas.setLayout(new WrapLayout(FlowLayout.LEFT, 24, 24));
        contenedorCentral.add(panelTarjetas, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(contenedorCentral,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(bg);
        add(scroll, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(bg);

        btnAgregar = new JButton(I18n.t("boton.agregar"));
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 18));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setBackground(new Color(39, 174, 96));
        btnAgregar.setFocusPainted(false);
        btnAgregar.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96), 2, true));
        btnAgregar.setPreferredSize(new Dimension(170, 48));
        btnAgregar.setVisible(usuario.isAdmin());
        btnAgregar.addActionListener(e -> new AgregarRuta(this).setVisible(true));
        btnAgregar.setOpaque(true);
        panelBotones.add(btnAgregar);

        btnVolver = new JButton(I18n.t("boton.volver"));
        btnVolver.setFont(new Font("Arial", Font.BOLD, 18));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setBackground(new Color(41, 128, 185));
        btnVolver.setFocusPainted(false);
        btnVolver.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 2, true));
        btnVolver.setPreferredSize(new Dimension(170, 48));
        btnVolver.addActionListener(e -> {
            new MenuPrincipal(usuario).setVisible(true);
            dispose();
        });
        btnVolver.setOpaque(true);
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
            for (Ruta r : rutas) {
                JPanel tarjeta = crearTarjetaRuta(r);
                addClickListenerRecursivo(tarjeta, new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        mostrarDialogoDetalles(r);
                    }
                });
                panelTarjetas.add(tarjeta);
            }
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
        Color borderColor = new Color(52, 152, 219);
        Color bgTarjeta = dark ? new Color(52, 73, 94) : Color.WHITE;
        Color fgPanel = dark ? Color.WHITE : new Color(44, 62, 80);

        JPanel tarjeta = new JPanel();
        tarjeta.setPreferredSize(new Dimension(320, 180));
        tarjeta.setMaximumSize(new Dimension(320, 180));
        tarjeta.setBackground(bgTarjeta);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new ThemeManager.RoundedBorder(borderColor, 2, 24),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        tarjeta.setLayout(new BorderLayout(16, 0));
        tarjeta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel panelInfo = new JPanel();
        panelInfo.setOpaque(false);
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel lblNombre = new JLabel(ruta.getNombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 20));
        lblNombre.setForeground(fgPanel);
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
        txtDescripcion.setOpaque(true);
        txtDescripcion.setBackground(bgTarjeta);
        txtDescripcion.setBorder(null);
        txtDescripcion.setForeground(dark ? Color.LIGHT_GRAY : Color.DARK_GRAY);
        txtDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtDescripcion.setMaximumSize(new Dimension(180, 60));

        panelInfo.add(lblNombre);
        panelInfo.add(Box.createVerticalStrut(6));
        panelInfo.add(lblPrecio);
        panelInfo.add(lblDificultad);
        panelInfo.add(Box.createVerticalStrut(6));
        panelInfo.add(txtDescripcion);

        tarjeta.add(panelInfo, BorderLayout.WEST);

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
        tarjeta.add(lblImagen, BorderLayout.EAST);

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
        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
        Color bgGlobal = theme == ThemeManager.Theme.DARK
                ? new Color(34, 45, 65)
                : new Color(235, 241, 250);
        Color fgPanel = theme == ThemeManager.Theme.DARK
                ? Color.WHITE
                : new Color(44, 62, 80);
        Color borderColor = new Color(52, 152, 219);

        int minWidth = 800;
        int minHeight = 500;
        int prefWidth = 1000;
        int prefHeight = 700;

        JDialog dialogo = new JDialog(this, ruta.getNombre(), true);
        dialogo.setSize(prefWidth, prefHeight);
        dialogo.setMinimumSize(new Dimension(minWidth, minHeight));
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());

        JPanel panelFondo = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(bgGlobal);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelFondo.setOpaque(true);

        // Panel principal con GridBagLayout para responsividad
        JPanel panel = new JPanel(new GridBagLayout()) {
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
        panel.setBorder(BorderFactory.createEmptyBorder(32, 48, 32, 48));

        // Panel info a la IZQUIERDA
        JPanel panelInfo = new JPanel();
        panelInfo.setOpaque(false);
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setAlignmentY(Component.TOP_ALIGNMENT);

        // Título y barra decorativa alineados a la izquierda
        JLabel lblNombre = new JLabel(ruta.getNombre());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblNombre.setForeground(fgPanel);
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(260, 2));
        separator.setForeground(borderColor);
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Precio y dificultad en una sola línea
        JPanel panelDatos = new JPanel();
        panelDatos.setOpaque(false);
        panelDatos.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel lblPrecio = new JLabel("Precio: $" + ruta.getPrecio() + "   ");
        lblPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        lblPrecio.setForeground(theme == ThemeManager.Theme.DARK ? Color.LIGHT_GRAY : new Color(100, 100, 100));
        JLabel lblDificultad = new JLabel("Dificultad: " + ruta.getDificultad());
        lblDificultad.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        lblDificultad.setForeground(new Color(52, 152, 219));
        panelDatos.add(lblPrecio);
        panelDatos.add(lblDificultad);
        panelDatos.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Descripción SIN borde ni fondo, scroll si es larga, alineada a la izquierda y color igual al fondo
        JTextArea txtDescripcion = new JTextArea(ruta.getDescripcion());
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setEditable(false);
        txtDescripcion.setOpaque(true);
        txtDescripcion.setBackground(bgGlobal); // Fondo igual al fondo global
        txtDescripcion.setBorder(null);
        txtDescripcion.setForeground(theme == ThemeManager.Theme.DARK ? Color.LIGHT_GRAY : Color.DARK_GRAY);
        txtDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setBorder(null);
        scrollDesc.setOpaque(false);
        scrollDesc.getViewport().setOpaque(false);
        scrollDesc.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollDesc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollDesc.setPreferredSize(new Dimension(320, 100));
        scrollDesc.setMinimumSize(new Dimension(220, 60));
        scrollDesc.setMaximumSize(new Dimension(Short.MAX_VALUE, 200));

        panelInfo.add(lblNombre);
        panelInfo.add(Box.createVerticalStrut(2));
        panelInfo.add(separator);
        panelInfo.add(Box.createVerticalStrut(10));
        panelInfo.add(panelDatos);
        panelInfo.add(Box.createVerticalStrut(18));
        panelInfo.add(scrollDesc);

        // --- Botones en columna, rectangulares y uno debajo de otro ---
        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));

        Dimension btnSize = new Dimension(220, 38);
        int btnGap = 12;

        JButton btnValorar = new JButton(I18n.t("boton.valorar"));
        btnValorar.setBackground(new Color(241, 196, 15));
        btnValorar.setForeground(Color.BLACK);
        btnValorar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnValorar.setFocusPainted(false);
        btnValorar.setContentAreaFilled(true);
        btnValorar.setOpaque(true);
        btnValorar.setMaximumSize(btnSize);
        btnValorar.setPreferredSize(btnSize);
        btnValorar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnValorar.addActionListener(e -> {
            dialogo.dispose();
            VistaValoraciones.getInstance(this, ruta.getId(), ruta.getNombre(), usuario);
        });
        panelBotones.add(btnValorar);
        panelBotones.add(Box.createVerticalStrut(btnGap));

        JButton btnReservar = new JButton("Reservar");
        btnReservar.setBackground(new Color(52, 152, 219));
        btnReservar.setForeground(Color.WHITE);
        btnReservar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnReservar.setFocusPainted(false);
        btnReservar.setContentAreaFilled(true);
        btnReservar.setOpaque(true);
        btnReservar.setMaximumSize(btnSize);
        btnReservar.setPreferredSize(btnSize);
        btnReservar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReservar.addActionListener(e -> {
            String fechaStr = JOptionPane.showInputDialog(this, "Introduce la fecha de la reserva (YYYY-MM-DD):");
            if (fechaStr == null || fechaStr.trim().isEmpty())
                return;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = null;
            try {
                fecha = sdf.parse(fechaStr.trim());
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Usa YYYY-MM-DD.");
                return;
            }
            try {
                boolean ok = TurismoService.getInstance().reservarRuta(usuario.getId(), ruta.getId(), fecha);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Reserva realizada y pendiente de confirmación.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo realizar la reserva.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al reservar: " + ex.getMessage());
            }
            dialogo.dispose();
        });
        panelBotones.add(btnReservar);
        panelBotones.add(Box.createVerticalStrut(btnGap));

        if (usuario.isAdmin()) {
            JButton btnEditar = new JButton(I18n.t("boton.editar"));
            btnEditar.setBackground(new Color(52, 152, 219));
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btnEditar.setFocusPainted(false);
            btnEditar.setContentAreaFilled(true);
            btnEditar.setOpaque(true);
            btnEditar.setMaximumSize(btnSize);
            btnEditar.setPreferredSize(btnSize);
            btnEditar.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnEditar.addActionListener(e -> {
                new EditarRuta(this, ruta).setVisible(true);
                dialogo.dispose();
            });
            panelBotones.add(btnEditar);
            panelBotones.add(Box.createVerticalStrut(btnGap));

            JButton btnEliminar = new JButton(I18n.t("boton.eliminar"));
            btnEliminar.setBackground(new Color(231, 76, 60));
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btnEliminar.setFocusPainted(false);
            btnEliminar.setContentAreaFilled(true);
            btnEliminar.setOpaque(true);
            btnEliminar.setMaximumSize(btnSize);
            btnEliminar.setPreferredSize(btnSize);
            btnEliminar.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnEliminar.addActionListener(e -> {
                eliminarRuta(ruta);
                dialogo.dispose();
            });
            panelBotones.add(btnEliminar);
            panelBotones.add(Box.createVerticalStrut(btnGap));
        }

        JButton btnCerrar = new JButton(I18n.t("boton.cancelar"));
        btnCerrar.setBackground(new Color(189, 195, 199));
        btnCerrar.setForeground(Color.BLACK);
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setContentAreaFilled(true);
        btnCerrar.setOpaque(true);
        btnCerrar.setMaximumSize(btnSize);
        btnCerrar.setPreferredSize(btnSize);
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.addActionListener(e -> dialogo.dispose());
        panelBotones.add(btnCerrar);

        JPanel panelImagen = new JPanel(new GridBagLayout());
        panelImagen.setOpaque(false);
        JLabel lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(210, 210));
        lblImagen.setMaximumSize(new Dimension(210, 210));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setVerticalAlignment(SwingConstants.CENTER);
        lblImagen.setBorder(null); // Sin borde rectangular

        if (ruta.getImagen() != null) {
            try {
                java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(ruta.getImagen());
                BufferedImage original = javax.imageio.ImageIO.read(bais);
                // Recorta la imagen en círculo
                int size = 210;
                BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
                Image img = original.getScaledInstance(size, size, Image.SCALE_SMOOTH);
                g2.drawImage(img, 0, 0, size, size, null);
                g2.setClip(null);
                g2.setStroke(new BasicStroke(4));
                g2.setColor(borderColor);
                g2.drawOval(2, 2, size - 4, size - 4);
                g2.dispose();
                lblImagen.setIcon(new ImageIcon(circleBuffer));
                lblImagen.setText("");
            } catch (Exception ex) {
                lblImagen.setText("Sin imagen");
            }
        } else {
            // Círculo gris o azul oscuro según modo
            int size = 210;
            BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
            Color fondoCirculo = dark ? new Color(52, 73, 94) : new Color(200, 200, 200);
            g2.setColor(fondoCirculo);
            g2.fillOval(0, 0, size, size);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(4));
            g2.drawOval(2, 2, size - 4, size - 4);
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            FontMetrics fm = g2.getFontMetrics();
            String texto = "Sin imagen";
            int textWidth = fm.stringWidth(texto);
            int textHeight = fm.getHeight();
            g2.setColor(new Color(120, 120, 120));
            g2.drawString(texto, (size - textWidth) / 2, size / 2 + textHeight / 4);
            g2.dispose();
            lblImagen.setIcon(new ImageIcon(img));
            lblImagen.setText("");
        }
        panelImagen.add(lblImagen);

        // --- GridBagLayout para responsividad ---
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.weightx = 0.6;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(panelInfo, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.4;
        panel.add(panelImagen, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(32, 0, 0, 0);
        panel.add(panelBotones, gbc);

        panelFondo.add(panel, new GridBagConstraints());

        dialogo.setContentPane(panelFondo);

        // Responsividad: mantener proporciones y separación al redimensionar
        dialogo.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = dialogo.getWidth();
                int h = dialogo.getHeight();
                int descWidth = Math.max(220, (int) (w * 0.38));
                int descHeight = Math.max(100, (int) (h * 0.18));
                scrollDesc.setPreferredSize(new Dimension(descWidth, descHeight));
                scrollDesc.setMaximumSize(new Dimension(descWidth, descHeight * 2));
                panelInfo.revalidate();
                panelInfo.repaint();
            }
        });

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