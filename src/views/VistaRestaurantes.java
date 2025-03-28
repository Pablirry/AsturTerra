package views;

import javax.imageio.ImageIO;
import javax.swing.*;

import dao.ReservarDAO;
import dao.RutaDAO;
import java.util.List;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import model.Reserva;
import model.Restaurante;
import model.Ruta;
import model.Usuario;
import services.TurismoService;

public class VistaRestaurantes extends JFrame {

    private static VistaRestaurantes instance;

    private JList<String> listaRestaurantes;
    private JLabel lblImagen;
    private JButton btnValorar, btnCerrar, btnVolver, btnAgregar;
    private Usuario usuario;
    private DefaultListModel<String> modeloLista;
    private List<Restaurante> lista;

    public static VistaRestaurantes getInstance(Usuario usuario) {
        if (instance == null || !instance.isVisible()) {
            instance = new VistaRestaurantes(usuario);
        }
        instance.toFront();
        return instance;
    }

    public VistaRestaurantes(Usuario usuario) {
        this.usuario = usuario;
        inicializarComponentes();
        cargarRestaurantes();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                instance = null;
            }
        });
    }

    private void inicializarComponentes() {
        setTitle("Gestión de Restaurantes");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(44, 62, 80));
        JLabel lblTitulo = new JLabel("Gestión de Restaurantes");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        modeloLista = new DefaultListModel<>();
        listaRestaurantes = new JList<>(modeloLista);
        JScrollPane scrollPane = new JScrollPane(listaRestaurantes);
        add(scrollPane, BorderLayout.CENTER);

        lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(JLabel.CENTER);
        lblImagen.setPreferredSize(new Dimension(200, 200));
        add(lblImagen, BorderLayout.EAST);

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

        listaRestaurantes.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarImagenRestaurante();
            }
        });

        btnValorar.addActionListener(e -> {
            String seleccionado = listaRestaurantes.getSelectedValue();
            if (seleccionado != null) {
                try {
                    Restaurante restaurante = TurismoService.getInstance().obtenerRestaurantePorNombre(seleccionado);
                    if (restaurante != null) {
                        new ValorarRestaurantes(seleccionado, usuario).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "No se encontró el restaurante.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al buscar el restaurante: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un restaurante primero.");
            }
        });

        btnAgregar.addActionListener(e -> new AgregarRestaurante().setVisible(true));
        btnCerrar.addActionListener(e -> dispose());

        btnVolver.addActionListener(e -> {
            MenuPrincipal.getInstance(usuario).setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    private void cargarRestaurantes() {
        try {
            lista = TurismoService.getInstance().obtenerRestaurantes();
            modeloLista.clear();
            for (Restaurante r : lista) {
                modeloLista.addElement(r.getNombre());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar restaurantes: " + e.getMessage());
        }
    }

    private void mostrarImagenRestaurante() {
        int index = listaRestaurantes.getSelectedIndex();
        if (index != -1 && lista != null && index < lista.size()) {
            Restaurante restaurante = lista.get(index);
            byte[] imagenBytes = restaurante.getImagen();
            if (imagenBytes != null) {
                try {
                    ByteArrayInputStream bis = new ByteArrayInputStream(imagenBytes);
                    BufferedImage img = ImageIO.read(bis);
                    ImageIcon icon = new ImageIcon(img.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
                    lblImagen.setIcon(icon);
                } catch (Exception e) {
                    lblImagen.setIcon(null);
                    System.err.println("No se pudo cargar la imagen: " + e.getMessage());
                }
            } else {
                lblImagen.setIcon(null);
            }
        }
    }
}