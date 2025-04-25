package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import model.Usuario;
import services.TurismoService;
import utils.I18n;

public class MenuPrincipal extends JFrame {

    private static MenuPrincipal instance;

    private JPanel panelFondo;
    private JPanel panelRutas, panelReservas, panelRestaurantes, panelHistorial, panelChat;
    private JLabel lblTitulo, lblImagenPerfil;
    private JButton btnTema;

    private Usuario usuario;

    public MenuPrincipal(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Menú Principal - Turismo Asturias");
        setSize(705, 585);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);

        // Fondo con degradado
        panelFondo = new JPanel() {
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
        panelFondo.setBounds(0, 0, 700, 550);
        panelFondo.setLayout(null);

        // Botón de modo claro/oscuro
        btnTema = new JButton("Modo Oscuro");
        btnTema.setBounds(20, 20, 130, 30);
        btnTema.setBackground(new Color(52, 152, 219));
        btnTema.setForeground(Color.WHITE);
        btnTema.addActionListener(e -> {
            if (ThemeManager.getCurrentTheme() == ThemeManager.Theme.LIGHT) {
                ThemeManager.setTheme(ThemeManager.Theme.DARK, this);
                btnTema.setText("Modo Claro");
            } else {
                ThemeManager.setTheme(ThemeManager.Theme.LIGHT, this);
                btnTema.setText("Modo Oscuro");
            }
            panelFondo.repaint();
        });
        panelFondo.add(btnTema);

        // Título
        lblTitulo = new JLabel("Turismo Asturias", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(150, 30, 400, 40);
        panelFondo.add(lblTitulo);

        // Imagen de perfil
        lblImagenPerfil = new JLabel();
        lblImagenPerfil.setBounds(600, 10, 80, 80);
        lblImagenPerfil.setBorder(BorderFactory.createEmptyBorder());
        cargarImagenPerfil();
        panelFondo.add(lblImagenPerfil);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuIdioma = new JMenu("Idioma");
        JMenuItem itemEsp = new JMenuItem("Español");
        JMenuItem itemEng = new JMenuItem("English");

        itemEsp.addActionListener(e -> {
            I18n.setLocale(new java.util.Locale("es"));
            JOptionPane.showMessageDialog(this, "Idioma cambiado a Español");
            // Aquí puedes recargar la ventana si quieres aplicar el idioma automáticamente
        });

        // Acción para cambiar a inglés
        itemEng.addActionListener(e -> {
            I18n.setLocale(new java.util.Locale("en"));
            JOptionPane.showMessageDialog(this, "Language changed to English");
            // Aquí puedes recargar la ventana si quieres aplicar el idioma automáticamente
        });

        menuIdioma.add(itemEsp);
        menuIdioma.add(itemEng);
        menuBar.add(menuIdioma);

        JPopupMenu menuPerfil = new JPopupMenu();
        JMenuItem itemPerfil = new JMenuItem("Ver Perfil");
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión");
        menuPerfil.add(itemPerfil);
        menuPerfil.add(itemCerrarSesion);

        itemPerfil.addActionListener(e -> {
            new VentanaPerfil(this, usuario).setVisible(true);
            cargarImagenPerfil();
        });

        itemCerrarSesion.addActionListener(e -> {
            dispose();
            MenuPrincipal.instance = null;
            Login.usuarioActual = null;
            TurismoService.usuarioSesion = null;
            new Login().setVisible(true);
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                MenuPrincipal.instance = null;
            }
        });

        lblImagenPerfil.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblImagenPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                    menuPerfil.show(lblImagenPerfil, evt.getX(), evt.getY());
                }
            }
        });

        panelRutas = crearPanel(50, 100, "Rutas", "assets/rutas.png");
        panelReservas = crearPanel(370, 100, "Reservas", "assets/reserva.png");
        panelRestaurantes = crearPanel(50, 250, "Restaurantes", "assets/restaurante.png");
        panelHistorial = crearPanel(370, 250, "Historial", "assets/historial.png");
        panelChat = crearPanel(210, 400, "Soporte", "assets/chat.png");

        panelFondo.add(panelRutas);
        panelFondo.add(panelReservas);
        panelFondo.add(panelRestaurantes);
        panelFondo.add(panelHistorial);
        panelFondo.add(panelChat);

        if (usuario.getTipo().equals("admin")) {
            JButton btnSoporte = new JButton("Soporte");
            btnSoporte.setBounds(20, 60, 130, 30);
            btnSoporte.setBackground(new Color(52, 152, 219));
            btnSoporte.setForeground(Color.WHITE);
            btnSoporte.addActionListener(e -> new VistaSoporteAdmin().setVisible(true));
            panelFondo.add(btnSoporte);
        }
        agregarEventos();

        getContentPane().add(panelFondo);
        setVisible(true);
        ThemeManager.setTheme(ThemeManager.getCurrentTheme(), this);
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
                // Sombra
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 36, 36);
                // Fondo
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 36, 36);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(null);
        panel.setBounds(x, y, 250, 130);
        panel.setBackground(
                ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK ? Color.WHITE : new Color(44, 62, 80));
        panel.setBorder(new ThemeManager.RoundedBorder(ThemeManager.COLOR_SECUNDARIO, 2, 18));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        ImageIcon icon = new ImageIcon(rutaImagen);
        if (icon.getIconWidth() == -1) {
            System.err.println("No se pudo cargar la imagen: " + rutaImagen);
        } else {
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
                    panel.setBackground(new Color(52, 73, 94));
                    lblTexto.setForeground(new Color(236, 240, 241));
                } else {
                    panel.setBackground(new Color(52, 152, 219));
                    lblTexto.setForeground(Color.WHITE);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK) {
                    panel.setBackground(new Color(44, 62, 80));
                    lblTexto.setForeground(new Color(236, 240, 241));
                } else {
                    panel.setBackground(new Color(236, 240, 241));
                    lblTexto.setForeground(new Color(44, 62, 80));
                }
            }
        });

        return panel;
    }

    private void agregarEventos() {
        panelRutas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                VistaRutas.getInstance(usuario).setVisible(true);
                dispose();
            }
        });

        panelReservas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                VistaReservas.getInstance(usuario).setVisible(true);
                dispose();
            }
        });

        panelRestaurantes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                VistaRestaurantes.getInstance(usuario).setVisible(true);
                dispose();
            }
        });

        panelHistorial.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new VistaHistorial(usuario).setVisible(true);
                dispose();
            }
        });

        panelChat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new VistaChat(usuario).setVisible(true);
                dispose();
            }
        });
    }

    private void cargarImagenPerfil() {
        Image img;
        if (usuario.getImagenPerfil() != null) {
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(usuario.getImagenPerfil());
                BufferedImage bufferedImage = ImageIO.read(bais);
                img = bufferedImage.getScaledInstance(lblImagenPerfil.getWidth(), lblImagenPerfil.getHeight(),
                        Image.SCALE_SMOOTH);
            } catch (IOException e) {
                img = new ImageIcon("assets/LogoAsturTerra.png").getImage()
                        .getScaledInstance(lblImagenPerfil.getWidth(), lblImagenPerfil.getHeight(), Image.SCALE_SMOOTH);
            }
        } else {
            img = new ImageIcon("assets/LogoAsturTerra.png").getImage().getScaledInstance(lblImagenPerfil.getWidth(),
                    lblImagenPerfil.getHeight(), Image.SCALE_SMOOTH);
        }

        // Convertir la imagen a circular
        int size = Math.min(lblImagenPerfil.getWidth(), lblImagenPerfil.getHeight());
        BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();
        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
        g2.drawImage(img, 0, 0, size, size, null);
        g2.dispose();
        lblImagenPerfil.setIcon(new ImageIcon(circleBuffer));
    }
}