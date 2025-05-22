package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;
import model.Ruta;
import model.Usuario;
import services.TurismoService;
import utils.I18n;
import utils.WrapLayout;

/**
 * Vista principal para mostrar, agregar, editar y eliminar rutas turísticas.
 * Permite ver las rutas en tarjetas, ver detalles, editar y eliminar rutas.
 * @author Pablo
 */
public class VistaRutas extends JFrame {
    private final Usuario usuario;
    private JPanel panelTarjetas;

    /**
     * Crea la ventana principal de rutas para el usuario dado.
     * @param usuario Usuario autenticado
     */
    public VistaRutas(Usuario usuario) {
        this.usuario = usuario;
        setTitle(I18n.t("titulo.rutas"));
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color bg = dark ? new Color(44, 62, 80) : new Color(236, 240, 241);

        // Panel superior con título y botón menú principal a la derecha
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(44, 62, 80));

        JLabel lblTitulo = new JLabel(I18n.t("titulo.rutas"));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);

        JButton btnMenu = new JButton(I18n.t("boton.volver"));
        btnMenu.setFont(new Font("Arial", Font.BOLD, 16));
        btnMenu.setForeground(Color.WHITE);
        btnMenu.setBackground(new Color(41, 128, 185));
        btnMenu.setFocusPainted(false);
        btnMenu.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 2, true));
        btnMenu.setPreferredSize(new Dimension(140, 40));
        btnMenu.addActionListener(e -> {
            new MenuPrincipal(usuario).setVisible(true);
            dispose();
        });
        btnMenu.setOpaque(true);

        JPanel panelMenu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panelMenu.setOpaque(false);
        panelMenu.add(btnMenu);
        panelSuperior.add(panelMenu, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel tarjetas con WrapLayout centrado
        panelTarjetas = new JPanel(new WrapLayout(FlowLayout.CENTER, 24, 24));
        panelTarjetas.setBackground(bg);

        JScrollPane scroll = new JScrollPane(panelTarjetas,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(bg);
        add(scroll, BorderLayout.CENTER);

        // Panel inferior con botón agregar siempre visible y moderno
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 15));
        panelInferior.setBackground(bg);

        JButton btnAgregar = new JButton(I18n.t("boton.agregar"));
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 18));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setBackground(new Color(39, 174, 96));
        btnAgregar.setFocusPainted(false);
        btnAgregar.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96), 2, true));
        btnAgregar.setPreferredSize(new Dimension(200, 45));
        btnAgregar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAgregar.setOpaque(true);
        btnAgregar.addActionListener(e -> new AgregarRuta(this).setVisible(true));
        panelInferior.add(btnAgregar);

        add(panelInferior, BorderLayout.SOUTH);

        cargarRutas();
        setVisible(true);
    }

    /**
     * Recarga las rutas en el panel de tarjetas.
     */
    public void cargarRutas() {
        panelTarjetas.removeAll();
        try {
            List<Ruta> rutas = TurismoService.getInstance().obtenerRutas();
            if (rutas.isEmpty()) {
                JLabel lblVacio = new JLabel("No hay rutas registradas.");
                lblVacio.setFont(new Font("Arial", Font.ITALIC, 22));
                lblVacio.setForeground(Color.GRAY);
                panelTarjetas.add(lblVacio);
            } else {
                for (Ruta r : rutas) {
                    panelTarjetas.add(crearTarjetaRuta(r));
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar rutas: " + ex.getMessage());
        }
        panelTarjetas.revalidate();
        panelTarjetas.repaint();
    }

    /**
     * Crea una tarjeta visual para una ruta.
     * @param ruta Ruta a mostrar
     * @return JPanel con la información de la ruta
     */
        private JPanel crearTarjetaRuta(Ruta ruta) {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color borderColor = new Color(52, 152, 219);
        Color bgTarjeta = dark ? new Color(52, 73, 94) : Color.WHITE;
        Color fgPanel = dark ? Color.WHITE : new Color(44, 62, 80);
    
        JPanel tarjeta = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(52, 152, 219, 30));
                g2.fillRoundRect(8, 8, getWidth() - 16, getHeight() - 16, 28, 28);
            }
        };
        tarjeta.setPreferredSize(new Dimension(430, 180));
        tarjeta.setBackground(bgTarjeta);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new ThemeManager.RoundedBorder(borderColor, 2, 24),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        tarjeta.setLayout(new BorderLayout(18, 0));
        tarjeta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    
        // Imagen de la ruta (círculo)
        JLabel lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(100, 100));
        lblImagen.setHorizontalAlignment(SwingConstants.LEFT);
        if (ruta.getImagen() != null) {
            try {
                java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(ruta.getImagen());
                BufferedImage original = javax.imageio.ImageIO.read(bais);
                Image img = original.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                BufferedImage circleBuffer = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();
                g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 100, 100));
                g2.drawImage(img, 0, 0, 100, 100, null);
                g2.dispose();
                lblImagen.setIcon(new ImageIcon(circleBuffer));
            } catch (Exception ex) {
                lblImagen.setIcon(crearIconoSinImagen(dark));
            }
        } else {
            lblImagen.setIcon(crearIconoSinImagen(dark));
        }
        tarjeta.add(lblImagen, BorderLayout.WEST);
    
        // Info principal alineada a la izquierda
        JPanel panelInfo = new JPanel();
        panelInfo.setOpaque(false);
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setAlignmentY(Component.TOP_ALIGNMENT);
        panelInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
    
        JLabel lblNombre = new JLabel(ruta.getNombre());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblNombre.setForeground(new Color(41, 128, 185));
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
    
        JLabel lblPrecio = new JLabel("Precio: ");
        lblPrecio.setFont(new Font("Arial", Font.BOLD, 15));
        lblPrecio.setForeground(new Color(52, 152, 219));
        lblPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);
    
        JLabel lblPrecioValor = new JLabel(String.format("%.2f €", ruta.getPrecio()));
        lblPrecioValor.setFont(new Font("Arial", Font.BOLD, 15));
        lblPrecioValor.setForeground(new Color(230, 126, 34));
        lblPrecioValor.setAlignmentX(Component.LEFT_ALIGNMENT);
    
        JPanel panelPrecio = new JPanel();
        panelPrecio.setOpaque(false);
        panelPrecio.setLayout(new BoxLayout(panelPrecio, BoxLayout.X_AXIS));
        panelPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelPrecio.add(lblPrecio);
        panelPrecio.add(lblPrecioValor);
    
        // Dificultad (estrellas)
        JPanel panelDificultad = new JPanel();
        panelDificultad.setLayout(new BoxLayout(panelDificultad, BoxLayout.X_AXIS));
        panelDificultad.setOpaque(false);
        panelDificultad.setAlignmentX(Component.LEFT_ALIGNMENT);
    
        JLabel lblDificultad = new JLabel("Dificultad: ");
        lblDificultad.setFont(new Font("Arial", Font.BOLD, 15));
        lblDificultad.setForeground(new Color(39, 174, 96));
        lblDificultad.setAlignmentY(Component.CENTER_ALIGNMENT);
    
        JPanel panelEstrellas = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelEstrellas.setOpaque(false);
        panelEstrellas.setAlignmentY(Component.CENTER_ALIGNMENT);
        for (int i = 0; i < 5; i++) {
            JLabel estrella = new JLabel(i < ruta.getDificultad() ? "★" : "☆");
            estrella.setFont(new Font("Segoe UI Symbol", Font.BOLD, 18));
            estrella.setForeground(new Color(241, 196, 15));
            estrella.setAlignmentY(Component.CENTER_ALIGNMENT);
            panelEstrellas.add(estrella);
        }
        panelDificultad.add(lblDificultad);
        panelDificultad.add(panelEstrellas);
    
        // Descripción
        JLabel lblDescripcion = new JLabel(
                "<html><div style='width:270px;'>" + ruta.getDescripcion() + "</div></html>");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDescripcion.setForeground(dark ? new Color(200, 200, 200) : new Color(120, 120, 120));
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDescripcion.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
    
        panelInfo.add(lblNombre);
        panelInfo.add(Box.createVerticalStrut(6));
        panelInfo.add(panelPrecio);
        panelInfo.add(Box.createVerticalStrut(6));
        panelInfo.add(panelDificultad);
        panelInfo.add(lblDescripcion);
    
        // Alinear todo arriba a la izquierda
        JPanel panelInfoWrap = new JPanel();
        panelInfoWrap.setOpaque(false);
        panelInfoWrap.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelInfoWrap.add(panelInfo);
    
        tarjeta.add(panelInfoWrap, BorderLayout.CENTER);
    
        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarDetallesRuta(ruta);
            }
        });
    
        return tarjeta;
    }

    /**
     * Muestra un diálogo con los detalles completos de la ruta.
     * @param ruta Ruta a mostrar
     */
    private void mostrarDetallesRuta(Ruta ruta) {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        JDialog dialog = new JDialog(this, "Detalles de la Ruta", true);
        dialog.setSize(560, 670);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Panel principal con fondo y borde redondeado
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(dark ? new Color(44, 62, 80) : new Color(245, 245, 250));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        // Nombre con icono y sombra
        JLabel lblNombre = new JLabel("  " + ruta.getNombre());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblNombre.setForeground(new Color(41, 128, 185));
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNombre.setIcon(UIManager.getIcon("FileView.directoryIcon"));

        // Línea decorativa
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(400, 2));
        separator.setForeground(new Color(41, 128, 185, 80));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Precio destacado
        JLabel lblPrecio = new JLabel("Precio: " + ruta.getPrecio() + " €");
        lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblPrecio.setForeground(new Color(39, 174, 96));
        lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel dificultad perfectamente alineado y centrado
        JPanel panelDificultad = new JPanel();
        panelDificultad.setLayout(new BoxLayout(panelDificultad, BoxLayout.Y_AXIS));
        panelDificultad.setOpaque(false);
        panelDificultad.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel horizontal para etiqueta y estrellas, centrado y alineado verticalmente
        JPanel panelDificultadContenido = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelDificultadContenido.setOpaque(false);

        JLabel lblDificultad = new JLabel("Dificultad:");
        lblDificultad.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDificultad.setForeground(new Color(52, 152, 219));
        lblDificultad.setVerticalAlignment(SwingConstants.CENTER);

        JPanel panelEstrellas = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelEstrellas.setOpaque(false);
        for (int i = 0; i < 5; i++) {
            JLabel estrella = new JLabel(i < ruta.getDificultad() ? "★" : "☆");
            estrella.setFont(new Font("Segoe UI Symbol", Font.BOLD, 24));
            estrella.setForeground(i < ruta.getDificultad() ? new Color(241, 196, 15) : new Color(200, 200, 200));
            estrella.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
            estrella.setVerticalAlignment(SwingConstants.CENTER);
            panelEstrellas.add(estrella);
        }

        panelDificultadContenido.add(lblDificultad);
        panelDificultadContenido.add(panelEstrellas);

        panelDificultad.add(panelDificultadContenido);

        panel.add(lblNombre);
        panel.add(Box.createVerticalStrut(8));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(12));
        panel.add(lblPrecio);
        panel.add(Box.createVerticalStrut(10));
        panel.add(panelDificultad); // Dificultad centrada
        panel.add(Box.createVerticalStrut(18));

        JLabel lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(160, 160));
        lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (ruta.getImagen() != null) {
            try {
                java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(ruta.getImagen());
                BufferedImage original = javax.imageio.ImageIO.read(bais);
                Image img = original.getScaledInstance(160, 160, Image.SCALE_SMOOTH);
                BufferedImage circleBuffer = new BufferedImage(160, 160, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();
                g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 160, 160));
                g2.drawImage(img, 0, 0, 160, 160, null);
                g2.dispose();
                lblImagen.setIcon(new ImageIcon(circleBuffer));
            } catch (Exception ex) {
                lblImagen.setIcon(crearIconoSinImagen(dark));
            }
        } else {
            lblImagen.setIcon(crearIconoSinImagen(dark));
        }

        panel.add(lblImagen);
        panel.add(Box.createVerticalStrut(18));

        JTextArea txtDescripcion = new JTextArea(ruta.getDescripcion());
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setEditable(false);
        txtDescripcion.setHighlighter(null);
        txtDescripcion.setFocusable(false);
        txtDescripcion.setOpaque(true);
        txtDescripcion.setBorder(null);
        txtDescripcion.setBackground(dark ? new Color(44, 62, 80) : new Color(245, 245, 250));

        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setPreferredSize(new Dimension(420, 120));
        scrollDesc.setMaximumSize(new Dimension(420, 160));
        scrollDesc.setBorder(BorderFactory.createEmptyBorder());
        scrollDesc.getViewport().setOpaque(false);
        scrollDesc.setOpaque(false);
        scrollDesc.setBackground(new Color(0, 0, 0, 0));
        scrollDesc.getViewport().setBackground(new Color(0, 0, 0, 0));

        panel.add(scrollDesc);
        panel.add(Box.createVerticalStrut(18));

        // Botones modernos con iconos y colores
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 10));
        panelBotones.setOpaque(false);

        JButton btnEditar = new JButton("🖊️ " + I18n.t("boton.editar"));
        btnEditar.setBackground(new Color(52, 152, 219));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.setFont(new Font("Dialog", Font.BOLD, 15));
        btnEditar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEditar.setPreferredSize(new Dimension(120, 38));
        btnEditar.addActionListener(e -> {
            dialog.dispose();
            new EditarRuta(this, ruta).setVisible(true);
        });

        JButton btnEliminar = new JButton("✘ " + I18n.t("boton.eliminar"));
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setFont(new Font("Dialog", Font.BOLD, 15));
        btnEliminar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEliminar.setPreferredSize(new Dimension(130, 38));
        btnEliminar.addActionListener(e -> {
            dialog.dispose();
            eliminarRuta(ruta);
        });

        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(189, 195, 199));
        btnCerrar.setForeground(Color.DARK_GRAY);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrar.setPreferredSize(new Dimension(100, 34));
        btnCerrar.addActionListener(e -> dialog.dispose());

        JPanel panelCerrar = new JPanel();
        panelCerrar.setOpaque(false);
        panelCerrar.add(btnCerrar);

        panel.add(panelBotones);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(panelCerrar, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    /**
     * Crea un icono circular de "Sin imagen" para rutas sin imagen.
     * @param dark Si el tema es oscuro
     * @return Icono generado
     */
    private Icon crearIconoSinImagen(boolean dark) {
        int size = 100;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(dark ? new Color(80, 80, 80) : new Color(220, 220, 220));
        g2.fillOval(0, 0, size, size);
        g2.setColor(dark ? new Color(120, 120, 120) : new Color(160, 160, 160));
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(1, 1, size - 2, size - 2);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2.getFontMetrics();
        String texto = "Sin imagen";
        int textWidth = fm.stringWidth(texto);
        int textHeight = fm.getHeight();
        g2.setColor(dark ? new Color(180, 180, 180) : new Color(120, 120, 120));
        g2.drawString(texto, (size - textWidth) / 2, size / 2 + textHeight / 4);
        g2.dispose();
        return new ImageIcon(img);
    }

    /**
     * Elimina una ruta tras confirmación del usuario.
     * @param ruta Ruta a eliminar
     */
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