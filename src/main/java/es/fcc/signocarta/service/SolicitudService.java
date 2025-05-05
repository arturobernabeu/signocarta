package es.fcc.signocarta.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.fcc.signocarta.controller.salida.SalidaHistorico;
import es.fcc.signocarta.modelo.Asignacion;
import es.fcc.signocarta.modelo.Documento;
import es.fcc.signocarta.modelo.Estado;
import es.fcc.signocarta.modelo.Historico;
import es.fcc.signocarta.modelo.Presupuesto;
import es.fcc.signocarta.modelo.Solicitud;
import es.fcc.signocarta.modelo.UsuarioAplicacion;
import es.fcc.signocarta.repository.AsignacionRepository;
import es.fcc.signocarta.repository.DocumentoRepository;
import es.fcc.signocarta.repository.EstadoRepository;
import es.fcc.signocarta.repository.HistoricoRepository;
import es.fcc.signocarta.repository.PresupuestoRepository;
import es.fcc.signocarta.repository.SolicitudRepository;
import es.fcc.signocarta.repository.UsuarioAppRepository;

/**
 * Servicio encargado de la gestión de solicitudes de los usuarios de la
 * aplicación. Incluye operaciones de creación, consulta, eliminación y
 * actualización de solicitudes, así como gestión de documentos y presupuestos
 * asociados.
 */
@Service
public class SolicitudService {

	private final DocumentoRepository documentoRepository;
	private final SolicitudRepository solicitudRepository;
	private final EstadoRepository estadoRepository;
	private final UsuarioAppRepository usuarioAppRepository;
	private final HistoricoRepository historicoRepository;
	private final PresupuestoRepository presupuestoRepository;
	private final AsignacionRepository asignacionRepository;

	private final String directorioBase = "archivosUsuarios/"; // directorio donde se guardarán los archivos

	/**
	 * Constructor para inyección de dependencias.
	 */
	public SolicitudService(DocumentoRepository documentoRepository, SolicitudRepository solicitudRepository,
			EstadoRepository estadoRepository, UsuarioAppRepository usuarioAppRepository,
			HistoricoRepository historicoRepository, PresupuestoRepository presupuestoRepository,
			AsignacionRepository asignacionRepository) {
		super();
		this.documentoRepository = documentoRepository;
		this.solicitudRepository = solicitudRepository;
		this.estadoRepository = estadoRepository;
		this.usuarioAppRepository = usuarioAppRepository;
		this.historicoRepository = historicoRepository;
		this.presupuestoRepository = presupuestoRepository;
		this.asignacionRepository = asignacionRepository;
	}

	/**
	 * Crea una nueva solicitud a partir de un archivo y comentario del usuario.
	 *
	 * @param archivo         Archivo adjunto.
	 * @param comentario      Observaciones o comentarios del usuario.
	 * @param idUsuarioLogado ID del usuario que realiza la solicitud.
	 * @throws IOException si ocurre un error al guardar el archivo.
	 */
	public void crearSolicitud(MultipartFile archivo, String comentario, Integer idUsuarioLogado) throws IOException {
		if (archivo.isEmpty())
			return;

		// primero almacenamos el archivo en la base de datos
		// String nombreUnico = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
		String nombreUnico = archivo.getOriginalFilename();
		Path ruta = Paths.get(directorioBase, nombreUnico);
		Files.createDirectories(ruta.getParent());
		Files.write(ruta, archivo.getBytes());

		Documento documento = new Documento();
		documento.setNombre(archivo.getOriginalFilename());
		documento.setRuta(ruta.toString());
		documento.setOrigen("E");
		documentoRepository.save(documento);

		Date fecha = new Date();
		// una vez almacenado el documento, seguiremos con los datos de la solicitud

		Solicitud solicitud = new Solicitud();
		solicitud.setObservaciones(comentario);
		solicitud.setEstado(estadoRepository.findById(1).get());
		solicitud.getListaDocumentos().add(documentoRepository.findById(documento.getId()).get());
		solicitud.setFechaRegistro(fecha);
		solicitud.setUsuarioApp(usuarioAppRepository.findById(idUsuarioLogado).get());
		solicitud = solicitudRepository.save(solicitud); // guardamos la solicitud

		documento.setSolicitud(solicitud);
		documentoRepository.save(documento);

		// Tras crear la solicitud crearemos un histórico para esa solicitud donde se
		// irán resgistrando los cambios de estado y demás de dicha solicitud
		Historico historico = new Historico();
		historico.setEvento("Solicitud creada");
		historico.setSolicitud(solicitud);
		historico.setEstado(solicitud.getEstado());
		historico.setFechaRegistro(solicitud.getFechaRegistro());
		historicoRepository.save(historico);

	}

