package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import dao.ValoracionDAO;
import model.Usuario;
import model.ValoracionRuta;
import utils.I18n;

import java.util.List;
import java.awt.*;

public class VistaValoraciones extends JFrame {

    private static VistaValoraciones instance;
    private JTable tablaValoraciones;
    private DefaultTableModel modeloTabla;
    private JTextArea txtComentario;
    private JButton btnEnviar, btnCancelar;
    private int idRuta;
    private String nombreRuta;
    private Usuario usuario;
    private int puntuacionSeleccionada = 0;
    private JLabel[] estrellas = new JLabel[5];

    public static VistaValoraciones getInstance(int idRuta, String nombreRuta, Usuario usuario) {
        if (instance == null || !instance.isVisible()) {
            instance = new VistaValoraciones(idRuta, nombreRuta, usuario);
        }
        instance.toFront();
        return instance;
    }

    public VistaValoraciones(int idRuta, String nombreRuta, Usuario usuario) {
        this.usuario = usuario;
        this.idRuta = idRuta;
        this.nombreRuta = nombreRuta;

        setTitle("Valorar Ruta: " + nombreRuta);
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Detectar tema
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color fondo = dark ? new Color(44, 62, 80) : Color.WHITE;
        Color texto = dark ? Color.WHITE : new Color(44, 62, 80);

        // Panel título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(44, 62, 80));
        JLabel lblTitulo = new JLabel("Valorar " + nombreRuta);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel central con tabla y formulario
        JPanel panelCentral = new JPanel(new GridLayout(2, 1, 10, 10));
        panelCentral.setBackground(fondo);

        // Tabla de valoraciones
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Valoraciones existentes"));
        panelTabla.setBackground(fondo);

        modeloTabla = new DefaultTableModel(new String[] {
                I18n.t("columna.usuario"),
                I18n.t("columna.puntuacion"),
                I18n.t("columna.comentario") }, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tablaValoraciones = new JTable(modeloTabla);
        tablaValoraciones.getTableHeader().setReorderingAllowed(false);
        tablaValoraciones.setBackground(fondo);
        tablaValoraciones.setForeground(texto);
        tablaValoraciones.getTableHeader().setBackground(dark ? new Color(44, 62, 80) : new Color(236, 240, 241));
        tablaValoraciones.getTableHeader().setForeground(texto);

        panelTabla.add(new JScrollPane(tablaValoraciones), BorderLayout.CENTER);

        panelCentral.add(panelTabla);

        // Formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Nueva valoración"));
        panelFormulario.setBackground(fondo);

        // Puntuación con estrellas visuales
        JPanel panelPuntuacion = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelPuntuacion.setBackground(fondo);
        JLabel lblPuntuacion = new JLabel("Puntuación:");
        lblPuntuacion.setFont(new Font("Arial", Font.PLAIN, 16));
        lblPuntuacion.setForeground(texto);
        panelPuntuacion.add(lblPuntuacion);

        for (int i = 0; i < 5; i++) {
            estrellas[i] = new JLabel("☆");
            estrellas[i].setFont(new Font("Segoe UI Symbol", Font.BOLD, 28));
            estrellas[i].setForeground(new Color(241, 196, 15));
            final int estrellaIndex = i;
            estrellas[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            estrellas[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    puntuacionSeleccionada = estrellaIndex + 1;
                    actualizarEstrellas();
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    resaltarEstrellas(estrellaIndex + 1);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    actualizarEstrellas();
                }
            });
            panelPuntuacion.add(estrellas[i]);
        }
        panelFormulario.add(panelPuntuacion);
        panelFormulario.add(Box.createVerticalStrut(10));

        // Comentario
        JLabel lblComentario = new JLabel("Comentario:");
        lblComentario.setFont(new Font("Arial", Font.PLAIN, 16));
        lblComentario.setForeground(texto);
        panelFormulario.add(lblComentario);

        txtComentario = new JTextArea(3, 20);
        txtComentario.setFont(new Font("Arial", Font.PLAIN, 15));
        txtComentario.setLineWrap(true);
        txtComentario.setWrapStyleWord(true);
        txtComentario.setBackground(fondo);
        txtComentario.setForeground(texto);
        JScrollPane scrollComentario = new JScrollPane(txtComentario);
        scrollComentario.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        scrollComentario.setBackground(fondo);
        panelFormulario.add(scrollComentario);

        panelCentral.add(panelFormulario);
        add(panelCentral, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(dark ? new Color(52, 73, 94) : new Color(236, 240, 241));

        btnEnviar = new JButton(I18n.t("boton.enviar"));
        btnEnviar.setBackground(new Color(46, 204, 113));
        btnEnviar.setForeground(Color.WHITE);
        getRootPane().setDefaultButton(btnEnviar);
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnEnviar);

        btnCancelar = new JButton(I18n.t("boton.cancelar"));
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnEnviar.addActionListener(e -> enviarValoracion());
        btnCancelar.addActionListener(e -> dispose());

        cargarValoraciones();
        actualizarEstrellas();

        setVisible(true);
    }

    private void actualizarEstrellas() {
        for (int i = 0; i < 5; i++) {
            if (i < puntuacionSeleccionada) {
                estrellas[i].setText("★");
                estrellas[i].setForeground(new Color(241, 196, 15));
            } else {
                estrellas[i].setText("☆");
                estrellas[i].setForeground(new Color(189, 195, 199));
            }
        }
    }

    private void resaltarEstrellas(int hasta) {
        for (int i = 0; i < 5; i++) {
            if (i < hasta) {
                estrellas[i].setFont(new Font("Dialog", Font.BOLD, 28));
                estrellas[i].setText("★");
                estrellas[i].setForeground(new Color(241, 196, 15));
            } else {
                estrellas[i].setFont(new Font("Dialog", Font.BOLD, 28));
                estrellas[i].setText("☆");
                estrellas[i].setForeground(new Color(189, 195, 199));
            }
        }
    }

    private void cargarValoraciones() {
        try {
            ValoracionDAO dao = new ValoracionDAO();
            List<ValoracionRuta> valoraciones = dao.obtenerValoracionesRuta(idRuta);
            modeloTabla.setRowCount(0); // limpiar tabla

            for (ValoracionRuta v : valoraciones) {
                modeloTabla.addRow(new Object[] {
                        "Usuario ID: " + v.getIdUsuario(),
                        v.getPuntuacion(),
                        v.getComentario()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar valoraciones: " + e.getMessage());
        }
    }

    private void enviarValoracion() {
        if (puntuacionSeleccionada == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una puntuación.");
            return;
        }
        String comentario = txtComentario.getText().trim();
        try {
            ValoracionRuta valoracion = new ValoracionRuta(
                    0,
                    usuario.getId(),
                    idRuta,
                    puntuacionSeleccionada,
                    comentario);

            ValoracionDAO dao = new ValoracionDAO();
            boolean registrada = dao.registrarValoracionRuta(valoracion);

            if (registrada) {
                JOptionPane.showMessageDialog(this, "¡Valoración enviada con éxito!");
                cargarValoraciones();
                txtComentario.setText("");
                puntuacionSeleccionada = 0;
                actualizarEstrellas();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar la valoración.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}