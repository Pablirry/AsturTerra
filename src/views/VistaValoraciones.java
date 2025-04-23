package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import dao.ValoracionDAO;
import model.Usuario;
import model.ValoracionRuta;
import java.util.List;
import java.awt.*;

public class VistaValoraciones extends JFrame {
	
	private static VistaValoraciones instance;
    private JTable tablaValoraciones;
    private DefaultTableModel modeloTabla;
    private JComboBox<Integer> cmbPuntuacion;
    private JTextArea txtComentario;
    private JButton btnEnviar, btnCancelar;
    private int idRuta;
    private String nombreRuta;
    private Usuario usuario;

    
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

        // Tabla de valoraciones
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Valoraciones existentes"));

        modeloTabla = new DefaultTableModel(new String[]{"Usuario", "Puntuación", "Comentario"}, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tablaValoraciones = new JTable(modeloTabla);
        tablaValoraciones.getTableHeader().setReorderingAllowed(false);
        panelTabla.add(new JScrollPane(tablaValoraciones), BorderLayout.CENTER);

        panelCentral.add(panelTabla);

        // Formulario
        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Nueva valoración"));

        JLabel lblPuntuacion = new JLabel("Puntuación:");
        cmbPuntuacion = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        JLabel lblComentario = new JLabel("Comentario:");
        txtComentario = new JTextArea(3, 20);
        JScrollPane scrollComentario = new JScrollPane(txtComentario);

        panelFormulario.add(lblPuntuacion);
        panelFormulario.add(cmbPuntuacion);
        panelFormulario.add(lblComentario);
        panelFormulario.add(scrollComentario);

        panelCentral.add(panelFormulario);
        add(panelCentral, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(236, 240, 241));

        btnEnviar = new JButton("Enviar");
        btnEnviar.setBackground(new Color(46, 204, 113));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnEnviar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnEnviar.addActionListener(e -> enviarValoracion());
        btnCancelar.addActionListener(e -> dispose());

        cargarValoraciones();

        setVisible(true);
    }

    private void cargarValoraciones() {
        try {
            ValoracionDAO dao = new ValoracionDAO();
            List<ValoracionRuta> valoraciones = dao.obtenerValoracionesRuta(idRuta);
            modeloTabla.setRowCount(0); // limpiar tabla

            for (ValoracionRuta v : valoraciones) {
                modeloTabla.addRow(new Object[]{
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
        try {
            int puntuacion = (Integer) cmbPuntuacion.getSelectedItem();
            String comentario = txtComentario.getText().trim();

            ValoracionRuta valoracion = new ValoracionRuta(
                0,
                usuario.getId(),
                idRuta,
                puntuacion,
                comentario
            );

            ValoracionDAO dao = new ValoracionDAO();
            boolean registrada = dao.registrarValoracionRuta(valoracion);

            if (registrada) {
                JOptionPane.showMessageDialog(this, "¡Valoración enviada con éxito!");
                cargarValoraciones();
                txtComentario.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar la valoración.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}