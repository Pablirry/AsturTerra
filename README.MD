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
✅ **Exportación de datos** a **PDF y Excel**. --> Implementacion futura
✅ **Modo oscuro/claro** para la interfaz gráfica.  

mejoras que hacer 

9. Mejoras en reservas y valoraciones
Evitar reservas duplicadas para la misma ruta y usuario.
Mostrar la media de valoraciones en tiempo real y permitir ver comentarios de otros usuarios.
Permitir modificar/cancelar valoraciones propias.
10. Documentación y ayuda
Manual de usuario accesible desde la app.
Tooltips en botones y campos para explicar su función.
Estas mejoras harán tu aplicación más robusta, profesional y agradable de usar tanto para usuarios como para administradores.

---

# 📂 **Estructura del Proyecto**
📁 TurismoAsturias  
├── 📂 src  
│ ├── 📂 config → Conexión a la base de datos  
│ ├── 📂 dao → Acceso a la base de datos (CRUD)  
│ ├── 📂 modelo → Clases de datos (Usuario, Ruta, Reserva, etc.)  
│ ├── 📂 vista → Interfaces gráficas (Swing)  
│ ├── 📂 services → Lógica  
├── 📂 assets → Imágenes de prueba  
├── 📜 README.md → Documentación del proyecto  
├── 📜 TurismoAsturias.sql → Base de datos MySQL  

# 📜 **Diagrama de Clases**

![Diagrama de Clases](/assets/DiagramaDeClases.png)
    
# 📜 **Diagrama casos de uso**

![Diagrama de Casos](/assets/diagramaCasosDeUso.png)

# 📜 **Diagrama ER**

![Diagrama de Entidad-Relacion](/assets/DiagramaER.png)

---
# 🛠️ **Instalación y Configuración**

## 📌 **1. Clonar el repositorio**
```bash
git clone https://github.com/Pablirry/TurismoApp.git

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
