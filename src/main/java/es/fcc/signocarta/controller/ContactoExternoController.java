package es.fcc.signocarta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.fcc.signocarta.controller.entrada.EntradaContacto;
import es.fcc.signocarta.controller.util.Validation;
import es.fcc.signocarta.service.ContactoService;
import jakarta.validation.Valid;

@Controller
public class ContactoExternoController {

	private final ContactoService contactoService;

	public ContactoExternoController(ContactoService contactoService) {
		super();
		this.contactoService = contactoService;
	}

	@RequestMapping("/contacto_externo")
	public String contactoExterno(Model model) {
		model.addAttribute("entradaContacto", new EntradaContacto());
		return "contacto_externo";
	}

	@PostMapping("/contacto_externo/enviar")
	public String enviarContacto(@Valid EntradaContacto contacto, BindingResult result) {

		// Si hay errores, volver al formulario
		if (result.hasErrors()) {
			return "/contacto_externo"; // o el nombre de tu vista
		}
		// comprobamos si es un email válido antes de enviar el formulario
		if (!Validation.isEmail(contacto.getEmail())) {
			result.rejectValue("email", "error.email", "Escriba un email válido");
			return "contacto_externo";
		}
		// enviar formulario.....
		contactoService.enviarEmail(contacto);

		return "redirect:/";
	}
}
