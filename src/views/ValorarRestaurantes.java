package views;

import javax.swing.*;
import model.Restaurante;
import model.Usuario;
import model.ValoracionRestaurante;
import services.TurismoService;

import java.awt.*;
import java.awt.event.ActionEvent;

public class ValorarRestaurantes extends JFrame {

    private JComboBox<Integer> cmbPuntuacion;
    private JTextArea txtComentario;
    private JButton btnEnviar;
    private JButton btnCancelar;
    private int idRestaurante;
    private Usuario usuario;

    public ValorarRestaurantes(String nombreRestaurante, Usuario usuario) {
        this.usuario = usuario;

        try {
            Restaurante restaurante = TurismoService.getInstance().obtenerRestaurantePorNombre(nombreRestaurante);
            if (restaurante == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el restaurante.");
                dispose();
                return;
            }
            this.idRestaurante = restaurante.getId();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error obteniendo restaurante: " + e.getMessage());
            dispose();
            return;
        }

        setTitle("Valorar Restaurante");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(44, 62, 80));
        JLabel lblTitulo = new JLabel("Valorar Restaurante");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel contenido
        JPanel panelContenido = new JPanel(new GridLayout(3, 2, 10, 10));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblPuntuacion = new JLabel("Puntuación:");
        cmbPuntuacion = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        JLabel lblComentario = new JLabel("Comentario:");
        txtComentario = new JTextArea(4, 20);
        JScrollPane scrollComentario = new JScrollPane(txtComentario);

        panelContenido.add(lblPuntuacion);
        panelContenido.add(cmbPuntuacion);
        panelContenido.add(lblComentario);
        panelContenido.add(scrollComentario);

        add(panelContenido, BorderLayout.CENTER);

        // Panel botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(236, 240, 241));

        btnEnviar = new JButton("Enviar");
        btnEnviar.setBackground(new Color(46, 204, 113));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEnviar.addActionListener(this::enviarValoracion);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnEnviar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void enviarValoracion(ActionEvent e) {
        try {
            int puntuacion = (Integer) cmbPuntuacion.getSelectedItem();
            String comentario = txtComentario.getText().trim();

            ValoracionRestaurante valoracion = new ValoracionRestaurante(
                0,
                usuario.getId(),
                idRestaurante,
                puntuacion,
                comentario
            );

            boolean guardado = TurismoService.getInstance().valorarRestaurante(valoracion);

            if (guardado) {
                JOptionPane.showMessageDialog(this, "¡Valoración registrada con éxito!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar la valoración.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
