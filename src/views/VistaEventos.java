package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;
import model.Evento;
import model.Usuario;
import services.TurismoService;
import utils.I18n;

public class VistaEventos extends JFrame {
    private final Usuario usuario;
    private JPanel panelTarjetas;

    public VistaEventos(Usuario usuario) {
        this.usuario = usuario;
        setTitle(I18n.t("titulo.eventos"));
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color bg = dark ? new Color(44, 62, 80) : new Color(236, 240, 241);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(44, 62, 80));

        JLabel lblTitulo = new JLabel(I18n.t("titulo.eventos"));
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

        panelTarjetas = new JPanel(new utils.WrapLayout(FlowLayout.CENTER, 24, 24));
        panelTarjetas.setBackground(bg);

        JScrollPane scroll = new JScrollPane(panelTarjetas,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(bg);
        add(scroll, BorderLayout.CENTER);

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
        btnAgregar.addActionListener(e -> new AgregarEvento(this).setVisible(true));
        panelInferior.add(btnAgregar);

        add(panelInferior, BorderLayout.SOUTH);

        cargarEventos();
        setVisible(true);
    }

    public void cargarEventos() {
        panelTarjetas.removeAll();
        try {
            List<Evento> eventos = TurismoService.getInstance().obtenerEventos();
            if (eventos.isEmpty()) {
                JLabel lblVacio = new JLabel(I18n.t("mensaje.vacio.eventos"));
                lblVacio.setFont(new Font("Arial", Font.ITALIC, 22));
                lblVacio.setForeground(Color.GRAY);
                panelTarjetas.add(lblVacio);
            } else {
                for (Evento e : eventos) {
                    panelTarjetas.add(crearTarjetaEvento(e));
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, I18n.t("mensaje.error.cargar.eventos") + ex.getMessage());
        }
        panelTarjetas.revalidate();
        panelTarjetas.repaint();
    }

    private JPanel crearTarjetaEvento(Evento evento) {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color borderColor = new Color(52, 152, 219);
        Color bgTarjeta = dark ? new Color(52, 73, 94) : Color.WHITE;

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

        JLabel lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(100, 100));
        lblImagen.setHorizontalAlignment(SwingConstants.LEFT);
        if (evento.getImagen() != null) {
            try {
                java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(evento.getImagen());
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

        JPanel panelInfo = new JPanel();
        panelInfo.setOpaque(false);
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setAlignmentY(Component.TOP_ALIGNMENT);
        panelInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblNombre = new JLabel(evento.getNombre());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblNombre.setForeground(new Color(41, 128, 185));
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTipo = new JLabel(I18n.t("label.tipo") + ": " + evento.getTipo());
        lblTipo.setFont(new Font("Dialog", Font.BOLD, 15));
        lblTipo.setForeground(new Color(39, 174, 96));
        lblTipo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblUbicacion = new JLabel(I18n.t("label.ubicacion") + ": " + evento.getUbicacion());
        lblUbicacion.setFont(new Font("Dialog", Font.PLAIN, 15));
        lblUbicacion.setForeground(dark ? Color.LIGHT_GRAY : new Color(100, 100, 100));
        lblUbicacion.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPrecio = new JLabel("Precio: ");
        lblPrecio.setFont(new Font("Dialog", Font.BOLD, 15));
        lblPrecio.setForeground(new Color(52, 152, 219));
        lblPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPrecioValor = new JLabel(String.format("%.2f €", evento.getPrecio()));
        lblPrecioValor.setFont(new Font("Dialog", Font.BOLD, 15));
        lblPrecioValor.setForeground(new Color(230, 126, 34));
        lblPrecioValor.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel panelPrecio = new JPanel();
        panelPrecio.setOpaque(false);
        panelPrecio.setLayout(new BoxLayout(panelPrecio, BoxLayout.X_AXIS));
        panelPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelPrecio.add(lblPrecio);
        panelPrecio.add(lblPrecioValor);

        JLabel lblDescripcion = new JLabel(
                "<html><div style='width:270px;'>" + evento.getDescripcion() + "</div></html>");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDescripcion.setForeground(dark ? new Color(200, 200, 200) : new Color(120, 120, 120));
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDescripcion.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        panelInfo.add(lblNombre);
        panelInfo.add(Box.createVerticalStrut(6));
        panelInfo.add(lblTipo);
        panelInfo.add(Box.createVerticalStrut(6));
        panelInfo.add(lblUbicacion);
        panelInfo.add(Box.createVerticalStrut(6));
        panelInfo.add(panelPrecio);
        panelInfo.add(lblDescripcion);

        JPanel panelInfoWrap = new JPanel();
        panelInfoWrap.setOpaque(false);
        panelInfoWrap.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelInfoWrap.add(panelInfo);

        tarjeta.add(panelInfoWrap, BorderLayout.CENTER);

        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarDetallesEvento(evento);
            }
        });

        return tarjeta;
    }

    private void mostrarDetallesEvento(Evento evento) {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        JDialog dialog = new JDialog(this, I18n.t("titulo.detalles.evento"), true);
        dialog.setSize(550, 680);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

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

        JLabel lblNombre = new JLabel("  " + evento.getNombre());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblNombre.setForeground(new Color(41, 128, 185));
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(400, 2));
        separator.setForeground(new Color(41, 128, 185, 80));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTipo = new JLabel(I18n.t("label.tipo") + ": " + evento.getTipo());
        lblTipo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTipo.setForeground(new Color(39, 174, 96));
        lblTipo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUbicacion = new JLabel(I18n.t("label.ubicacion") + ": " + evento.getUbicacion());
        lblUbicacion.setFont(new Font("Arial", Font.BOLD, 20));
        lblUbicacion.setForeground(new Color(142, 68, 173));
        lblUbicacion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblPrecio = new JLabel("Precio: " + evento.getPrecio() + " €");
        lblPrecio.setFont(new Font("Arial", Font.BOLD, 20));
        lblPrecio.setForeground(new Color(52, 152, 219));
        lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblNombre);
        panel.add(Box.createVerticalStrut(8));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(12));
        panel.add(lblTipo);
        panel.add(Box.createVerticalStrut(8));
        panel.add(lblUbicacion);
        panel.add(Box.createVerticalStrut(8));
        panel.add(lblPrecio);
        panel.add(Box.createVerticalStrut(18));

        JLabel lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(160, 160));
        lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (evento.getImagen() != null) {
            try {
                java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(evento.getImagen());
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

        JTextArea txtDescripcion = new JTextArea(evento.getDescripcion());
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
            new EditarEvento(this, evento).setVisible(true);
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
            eliminarEvento(evento);
        });

        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        JButton btnCerrar = new JButton(I18n.t("boton.cerrar"));
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

    private void eliminarEvento(Evento evento) {
        int confirm = JOptionPane.showConfirmDialog(this, I18n.t("mensaje.confirmar.eliminar.evento"),
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION)
            return;
        try {
            boolean eliminado = TurismoService.getInstance().eliminarEvento(evento.getId());
            if (eliminado) {
                cargarEventos();
                JOptionPane.showMessageDialog(this, I18n.t("mensaje.evento.eliminado"));
            } else {
                JOptionPane.showMessageDialog(this, I18n.t("mensaje.evento.noeliminado"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}