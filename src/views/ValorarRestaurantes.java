package views;

import javax.swing.*;
import java.awt.*;
import controllers.ValorarRestauranteController;

public class ValorarRestaurantes extends JFrame {

    private JLabel lblTitulo;
    private JLabel lblPuntuacion;
    private JLabel lblComentario;
    private JComboBox<Integer> cmbPuntuacion;
    private JTextArea txtComentario;
    private JButton btnEnviar;
    private JButton btnCancelar;
    private String restaurante;

    public ValorarRestaurantes(String restaurante) {
        this.restaurante = restaurante;

        setTitle("Valorar Restaurante");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(44, 62, 80));
        lblTitulo = new JLabel("Valorar " + restaurante);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel de contenido
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new GridLayout(3, 2, 10, 10));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblPuntuacion = new JLabel("Puntuación:");
        cmbPuntuacion = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        lblComentario = new JLabel("Comentario:");
        txtComentario = new JTextArea(5, 20);
        JScrollPane scrollComentario = new JScrollPane(txtComentario);

        panelContenido.add(lblPuntuacion);
        panelContenido.add(cmbPuntuacion);
        panelContenido.add(lblComentario);
        panelContenido.add(scrollComentario);

        add(panelContenido, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
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

        // Inicializar el controlador después de crear la interfaz gráfica
        ValorarRestauranteController valorarRestauranteController = new ValorarRestauranteController(this, restaurante);

        // Eventos
        btnEnviar.addActionListener(e -> valorarRestauranteController.enviarValoracion());
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    public JComboBox<Integer> getCmbPuntuacion() { return cmbPuntuacion; }
    public JTextArea getTxtComentario() { return txtComentario; }
    public JButton getBtnEnviar() { return btnEnviar; }
    public JButton getBtnCancelar() { return btnCancelar; }
}
