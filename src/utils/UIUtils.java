package utils;

import javax.swing.*;

import views.ThemeManager;

import java.awt.*;
import java.util.regex.Pattern;

public class UIUtils {

    // Crear botón estilizado (rectangular, sin fondo)
    public static JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.putClientProperty("colorBase", color);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        return btn;
    }

    // Crear botón redondeado y colorido, sin esquinas
    public static JButton crearBotonRedondeado(String texto, Color color, int radio) {
        ThemeManager.RoundedButton btn = new ThemeManager.RoundedButton(texto, radio);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.putClientProperty("colorBase", color);
        return btn;
    }

    // Validar email
    public static boolean validarEmail(String email) {
        return Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email);
    }

    /*
     * ! (?=.*[a-z]) al menos una minúscula
     * ! (?=.*[A-Z]) al menos una mayúscula
     * ! (?=(?:.*\d){2,}) al menos dos dígitos
     * ! (?=.*[!@#$%^&*()_+\-={}:;"'|<>,.?/~])` al menos un carácter especial
     * ! .{8,} mínimo 8 caracteres
     */

    // Validar contraseña segura
    public static boolean validarPasswordSegura(String password) {
        // Al menos 8 caracteres, 1 mayúscula, 1 minúscula, 2 números, 1 especial
        return password
                .matches("^(?=.*[a-z])(?=.*[A-Z])(?=(?:.*\\d){2,})(?=.*[!@#$%^&*()_+\\-={}:;\"'|<>,.?/~`]).{8,}$");
    }

    // Mostrar mensaje de error
    public static void mostrarError(Component parent, String mensaje) {
        JOptionPane.showMessageDialog(parent, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Mostrar mensaje de información
    public static void mostrarInfo(Component parent, String mensaje) {
        JOptionPane.showMessageDialog(parent, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
