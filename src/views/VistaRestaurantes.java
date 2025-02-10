package views;


import javax.swing.*;

public class VistaRestaurantes extends JFrame {

	private JList<String> listaRestaurantes;
    private JButton btnValorar, btnCerrar;

    public VistaRestaurantes() {
        setTitle("Restaurantes y Valoraciones");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        listaRestaurantes = new JList<>();
        btnValorar = new JButton("Valorar");
        btnCerrar = new JButton("Cerrar");

        add(new JScrollPane(listaRestaurantes));
        add(btnValorar);
        add(btnCerrar);

        setVisible(true);
    }

    public JList<String> getListaRestaurantes() { return listaRestaurantes; }
    public JButton getBtnValorar() { return btnValorar; }
    public JButton getBtnCerrar() { return btnCerrar; }

}
