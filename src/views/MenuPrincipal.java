package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import model.Usuario;

public class MenuPrincipal extends JFrame {

    private static MenuPrincipal instance;

    private JPanel panelFondo;
    private JPanel panelRutas, panelReservas, panelRestaurantes, panelHistorial, panelChat;
    private JLabel lblTitulo, lblImagenPerfil;

    private Usuario usuario;

    public MenuPrincipal(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Menú Principal - Turismo Asturias");
        setSize(705, 567);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);

        // Fondo con degradado
        panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(52, 152, 219), getWidth(), getHeight(), new Color(44, 62, 80));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelFondo.setBounds(0, 0, 700, 550);
        panelFondo.setLayout(null);

        // Título
        lblTitulo = new JLabel("Turismo Asturias", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(150, 30, 400, 40);
        panelFondo.add(lblTitulo);

        // Imagen de perfil
        lblImagenPerfil = new JLabel();
        lblImagenPerfil.setBounds(600, 10, 80, 80);
        lblImagenPerfil.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        cargarImagenPerfil();
        panelFondo.add(lblImagenPerfil);

        // Creación de paneles con imágenes
        panelRutas = crearPanel(50, 100, "Rutas", "assets/rutas.png");
        panelReservas = crearPanel(370, 100, "Reservas", "assets/reserva.png");
        panelRestaurantes = crearPanel(50, 250, "Restaurantes", "assets/restaurante.png");
        panelHistorial = crearPanel(370, 250, "Historial", "assets/historial.png");
        panelChat = crearPanel(210, 400, "Soporte", "assets/chat.png");

        // Agregar paneles al fondo
        panelFondo.add(panelRutas);
        panelFondo.add(panelReservas);
        panelFondo.add(panelRestaurantes);
        panelFondo.add(panelHistorial);
        panelFondo.add(panelChat);

        // Eventos
        agregarEventos();

        getContentPane().add(panelFondo);
        setVisible(true);
    }

    public static MenuPrincipal getInstance(Usuario usuario) {
        if (instance == null) {
            instance = new MenuPrincipal(usuario);
        }
        return instance;
    }

    private JPanel crearPanel(int x, int y, String texto, String rutaImagen) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(x, y, 250, 130);
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createLineBorder(new Color(44, 62, 80), 3));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblImagen = new JLabel(new ImageIcon(rutaImagen));
        lblImagen.setBounds(80, 10, 100, 50);
        panel.add(lblImagen);

        JLabel lblTexto = new JLabel(texto, SwingConstants.CENTER);
        lblTexto.setBounds(0, 80, 250, 30);
        lblTexto.setFont(new Font("Arial", Font.BOLD, 18));
        lblTexto.setForeground(new Color(44, 62, 80));
        panel.add(lblTexto);

        // Efecto hover (cambio de color al pasar el mouse)
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(52, 152, 219));
                lblTexto.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(new Color(236, 240, 241));
                lblTexto.setForeground(new Color(44, 62, 80));
            }
        });

        return panel;
    }

    private void agregarEventos() {
        panelRutas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new VistaRutas(usuario).setVisible(true);
                dispose();
            }
        });

        panelReservas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new VistaReservas(usuario).setVisible(true);
                dispose();
            }
        });

        panelRestaurantes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new VistaRestaurantes(usuario).setVisible(true);
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
        if (usuario.getImagenPerfil() != null) {
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(usuario.getImagenPerfil());
                BufferedImage bufferedImage = ImageIO.read(bais);
                ImageIcon icon = new ImageIcon(bufferedImage.getScaledInstance(lblImagenPerfil.getWidth(), lblImagenPerfil.getHeight(), Image.SCALE_SMOOTH));
                lblImagenPerfil.setIcon(icon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}