	/**
	 * Devuelve los datos de una solicitud en formato de salida para histórico.
	 *
	 * @param solicitud Objeto solicitud.
	 * @return Objeto SalidaHistorico con datos relevantes.
	 */
	public SalidaHistorico datosSolicitudHistorico(Solicitud solicitud) {

		List<Documento> listDocumentos = solicitud.getListaDocumentos();
		Documento archivoEntrada = new Documento();
		Documento archivoSalida = new Documento();

		for (int i = 0; i < listDocumentos.size(); i++) {
			Documento archivo = listDocumentos.get(i);
			if (archivo.getOrigen().toLowerCase().startsWith("e")) {// localizo el documento que tenga origen de salida
																	// E o si empieza por e en el caso de que sea la
																	// palaba "entrada"
				archivoEntrada = archivo;
			} else if (archivo.getOrigen().toLowerCase().startsWith("s") & archivo.getOrigen() != null) {
				archivoSalida = archivo;
			}
		}
		SalidaHistorico salida = new SalidaHistorico();
		salida.setId(solicitud.getId());
		salida.setFecha(solicitud.getFechaRegistro());
		salida.setDocEntrada(archivoEntrada);
		salida.setEstado(solicitud.getEstado().getNombre());
		if (archivoSalida.getOrigen() != null) {
			salida.setDocSalida(archivoSalida);
		}
		// comprobamos si hay presupuesto para almacenarlo en la salida de datos
		if (presupuestoRepository.findAllBySolicitud(solicitud) != null) {
			salida.setPresupuesto(presupuestoRepository.findAllBySolicitud(solicitud));
		}
		return salida;
	}

	/**
	 * Devuelve todas las solicitudes realizadas por un usuario.
	 *
	 * @param idUsuario ID del usuario.
	 * @return Lista de solicitudes asociadas al usuario.
	 */
	public List<Solicitud> solicitudesPorUsuario(Integer idUsuario) {
		UsuarioAplicacion usuario = usuarioAppRepository.findById(idUsuario).get();
		List<Solicitud> listaSolicitudes = solicitudRepository.findAllByUsuarioApp(usuario);
		return listaSolicitudes;
	}

	/**
	 * Obtiene el presupuesto asociado a una solicitud.
	 *
	 * @param solicitud Objeto solicitud.
	 * @return Presupuesto asociado o null si no existe.
	 */
	public Presupuesto obtenerPresupuesto(Solicitud solicitud) {
		Presupuesto presupuesto = new Presupuesto();
		presupuesto = presupuestoRepository.findAllBySolicitud(solicitud);
		return presupuesto;
	}

	/**
	 * Elimina una solicitud por su ID. Si existe una asignación relacionada,
	 * también la elimina.
	 *
	 * @param id ID de la solicitud a eliminar.
	 */
	public void borrarPorId(Integer id) {
		Optional<Solicitud> solicitud = solicitudRepository.findById(id);

		if (solicitud.isPresent()) {
			Asignacion asignacion = asignacionRepository.findBySolicitud(solicitud.get());
			if (asignacion != null) {
				asignacionRepository.delete(asignacion);
			}
			solicitudRepository.delete(solicitud.get());
		}
	}

	/**
	 * Rechaza el presupuesto de una solicitud y actualiza su estado.
	 *
	 * @param id ID de la solicitud.
	 */
	public void rechazoPre(Integer id) {
		Optional<Solicitud> optionalSolicitud = solicitudRepository.findById(id);

		if (optionalSolicitud.isPresent()) {
			Solicitud solicitud = optionalSolicitud.get();
			Optional<Estado> estadoOptional = estadoRepository.findById(4);
			Estado estado = estadoOptional.get();
			solicitud.setEstado(estado);
			solicitud.getPresupuesto().setEstado_presupuesto("Presupuesto rechazado");
			solicitudRepository.save(solicitud);
			// almacenamos en el histórico que se ha rechazado el presupuesto
			Historico historico = new Historico();
			historico.setEvento("Presupuesto rechazado por el usuario");
			historico.setSolicitud(solicitud);
			historico.setEstado(estado);
			historico.setFechaRegistro(new Date());
			historicoRepository.save(historico);
		}
	}

	/**
	 * Acepta y registra el pago de un presupuesto de una solicitud.
	 *
	 * @param id ID de la solicitud.
	 */
	public void aceptarPresupuestoPagar(Integer id) {
		Optional<Solicitud> optionalSolicitud = solicitudRepository.findById(id);

		if (optionalSolicitud.isPresent()) {
			Solicitud solicitud = optionalSolicitud.get();
			Optional<Estado> estadoOptional = estadoRepository.findById(5);
			Estado estado = estadoOptional.get();
			solicitud.setEstado(estado);
			solicitud.getPresupuesto().setEstado_presupuesto("Presupuesto aceptado");
			solicitud.getPresupuesto().setFecha_pago(new Date());
			solicitud.getPresupuesto().setMetodo_pago("Tarjeta");
			solicitudRepository.save(solicitud);
			//// almacenamos en el histórico que se ha aceptado y pagado el presupuesto
			Historico historico = new Historico();
			historico.setEvento("Presupuesto aceptado y pagado");
			historico.setSolicitud(solicitud);
			historico.setEstado(estado);
			historico.setFechaRegistro(new Date());
			historicoRepository.save(historico);
		}
	}

}
