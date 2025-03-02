package views;

import javax.swing.*;
import java.awt.*;
import controllers.HistorialController;
import model.Usuario;

public class VistaHistorial extends JFrame {

    private JList<String> listaHistorial;
    private JButton btnCerrar, btnVolver;
    private Usuario usuario;
    private HistorialController historialController;

    public VistaHistorial(Usuario usuario) {
        this.usuario = usuario;
        inicializarComponentes();
        historialController = new HistorialController(this);
    }

    private void inicializarComponentes() {
        setTitle("Historial de Actividades");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        listaHistorial = new JList<>();
        btnCerrar = new JButton("Cerrar");
        btnVolver = new JButton("Volver al Menú");

        add(new JScrollPane(listaHistorial));
        add(btnVolver);
        add(btnCerrar);

        btnVolver.addActionListener(e -> {
            MenuPrincipal.getInstance(usuario).setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    public JButton getBtnVolver() { return btnVolver; }
    public JList<String> getListaHistorial() { return listaHistorial; }
    public JButton getBtnCerrar() { return btnCerrar; }
}