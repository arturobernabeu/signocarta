package es.fcc.signocarta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import es.fcc.signocarta.controller.entrada.EntradaContacto;

/**
 * Servicio responsable de gestionar el envío de correos electrónicos a través
 * del formulario de contacto externo.
 */
@Service
public class ContactoService {

	/**
	 * Componente de Spring para el envío de correos electrónicos.
	 */
	@Autowired
	private JavaMailSender mailSender;

	/**
	 * Envía un correo electrónico con los datos recibidos desde el formulario de
	 * contacto.
	 *
	 * @param entradaContacto Objeto que contiene los datos del formulario de
	 *                        contacto: nombre, email, motivo y mensaje.
	 * @return Mensaje de éxito si el envío se realizó correctamente, o mensaje de
	 *         error en caso de fallo.
	 */
	public String enviarEmail(EntradaContacto entradaContacto) {
		String resultado = "";
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo("paquicalatayudcastillo@gmail.com"); // destino
			message.setSubject("Nuevo mensaje de contacto");
			message.setText("Nombre: " + entradaContacto.getNombre() + "\nEmail: " + entradaContacto.getEmail()
					+ "\nAsunto:" + entradaContacto.getMotivo() + "\nMensaje:\n" + entradaContacto.getMensaje());
			mailSender.send(message);
			resultado = "Mensaje enviado correctamente";
		} catch (Exception e) {
			resultado = "Error al enviar el mensaje" + e.getMessage();
		}
		return resultado;

	}

}
