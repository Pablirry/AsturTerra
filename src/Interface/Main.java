package Interface;

import javax.swing.SwingUtilities;

import model.Usuario;
import views.MenuPrincipal;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Usuario admin = new Usuario(1, "Administrador", null, null, null, null);
                new MenuPrincipal(admin).setVisible(true);
            }
        });
    }
}