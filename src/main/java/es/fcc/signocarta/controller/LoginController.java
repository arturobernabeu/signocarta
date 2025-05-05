package es.fcc.signocarta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.fcc.signocarta.controller.entrada.EntradaAcceso;
import es.fcc.signocarta.controller.util.Validation;
import es.fcc.signocarta.modelo.Usuario;
import es.fcc.signocarta.service.LoginService;
import es.fcc.signocarta.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/**
 * Controlador encargado de gestionar el proceso de autenticación de usuarios.
 * Permite acceder al formulario de login, validar credenciales, gestionar
 * sesiones y cerrar sesión.
 */
@Controller
public class LoginController {

	private final LoginService loginService;
	private final UsuarioService usuarioService;

	/**
	 * Constructor que inyecta los servicios necesarios para la autenticación.
	 *
	 * @param loginService   Servicio para la validación de credenciales.
	 * @param usuarioService Servicio para el acceso y gestión de usuarios.
	 */
	public LoginController(LoginService loginService, UsuarioService usuarioService) {
		super();
		this.loginService = loginService;
		this.usuarioService = usuarioService;
	}

	/**
	 * Muestra la pantalla de login. Invalida cualquier sesión previa activa para
	 * evitar conflictos de sesión.
	 *
	 * @param session Sesión HTTP.
	 * @param model   Modelo de atributos para la vista.
	 * @return La vista "login".
	 */
	@RequestMapping(value = { "/", "/login" })
	public String login(HttpSession session, Model model) {
		// si accedemos en cualquier momento a la pantalla de login, se debe cerrar
		// sesión, no mantener la sesión anterior iniciada
		session.invalidate();

		model.addAttribute("EntradaAcceso", new EntradaAcceso()); // añadiremos al modelo un EntradaAcceso vacio para //
																	// poder rellenarlo con los campos del formulario
		return "login";
	}

	/**
	 * Procesa el formulario de login. Valida si el usuario existe (ya sea por email
	 * o por nombre de trabajador) y verifica la contraseña. Si las credenciales son
	 * correctas, establece la sesión y redirige a la vista principal.
	 *
	 * @param entradaAcceso Datos introducidos por el usuario en el formulario.
	 * @param result        Resultado de la validación de los datos.
	 * @param model         Modelo para pasar atributos a la vista.
	 * @param session       Sesión HTTP para almacenar el ID del usuario
	 *                      autenticado.
	 * @return Redirección a "index" si el login es exitoso, o vuelve a "login" en
	 *         caso contrario.
	 */
	@PostMapping("/acceso")
	public String acceso(@Valid @ModelAttribute("EntradaAcceso") EntradaAcceso entradaAcceso, BindingResult result,
			Model model, HttpSession session) {
		boolean credencialesOk = false;
		Usuario usuario;

		// Si hay errores, volver al formulario
		if (result.hasErrors()) {
			return "login"; // o el nombre de tu vista
		}
		// comprobamos si es un email
		if (Validation.isEmail(entradaAcceso.getNombreAcceso())) {
			// si es un email ahora comprobaremos si está reguistrado en la base de datos
			if (usuarioService.existeEmailUsuario(entradaAcceso.getNombreAcceso())) {
				usuario = usuarioService.obtenerUsuarioAplicacion(entradaAcceso.getNombreAcceso()).get();// alamacenamos
																											// usuario
																											// aplicacion
				credencialesOk = true;
			} else {
				result.rejectValue("nombreAcceso", "error.email", "El email no está registrado");
				return "login";
			} // sino comprobaremos si es un trabajador
		} else if (usuarioService.isTrabajador(entradaAcceso.getNombreAcceso())) {
			usuario = usuarioService.obtenerUsuarioTrabajador(entradaAcceso.getNombreAcceso()).get();// alamacenamos
																										// usuario
																										// trabajador
			credencialesOk = true;
		} else {
			// si tampoco es un trabajador entonces el usuario no existe
			result.rejectValue("nombreAcceso", "error.nombreAcceso",
					"No hay ningun usuario registrado con ese nombre de acceso");
			return "login";
		}

		// una vez tenemos identificado al usuario, comprobaremos la contraseña
		if (credencialesOk == true) {
			if (loginService.passwordOK(entradaAcceso.getNombreAcceso(), entradaAcceso.getPassword())) {
				session.setAttribute("usuarioId", usuario.getId().longValue());

				Usuario usuarioLogado = usuarioService.buscarPorId(usuario.getId().longValue()).get();// almaceno el
																										// usuario
																										// logado
				// paso el idRol al modelo para controlar las opciones que tienen que aparecer
				// en el menu lateral
				int rolId = usuarioLogado.getRol().getId();
				model.addAttribute("rolId", rolId);
				return "index";
			} else {
				result.rejectValue("password", "error.password", "La contraseña es incorrecta");
				return "login";
			}
		} else
			return "login";
	}

	/**
	 * Cierra la sesión del usuario y redirige a la pantalla de login con un
	 * indicador de cierre de sesión.
	 *
	 * @param session Sesión actual del usuario.
	 * @return Redirección al login.
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // borra todo
		return "redirect:/login?logout";
	}

}
