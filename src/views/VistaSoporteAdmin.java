package views;

import model.Mensaje;
import services.TurismoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;

/**
 * Vista de soporte para el administrador con diseño moderno, claro y
 * profesional tipo chat.
 * Optimizada para mayor fluidez: solo recarga mensajes del usuario seleccionado,
 * scroll más rápido y menos repintados.
 */
public class VistaSoporteAdmin extends JFrame {

    private JPanel panelMensajes;
    private JTextArea txtRespuesta;
    private JButton btnEnviar;
    private JList<Integer> listaUsuarios;
    private DefaultListModel<Integer> modeloUsuarios;
    private Map<Integer, List<Mensaje>> mensajesPorUsuario;
    private Map<Integer, String> nombresUsuarios;

    // Guardamos referencias para poder cambiar colores dinámicamente
    private JPanel panelPrincipal;
    private JScrollPane scrollLista;
    private JScrollPane scrollMensajes;
    private JPanel panelRespuesta;

    private static VistaSoporteAdmin instance;

    // Timer para recarga periódica optimizada
    private javax.swing.Timer timer;

    public static VistaSoporteAdmin getInstance() {
        if (instance == null) {
            instance = new VistaSoporteAdmin();
        }
        return instance;
    }

    public VistaSoporteAdmin() {
        instance = this;
        inicializarComponentes();
        setVisible(true);
    }

