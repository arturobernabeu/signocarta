package es.fcc.signocarta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import es.fcc.signocarta.controller.entrada.EntradaContacto;

@Service
public class ContactoService {


	@Autowired
	private JavaMailSender mailSender;
	

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
			resultado = "Error al enviar el mensaje"+ e.getMessage();
		}
		return resultado;

	}

}
