package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import controllers.RestauranteController;

public class AgregarRestaurante extends JFrame {

    private JTextField txtNombre, txtUbicacion;
    private JSpinner spnValoracion;
    private JButton btnSeleccionarImagen, btnAgregar, btnCancelar;
    private JLabel lblImagen;
    private File imagenPerfil;
    private RestauranteController restauranteController;

    public AgregarRestaurante(RestauranteController restauranteController) {
        this.restauranteController = restauranteController;

        setTitle("Agregar Restaurante");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(44, 62, 80));
        JLabel lblTitulo = new JLabel("Agregar Restaurante");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel de contenido
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new GridLayout(5, 2, 10, 10));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField();
        JLabel lblUbicacion = new JLabel("Ubicación:");
        txtUbicacion = new JTextField();
        JLabel lblValoracion = new JLabel("Valoración:");
        spnValoracion = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        JLabel lblImagen = new JLabel("Imagen:");
        btnSeleccionarImagen = new JButton("Seleccionar Imagen");

        panelContenido.add(lblNombre);
        panelContenido.add(txtNombre);
        panelContenido.add(lblUbicacion);
        panelContenido.add(txtUbicacion);
        panelContenido.add(lblValoracion);
        panelContenido.add(spnValoracion);
        panelContenido.add(lblImagen);
        panelContenido.add(btnSeleccionarImagen);

        add(panelContenido, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(236, 240, 241));

        btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new Color(46, 204, 113));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnAgregar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());
        btnAgregar.addActionListener(e -> restauranteController.agregarRestaurante());
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            imagenPerfil = fileChooser.getSelectedFile();
        }
    }

    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtUbicacion() { return txtUbicacion; }
    public JSpinner getSpnValoracion() { return spnValoracion; }
    public JButton getBtnSeleccionarImagen() { return btnSeleccionarImagen; }
    public JButton getBtnAgregar() { return btnAgregar; }
    public JButton getBtnCancelar() { return btnCancelar; }
    public File getImagenPerfil() { return imagenPerfil; }
}
