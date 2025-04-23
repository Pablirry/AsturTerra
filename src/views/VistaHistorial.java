package views;

import javax.swing.*;

import dao.HistorialDAO;

import java.awt.*;
import java.util.List;
import model.Historial;
import model.Usuario;

public class VistaHistorial extends JFrame {

    private JList<String> listaHistorial;
    private JButton btnCerrar, btnVolver;
    private Usuario usuario;

    public VistaHistorial(Usuario usuario) {
        this.usuario = usuario;
        inicializarComponentes();
        cargarHistorial();
    }

    private void inicializarComponentes() {
        setTitle("Historial");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        listaHistorial = new JList<>();
        JScrollPane scrollPane = new JScrollPane(listaHistorial);
        add(scrollPane, BorderLayout.CENTER);

        btnVolver = new JButton("Volver al Menú");
        btnCerrar = new JButton("Cerrar");

        btnVolver.setBackground(new Color(52, 152, 219));
        btnVolver.setForeground(Color.WHITE);
        btnCerrar.setBackground(new Color(231, 76, 60));
        btnCerrar.setForeground(Color.WHITE);

        panelBotones.add(btnVolver);
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);

        btnVolver.addActionListener(e -> {
            MenuPrincipal.getInstance(usuario).setVisible(true);
            dispose();
        });

        btnCerrar.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void cargarHistorial() {
        try {
            HistorialDAO dao = new HistorialDAO();
            List<Historial> historial = dao.obtenerHistorialUsuario(usuario.getId());

            DefaultListModel<String> model = new DefaultListModel<>();
            for (Historial h : historial) {
                model.addElement(h.getFecha() + " - " + h.getAccion());
            }
            listaHistorial.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage());
        }
    }

    public JButton getBtnVolver() { return btnVolver; }
    public JButton getBtnCerrar() { return btnCerrar; }
    public JList<String> getListaHistorial() { return listaHistorial; }
}