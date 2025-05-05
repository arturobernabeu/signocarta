package es.fcc.signocarta.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.fcc.signocarta.controller.entrada.EntradaSolicitud;
import es.fcc.signocarta.controller.salida.SalidaHistorico;
import es.fcc.signocarta.modelo.Solicitud;
import es.fcc.signocarta.modelo.Usuario;
import es.fcc.signocarta.service.SolicitudService;
import es.fcc.signocarta.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/**
 * Controlador encargado de gestionar las solicitudes de los usuarios. Permite
 * visualizar el historial de solicitudes, crear nuevas y gestionar presupuestos
 * asociados.
 */
@Controller
public class SolicitudesController {

	private final UsuarioService usuarioService;
	private final SolicitudService solicitudService;

	/**
	 * Constructor para inyectar dependencias de servicios necesarios.
	 *
	 * @param usuarioService   Servicio para operaciones relacionadas con usuarios.
	 * @param solicitudService Servicio para operaciones relacionadas con
	 *                         solicitudes.
	 */
	public SolicitudesController(UsuarioService usuarioService, SolicitudService solicitudService) {
		super();
		this.usuarioService = usuarioService;
		this.solicitudService = solicitudService;
	}

	/**
	 * Muestra la página de solicitudes del usuario logado. Carga el historial de
	 * solicitudes y lo añade al modelo.
	 *
	 * @param session Sesión actual del usuario.
	 * @param model   Modelo para pasar atributos a la vista.
	 * @return Vista "solicitudes" si el usuario está logado, de lo contrario
	 *         redirige a login.
	 */
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
		// Añadimos al modelo entradaSolicitud que es donde se almacenaran los datos que
		// recogemos de pantalla
		model.addAttribute("entradaSolicitud", new EntradaSolicitud());

		// vamos a almacenar en una lista las solicitudes que tiene el usuario
		List<Solicitud> listaSolicitudes = solicitudService.solicitudesPorUsuario(usuarioId.intValue());
		// como necesitamos pasar al modelo una lista de objetos SalidaHistorico,
		// crearemos uno por cada solicitud y almacenaremos en una lista
		List<SalidaHistorico> listaSalida = new ArrayList<>();
		for (int i = 0; i < listaSolicitudes.size(); i++) {
			listaSalida.add(solicitudService.datosSolicitudHistorico(listaSolicitudes.get(i)));
		}
		// y añadimos la lista al modelo para que pueda acceder a los datos necesarios y
		// pintarlos en la tabla de historico
		model.addAttribute("salidas", listaSalida);

		model.addAttribute("paginaActual", "solicitudes");
		return "solicitudes";
	}

	/**
	 * Maneja la creación de una nueva solicitud por parte del usuario.
	 *
	 * @param entradaSolicitud Datos del formulario de solicitud.
	 * @param session          Sesión del usuario.
	 * @param result           Resultado de la validación del formulario.
	 * @return Redirección a la vista de solicitudes.
	 */
	@PostMapping("/solicitudes/crear")
	public String crearSolicitud(@Valid @ModelAttribute EntradaSolicitud entradaSolicitud, HttpSession session,
			BindingResult result) {

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

	/**
	 * Elimina una solicitud por su ID.
	 *
	 * @param id                 ID de la solicitud a eliminar.
	 * @param redirectAttributes Atributos para mostrar mensajes tras redirección.
	 * @return Redirección a la vista de solicitudes con mensaje de éxito.
	 */
	@GetMapping("/salida/borrar/{id}")
	public String borrarSolicitud(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		solicitudService.borrarPorId(id); // Lógica para eliminar
		redirectAttributes.addFlashAttribute("mensaje", "Registro borrado correctamente");
		return "redirect:/solicitudes"; // O donde muestres tu tabla
	}

	/**
	 * Rechaza un presupuesto asociado a una solicitud.
	 *
	 * @param id                 ID de la solicitud/presupuesto.
	 * @param redirectAttributes Atributos para mensajes flash.
	 * @return Redirección a la vista de solicitudes con mensaje de confirmación.
	 */
	@PostMapping("/presupuesto/rechazar/{id}")
	public String rechazarPresupuesto(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		solicitudService.rechazoPre(id);
		redirectAttributes.addFlashAttribute("mensaje", "Ha rechazado el presupuesto");
		return "redirect:/solicitudes";
	}

	/**
	 * Acepta y paga un presupuesto asociado a una solicitud.
	 *
	 * @param id                 ID de la solicitud/presupuesto.
	 * @param redirectAttributes Atributos para mensajes flash.
	 * @return Redirección a la vista de solicitudes con mensaje de confirmación.
	 */
	@PostMapping("/presupuesto/pagar/{id}")
	public String aceptarPresupuesto(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		solicitudService.aceptarPresupuestoPagar(id);
		redirectAttributes.addFlashAttribute("mensaje", "Ha aceptado el presupuesto");
		return "redirect:/solicitudes";
	}

}
