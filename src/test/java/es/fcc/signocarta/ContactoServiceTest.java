package es.fcc.signocarta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import es.fcc.signocarta.controller.entrada.EntradaContacto;
import es.fcc.signocarta.service.ContactoService;

@ExtendWith(MockitoExtension.class)
public class ContactoServiceTest {
	
	@Mock
	private JavaMailSender mailSender;
	
	@InjectMocks
	private ContactoService contactoService;
	
	@Test
	@DisplayName("Enviar email correctamente")
	public void testEnviarEmail() {
		EntradaContacto entrada = new EntradaContacto("Ana", "ana@mail.com", "Consulta", "Hola, tengo una duda");

		doNothing().when(mailSender).send(any(SimpleMailMessage.class));

		String resultado = contactoService.enviarEmail(entrada);

		assertEquals("Mensaje enviado correctamente", resultado);
	}

}
