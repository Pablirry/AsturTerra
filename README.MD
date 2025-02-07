# 🏔️ Turismo Asturias - Sistema de Gestión de Turismo

Este es un **sistema de gestión de turismo** desarrollado en **Java (Swing) con MySQL** para gestionar rutas turísticas, reservas, valoraciones, restaurantes y comunicación con soporte.

---

## 📌 **Características Principales**
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

## 📂 **Estructura del Proyecto**
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

---
## 🛠️ **Instalación y Configuración**

### 📌 **1. Clonar el repositorio**
```bash
git clone https://github.com/Pablirry/TurismoAsturias.git

```
### 📌 **2. Configurar la base de datos**

# 1. Importar el archivo TurismoAsturias.sql en MySQL:
CREATE DATABASE turismo_asturias;
USE turismo_asturias;
SOURCE TurismoAsturias.sql;

# 2. Configurar la conexión en src/config/ConexionBD.java:
private static final String URL = "jdbc:mysql://localhost:3306/turismo_asturias";
private static final String USER = "root";
private static final String PASSWORD = "tu_contraseña";

### 📌 **3. Compilar y ejecutar el proyecto**
Abrir el proyecto en Eclipse o NetBeans.
Asegurar que los drivers JDBC para MySQL están configurados.
Ejecutar la clase Main.java para iniciar la aplicación.

📸 Capturas de Pantalla
📍 Menú Principal
📍 Gestión de Rutas
📍 Reservas y Valoraciones
📍 Modo Oscuro y Exportación de Datos

(Se agregarán imágenes después de la implementación completa de las vistas)

🧑‍💻 Tecnologías Utilizadas
Java (Swing, AWT)
MySQL
JDBC (Java Database Connectivity)
Apache POI (para exportar a Excel)
iText (para exportar a PDF)
GitHub (control de versiones)

📩 Contacto y Soporte
Si tienes alguna pregunta o sugerencia, contacta a:
📧 pablolopezruiz1@gmail.com
🔗 GitHub: Pablirry