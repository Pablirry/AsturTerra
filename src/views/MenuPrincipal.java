package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Locale;

import javax.imageio.ImageIO;
import model.Usuario;
import utils.I18n;
import utils.UIUtils;

public class MenuPrincipal extends JFrame {

    private static MenuPrincipal instance;


    private JPanel panelFondo;
    private JPanel panelRutas, panelReservas, panelRestaurantes, panelSoporte;
    private JLabel lblImagenPerfil;
    private JLabel lblRutas, lblReservas, lblRestaurantes, lblSoporte;
    private JButton btnTema;
    private Usuario usuario;

    public MenuPrincipal(Usuario usuario) {
        setTitle(I18n.t("titulo.menu.principal") + " - " + I18n.t("app.nombre"));
        setMinimumSize(new Dimension(900, 700));
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Fondo con degradado
        panelFondo = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                if (ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK) {
                    g.setColor(new Color(44, 62, 80));
                    g.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    Graphics2D g2d = (Graphics2D) g;
                    GradientPaint gradient = new GradientPaint(0, 0, new Color(52, 152, 219), getWidth(), getHeight(),
                            new Color(44, 62, 80));
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panelFondo.setLayout(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuIdioma = new JMenu(I18n.t("menu.idioma"));
        JMenuItem itemEsp = new JMenuItem(I18n.t("menu.idioma.es"));
        JMenuItem itemEng = new JMenuItem(I18n.t("menu.idioma.en"));

        itemEsp.addActionListener(e -> {
            Locale actual = Locale.getDefault();
            if (!actual.getLanguage().equals("es")) {
                I18n.setLocale(new Locale("es"));
                Locale.setDefault(new Locale("es"));
                actualizarTextos();
            }
        });
        itemEng.addActionListener(e -> {
            Locale actual = Locale.getDefault();
            if (!actual.getLanguage().equals("en")) {
                I18n.setLocale(new Locale("en"));
                Locale.setDefault(new Locale("en"));
                actualizarTextos();
            }
        });

        menuIdioma.add(itemEsp);
        menuIdioma.add(itemEng);
        menuBar.add(menuIdioma);
        setJMenuBar(menuBar);

        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color colorBotonFondo = dark ? new Color(44, 62, 80) : new Color(52, 152, 219);

        btnTema = UIUtils.crearBotonRedondeado(
                dark ? I18n.t("boton.modo.claro") : I18n.t("boton.modo.oscuro"),
                colorBotonFondo,
                18);
        btnTema.setBounds(20, 20, 130, 36);
        btnTema.setFont(new Font("Arial", Font.BOLD, 16));
        btnTema.setPreferredSize(new Dimension(130, 36));
        btnTema.setForeground(Color.WHITE);
        btnTema.setBackground(colorBotonFondo);
        btnTema.setText(dark ? I18n.t("boton.modo.claro") : I18n.t("boton.modo.oscuro"));
        for (MouseListener ml : btnTema.getMouseListeners())
            btnTema.removeMouseListener(ml);
        btnTema.addActionListener(e -> {
            boolean esClaro = ThemeManager.getCurrentTheme() == ThemeManager.Theme.LIGHT;
            ThemeManager.setTheme(esClaro ? ThemeManager.Theme.DARK : ThemeManager.Theme.LIGHT, this);
            boolean darkNow = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
            Color nuevoColor = darkNow ? new Color(44, 62, 80) : new Color(52, 152, 219);
            btnTema.setText(darkNow ? I18n.t("boton.modo.claro") : I18n.t("boton.modo.oscuro"));
            btnTema.setBackground(nuevoColor);
            panelFondo.setBackground(nuevoColor);
            panelFondo.repaint();
        });
        panelFondo.add(btnTema);

        JLabel lblLogo = new JLabel();
        lblLogo.setOpaque(false);
        ImageIcon iconLogo = new ImageIcon("assets/LogoAsturTerra.png");
        int maxAncho = 500;
        int maxAlto = 200;
        int anchoLogo = iconLogo.getIconWidth();
        int altoLogo = iconLogo.getIconHeight();
        if (anchoLogo > maxAncho || altoLogo > maxAlto) {
            double escala = Math.min((double) maxAncho / anchoLogo, (double) maxAlto / altoLogo);
            anchoLogo = (int) (anchoLogo * escala);
            altoLogo = (int) (altoLogo * escala);
            Image imgEscalada = iconLogo.getImage().getScaledInstance(anchoLogo, altoLogo, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(imgEscalada));
        } else {
            lblLogo.setIcon(iconLogo);
        }
        lblLogo.setBounds(getWidth() / 2 - anchoLogo / 2, 60, anchoLogo, altoLogo);
        lblLogo.setVisible(true);
        panelFondo.add(lblLogo);

        // Imagen de perfil (solo decorativa, no editable)
        lblImagenPerfil = new JLabel();
        lblImagenPerfil.setBounds(600, 10, 80, 80);
        lblImagenPerfil.setBorder(BorderFactory.createEmptyBorder());
        cargarImagenPerfil();
        panelFondo.add(lblImagenPerfil);

        // Paneles de menú y labels internos
        panelRutas = crearPanel(0, 0, I18n.t("titulo.rutas"), "assets/rutas.png");
        lblRutas = getPanelLabel(panelRutas);

        panelReservas = crearPanel(0, 0, I18n.t("titulo.reservas"), "assets/reserva.png");
        lblReservas = getPanelLabel(panelReservas);

        panelRestaurantes = crearPanel(0, 0, I18n.t("titulo.restaurantes"), "assets/restaurante.png");
        lblRestaurantes = getPanelLabel(panelRestaurantes);

        panelSoporte = crearPanel(0, 0, I18n.t("boton.soporte"), "assets/chat.png");
        lblSoporte = getPanelLabel(panelSoporte);

        panelFondo.add(panelRutas);
        panelFondo.add(panelReservas);
        panelFondo.add(panelRestaurantes);
        panelFondo.add(panelSoporte);

        agregarEventos();

        getContentPane().add(panelFondo, BorderLayout.CENTER);
        setVisible(true);
        ThemeManager.setTheme(ThemeManager.getCurrentTheme(), this);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarComponentes();
            }
        });
        ajustarComponentes();
    }

    public static MenuPrincipal getInstance(Usuario usuario) {
        if (instance == null || instance.usuario == null || instance.usuario.getId() != usuario.getId()) {
            if (instance != null) {
                instance.dispose();
            }
            instance = new MenuPrincipal(usuario);
        }
        return instance;
    }

    private JPanel crearPanel(int x, int y, String texto, String rutaImagen) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 36, 36);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 36, 36);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(null);
        panel.setBounds(x, y, 250, 130);
        panel.setBackground(
                ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK ? new Color(52, 73, 94)
                        : new Color(236, 240, 241));
        panel.setBorder(new ThemeManager.RoundedBorder(ThemeManager.COLOR_SECUNDARIO, 2, 18));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        ImageIcon icon = new ImageIcon(rutaImagen);
        if (icon.getIconWidth() != -1) {
            JLabel lblImagen = new JLabel(icon);
            lblImagen.setBounds(80, 10, 100, 50);
            panel.add(lblImagen);
        }

        JLabel lblTexto = new JLabel(texto, SwingConstants.CENTER);
        lblTexto.setBounds(0, 80, 250, 30);
        lblTexto.setFont(new Font("Arial", Font.BOLD, 18));
        lblTexto.setForeground(new Color(44, 62, 80));
        panel.add(lblTexto);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK) {
                    panel.setBackground(new Color(41, 128, 185));
                    lblTexto.setForeground(Color.WHITE);
                } else {
                    panel.setBackground(new Color(52, 152, 219));
                    lblTexto.setForeground(Color.WHITE);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK) {
                    panel.setBackground(new Color(52, 73, 94));
                    lblTexto.setForeground(new Color(236, 240, 241));
                } else {
                    panel.setBackground(new Color(236, 240, 241));
                    lblTexto.setForeground(new Color(44, 62, 80));
                }
            }
        });

        return panel;
    }

    // Devuelve el JLabel de texto principal de un panel
    private JLabel getPanelLabel(JPanel panel) {
        for (Component c : panel.getComponents()) {
            if (c instanceof JLabel && ((JLabel) c).getFont().getSize() == 18) {
                return (JLabel) c;
            }
        }
        return null;
    }

    private void agregarEventos() {
        panelRutas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new VistaRutas(usuario).setVisible(true);
            }
        });

        panelReservas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new VistaReservas(usuario).setVisible(true);
            }
        });

        panelRestaurantes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new VistaRestaurantes(usuario).setVisible(true);
            }
        });

        panelSoporte.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new VistaSoporteAdmin().setVisible(true);
            }
        });
    }

    private void ajustarComponentes() {
        int w = panelFondo.getWidth();
        int h = panelFondo.getHeight();

        int panelWidth = 270;
        int panelHeight = 140;
        int sepX = (w - 2 * panelWidth) / 3;
        int sepY = 40;
        int top = h / 5;

        panelRutas.setBounds(sepX, top, panelWidth, panelHeight);
        panelReservas.setBounds(2 * sepX + panelWidth, top, panelWidth, panelHeight);

        panelRestaurantes.setBounds(sepX, top + panelHeight + sepY, panelWidth, panelHeight);
        panelSoporte.setBounds(2 * sepX + panelWidth, top + panelHeight + sepY, panelWidth, panelHeight);

        for (Component c : panelFondo.getComponents()) {
            if (c instanceof JLabel && ((JLabel) c).getIcon() != null) {
                int anchoLogo = ((JLabel) c).getIcon().getIconWidth();
                int altoLogo = ((JLabel) c).getIcon().getIconHeight();
                c.setBounds(w / 2 - anchoLogo / 2, 60, anchoLogo, altoLogo);
            }
        }
        lblImagenPerfil.setBounds(w - 100, 10, 80, 80);
    }

    private void cargarImagenPerfil() {
        Image img = null;
        try {
            if (usuario.getImagenPerfil() != null) {
                ByteArrayInputStream bais = new ByteArrayInputStream(usuario.getImagenPerfil());
                BufferedImage bufferedImage = ImageIO.read(bais);
                img = bufferedImage.getScaledInstance(lblImagenPerfil.getWidth(), lblImagenPerfil.getHeight(),
                        Image.SCALE_SMOOTH);
            } else {
                java.net.URL url = getClass().getClassLoader().getResource("assets/usuario.png");
                if (url != null) {
                    img = new ImageIcon(url).getImage().getScaledInstance(lblImagenPerfil.getWidth(),
                            lblImagenPerfil.getHeight(), Image.SCALE_SMOOTH);
                } else {
                    img = new ImageIcon("assets/usuario.png").getImage().getScaledInstance(lblImagenPerfil.getWidth(),
                            lblImagenPerfil.getHeight(), Image.SCALE_SMOOTH);
                }
            }
        } catch (Exception e) {
            java.net.URL url = getClass().getClassLoader().getResource("assets/usuario.png");
            if (url != null) {
                img = new ImageIcon(url).getImage().getScaledInstance(lblImagenPerfil.getWidth(),
                        lblImagenPerfil.getHeight(), Image.SCALE_SMOOTH);
            } else {
                img = new BufferedImage(lblImagenPerfil.getWidth(), lblImagenPerfil.getHeight(), BufferedImage.TYPE_INT_ARGB);
            }
        }

        int size = Math.min(lblImagenPerfil.getWidth(), lblImagenPerfil.getHeight());
        BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();
        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
        g2.drawImage(img, 0, 0, size, size, null);
        g2.dispose();
        lblImagenPerfil.setIcon(new ImageIcon(circleBuffer));
    }

    private void actualizarTextos() {
        setTitle(I18n.t("titulo.menu.principal") + " - " + I18n.t("app.nombre"));
        JMenuBar menuBar = getJMenuBar();
        if (menuBar != null && menuBar.getMenuCount() > 0) {
            JMenu menuIdioma = menuBar.getMenu(0);
            menuIdioma.setText(I18n.t("menu.idioma"));
            menuIdioma.getItem(0).setText(I18n.t("menu.idioma.es"));
            menuIdioma.getItem(1).setText(I18n.t("menu.idioma.en"));
        }
        btnTema.setText(ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK
                ? I18n.t("boton.modo.claro")
                : I18n.t("boton.modo.oscuro"));
        panelRutas.setToolTipText(I18n.t("titulo.rutas"));
        lblRutas.setText(I18n.t("titulo.rutas"));
        panelReservas.setToolTipText(I18n.t("titulo.reservas"));
        lblReservas.setText(I18n.t("titulo.reservas"));
        panelRestaurantes.setToolTipText(I18n.t("titulo.restaurantes"));
        lblRestaurantes.setText(I18n.t("titulo.restaurantes"));
        panelSoporte.setToolTipText(I18n.t("boton.soporte"));
        lblSoporte.setText(I18n.t("boton.soporte"));
        repaint();
    }
}