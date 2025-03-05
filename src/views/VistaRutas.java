package views;

import javax.swing.*;
import java.awt.*;
import controllers.RutaController;
import model.Usuario;

public class VistaRutas extends JFrame {

    private JList<String> listaRutas;
    private JButton btnAgregar, btnEliminar, btnVerDetalles, btnValorar, btnReservar, btnVolver;
    private RutaController rutaController;
    private Usuario usuario;

    public VistaRutas(Usuario usuario) {
        this.usuario = usuario;
        inicializarComponentes();
        rutaController = new RutaController(this);
    }

    private void inicializarComponentes() {
        setTitle("Gestión de Rutas");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(44, 62, 80));
        JLabel lblTitulo = new JLabel("Gestión de Rutas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel de lista de rutas
        listaRutas = new JList<>();
        JScrollPane scrollPane = new JScrollPane(listaRutas);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(236, 240, 241));

        btnAgregar = new JButton("Agregar Ruta");
        btnAgregar.setBackground(new Color(52, 152, 219));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnAgregar);

        btnEliminar = new JButton("Eliminar Ruta");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnEliminar);

        btnVerDetalles = new JButton("Ver Detalles");
        btnVerDetalles.setBackground(new Color(46, 204, 113));
        btnVerDetalles.setForeground(Color.WHITE);
        btnVerDetalles.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnVerDetalles);

        btnValorar = new JButton("Valorar Ruta");
        btnValorar.setBackground(new Color(46, 204, 113));
        btnValorar.setForeground(Color.WHITE);
        btnValorar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnValorar);

        btnReservar = new JButton("Reservar Ruta");
        btnReservar.setBackground(new Color(46, 204, 113));
        btnReservar.setForeground(Color.WHITE);
        btnReservar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnReservar);

        btnVolver = new JButton("Volver al Menú");
        btnVolver.setBackground(new Color(52, 152, 219));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnVolver);

        add(panelBotones, BorderLayout.SOUTH);

        // Inicializar el controlador después de crear la interfaz gráfica
        rutaController = new RutaController(this);

        // Eventos
        btnAgregar.addActionListener(e -> rutaController.abrirAgregarRuta());
        btnEliminar.addActionListener(e -> rutaController.eliminarRuta());
        btnVerDetalles.addActionListener(e -> rutaController.verDetallesRuta());
        btnValorar.addActionListener(e -> rutaController.valorarRuta());
        btnReservar.addActionListener(e -> rutaController.reservarRuta());
        btnVolver.addActionListener(e -> {
            MenuPrincipal.getInstance(usuario).setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    public JList<String> getListaRutas() { return listaRutas; }
    public JButton getBtnAgregar() { return btnAgregar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnVerDetalles() { return btnVerDetalles; }
    public JButton getBtnValorar() { return btnValorar; }
    public JButton getBtnReservar() { return btnReservar; }
    public JButton getBtnVolver() { return btnVolver; }
    public Usuario getUsuario() { return usuario; }
}