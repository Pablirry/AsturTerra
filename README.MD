# 🏔️ AsturTerra - Sistema de Gestión de Turismo en Asturias

Este es un **sistema de gestión de turismo** desarrollado en **Java (Swing) con MySQL** para gestionar rutas turísticas, reservas, valoraciones, restaurantes y comunicación con soporte.

---

# 📌 **Características Principales**
✅ Registro e inicio de sesión de usuarios (Clientes / Administradores).  
✅ CRUD de **Rutas** con imágenes, precios y niveles de dificultad.  
✅ CRUD de **Actividades y Restaurantes** con valoraciones.  
✅ **Reservas y cancelaciones** de rutas.  
✅ Sistema de **Valoraciones** de rutas y restaurantes.  
✅ **Chat en tiempo real** entre usuarios y administradores.  
✅ Registro de **Historial de actividades** de los usuarios.  
✅ **Exportación de datos** a **PDF y Excel**.  
✅ **Modo oscuro/claro** para la interfaz gráfica.  

---

# 📂 **Estructura del Proyecto**
📁 TurismoAsturias  
├── 📂 src  
│ ├── 📂 config → Conexión a la base de datos  
│ ├── 📂 dao → Acceso a la base de datos (CRUD)  
│ ├── 📂 modelo → Clases de datos (Usuario, Ruta, Reserva, etc.)  
│ ├── 📂 vista → Interfaces gráficas (Swing)  
│ ├── 📂 controlador → Lógica de negocio  
├── 📂 assets → Imágenes de prueba  
├── 📜 README.md → Documentación del proyecto  
├── 📜 TurismoAsturias.sql → Base de datos MySQL  

# 📜 **Diagrama de Clases**

```mermaid
classDiagram
    class Usuario {
        +int id
        +String nombre
        +String correo
        +String contrasena
        +String tipo
        +byte[] imagenPerfil
    }

    class Ruta {
        +int id
        +String nombre
        +String descripcion
        +byte[] imagen
        +double precio
        +String dificultad
    }

    class Reserva {
        +int id
        +int idUsuario
        +int idRuta
        +Date fecha
    }

    class ValoracionRuta {
        +int id
        +int idUsuario
        +int idRuta
        +int puntuacion
        +String comentario
    }

    class Restaurante {
        +int id
        +String nombre
        +String ubicacion
        +float valoracion
        +byte[] imagen
    }

    class ValoracionRestaurante {
        +int id
        +int idUsuario
        +int idRestaurante
        +int puntuacion
        +String comentario
    }

    class Historial {
        +int id
        +int idUsuario
        +String accion
        +Date fecha
    }

    class Mensaje {
        +int id
        +int idUsuario
        +String mensaje
        +String respuesta
        +Date fecha
    }

    class UsuarioDAO {
        +Usuario iniciarSesion(String correo, String contrasena)
        +boolean registrarUsuario(Usuario usuario)
    }

    class HistorialDAO {
        +void registrarAccion(int idUsuario, String accion)
    }

    class UsuarioController {
        -UsuarioDAO usuarioDAO
        -HistorialDAO historialDAO
        -Login loginVista
        +UsuarioController()
        +UsuarioController(Login loginVista)
        +void iniciarSesion()
        +void abrirRegistro()
        +boolean registrarUsuario(Usuario usuario)
    }

    class Login {
        -JTextField txtCorreo
        -JPasswordField txtContraseña
        -JButton btnLogin
        -JButton btnRegistro
        -JLabel lblTitulo, lblCorreo, lblContraseña
        -JPanel panelFondo
        -UsuarioController usuarioController
        +Login()
        +JTextField getTxtCorreo()
        +JPasswordField getTxtContraseña()
        +JButton getBtnLogin()
        +JButton getBtnRegistro()
    }

    class Registro {
        -JTextField txtNombre, txtCorreo
        -JPasswordField txtContraseña
        -JButton btnRegistrar, btnSeleccionarImagen
        -JLabel lblFondo
        -JCheckBox chkAdmin
        -File imagenPerfil
        +Registro()
        +void registrarUsuario()
    }

    class MenuPrincipal {
        +MenuPrincipal()
    }

    class ConexionDB {
        +static Connection getConection()
    }

    UsuarioController --> UsuarioDAO : usa
    UsuarioController --> HistorialDAO : usa
    UsuarioController --> Login : controla
    UsuarioController --> Registro : controla
    Login --> UsuarioController : usa
    Registro --> UsuarioController : usa
    UsuarioDAO --> ConexionDB : usa
    HistorialDAO --> ConexionDB : usa
    Usuario "1" -- "0..*" Historial : genera
    Usuario "1" -- "0..*" Reserva : realiza
    Usuario "1" -- "0..*" ValoracionRuta : deja
    Usuario "1" -- "0..*" ValoracionRestaurante : deja
    Usuario "1" -- "0..*" Mensaje : envía
    Usuario "1" -- "0..*" Restaurante : recomienda
    Ruta "1" -- "0..*" Reserva : es reservada en
    Ruta "1" -- "0..*" ValoracionRuta : es valorada en
    Restaurante "1" -- "0..*" ValoracionRestaurante : es valorado en
    Usuario "1" o-- "0..*" Historial : tiene
    Usuario "1" o-- "0..*" Restaurante : visita    