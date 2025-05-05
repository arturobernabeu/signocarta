package es.fcc.signocarta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import es.fcc.signocarta.controller.entrada.EntradaContacto;
import es.fcc.signocarta.controller.entrada.EntradaRegistro;
import es.fcc.signocarta.modelo.Rol;
import es.fcc.signocarta.modelo.UsuarioAplicacion;
import es.fcc.signocarta.modelo.UsuarioTrabajador;
import es.fcc.signocarta.repository.RolRepository;
import es.fcc.signocarta.repository.TrabajadorRepository;
import es.fcc.signocarta.repository.UsuarioAppRepository;
import es.fcc.signocarta.repository.UsuarioRepository;
import es.fcc.signocarta.service.ContactoService;
import es.fcc.signocarta.service.LoginService;
import es.fcc.signocarta.service.PerfilService;
import es.fcc.signocarta.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTests {

	@Test
	void contextLoads() {
	}

	@Mock
	private UsuarioAppRepository usuarioAppRepository;

	@Mock
	private RolRepository rolRepository;

	@Mock
	private TrabajadorRepository trabajadorRepository;


	@InjectMocks
	private UsuarioService usuarioService;


	@Test
	@DisplayName("Guardar usuario correctamente")
	public void testGuardarUsuario() {
		EntradaRegistro entrada = new EntradaRegistro("Juan", "Perez", "juan@mail.com", "123456", "123456");
		Rol rol = new Rol();
		rol.setId(3);

		when(rolRepository.getReferenceById(3)).thenReturn(rol);
		when(usuarioAppRepository.save(any(UsuarioAplicacion.class))).thenAnswer(i -> i.getArguments()[0]);

		UsuarioAplicacion resultado = usuarioService.guardar(entrada);

		assertEquals("Juan", resultado.getNombre());
		assertEquals("juan@mail.com", resultado.getEmail());
		assertEquals("123456", resultado.getPassword());
		assertEquals(3, resultado.getRol().getId());
	}


	@Test
	@DisplayName("Validar trabajador existente")
	public void testIsTrabajador() {
		when(trabajadorRepository.existsByCodTrabajador("T001")).thenReturn(true);

		assertTrue(usuarioService.isTrabajador("T001"));
	}


	@Test
	@DisplayName("Lista trabajadores devuelve elementos")
	public void testListaTrabajadores() {
		List<UsuarioTrabajador> lista = Arrays.asList(new UsuarioTrabajador(), new UsuarioTrabajador());
		when(trabajadorRepository.findAll()).thenReturn(lista);

		List<UsuarioTrabajador> resultado = usuarioService.listaTrabajadores();
		assertEquals(2, resultado.size());
	}

}
