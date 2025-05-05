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

/**
 * Controlador para gestionar el formulario de contacto externo. Esta versión
 * del formulario está disponible sin necesidad de iniciar sesión.
 */
@Controller
public class ContactoExternoController {

	private final ContactoService contactoService;

	/**
	 * Constructor que inyecta el servicio de contacto.
	 *
	 * @param contactoService servicio utilizado para enviar correos desde el
	 *                        formulario.
	 */
	public ContactoExternoController(ContactoService contactoService) {
		super();
		this.contactoService = contactoService;
	}

	/**
	 * Muestra la vista del formulario de contacto externo.
	 * 
	 * @param model el modelo usado para enviar datos a la vista.
	 * @return el nombre de la vista "contacto_externo".
	 */
	@RequestMapping("/contacto_externo")
	public String contactoExterno(Model model) {
		model.addAttribute("entradaContacto", new EntradaContacto());
		return "contacto_externo";
	}

	/**
	 * Procesa el envío del formulario de contacto externo.
	 *
	 * @param contacto objeto que contiene los datos del formulario.
	 * @param result   resultado de la validación del formulario.
	 * @return redirige a la página de inicio si el envío fue exitoso, o retorna a
	 *         la vista del formulario si hay errores.
	 */
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
