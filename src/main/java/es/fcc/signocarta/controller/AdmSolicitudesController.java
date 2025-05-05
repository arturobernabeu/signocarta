package es.fcc.signocarta.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.fcc.signocarta.controller.salida.SalidaHistoricoAdm;
import es.fcc.signocarta.modelo.Asignacion;
import es.fcc.signocarta.modelo.Historico;
import es.fcc.signocarta.modelo.Solicitud;
import es.fcc.signocarta.modelo.Usuario;
import es.fcc.signocarta.service.SolicitudAdmService;
import es.fcc.signocarta.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador para la gestión de solicitudes administrativas por parte del
 * administrador.
 * 
 * Proporciona endpoints para listar solicitudes, asignarlas, ver su historial,
 * presupuestarlas y subir documentos asociados.
 */
@Controller
public class AdmSolicitudesController {

	private final UsuarioService usuarioService;
	private final SolicitudAdmService solicitudAdmService;
	private List<Historico> listaHistoricos = new ArrayList<Historico>();

	/**
	 * 
	 * @param usuarioService      servicio de gestión de usuarios
	 * @param solicitudAdmService servicio de gestión de solicitudes administrativas
	 */

	public AdmSolicitudesController(UsuarioService usuarioService, SolicitudAdmService solicitudAdmService) {
		super();
		this.usuarioService = usuarioService;
		this.solicitudAdmService = solicitudAdmService;
	}

	/**
	 * Muestra la pantalla de solicitudes administrativas para el administrador. Si
	 * el usuario no está logado, se redirige al login.
	 * 
	 * @param session sesión HTTP actual
	 * @param model   modelo de datos para la vista
	 * @return nombre de la vista a renderizar
	 */
	@RequestMapping("/adm-solicitudes")
	public String admSolicitudes(HttpSession session, Model model) {
		// controlamos que el usuario esté logado
		Long usuarioId = (Long) session.getAttribute("usuarioId");
		// Si el usuario no está logado no podrá acceder a la pantalla, sino que le
		// redirige a login.
		if (usuarioId == null) {
			return "redirect:/login";
		}
		Usuario usuarioLogado = usuarioService.buscarPorId(usuarioId).get();// almaceno el usuario logado
		model.addAttribute("usuarioLogado", usuarioLogado);
		// paso el idRol al modelo para controlar las opciones que tienen que aparecer
		// en el menu lateral
		int rolId = usuarioLogado.getRol().getId();
		model.addAttribute("rolId", rolId);

		// vamos a almacenar en una lista los registros del historico
		List<Solicitud> listaSolicitudes = solicitudAdmService.listaSolicitudes();
		// como necesitamos pasar al modelo una lista de objetos SalidaHistoricoAdm,
		// crearemos uno por cada historico y almacenaremos en una lista
		List<SalidaHistoricoAdm> listaSalida = new ArrayList<>();
		for (int i = 0; i < listaSolicitudes.size(); i++) {
			listaSalida.add(solicitudAdmService.datosCadaSolicitud(listaSolicitudes.get(i)));
		}
		// y añadimos la lista al modelo para que pueda acceder a los datos necesarios y
		// pintarlos en la tabla del administrador
		model.addAttribute("salidas", listaSalida);

		// añadimos al model la lista de historico segun hayamos seleccionado una
		// solicitud
		List<SalidaHistoricoAdm> listaSalidaHist = new ArrayList<>();
		for (Historico h : listaHistoricos) {
			listaSalidaHist.add(solicitudAdmService.datosCadaHistorico(h));
		}

		model.addAttribute("salidaHistorico", listaSalidaHist);
		model.addAttribute("paginaActual", "adm-solicitudes");
		return "adm-solicitudes";
	}

	/**
	 * Carga el historial de una solicitud seleccionada y redirige a la pantalla
	 * principal.
	 * 
	 * @param id    ID de la solicitud
	 * @param model modelo de datos para la vista
	 * @return redirección a la vista de solicitudes administrativas
	 */
	@GetMapping("/salida/ver/{id}")
	public String verHistoricoSolicitud(@PathVariable Integer id, Model model) {

		listaHistoricos = solicitudAdmService.listarHistoricoXSolicitud(id);

		return "redirect:/adm-solicitudes";
	}

	/**
	 * Asigna una solicitud al usuario logado y actualiza su historial.
	 * 
	 * @param id      ID de la solicitud
	 * @param session sesión HTTP actual
	 * @return redirección a la vista de solicitudes administrativas
	 */
	@GetMapping("/salida/asignar/{id}")
	public String asignarSolicitud(@PathVariable Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
		Long idTrabajador = (Long) session.getAttribute("usuarioId");
		Asignacion asignacion = solicitudAdmService.buscarAsignacionPorSolicitud(id);
	    if (asignacion != null) {
	        if (!asignacion.getUsuarioTrabajador().getId().equals(idTrabajador.intValue())) {
	            redirectAttributes.addFlashAttribute("error", "La solicitud ya está asignada a otro trabajador.");
	        }
	        return "redirect:/adm-solicitudes";
	    }
		solicitudAdmService.asignarSolicitudTrabajador(id, idTrabajador.intValue());
		listaHistoricos = solicitudAdmService.listarHistoricoXSolicitud(id);
		return "redirect:/adm-solicitudes";
	}

	/**
	 * Registra un presupuesto para una solicitud determinada.
	 * 
	 * @param id      ID de la solicitud
	 * @param importe importe presupuestado
	 * @return redirección a la vista de solicitudes administrativas
	 */
	@PostMapping("/salida/presupuestar/{id}")
	public String presupuestarSolicitud(@PathVariable Integer id, @RequestParam double importe) {

		solicitudAdmService.presupuestoSolicitud(id, importe);
		listaHistoricos = solicitudAdmService.listarHistoricoXSolicitud(id);
		return "redirect:/adm-solicitudes";
	}

	/**
	 * Sube un archivo relacionado con una solicitud y lo guarda como documento de
	 * salida.
	 * 
	 * @param id                 ID de la solicitud
	 * @param archivo            archivo a subir
	 * @param redirectAttributes atributos para mensajes flash en redirecciones
	 * @return redirección a la vista de solicitudes administrativas
	 */
	@PostMapping("/subir-archivo/{id}")
	public String subirArchivo(@PathVariable("id") Integer id, @RequestParam("archivo") MultipartFile archivo,
			RedirectAttributes redirectAttributes) {

		try {
			solicitudAdmService.subirDocumentoSalida(id, archivo);
			redirectAttributes.addFlashAttribute("success", "Archivo subido correctamente.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error al subir archivo: " + e.getMessage());
		}
		listaHistoricos = solicitudAdmService.listarHistoricoXSolicitud(id);
		return "redirect:/adm-solicitudes";
	}

}
