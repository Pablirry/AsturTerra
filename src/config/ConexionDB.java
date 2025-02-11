package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:mysql://usrshnydk2vnkzsm:jrbyBVqDOaUSMx0BXjMq@be6i3twqn0pieiochkud-mysql.services.clever-cloud.com:3306/be6i3twqn0pieiochkud";
    private static final String USER = "usrshnydk2vnkzsm";
    private static final String PASSWORD = "jrbyBVqDOaUSMx0BXjMq";

    public static Connection getConection() throws ClassNotFoundException {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos");
            e.printStackTrace();
        }
        return con;
    }

    public void closeConection(Connection con) {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión");
            e.printStackTrace();
        }
    }

}
