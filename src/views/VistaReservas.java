package views;

import javax.swing.*;

public class VistaReservas extends JFrame {

    private JList<String> listaReservas;
    private JButton btnReservar, btnCancelar, btnVolver;

    public VistaReservas() {
        setTitle("Gestión de Reservas");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        listaReservas = new JList<>();
        btnReservar = new JButton("Reservar");
        btnCancelar = new JButton("Cancelar");
        btnVolver = new JButton("Volver al Menú");

        add(new JScrollPane(listaReservas));
        add(btnReservar);
        add(btnCancelar);
        add(btnVolver);

        btnVolver.addActionListener(e -> {
            MenuPrincipal.getInstance().setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    public JList<String> getListaReservas() { return listaReservas; }
    public JButton getBtnReservar() { return btnReservar; }
    public JButton getBtnCancelar() { return btnCancelar; }
    public JButton getBtnVolver() { return btnVolver; }

}
