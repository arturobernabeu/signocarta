package es.fcc.signocarta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.fcc.signocarta.controller.entrada.EntradaRegistro;
import es.fcc.signocarta.controller.util.Validation;
import es.fcc.signocarta.service.UsuarioService;
import jakarta.validation.Valid;

@Controller
public class RegistroController {
	
	
	private final UsuarioService usuarioService;

	public RegistroController(UsuarioService usuarioService) {
		super();
		this.usuarioService = usuarioService;
	}

	@RequestMapping("/registro")
	public String registro(Model model) {
		model.addAttribute("solicitudRegistro", new EntradaRegistro()); // añadiremos al modelo un  SolicitudRegistro vacio para poder rellenarlo con los campos del formulario
		return "registro";
	}

	@PostMapping("/registro/crear")
	public String crearUsuario(@Valid EntradaRegistro solicitudRegistro, BindingResult result) {
		// Validar si las contraseñas coinciden
		if (!solicitudRegistro.getPassword().equals(solicitudRegistro.getPasswordRepeat())) {
			result.rejectValue("passwordRepeat", "error.passwordRepeat", "Las contraseñas no coinciden");
		}

		// Si hay errores, volver al formulario
		if (result.hasErrors()) {
			return "/registro"; // o el nombre de tu vista
		}
		// comprobamos si es un email válido antes de poder hacer el registro
		if(!Validation.isEmail(solicitudRegistro.getEmail())) {
			result.rejectValue("email", "error.email", "Escriba un email válido");
			return "registro";
		}
		// comprobamos si el email ya está registrado
		if (usuarioService.existeEmailUsuario(solicitudRegistro.getEmail())) {
			result.rejectValue("email", "error.email", "El email ya está registrado");
			return "registro";
		}	
		// si todo va bien, guardamos el registro
		usuarioService.guardar(solicitudRegistro);		
		return "redirect:/";
	}
}
