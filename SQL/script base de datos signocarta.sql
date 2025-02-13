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