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
@startuml

Class Diagram

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
    


@enduml
    
```
# 📜 **Diagrama casos de uso**

```mermaid
graph TD;
    Usuario["🧑 Usuario"];
    Administrador["🔧 Administrador"];
    Sistema["💾 Sistema"];

    subgraph "Gestión de Usuarios"
        UC1["🔑 Iniciar sesión"];
        UC2["📝 Registrarse"];
        UC3["👤 Ver perfil"];
        UC4["✏️ Modificar perfil"];
    end

    subgraph "Gestión de Rutas Turísticas"
        UC5["📍 Explorar rutas"];
        UC6["🗺️ Reservar ruta"];
        UC7["❌ Cancelar reserva"];
        UC8["⭐ Valorar ruta"];
    end

    subgraph "Gestión de Restaurantes"
        UC9["🍽️ Explorar restaurantes"];
        UC10["👍 Recomendar restaurante"];
        UC11["⭐ Valorar restaurante"];
    end

    subgraph "Interacción con el Sistema"
        UC12["📩 Enviar mensaje"];
        UC13["📨 Recibir respuesta"];
        UC14["📜 Historial de acciones"];
    end

    subgraph "Gestión de Administración"
        UC15["🛠️ Administrar usuarios"];
        UC16["🗺️ Administrar rutas"];
        UC17["🏨 Administrar restaurantes"];
        UC18["📊 Consultar estadísticas"];
    end

    Usuario -->|Usa| UC1;
    Usuario -->|Usa| UC2;
    Usuario -->|Usa| UC3;
    Usuario -->|Usa| UC4;
    Usuario -->|Usa| UC5;
    Usuario -->|Usa| UC6;
    Usuario -->|Usa| UC7;
    Usuario -->|Usa| UC8;
    Usuario -->|Usa| UC9;
    Usuario -->|Usa| UC10;
    Usuario -->|Usa| UC11;
    Usuario -->|Usa| UC12;
    Usuario -->|Usa| UC13;
    Usuario -->|Consulta| UC14;

    Administrador -->|Gestiona| UC15;
    Administrador -->|Gestiona| UC16;
    Administrador -->|Gestiona| UC17;
    Administrador -->|Consulta| UC18;

    Sistema -->|Registra acciones| UC14;

    UC2 --> UC1;
    UC6 --> UC5;
    UC7 -.-> UC6;
    UC8 --> UC6;
    UC10 --> UC9;
    UC11 --> UC9;
    UC13 --> UC12;
    UC15 --> UC1;
    UC16 --> UC5;
    UC17 --> UC9;
```
# 📜 **Diagrama ER**
```mermaid

erDiagram
    %% Definición de entidades
    USUARIO {
        int id PK
        String nombre
        String correo
        String contrasena
        String tipo
        byte[] imagenPerfil
    }
    
    RUTA {
        int id PK
        String nombre
        String descripcion
        byte[] imagen
        double precio
        String dificultad
    }
    
    RESERVA {
        int id PK
        int idUsuario FK
        int idRuta FK
        Date fecha
    }
    
    VALORACION_RUTA {
        int id PK
        int idUsuario FK
        int idRuta FK
        int puntuacion
        String comentario
    }
    
    RESTAURANTE {
        int id PK
        String nombre
        String ubicacion
        float valoracion
        byte[] imagen
    }
    
    VALORACION_RESTAURANTE {
        int id PK
        int idUsuario FK
        int idRestaurante FK
        int puntuacion
        String comentario
    }
    
    HISTORIAL {
        int id PK
        int idUsuario FK
        String accion
        Date fecha
    }
    
    MENSAJE {
        int id PK
        int idUsuario FK
        String mensaje
        String respuesta
        Date fecha
    }
    
    %% Relaciones
    USUARIO ||--o{ HISTORIAL : genera
    USUARIO ||--o{ RESERVA : realiza
    USUARIO ||--o{ VALORACION_RUTA : deja
    USUARIO ||--o{ VALORACION_RESTAURANTE : deja
    USUARIO ||--o{ MENSAJE : envia
    USUARIO ||--o{ RESTAURANTE : valora

```

---
# 🛠️ **Instalación y Configuración**

## 📌 **1. Clonar el repositorio**
```bash
git clone https://github.com/Pablirry/TurismoAsturias.git

```
## 📌 **2. Configurar la base de datos**

### 1. Importar el archivo TurismoAsturias.sql en MySQL:
```bash
CREATE DATABASE turismo_asturias;
USE turismo_asturias;
SOURCE TurismoAsturias.sql;
```

### 2. Configurar la conexión en src/config/ConexionBD.java:
```bash
private static final String URL = "jdbc:mysql://localhost:3306/turismo_asturias";
private static final String USER = "root";
private static final String PASSWORD = "tu_contraseña";
```

## 📌 **3. Compilar y ejecutar el proyecto**
Abrir el proyecto en Eclipse o NetBeans.  
Asegurar que los drivers JDBC para MySQL están configurados.  
Ejecutar la clase Main.java para iniciar la aplicación.

### 📸 Capturas de Pantalla
📍 Menú Principal  
📍 Gestión de Rutas  
📍 Reservas y Valoraciones  
📍 Modo Oscuro y Exportación de Datos  

(Se agregarán imágenes después de la implementación completa de las vistas)  

### 🧑‍💻 Tecnologías Utilizadas
<table> <tr> <td align="center"><img src="https://cdn-icons-png.flaticon.com/128/226/226777.png" width="50px"><br>Java (Swing)</td> <td align="center"><img src="https://cdn-icons-png.flaticon.com/128/5968/5968313.png" width="50px"><br>MySQL</td> <td align="center"><img src="https://cdn-icons-png.flaticon.com/128/733/733553.png" width="50px"><br>GitHub</td> </tr> <tr> <td align="center"><img src="https://cdn-icons-png.flaticon.com/128/9167/9167862.png" width="50px"><br>JDBC</td> <td align="center"><img src="https://cdn-icons-png.flaticon.com/128/4325/4325937.png" width="50px"><br>Apache POI</td> <td align="center"><img src="https://cdn-icons-png.flaticon.com/128/732/732220.png" width="50px"><br>iText (PDF)</td> </tr> </table> 

### 📩 Contacto y Soporte
Si tienes alguna pregunta o sugerencia, contacta a:  
📧 pablolopezruiz1@gmail.com  
🔗 GitHub: Pablirry
