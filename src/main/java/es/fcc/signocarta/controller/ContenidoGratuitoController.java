package es.fcc.signocarta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import es.fcc.signocarta.modelo.Usuario;
import es.fcc.signocarta.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

@Controller
public class ContenidoGratuitoController {

	private final UsuarioService usuarioService;

	public ContenidoGratuitoController(UsuarioService usuarioService) {
		super();
		this.usuarioService = usuarioService;
	}

	@RequestMapping("/contenido-gratuito")
	public String contenidoGratuito(HttpSession session, Model model) {
		// controlamos que el usuario esté logado
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
		model.addAttribute("paginaActual", "contenido-gratuito");
		return "contenido-gratuito";
	}

}
