package es.fcc.signocarta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.fcc.signocarta.controller.entrada.EntradaContacto;
import es.fcc.signocarta.controller.util.Validation;
import es.fcc.signocarta.modelo.Usuario;
import es.fcc.signocarta.service.ContactoService;
import es.fcc.signocarta.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class ContactoController {

	private final ContactoService contactoService;
	private final UsuarioService usuarioService;

	public ContactoController(ContactoService contactoService, UsuarioService usuarioService) {
		super();
		this.contactoService = contactoService;
		this.usuarioService = usuarioService;
	}

	@RequestMapping("/contacto")
	public String contacto(HttpSession session, Model model) {
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
		model.addAttribute("paginaActual", "contacto");
		model.addAttribute("entradaContacto", new EntradaContacto());
		return "contacto";
	}

	@PostMapping("/contacto/enviar")
	public String enviarContacto(@Valid EntradaContacto contacto, BindingResult result) {

		// Si hay errores, volver al formulario
		if (result.hasErrors()) {
			return "contacto"; // o el nombre de tu vista
		}
		// comprobamos si es un email válido antes de enviar el formulario
		if (!Validation.isEmail(contacto.getEmail())) {
			result.rejectValue("email", "error.email", "Escriba un email válido");
			return "contacto";
		}
		// enviar formulario.....
		contactoService.enviarEmail(contacto);
		return "index";
	}

}
