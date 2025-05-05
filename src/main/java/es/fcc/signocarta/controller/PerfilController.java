package es.fcc.signocarta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.fcc.signocarta.controller.entrada.EntradaRegistro;
import es.fcc.signocarta.controller.util.Validation;
import es.fcc.signocarta.modelo.Usuario;
import es.fcc.signocarta.service.PerfilService;
import es.fcc.signocarta.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador encargado de gestionar la visualización y actualización del
 * perfil de usuario. Solo los usuarios autenticados pueden acceder a estas
 * funcionalidades.
 */
@Controller
public class PerfilController {

	private final UsuarioService usuarioService;
	private final PerfilService perfilService;

	/**
	 * Constructor que inyecta los servicios necesarios para la gestión de perfil.
	 *
	 * @param usuarioService Servicio de usuarios.
	 * @param perfilService  Servicio para operaciones relacionadas con el perfil.
	 */
	public PerfilController(UsuarioService usuarioService, PerfilService perfilService) {
		super();
		this.usuarioService = usuarioService;
		this.perfilService = perfilService;
	}

	/**
	 * Muestra la pantalla del perfil del usuario autenticado. Si no hay sesión
	 * activa, redirige al login.
	 *
	 * @param session Sesión HTTP que contiene el ID del usuario autenticado.
	 * @param model   Modelo de datos para la vista.
	 * @return La vista "perfil", o redirección a login si no hay sesión activa.
	 */
	@RequestMapping("/perfil")
	public String perfil(HttpSession session, Model model) {

		// controlamos que el usuario esté logado
		Object usuarioIdObj = session.getAttribute("usuarioId");
		Long usuarioId;
		usuarioId = (Long) usuarioIdObj;

		// Si el usuario no está logado no podrá acceder a la pantalla, sino que le
		// redirige a login.
		if (usuarioId == null) {
			return "redirect:/login";
		}
		Usuario usuarioLogado = usuarioService.buscarPorId(usuarioId).get();// almaceno el usuario logado
		// paso el idRol al modelo para controlar las opciones que tienen que aparecer
		// en el menu lateral
		int rolId = usuarioLogado.getRol().getId();
		model.addAttribute("rolId", rolId);

		model.addAttribute("usuario", perfilService.cargarDatosPerfil(usuarioLogado));
		model.addAttribute("paginaActual", "perfil");
		return "perfil";
	}

	/**
	 * Procesa la actualización del perfil del usuario autenticado. Si el usuario no
	 * está logado, redirige a login.
	 *
	 * @param datos   Objeto que contiene los datos del formulario de perfil.
	 * @param session Sesión HTTP para obtener el ID del usuario actual.
	 * @return Redirección a la pantalla de perfil con parámetro de confirmación.
	 */
	@PostMapping("/perfil/guardar")
	public String guardarPerfil(@ModelAttribute("usuario") EntradaRegistro datos, HttpSession session, BindingResult result, Model model) {

		Object usuarioIdObj = session.getAttribute("usuarioId");
		Long usuarioId;
		usuarioId = (Long) usuarioIdObj;

		if (usuarioId == null) {
			return "redirect:/login";
		}
		
		if (datos.getNombre().isBlank()) {
			result.rejectValue("nombre", "error.nombre", "No puede dejar su nombre en blanco");
			return "perfil";
		}
		
		if (datos.getApellidos().isBlank()) {
			result.rejectValue("apellidos", "error.apellidos", "No puede dejar sus apellidos en blanco");
			return "perfil";
		}
		
		if (datos.getEmail().isBlank()) {
			result.rejectValue("email", "error.email", "No puede dejar su email en blanco.");
			return "perfil";
		}
		
		if (!Validation.isEmail(datos.getEmail())) {
			result.rejectValue("email", "error.email", "Escriba un email válido");
			return "registro";
		}
		
		if (!datos.esPasswordModificada()) {
			if (!datos.esPasswordValida()) {
				result.rejectValue("password", "error.password", "La contraseña debe tener al menos 6 caracteres, una mayúscula y un número");
				return "perfil";
			}
			
			result.rejectValue("passwordRepeat", "error.passwordRepeat", "Las contraseñas no coinciden.");
			return "perfil";
		}
		
	
		// actualizamos datos del usuario
		perfilService.actualizarDatos(datos, usuarioId);

		return "redirect:/perfil?guardado";
	}

}
