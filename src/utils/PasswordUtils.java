package utils;

import java.security.MessageDigest;

public class PasswordUtils {

    /**
     * Metodo para hashear una contraseña usando SHA-256
     * @param password : String
     * @return : String
     */

    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for(byte b : hash) {
                sb.append(String.format("%02x",b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
