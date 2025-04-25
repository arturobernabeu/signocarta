package es.fcc.signocarta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.fcc.signocarta.controller.entrada.EntradaRegistro;
import es.fcc.signocarta.modelo.Usuario;
import es.fcc.signocarta.service.PerfilService;
import es.fcc.signocarta.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

@Controller
public class PerfilController {

	private final UsuarioService usuarioService;
	private final PerfilService perfilService;

	public PerfilController(UsuarioService usuarioService, PerfilService perfilService) {
		super();
		this.usuarioService = usuarioService;
		this.perfilService = perfilService;
	}

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
		// paso el idRol al modelo para controlar las opciones que tienen que aparecer en el menu lateral
		int rolId = usuarioLogado.getRol().getId();
		model.addAttribute("rolId", rolId);

		model.addAttribute("usuario", perfilService.cargarDatosPerfil(usuarioLogado));
		model.addAttribute("paginaActual", "perfil");
		return "perfil";
	}

	@PostMapping("/perfil/guardar")
	public String guardarPerfil(@ModelAttribute EntradaRegistro datos, HttpSession session) {

		Object usuarioIdObj = session.getAttribute("usuarioId");
		Long usuarioId;
		usuarioId = (Long) usuarioIdObj;

		if (usuarioId == null) {
			return "redirect:/login";
		}
		// actualizamos datos del usuario
		perfilService.actualizarDatos(datos, usuarioId);

		return "redirect:/perfil?guardado";
	}

}
