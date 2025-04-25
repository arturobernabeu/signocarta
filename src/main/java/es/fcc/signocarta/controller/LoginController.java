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

@Controller
public class LoginController {

	private final LoginService loginService;
	private final UsuarioService usuarioService;

	public LoginController(LoginService loginService, UsuarioService usuarioService) {
		super();
		this.loginService = loginService;
		this.usuarioService = usuarioService;
	}

	@RequestMapping(value = { "/", "/login" })
	public String login(HttpSession session, Model model) {
		// si accedemos en cualquier momento a la pantalla de login, se debe cerrar
		// sesión, no mantener la sesión anterior iniciada
		session.invalidate();

		model.addAttribute("EntradaAcceso", new EntradaAcceso()); // añadiremos al modelo un EntradaAcceso vacio para //
																	// poder rellenarlo con los campos del formulario
		return "login";
	}

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
		} else if (loginService.isTrabajador(entradaAcceso.getNombreAcceso())) {
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

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // borra todo
		return "redirect:/login?logout";
	}

}
