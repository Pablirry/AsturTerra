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
    dificultad INT CHECK(dificultad BETWEEN 1 AND 5)
);

-- Tabla de reservas
CREATE TABLE reservas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    id_ruta INT,
    fecha DATE,
    confirmada BOOLEAN NOT NULL DEFAULT FALSE,
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
    especialidad VARCHAR(100),
    imagen LONGBLOB,
    descripcion TEXT
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
    usuarioNombre VARCHAR(50),
    mensaje TEXT,
    respuesta TEXT,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
    FOREIGN KEY (usuarioNombre) REFERENCES usuarios(nombre)
);

-- Tabla de eventos
CREATE TABLE eventos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    ubicacion VARCHAR(255),
    tipo VARCHAR(50),
    precio DECIMAL(10,2),
    imagen LONGBLOB
);

-- Tabla de valoraciones de eventos
CREATE TABLE valoraciones_eventos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    id_evento INT,
    puntuacion INT CHECK(puntuacion BETWEEN 1 AND 5),
    comentario TEXT,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    FOREIGN KEY (id_evento) REFERENCES eventos(id)
);

-- Tabla reservas eventos
CREATE TABLE reservas_eventos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    evento_id INT NOT NULL,
    fecha_reserva TIMESTAMP NOT NULL,
    confirmada BOOLEAN DEFAULT FALSE
);