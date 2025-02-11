package views;


import javax.swing.*;

public class VistaRestaurantes extends JFrame {

	private JList<String> listaRestaurantes;
    private JButton btnValorar, btnCerrar, btnVolver;

    public VistaRestaurantes() {
        setTitle("Gestión de Restaurantes");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        listaRestaurantes = new JList<>();
        btnValorar = new JButton("Valorar");
        btnCerrar = new JButton("Cerrar");
        btnVolver = new JButton("Volver al Menú");

        add(new JScrollPane(listaRestaurantes));
        add(btnValorar);
        add(btnVolver);
        add(btnCerrar);

        btnVolver.addActionListener(e -> {
            new MenuPrincipal();
            dispose();
        });

        setVisible(true);
    }

    public JButton getBtnVolver() { return btnVolver; }
    public JList<String> getListaRestaurantes() { return listaRestaurantes; }
    public JButton getBtnValorar() { return btnValorar; }
    public JButton getBtnCerrar() { return btnCerrar; }

}
