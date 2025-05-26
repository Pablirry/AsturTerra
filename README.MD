# AsturTerra

![Java](https://img.shields.io/badge/Java-21%2B-blue?logo=java)
![Swing](https://img.shields.io/badge/Swing-GUI-orange)
![MySQL](https://img.shields.io/badge/MySQL-8%2B-blue?logo=mysql)
![JDBC](https://img.shields.io/badge/JDBC-Connector-informational)
![License](https://img.shields.io/badge/license-MIT-green)
![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20Linux-lightgrey)

---

## Tabla de Contenidos

- [Descripción](#descripción)
- [Características](#características)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Requisitos](#requisitos)
- [Instalación y Configuración](#instalación-y-configuración)
- [Capturas de Pantalla](#capturas-de-pantalla)
- [Diagramas](#diagramas)
- [Contacto y Soporte](#contacto-y-soporte)

---

## Descripción

**AsturTerra** es una aplicación de escritorio desarrollada en Java (Swing) y MySQL para la gestión integral del turismo en Asturias. Permite a usuarios y administradores gestionar rutas turísticas, eventos, reservas, valoraciones, restaurantes y soporte, todo desde una interfaz moderna y adaptable a modo claro/oscuro.

---

## Características

- Registro e inicio de sesión de usuarios (Clientes(Movil))
- Gestión CRUD de **Rutas** con imágenes, precios y dificultad
- Gestión CRUD de **Restaurantes** y **Eventos** con valoraciones y reservas
- Reservas y cancelaciones de rutas y eventos
- Sistema de **Valoraciones** de rutas, restaurantes y eventos
- Chat en tiempo real entre usuarios y administradores (soporte)
- Registro de **Historial de actividades** de los usuarios
- **Modo oscuro/claro** para la interfaz gráfica
- **Evita reservas duplicadas** para la misma ruta/evento y usuario
- **Visualización de medias y comentarios** en valoraciones
- **Manual de usuario**

---

## Estructura del Proyecto
```
📁 TurismoApp  
├── 📂 src  
│   ├── 📂 config         # Conexión a la base de datos  
│   ├── 📂 dao            # Acceso a la base de datos (CRUD)  
│   ├── 📂 model          # Clases de datos (Usuario, Ruta, Reserva, etc.)  
│   ├── 📂 views          # Interfaces gráficas (Swing)  
│   ├── 📂 services       # Lógica de negocio  
├── 📂 assets             # Imágenes de prueba  
├── 📜 README.md          # Documentación del proyecto  
├── 📜 TurismoAsturias.sql # Script de base de datos MySQL  
```

---

## Tecnologías Utilizadas
<table>
  <tr>
        <td align="center"><img src="https://cdn-icons-png.flaticon.com/128/226/226777.png" width="50px"><br>Java (Swing)</td>
        <td align="center"><img src="https://cdn-icons-png.flaticon.com/128/5968/5968313.png" width="50px"><br>MySQL</td>
</tr>
<tr>
    <td align="center"><img src="https://cdn-icons-png.flaticon.com/128/733/733553.png" width="50px"><br>GitHub</td>
    <td align="center"><img src="https://cdn-icons-png.flaticon.com/128/9167/9167862.png" width="50px"><br>JDBC</td>
  </tr>
</table>

---

## Requisitos
- JDK 21 o superior
- MySQL Server
- IDE recomendado: Eclipse, NetBeans o VS Code
- Driver JDBC para MySQL (incluido en `/lib`)

---

## Instalación y Configuración

### 1. Clonar el repositorio
```bash
git clone https://github.com/Pablirry/TurismoApp.git
```

### 2. Configurar la base de datos

1. Importa el archivo `TurismoAsturias.sql` en tu MySQL:
    ```bash
    CREATE DATABASE turismo_asturias;
    USE turismo_asturias;
    SOURCE TurismoAsturias.sql;
    ```

2. Configura la conexión en `src/config/ConexionDB.java`:
    ```java
    private static final String URL = "jdbc:mysql://localhost:3306/turismo_asturias";
    private static final String USER = "root";
    private static final String PASSWORD = "tu_contraseña";
    ```

### 3. Compilar y ejecutar el proyecto
- Abre el proyecto en tu IDE favorito.
- Asegúrate de que el driver JDBC de MySQL esté en el classpath.
- Ejecuta la clase `Main.java` desde `src/Interface/Main.java`.

---

## Capturas de Pantalla
- Menú Principal  
- Gestión de Rutas  
- Gestión de Restaurantes  
- Gestión de Eventos  
- Reservas y Valoraciones  
- Chat de Soporte  
- Modo Oscuro

*(Las imágenes se agregarán tras la implementación final de las vistas)*

---

## Diagramas

### Diagrama de Clases
![Diagrama de Clases](/assets/DiagramaDeClases.png)

### Diagrama ER
![Diagrama de Entidad-Relacion](/assets/DiagramaER.png)

---

## Contacto y Soporte
¿Tienes dudas o sugerencias?  
📧 pablolopezruiz1@gmail.com  
🔗 GitHub: [Pablirry](https://github.com/Pablirry)

---
