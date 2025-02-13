USE SIGNOCARTA;

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

INSERT INTO SOLICITUD (observaciones, id_estado, fecha_registro, id_usuario_app) VALUES ('Gracias','3', now(),  (select id from USUARIO_APLICACION where email='mariafl@gmail.com')); 
SET @last_id = LAST_INSERT_ID();
INSERT INTO DOCUMENTO (origen, ruta, id_solicitud) VALUES ('entrada', 'archivo.pdf',@last_id);

INSERT INTO ASIGNACION (id_solicitud, id_trabajador) VALUES (@last_id, (select id from USUARIO_TRABAJADOR where cod_trabajador='Trab02'));

INSERT INTO PRESUPUESTO (estado_presupuesto, fecha_registro, fecha_pago, importe, metodo_pago, id_solicitud) VALUES ('Presupuesto realizado', now(), NULL,  25.00, NULL, @last_id);

INSERT INTO HISTORICO (evento, fecha_registro, id_estado, id_solicitud, id_trabajador) VALUES ('solicitud creada', now(),'1', @last_id, NULL);
INSERT INTO HISTORICO (evento, fecha_registro, id_estado, id_solicitud, id_trabajador) VALUES ('solicitud asignada a trabajador Trab02', now(), '1', @last_id,  (select id from USUARIO_TRABAJADOR where cod_trabajador='Trab02'));


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





