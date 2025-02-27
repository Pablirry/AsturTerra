package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import controllers.ReservaController;
import model.Usuario;

public class VistaReservas extends JFrame {

    private JList<String> listaReservas;
    private JButton btnReservar, btnCancelar, btnVolver;
    private ReservaController reservaController;
    private Usuario usuario;

    public VistaReservas( Usuario usuario ) {
        setTitle("Gestión de Reservas");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(44, 62, 80));
        JLabel lblTitulo = new JLabel("Gestión de Reservas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel de lista de reservas
        listaReservas = new JList<>();
        JScrollPane scrollPane = new JScrollPane(listaReservas);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(236, 240, 241));

        btnReservar = new JButton("Reservar");
        btnReservar.setBackground(new Color(46, 204, 113));
        btnReservar.setForeground(Color.WHITE);
        btnReservar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnReservar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnCancelar);

        btnVolver = new JButton("Volver al Menú");
        btnVolver.setBackground(new Color(52, 152, 219));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnVolver);

        add(panelBotones, BorderLayout.SOUTH);

        // Inicializar el controlador después de crear la interfaz gráfica
        reservaController = new ReservaController(this);

        // Eventos
        btnReservar.addActionListener(e -> reservaController.reservarRuta());
        btnCancelar.addActionListener(e -> reservaController.cancelarReserva());
        btnVolver.addActionListener(e -> {
            MenuPrincipal.getInstance(usuario).setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    public JList<String> getListaReservas() { return listaReservas; }
    public JButton getBtnReservar() { return btnReservar; }
    public JButton getBtnCancelar() { return btnCancelar; }
    public JButton getBtnVolver() { return btnVolver; }

}
