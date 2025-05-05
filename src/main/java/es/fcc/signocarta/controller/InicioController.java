package es.fcc.signocarta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import es.fcc.signocarta.modelo.Usuario;
import es.fcc.signocarta.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador encargado de gestionar el acceso a la página de inicio de la
 * aplicación. Verifica que el usuario esté autenticado antes de mostrar la
 * vista principal.
 */
@Controller
public class InicioController {

	private final UsuarioService usuarioService;

	/**
	 * Constructor que inyecta el servicio de usuarios.
	 * 
	 * @param usuarioService Servicio para la gestión de usuarios.
	 */
	public InicioController(UsuarioService usuarioService) {
		super();
		this.usuarioService = usuarioService;
	}

	/**
	 * Muestra la página de inicio principal de la aplicación si el usuario está
	 * logado.
	 * 
	 * @param session Sesión HTTP para obtener el ID del usuario autenticado.
	 * @param model   Modelo para pasar atributos a la vista.
	 * @return la vista "index" si el usuario está autenticado, o redirige a
	 *         "/login" en caso contrario.
	 */
	@GetMapping("/index")
	public String inicio(HttpSession session, Model model) {
		Long usuarioId = (Long) session.getAttribute("usuarioId");
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

		model.addAttribute("paginaActual", "index");

		return "index";
	}

}
