package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private JTextField txtCorreo;
    private JPasswordField txtContraseña;
    private JButton btnLogin;
    private JButton btnRegistro;
    private JLabel lblTitulo, lblCorreo, lblContraseña;
    private JPanel panelFondo;

    public Login() {
        setTitle("Inicio de Sesión");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // Fondo con color gradiente
        panelFondo = new JPanel();
        panelFondo.setBackground(new Color(44, 62, 80));
        panelFondo.setBounds(0, 0, 400, 500);
        panelFondo.setLayout(null);

        // Título
        lblTitulo = new JLabel("Bienvenido a Turismo Asturias");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(50, 40, 300, 30);
        panelFondo.add(lblTitulo);

        // Campos
        lblCorreo = new JLabel("Correo:");
        lblCorreo.setForeground(Color.WHITE);
        lblCorreo.setBounds(50, 120, 100, 25);
        txtCorreo = new JTextField();
        txtCorreo.setBounds(50, 150, 300, 30);

        lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setForeground(Color.WHITE);
        lblContraseña.setBounds(50, 200, 100, 25);
        txtContraseña = new JPasswordField();
        txtContraseña.setBounds(50, 230, 300, 30);

        // Botones
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBounds(50, 300, 300, 40);
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(Color.WHITE);

        btnRegistro = new JButton("Registrarse");
        btnRegistro.setBounds(50, 360, 300, 40);
        btnRegistro.setBackground(new Color(46, 204, 113));
        btnRegistro.setForeground(Color.WHITE);

        // Agregar elementos
        panelFondo.add(lblCorreo);
        panelFondo.add(txtCorreo);
        panelFondo.add(lblContraseña);
        panelFondo.add(txtContraseña);
        panelFondo.add(btnLogin);
        panelFondo.add(btnRegistro);

        add(panelFondo);
        setVisible(true);
    }

    public void agregarEventos(ActionListener loginListener, ActionListener registroListener) {
        btnLogin.addActionListener(loginListener);
        btnRegistro.addActionListener(registroListener);
    }

    public JTextField getTxtCorreo() { return txtCorreo; }
    public JPasswordField getTxtContraseña() { return txtContraseña; }
    public JButton getBtnLogin() { return btnLogin; }
    public JButton getBtnRegistro() { return btnRegistro; }
}
