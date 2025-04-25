package es.fcc.signocarta.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import es.fcc.signocarta.controller.entrada.EntradaSolicitud;
import es.fcc.signocarta.controller.salida.SalidaHistorico;
import es.fcc.signocarta.modelo.Solicitud;
import es.fcc.signocarta.modelo.Usuario;
import es.fcc.signocarta.service.SolicitudService;
import es.fcc.signocarta.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class SolicitudesController {

	private final UsuarioService usuarioService;
	private final SolicitudService solicitudService;

	public SolicitudesController(UsuarioService usuarioService, SolicitudService solicitudService) {
		super();
		this.usuarioService = usuarioService;
		this.solicitudService = solicitudService;
	}

	@GetMapping("/solicitudes")
	public String solicitudes(HttpSession session, Model model) {
		// controlamos que el usuario esté logado
		Long usuarioId = (Long) session.getAttribute("usuarioId");
		// Si el usuario no está logado no podrá acceder a la pantalla, sino que le
		// redirige a login.
		if (usuarioId == null) {
			return "redirect:/login";
		}
		Usuario usuarioLogado = usuarioService.buscarPorId(usuarioId).get();// almaceno el usuario logado
		// paso el idRol al modelo para controlar las opciones que tienen que apareceren
		// el menu lateral
		int rolId = usuarioLogado.getRol().getId();
		model.addAttribute("rolId", rolId);
		//Añadimos al modelo entradaSolicitud que es donde se almacenaran los datos que recogemos de pantalla
		model.addAttribute("entradaSolicitud", new EntradaSolicitud());
		
		//vamos a almacenar en una lista las solicitudes que tiene el usuario
		List <Solicitud> listaSolicitudes = solicitudService.solicitudesPorUsuario(usuarioId.intValue());
		// como necesitamos pasar al modelo una lista de objetos SalidaHistorico, crearemos uno por cada solicitud y almacenaremos en una lista
		List <SalidaHistorico> listaSalida = new ArrayList<>();
		for(int i=0; i<listaSolicitudes.size();i++) {
			listaSalida.add(solicitudService.datosSolicitudHistorico(listaSolicitudes.get(i)));	
		}
		// y añadimos la lista al modelo para que pueda acceder a los datos necesarios y pintarlos en la tabla de historico
		model.addAttribute("salidas", listaSalida);
			
		model.addAttribute("paginaActual", "solicitudes");
		return "solicitudes";
	}

	@PostMapping("/solicitudes/crear")
	public String crearSolicitud(@Valid @ModelAttribute EntradaSolicitud entradaSolicitud, HttpSession session, BindingResult result) {

		Long usuarioId = (Long) session.getAttribute("usuarioId");

		if (entradaSolicitud.getArchivo().isEmpty()) {
			result.rejectValue("archivo", "error.archivo", "No ha seleccionado ningún archivo");
			return "solicitudes";
		}
		try {

			solicitudService.crearSolicitud(entradaSolicitud.getArchivo(), entradaSolicitud.getComentario(),
					usuarioId.intValue());

		} catch (IOException e) {

		}
		return "redirect:/solicitudes";
	}

}
