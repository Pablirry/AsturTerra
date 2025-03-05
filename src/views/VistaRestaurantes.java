package views;

import javax.swing.*;
import java.awt.*;
import controllers.RestauranteController;
import model.Usuario;

public class VistaRestaurantes extends JFrame {

    private JList<String> listaRestaurantes;
    private JLabel lblImagen;
    private JButton btnValorar, btnCerrar, btnVolver, btnAgregar;
    private RestauranteController restauranteController;
    private Usuario usuario;

    public VistaRestaurantes(Usuario usuario) {
        this.usuario = usuario;
        inicializarComponentes();
        restauranteController = new RestauranteController(this);
    }

    private void inicializarComponentes() {
        setTitle("Gestión de Restaurantes");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(44, 62, 80));
        JLabel lblTitulo = new JLabel("Gestión de Restaurantes");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel de lista de restaurantes
        listaRestaurantes = new JList<>();
        JScrollPane scrollPane = new JScrollPane(listaRestaurantes);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de imagen
        lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(JLabel.CENTER);
        add(lblImagen, BorderLayout.EAST);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(236, 240, 241));

        btnValorar = new JButton("Valorar");
        btnValorar.setBackground(new Color(46, 204, 113));
        btnValorar.setForeground(Color.WHITE);
        btnValorar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnValorar);

        btnAgregar = new JButton("Agregar Restaurante");
        btnAgregar.setBackground(new Color(52, 152, 219));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnAgregar);

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(231, 76, 60));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnCerrar);

        btnVolver = new JButton("Volver al Menú");
        btnVolver.setBackground(new Color(52, 152, 219));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnVolver);

        add(panelBotones, BorderLayout.SOUTH);

        // Inicializar el controlador después de crear la interfaz gráfica
        restauranteController = new RestauranteController(this);

        // Eventos
        btnValorar.addActionListener(e -> restauranteController.valorarRestaurante());
        btnAgregar.addActionListener(e -> restauranteController.abrirAgregarRestaurante());
        btnCerrar.addActionListener(e -> dispose());
        btnVolver.addActionListener(e -> {
            MenuPrincipal.getInstance(usuario).setVisible(true);
            dispose();
        });

        listaRestaurantes.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                restauranteController.mostrarImagenRestaurante();
            }
        });

        setVisible(true);
    }

    public JList<String> getListaRestaurantes() { return listaRestaurantes; }
    public JLabel getLblImagen() { return lblImagen; }
    public JButton getBtnValorar() { return btnValorar; }
    public JButton getBtnCerrar() { return btnCerrar; }
    public JButton getBtnVolver() { return btnVolver; }
    public JButton getBtnAgregar() { return btnAgregar; }
}