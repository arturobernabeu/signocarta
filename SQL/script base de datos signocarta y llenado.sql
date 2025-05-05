/* CREACION DE BASE DE DATOS  */
CREATE DATABASE IF NOT EXISTS SIGNOCARTA;

/* ENTRAMOS EN LA BASE DE DATOS */
USE SIGNOCARTA;

/* CREAMOS TABLA ROL*/ 
CREATE TABLE IF NOT EXISTS `rol` (
  `id` int NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/* CREAMOS TABLA USUARIO*/ 
CREATE TABLE IF NOT EXISTS `usuario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `apellidos` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `id_rol` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_usuario_rol` (`id_rol`),
  CONSTRAINT `fk_usuario_rol` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/* CREAMOS TABLA USUARIO_TRABAJADOR*/ 
CREATE TABLE IF NOT EXISTS `usuario_trabajador` (
  `cod_trabajador` varchar(255) NOT NULL UNIQUE,
  `puesto` varchar(255) DEFAULT NULL,
  `id` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_trabajador_usuario` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci; 

/* CREAMOS TABLA USUARIO_APLICACION*/ 
CREATE TABLE IF NOT EXISTS `usuario_aplicacion` (
  `email` varchar(255) NOT NULL UNIQUE,
  `id` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_usuarioapp_usuario` FOREIGN KEY (`id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/* CREAMOS TABLA ESTADO*/ 
CREATE TABLE IF NOT EXISTS `estado` (
  `id` int NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/* CREAMOS TABLA SOLICITUD*/ 
CREATE TABLE IF NOT EXISTS `solicitud` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fecha_registro` datetime(6) DEFAULT NULL,
  `observaciones` varchar(400) DEFAULT NULL,
  `id_estado` int DEFAULT NULL,
  `id_usuario_app` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `inx_solicitud_estado` (`id_estado`),
  KEY `idx_solicitud_usuarioApp` (`id_usuario_app`),
  CONSTRAINT `fk_solicitud_estado` FOREIGN KEY (`id_estado`) REFERENCES `estado` (`id`),
  CONSTRAINT `fk_solicitud_usuarioApp` FOREIGN KEY (`id_usuario_app`) REFERENCES `usuario_aplicacion` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/* CREAMOS TABLA DOCUMENTO*/ 
CREATE TABLE IF NOT EXISTS `documento` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `origen` varchar(255) DEFAULT NULL,
  `ruta` varchar(400) DEFAULT NULL,
  `id_solicitud` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_solicitud_documento` (`id_solicitud`),
  CONSTRAINT `fk_solicitud_documento` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitud` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/* CREAMOS TABLA ASIGNACION*/ 
CREATE TABLE IF NOT EXISTS `asignacion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_solicitud` int DEFAULT NULL,
  `id_trabajador` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_inx_asignacion_solicitud` (`id_solicitud`),
  UNIQUE KEY `uq_inx_asignacion_trabajador` (`id_trabajador`),
  CONSTRAINT `fk_asignacion_solicitud` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitud` (`id`),
  CONSTRAINT `fk_asignacion_usuarioTrabajador` FOREIGN KEY (`id_trabajador`) REFERENCES `usuario_trabajador` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/* CREAMOS TABLA PRESUPUESTO*/ 
CREATE TABLE IF NOT EXISTS `presupuesto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `estado_presupuesto` varchar(255) DEFAULT NULL,
  `fecha_registro` datetime(6) DEFAULT NULL,
  `fecha_pago` datetime(6) DEFAULT NULL,
  `importe` double NOT NULL,
  `metodo_pago` varchar(255) DEFAULT NULL,
  `id_solicitud` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_inx_presupuesto_solicitud` (`id_solicitud`),
  CONSTRAINT `fk_presupuesto_solicitud` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitud` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/* CREAMOS TABLA HISTORICO*/ 
CREATE TABLE IF NOT EXISTS `historico` (
  `id` int NOT NULL AUTO_INCREMENT,
  `evento` varchar(255) DEFAULT NULL,
  `fecha_registro` datetime(6) DEFAULT NULL,
  `id_estado` int NOT NULL,
  `id_solicitud` int DEFAULT NULL,
  `id_trabajador` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_historico_estado` (`id_estado`),
  KEY `fk_historico_solicitud` (`id_solicitud`),
  KEY `fk_historico_usuarioTrabajador` (`id_trabajador`),
  CONSTRAINT `fk_historico_estado` FOREIGN KEY (`id_estado`) REFERENCES `estado` (`id`),
  CONSTRAINT `fk_historico_solicitud` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitud` (`id`),
  CONSTRAINT `fk_historico_usuarioTrabajador` FOREIGN KEY (`id_trabajador`) REFERENCES `usuario_trabajador` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



/* LLENADO DE DATOS */ 

INSERT INTO ROL VALUES('1','Administrador');
INSERT INTO ROL VALUES('2','Trabajador');
INSERT INTO ROL VALUES('3','UsuarioApp');



INSERT INTO USUARIO (apellidos, nombre, password, id_rol) VALUES('Calatayud Castillo','Paqui','Admin003@','1');
SET @last_id = LAST_INSERT_ID();
INSERT INTO USUARIO_TRABAJADOR VALUES ('Admin01', 'ADMINISTRADOR', @last_id);

INSERT INTO USUARIO (apellidos, nombre, password, id_rol) VALUES('Mendoza Sola','Clara','Casti89&','2');
SET @last_id = LAST_INSERT_ID();
INSERT INTO USUARIO_TRABAJADOR VALUES ('Trab02', 'INTERPRETE01', @last_id);

INSERT INTO USUARIO (apellidos, nombre, password, id_rol) VALUES('Antunez Vega','Antonio','Casti89&','3');
SET @last_id = LAST_INSERT_ID();
INSERT INTO USUARIO_APLICACION VALUES ('antonio@hotmail.com', @last_id);

INSERT INTO USUARIO (apellidos, nombre, password, id_rol) VALUES('Fernández López', 'María', 'MariaPass123', '3');
SET @last_id = LAST_INSERT_ID();
INSERT INTO USUARIO_APLICACION VALUES ('mariafl@gmail.com', @last_id);

INSERT INTO USUARIO (apellidos, nombre, password, id_rol) VALUES('Gutiérrez Pérez', 'Carlos','C4rlosG!', '3');
SET @last_id = LAST_INSERT_ID();
INSERT INTO USUARIO_APLICACION VALUES ('cgp02@gmail.com', @last_id);

INSERT INTO USUARIO (apellidos, nombre, password, id_rol) VALUES( 'Martínez Ruiz', 'Laura', 'LauMtz_99', '3');
SET @last_id = LAST_INSERT_ID();
INSERT INTO USUARIO_APLICACION VALUES ('lmartinez@gmail.com', @last_id);

INSERT INTO USUARIO (apellidos, nombre, password, id_rol) VALUES('Sánchez Torres','Pedro', 'Pedrito2024*', '3');
SET @last_id = LAST_INSERT_ID();
INSERT INTO USUARIO_APLICACION VALUES ('pedrito2000@hotmail.com', @last_id);

INSERT INTO ESTADO VALUES ('1','Enviada');
INSERT INTO ESTADO VALUES ('2','Pendiente presupuesto');
INSERT INTO ESTADO VALUES ('3','Pendiente pago');
INSERT INTO ESTADO VALUES ('4','Presupuesto rechazado');
INSERT INTO ESTADO VALUES ('5','Pagado');
INSERT INTO ESTADO VALUES ('6','Gestionando solicitud');
INSERT INTO ESTADO VALUES ('7','Gestionada / Finalizada');

INSERT INTO `solicitud` VALUES (1, '2025-05-04 23:14:43.737000', 'Necesito aclaración del contrato ', 6, 7);
INSERT INTO `solicitud` VALUES (2, '2025-05-04 23:18:52.073000', 'Necesito interpretación por favor. ', 3, 3);

INSERT INTO `presupuesto` VALUES (1, 'Presupuesto aceptado', '2025-05-04 23:15:09.878000', '2025-05-04 23:15:33.977000', 20, 'Tarjeta', 1);
INSERT INTO `presupuesto` VALUES (2, 'Presupuesto realizado', '2025-05-04 23:20:15.849000', NULL, 5, NULL, 2);

INSERT INTO `documento` VALUES (1, 'Contrato_Pedrito.pdf', 'E', 'archivosUsuarios\\Contrato_Pedrito.pdf', 1);
INSERT INTO `documento` VALUES (2, 'Multa_Antonio.pdf', 'E', 'archivosUsuarios\\Multa_Antonio.pdf', 2);

INSERT INTO `historico` VALUES (1, 'Solicitud creada', '2025-05-04 23:14:43.737000', 1, 1, NULL);
INSERT INTO `historico` VALUES (2, 'Solicitud asignada para presupuestar', '2025-05-04 23:14:58.892000', 2, 1, 1);
INSERT INTO `historico` VALUES (3, 'Se ha creado presupuesto de la solicitud', '2025-05-04 23:15:09.913000', 3, 1, 1);
INSERT INTO `historico` VALUES (4, 'Presupuesto aceptado y pagado', '2025-05-04 23:15:33.987000', 5, 1, NULL);
INSERT INTO `historico` VALUES (5, 'Solicitud asignada para gestionar', '2025-05-04 23:15:52.140000', 6, 1, 1);
INSERT INTO `historico` VALUES (6, 'Solicitud creada', '2025-05-04 23:18:52.073000', 1, 2, NULL);
INSERT INTO `historico` VALUES (7, 'Solicitud asignada para presupuestar', '2025-05-04 23:20:03.467000', 2, 2, 2);
INSERT INTO `historico` VALUES (8, 'Se ha creado presupuesto de la solicitud', '2025-05-04 23:20:15.907000', 3, 2, 2);


-- Usuario Administrador (Todos los permisos)
CREATE USER 'ADMIN_USER'@'%' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON signocarta.* TO 'ADMIN_USER'@'%';
 
-- Usuario Escritura (INSERT, UPDATE, DELETE, pero sin modificar estructura)
CREATE USER 'WRITER_USER'@'%' IDENTIFIED BY 'us_escritura';
GRANT INSERT, UPDATE, DELETE ON signocarta.* TO 'WRITER_USER'@'%';
 
-- Usuario Solo Lectura (SELECT)
CREATE USER 'READER_USER'@'%' IDENTIFIED BY 'us_lectura';
GRANT SELECT ON signocarta.* TO 'READER_USER'@'%';
 
-- Aplicar cambios
FLUSH PRIVILEGES;