    private void inicializarComponentes() {
        setTitle("Soporte - Admin");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;

        // Paleta de colores clara y oscura
        Color azulPrincipal = new Color(52, 152, 219);
        Color grisFondoClaro = new Color(245, 247, 250);
        Color grisClaroClaro = new Color(236, 240, 241);
        Color grisOscuroClaro = new Color(44, 62, 80);

        Color grisFondoOscuro = new Color(34, 40, 49);
        Color grisClaroOscuro = new Color(57, 62, 70);
        Color grisOscuroOscuro = new Color(20, 22, 30);

        Color grisFondo = dark ? grisFondoOscuro : grisFondoClaro;
        Color grisClaro = dark ? grisClaroOscuro : grisClaroClaro;
        Color grisOscuro = dark ? grisOscuroOscuro : grisOscuroClaro;

        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(grisFondo);

        // Panel izquierdo: lista de usuarios
        modeloUsuarios = new DefaultListModel<>();
        listaUsuarios = new JList<>(modeloUsuarios);
        listaUsuarios.setCellRenderer(new UsuarioRenderer());
        listaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaUsuarios.setBackground(grisClaro);
        listaUsuarios.setForeground(dark ? Color.WHITE : grisOscuroClaro);
        listaUsuarios.setFixedCellHeight(54);
        scrollLista = new JScrollPane(listaUsuarios);
        scrollLista.setPreferredSize(new Dimension(260, 0));
        scrollLista.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, azulPrincipal));
        scrollLista.getViewport().setBackground(grisClaro);

        // Panel derecho: chat visual
        panelMensajes = new JPanel();
        panelMensajes.setLayout(new BoxLayout(panelMensajes, BoxLayout.Y_AXIS));
        panelMensajes.setBackground(grisFondo);
        scrollMensajes = new JScrollPane(panelMensajes);
        scrollMensajes.setBorder(null);
        scrollMensajes.getVerticalScrollBar().setUnitIncrement(40); // scroll más rápido
        scrollMensajes.getViewport().setBackground(grisFondo);

        // Panel de respuesta
        panelRespuesta = new JPanel(new BorderLayout());
        panelRespuesta.setBackground(grisClaro);
        panelRespuesta.setBorder(new EmptyBorder(14, 20, 14, 20)); 

        // Campo de texto más estrecho de altura
        txtRespuesta = new JTextArea(2, 30);
        txtRespuesta.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtRespuesta.setLineWrap(true);
        txtRespuesta.setWrapStyleWord(true);
        txtRespuesta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(azulPrincipal, 1, true),
                new EmptyBorder(6, 10, 6, 10) 
        ));
        txtRespuesta.setBackground(dark ? grisOscuro : Color.WHITE);
        txtRespuesta.setForeground(dark ? Color.WHITE : grisOscuroClaro);

        // Panel para separar el botón del área de texto
        JPanel panelBtn = new JPanel(new BorderLayout());
        panelBtn.setOpaque(false);
        panelBtn.setBorder(new EmptyBorder(0, 8, 0, 0));

        btnEnviar = new JButton("Responder");
        btnEnviar.setBackground(azulPrincipal);
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnEnviar.setFocusPainted(false);
        btnEnviar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(40, 120, 180), 1, true),
                new EmptyBorder(6, 20, 6, 20) 
        ));
        btnEnviar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panelBtn.add(btnEnviar, BorderLayout.CENTER);

        panelRespuesta.add(txtRespuesta, BorderLayout.CENTER);
        panelRespuesta.add(panelBtn, BorderLayout.EAST);

        // Layout principal
        panelPrincipal.add(scrollLista, BorderLayout.WEST);
        panelPrincipal.add(scrollMensajes, BorderLayout.CENTER);
        panelPrincipal.add(panelRespuesta, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);

        // Eventos
        listaUsuarios.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarConversacion(listaUsuarios.getSelectedValue());
            }
        });

        btnEnviar.addActionListener(e -> responderMensaje());

        cargarMensajes();

        // Timer optimizado: solo recarga mensajes del usuario seleccionado
        timer = new javax.swing.Timer(2000, e -> recargarMensajesUsuarioSeleccionado());
        timer.start();
    }

    private void recargarMensajesUsuarioSeleccionado() {
        Integer idUsuario = listaUsuarios.getSelectedValue();
        if (idUsuario != null) {
            // Solo recarga los mensajes de ese usuario
            List<Mensaje> nuevos = TurismoService.getInstance().obtenerMensajesSoporteUsuario(idUsuario);
            if (nuevos != null) {
                mensajesPorUsuario.put(idUsuario, nuevos);
                mostrarConversacion(idUsuario);
            }
        } else {
            // Si no hay usuario seleccionado, recarga la lista de usuarios
            cargarMensajes();
        }
    }

    public void actualizarTema() {
        getContentPane().removeAll();
        inicializarComponentes();
        revalidate();
        repaint();
    }

    @Override
    public void dispose() {
        if (timer != null) timer.stop();
        instance = null;
        super.dispose();
    }

    /**
     * Carga los mensajes de soporte y los agrupa por usuario.
     * Ahora intenta obtener el nombre real del usuario por su id si viene vacío.
     */
    private void cargarMensajes() {
        modeloUsuarios.clear();
        mensajesPorUsuario = new LinkedHashMap<>();
        nombresUsuarios = new LinkedHashMap<>();
        List<Mensaje> mensajes = TurismoService.getInstance().obtenerMensajesSoporte();
        if (mensajes != null) {
            for (Mensaje m : mensajes) {
                int idUsuario = m.getIdUsuario();
                String nombreUsuario = m.getUsuarioNombre();
                if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
                    try {
                        nombreUsuario = TurismoService.getInstance().obtenerUsuarioPorId(idUsuario).getNombre();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
                    nombreUsuario = "Desconocido";
                }
                if (!mensajesPorUsuario.containsKey(idUsuario)) {
                    mensajesPorUsuario.put(idUsuario, new ArrayList<>());
                    nombresUsuarios.put(idUsuario, nombreUsuario);
                    modeloUsuarios.addElement(idUsuario);
                }
                mensajesPorUsuario.get(idUsuario).add(m);
            }
            if (!modeloUsuarios.isEmpty()) {
                listaUsuarios.setSelectedIndex(0);
            }
        }
    }

    /**
     * Muestra toda la conversación tipo chat con el usuario seleccionado.
     */
    private void mostrarConversacion(Integer idUsuario) {
        panelMensajes.removeAll();
        if (idUsuario == null)
            return;
        List<Mensaje> conversacion = mensajesPorUsuario.get(idUsuario);
        if (conversacion == null)
            return;

        // Limita a los últimos 100 mensajes para mayor fluidez
        int start = Math.max(0, conversacion.size() - 100);
        for (int i = start; i < conversacion.size(); i++) {
            Mensaje mensaje = conversacion.get(i);
            panelMensajes.add(crearBurbujaChat(
                    mensaje.getMensaje(),
                    false,
                    new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(mensaje.getFecha()),
                    nombresUsuarios.get(idUsuario).substring(0, 1).toUpperCase()));
            if (mensaje.getRespuesta() != null && !mensaje.getRespuesta().isEmpty()) {
                panelMensajes.add(Box.createVerticalStrut(4));
                panelMensajes.add(crearBurbujaChat(
                        mensaje.getRespuesta(),
                        true,
                        new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(mensaje.getFecha()),
                        "A"));
            }
            panelMensajes.add(Box.createVerticalStrut(8));
        }
        panelMensajes.add(Box.createVerticalGlue());
        panelMensajes.revalidate();
        panelMensajes.repaint();

        JScrollPane scroll = (JScrollPane) panelMensajes.getParent().getParent();
        JScrollBar bar = scroll.getVerticalScrollBar();
        int value = bar.getValue();
        int extent = bar.getModel().getExtent();
        int max = bar.getMaximum();

        boolean estabaAbajo = (value + extent + 20 >= max);

        SwingUtilities.invokeLater(() -> {
            if (estabaAbajo) {
                bar.setValue(bar.getMaximum());
            }
        });
    }

    /**
     * Envía la respuesta al usuario (responde al último mensaje sin respuesta).
     */
    private void responderMensaje() {
        Integer idUsuario = listaUsuarios.getSelectedValue();
        if (idUsuario == null)
            return;
        String respuesta = txtRespuesta.getText().trim();
        if (respuesta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La respuesta no puede estar vacía.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Mensaje> conversacion = mensajesPorUsuario.get(idUsuario);
        Mensaje mensajeSinResponder = null;
        if (conversacion != null) {
            for (int i = conversacion.size() - 1; i >= 0; i--) {
                Mensaje m = conversacion.get(i);
                if (m.getRespuesta() == null || m.getRespuesta().isEmpty()) {
                    mensajeSinResponder = m;
                    break;
                }
            }
        }
        if (mensajeSinResponder == null) {
            JOptionPane.showMessageDialog(this, "No hay mensajes pendientes de respuesta para este usuario.", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        boolean ok = TurismoService.getInstance().responderMensajeSoporte(mensajeSinResponder.getId(), respuesta);
        if (ok) {
            recargarMensajesUsuarioSeleccionado();
            listaUsuarios.setSelectedValue(idUsuario, true);
            txtRespuesta.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo enviar la respuesta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Crea una burbuja de chat visual moderna, alineada y con avatar.
     */
    private JPanel crearBurbujaChat(String texto, boolean admin, String fecha, String avatarInicial) {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color azulPrincipal = new Color(52, 152, 219);
        Color grisClaro = dark ? new Color(57, 62, 70) : new Color(236, 240, 241);
        Color grisOscuro = dark ? new Color(34, 40, 49) : new Color(44, 62, 80);

        JPanel burbuja = new JPanel();
        burbuja.setLayout(new BoxLayout(burbuja, BoxLayout.X_AXIS));
        burbuja.setOpaque(false);

        JLabel avatar = new JLabel(avatarInicial, SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(36, 36));
        avatar.setMaximumSize(new Dimension(36, 36));
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 17));
        avatar.setForeground(Color.WHITE);
        avatar.setOpaque(true);
        avatar.setBackground(admin ? azulPrincipal : grisOscuro);
        avatar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200, 80), 2, true),
                new EmptyBorder(2, 2, 2, 2)));
        avatar.setAlignmentY(Component.TOP_ALIGNMENT);

        JPanel panelBurbuja = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panelBurbuja.setLayout(new BoxLayout(panelBurbuja, BoxLayout.Y_AXIS));
        panelBurbuja.setBackground(admin ? azulPrincipal : grisClaro);
        panelBurbuja.setBorder(new EmptyBorder(8, 16, 8, 16));
        panelBurbuja.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel lblTexto = new JLabel("<html>" + texto.replace("\n", "<br>") + "</html>");
        lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblTexto.setForeground(admin ? Color.WHITE : (dark ? Color.WHITE : grisOscuro));
        JLabel lblFecha = new JLabel(fecha != null ? fecha : "");
        lblFecha.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblFecha.setForeground(admin ? Color.WHITE : new Color(120, 120, 120));
        panelBurbuja.add(lblTexto);
        panelBurbuja.add(lblFecha);

        if (admin) {
            burbuja.add(Box.createHorizontalGlue());
            burbuja.add(panelBurbuja);
            burbuja.add(Box.createHorizontalStrut(8));
            burbuja.add(avatar);
        } else {
            burbuja.add(avatar);
            burbuja.add(Box.createHorizontalStrut(8));
            burbuja.add(panelBurbuja);
            burbuja.add(Box.createHorizontalGlue());
        }
        burbuja.setBorder(new EmptyBorder(2, 18, 2, 18));
        return burbuja;
    }

    /**
     * Renderiza los usuarios en la lista con un estilo moderno y avatar.
     */
    private class UsuarioRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
            Integer idUsuario = (Integer) value;
            String nombre = nombresUsuarios.get(idUsuario);
            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(true);
            panel.setBackground(isSelected
                    ? (dark ? new Color(52, 73, 94) : new Color(220, 240, 255))
                    : (dark ? new Color(57, 62, 70) : new Color(236, 240, 241)));

            JLabel avatar = new JLabel(nombre.substring(0, 1).toUpperCase(), SwingConstants.CENTER);
            avatar.setPreferredSize(new Dimension(32, 32));
            avatar.setMaximumSize(new Dimension(32, 32));
            avatar.setFont(new Font("Segoe UI", Font.BOLD, 15));
            avatar.setForeground(Color.WHITE);
            avatar.setOpaque(true);
            avatar.setBackground(new Color(127, 140, 141));
            avatar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            avatar.setAlignmentY(Component.CENTER_ALIGNMENT);

            JLabel lbl = new JLabel(nombre);
            lbl.setForeground(dark ? Color.WHITE : new Color(44, 62, 80));
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lbl.setBorder(new EmptyBorder(0, 12, 0, 0));
            panel.add(avatar, BorderLayout.WEST);
            panel.add(lbl, BorderLayout.CENTER);
            panel.setBorder(new EmptyBorder(6, 8, 6, 8));
            return panel;
        }
    }
}