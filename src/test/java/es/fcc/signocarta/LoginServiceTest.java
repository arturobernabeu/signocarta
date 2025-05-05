package es.fcc.signocarta;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.fcc.signocarta.modelo.UsuarioAplicacion;
import es.fcc.signocarta.modelo.UsuarioTrabajador;
import es.fcc.signocarta.repository.TrabajadorRepository;
import es.fcc.signocarta.repository.UsuarioAppRepository;
import es.fcc.signocarta.service.LoginService;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

	@Mock
	private UsuarioAppRepository usuarioAppRepository;
	
	@InjectMocks
	private LoginService loginService;
	
	@Mock
	private TrabajadorRepository trabajadorRepository;
	
	@Test
	@DisplayName("Contraseña correcta para usuario app")
	public void testPasswordOKUsuarioApp() {
		UsuarioAplicacion usuario = new UsuarioAplicacion();
		usuario.setPassword("pass123");

		when(usuarioAppRepository.existsByEmail("test@mail.com")).thenReturn(true);
		when(usuarioAppRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(usuario));

		assertTrue(loginService.passwordOK("test@mail.com", "pass123"));
	}

	@Test
	@DisplayName("Contraseña incorrecta para trabajador")
	public void testPasswordOKTrabajadorIncorrecta() {
		UsuarioTrabajador trabajador = new UsuarioTrabajador();
		trabajador.setPassword("correcta");

		when(trabajadorRepository.findByCodTrabajador("Trab009")).thenReturn(Optional.of(trabajador));

		assertFalse(loginService.passwordOK("Trab009", "incorrecta"));
	}
}
