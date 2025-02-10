package views;

import javax.swing.*;
import java.awt.*;

public class Registro extends JFrame {

	private JTextField txtNombre, txtCorreo;
    private JPasswordField txtContraseña;
    private JButton btnRegistrar;
    private JLabel lblFondo;

    public Registro() {
        setTitle("Registro de Usuario");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);
        
        // Panel transparente para los campos
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(null);
        panelCampos.setBounds(50, 100, 400, 350);
        panelCampos.setBackground(new Color(255, 255, 255, 180)); // Transparencia

        JLabel lblTitulo = new JLabel("Crea tu cuenta");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBounds(50, 10, 200, 30);
        panelCampos.add(lblTitulo);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(50, 60, 100, 25);
        txtNombre = new JTextField();
        txtNombre.setBounds(50, 85, 300, 30);

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setBounds(50, 120, 100, 25);
        txtCorreo = new JTextField();
        txtCorreo.setBounds(50, 145, 300, 30);

        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setBounds(50, 180, 100, 25);
        txtContraseña = new JPasswordField();
        txtContraseña.setBounds(50, 205, 300, 30);

        btnRegistrar = new JButton("Registrarse");
        btnRegistrar.setBounds(50, 260, 300, 40);
        btnRegistrar.setBackground(new Color(46, 204, 113));
        btnRegistrar.setForeground(Color.WHITE);

        panelCampos.add(lblNombre);
        panelCampos.add(txtNombre);
        panelCampos.add(lblCorreo);
        panelCampos.add(txtCorreo);
        panelCampos.add(lblContraseña);
        panelCampos.add(txtContraseña);
        panelCampos.add(btnRegistrar);

        getContentPane().add(panelCampos);
        
                // Imagen de fondo
                lblFondo = new JLabel(new ImageIcon("D:\\Escritorio\\DAM_AFA\\2-DAM-1\\PROYECTO\\proyecto\\Turismo\\assets\\DALL·E 2025-01-28 10.42.42 - A minimalist tourism app icon for Asturias, Spain, designed for a 64px size. The icon features a clean and modern look with a stylized green mountain, (1).png"));
                lblFondo.setBounds(315, 5, 75, 80);
                panelCampos.add(lblFondo);
        setVisible(true);
    }

    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtCorreo() { return txtCorreo; }
    public JPasswordField getTxtContraseña() { return txtContraseña; }
    public JButton getBtnRegistrar() { return btnRegistrar; }

}
