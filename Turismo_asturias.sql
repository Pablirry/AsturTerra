DROP DATABASE turismo_asturias;
CREATE DATABASE turismo_asturias;
USE turismo_asturias;

-- Tabla de usuarios
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50),
    correo VARCHAR(50) UNIQUE,
    contrasena VARCHAR(255),
    tipo ENUM('cliente', 'admin'),
    imagen_perfil LONGBLOB
);

-- Tabla de rutas
CREATE TABLE rutas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    descripcion TEXT,
    imagen LONGBLOB,
    precio DECIMAL(10,2),
    dificultad ENUM('Fácil', 'Media', 'Difícil')
);

-- Tabla de reservas
CREATE TABLE reservas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    id_ruta INT,
    fecha DATE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    FOREIGN KEY (id_ruta) REFERENCES rutas(id)
);

-- Tabla de valoraciones de rutas
CREATE TABLE valoraciones_rutas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    id_ruta INT,
    puntuacion INT CHECK(puntuacion BETWEEN 1 AND 5),
    comentario TEXT,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    FOREIGN KEY (id_ruta) REFERENCES rutas(id)
);

-- Tabla de restaurantes
CREATE TABLE restaurantes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    ubicacion VARCHAR(255),
    valoracion FLOAT,
    imagen LONGBLOB
);

-- Tabla de valoraciones de restaurantes
CREATE TABLE valoraciones_restaurantes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    id_restaurante INT,
    puntuacion INT CHECK(puntuacion BETWEEN 1 AND 5),
    comentario TEXT,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    FOREIGN KEY (id_restaurante) REFERENCES restaurantes(id)
);

-- Tabla de historial
CREATE TABLE historial (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    accion VARCHAR(255),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

-- Tabla de mensajes
CREATE TABLE mensajes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    mensaje TEXT,
    respuesta TEXT,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);