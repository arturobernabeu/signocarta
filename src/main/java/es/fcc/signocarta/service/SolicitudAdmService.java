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

import es.fcc.signocarta.controller.salida.SalidaHistoricoAdm;
import es.fcc.signocarta.modelo.Asignacion;
import es.fcc.signocarta.modelo.Documento;
import es.fcc.signocarta.modelo.Historico;
import es.fcc.signocarta.modelo.Presupuesto;
import es.fcc.signocarta.modelo.Solicitud;
import es.fcc.signocarta.modelo.UsuarioTrabajador;
import es.fcc.signocarta.repository.AsignacionRepository;
import es.fcc.signocarta.repository.DocumentoRepository;
import es.fcc.signocarta.repository.EstadoRepository;
import es.fcc.signocarta.repository.HistoricoRepository;
import es.fcc.signocarta.repository.PresupuestoRepository;
import es.fcc.signocarta.repository.SolicitudRepository;
import es.fcc.signocarta.repository.TrabajadorRepository;
import jakarta.persistence.EntityNotFoundException;

/**
 * Servicio encargado de la gestión administrativa de las solicitudes. Contempla
 * funcionalidades como listar solicitudes, gestionar su histórico, asignar
 * solicitudes a trabajadores, generar presupuestos y subir documentos de
 * salida.
 */
@Service
public class SolicitudAdmService {

	private final HistoricoRepository historicoRepository;
	private final EstadoRepository estadoRepository;
	private final DocumentoRepository documentoRepository;
	private final SolicitudRepository solicitudRepository;
	private final PresupuestoRepository presupuestoRepository;
	private final TrabajadorRepository trabajadorRepository;
	private final AsignacionRepository asignacionRepository;

	private final String directorioBase = "archivosUsuarios/"; // directorio donde se guardarán los archivos

	/**
	 * Constructor con inyección de dependencias.
	 */
	public SolicitudAdmService(HistoricoRepository historicoRepository, EstadoRepository estadoRepository,
			DocumentoRepository documentoRepository, SolicitudRepository solicitudRepository,
			PresupuestoRepository presupuestoRepository, TrabajadorRepository trabajadorRepository,
			AsignacionRepository asignacionRepository) {
		super();
		this.historicoRepository = historicoRepository;
		this.estadoRepository = estadoRepository;
		this.documentoRepository = documentoRepository;
		this.solicitudRepository = solicitudRepository;
		this.presupuestoRepository = presupuestoRepository;
		this.trabajadorRepository = trabajadorRepository;
		this.asignacionRepository = asignacionRepository;
	}

	/**
	 * Devuelve todas las solicitudes registradas en el sistema.
	 *
	 * @return Lista de {@link Solicitud}.
	 */
	public List<Solicitud> listaSolicitudes() {
		List<Solicitud> lista = solicitudRepository.findAll();

		return lista;
	}
	
	/**
	 * método que devuelve los datos de cada historico existente para pintarlo en
	 * pantalla
	 *
	 * @param solicitud La solicitud a consultar.
	 * @return Objeto {@link SalidaHistoricoAdm} con los datos detallados.
	 */
	public SalidaHistoricoAdm datosCadaSolicitud(Solicitud solicitud) {

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
		SalidaHistoricoAdm salida = new SalidaHistoricoAdm();
		salida.setId(solicitud.getId());
		salida.setFecha(solicitud.getFechaRegistro());
		salida.setDocEntrada(archivoEntrada);
		salida.setEstado(solicitud.getEstado().getNombre());
		salida.setObservaciones(solicitud.getObservaciones());
		salida.setUsuarioAplicacion(solicitud.getUsuarioApp());
		if (archivoSalida.getOrigen() != null) {
			salida.setDocSalida(archivoSalida);
		}
		if (solicitud.getPresupuesto() != null) {
			salida.setPresupuesto(solicitud.getPresupuesto());
		}
		if (asignacionRepository.findBySolicitud(solicitud) != null) {
			salida.setUsuarioTrabajador(asignacionRepository.findBySolicitud(solicitud).getUsuarioTrabajador());
		}
		return salida;
	}

	/**
	 * Lista el histórico asociado a una solicitud concreta.
	 *
	 * @param id ID de la solicitud.
	 * @return Lista de {@link Historico}.
	 */
	public List<Historico> listarHistoricoXSolicitud(Integer idSolicitud) {
		Solicitud solicitud = solicitudRepository.findById(idSolicitud).get();
		List<Historico> lista = historicoRepository.findBySolicitud(solicitud);
		return lista;
	}

	/**
	 * Devuelve un objeto resumen de un histórico específico.
	 *
	 * @param historico Objeto {@link Historico}.
	 * @return Objeto {@link SalidaHistoricoAdm} con los datos del histórico.
	 */
	public SalidaHistoricoAdm datosCadaHistorico(Historico historico) {

		SalidaHistoricoAdm salida = new SalidaHistoricoAdm();
		salida.setId(historico.getSolicitud().getId());
		salida.setFecha(historico.getFechaRegistro());
		salida.setEstado(historico.getEstado().getNombre());
		if (historico.getUsuarioTrabajador() != null) {
			salida.setUsuarioTrabajador(historico.getUsuarioTrabajador());
		}
		salida.setEvento(historico.getEvento());
		return salida;
	}

	/**
	 * Asigna una solicitud a un trabajador, si cumple las condiciones requeridas.
	 *
	 * @param idSolicitud  ID de la solicitud.
	 * @param idTrabajador ID del trabajador.
	 */
	public void asignarSolicitudTrabajador(Integer idSolicitud, Integer idTrabajador) {
		Solicitud solicitud = solicitudRepository.findById(idSolicitud).get();
		UsuarioTrabajador trabajador = trabajadorRepository.findById(idTrabajador).get();
		// primero comprobamos que la solicitud no esté ya asignada
		if (asignacionRepository.findBySolicitud(solicitud) == null
				&& asignacionRepository.findByUsuarioTrabajador(trabajador) == null
				&& solicitud.getEstado() != estadoRepository.findById(7).get()) {

			// creamos la asignacion de la solicitud al trabajador
			Asignacion asignacion = new Asignacion();
			asignacion.setSolicitud(solicitud);
			asignacion.setUsuarioTrabajador(trabajador);
			asignacionRepository.saveAndFlush(asignacion);
			solicitud.setEstado(estadoRepository.findById(2).get());
			solicitudRepository.save(solicitud);

			// creamos historico del cambio
			Historico historico = new Historico();
			historico.setSolicitud(solicitud);
			historico.setUsuarioTrabajador(trabajador);
			if (solicitud.getPresupuesto() == null) {
				historico.setEstado(estadoRepository.findById(2).get());
				historico.setEvento("Solicitud asignada para presupuestar");
			} else {
				historico.setEstado(estadoRepository.findById(6).get());
				historico.setEvento("Solicitud asignada para gestionar");
			}
			historico.setFechaRegistro(new Date());
			historicoRepository.save(historico);
		}

	}

	/**
	 * Crea o actualiza un presupuesto para una solicitud, registrando el cambio en
	 * el histórico.
	 *
	 * @param id      ID de la solicitud.
	 * @param importe Importe del presupuesto.
	 */
	public void presupuestoSolicitud(Integer id, double importe) {

		Solicitud solicitud = solicitudRepository.findById(id).get();

		if (presupuestoRepository.findAllBySolicitud(solicitud) == null) {
			Presupuesto presupuesto = new Presupuesto();
			presupuesto.setImporte(importe);
			presupuesto.setSolicitud(solicitud);
			presupuesto.setFechaRegistro(new Date());
			presupuesto.setEstado_presupuesto("Presupuesto realizado");
			presupuestoRepository.save(presupuesto);

			solicitud.setEstado(estadoRepository.findById(3).get());
			solicitudRepository.saveAndFlush(solicitud);

			UsuarioTrabajador trabajador = asignacionRepository.findBySolicitud(solicitud).getUsuarioTrabajador();
			Historico historico = new Historico();
			historico.setSolicitud(solicitud);
			historico.setEstado(solicitud.getEstado());
			historico.setFechaRegistro(new Date());
			historico.setUsuarioTrabajador(trabajador);
			historico.setEvento("Se ha creado presupuesto de la solicitud");
			historicoRepository.save(historico);

		} else {

			Presupuesto presupuesto = presupuestoRepository.findAllBySolicitud(solicitud);
			double importeAnterior = presupuesto.getImporte();
			presupuesto.setImporte(importe);
			presupuestoRepository.save(presupuesto);

			Asignacion asignacion = asignacionRepository.findBySolicitud(solicitud);
			UsuarioTrabajador trabajador = asignacion.getUsuarioTrabajador();
			Historico historico = new Historico();
			historico.setSolicitud(solicitud);
			historico.setEstado(solicitud.getEstado());
			historico.setFechaRegistro(new Date());
			historico.setUsuarioTrabajador(trabajador);
			historico.setEvento("Se modificado el importe de " + importeAnterior + "€");
			historicoRepository.save(historico);

		}
		// una vez se han realizado las gestiones oportunas se borra la asignación ya
		// que el trabajador solo puede tener asignada la solicitud que esta gestionando
		Asignacion asignacion = asignacionRepository.findBySolicitud(solicitud);
		asignacionRepository.delete(asignacion);
	}

	/**
	 * Sube un documento de salida para una solicitud y actualiza el estado e
	 * histórico correspondiente.
	 *
	 * @param idSolicitud ID de la solicitud.
	 * @param archivo     Archivo subido por el trabajador.
	 * @throws IOException Si ocurre un error al guardar el archivo.
	 */
	public void subirDocumentoSalida(Integer idSolicitud, MultipartFile archivo) throws IOException {

		Solicitud solicitud = solicitudRepository.findById(idSolicitud)
				.orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada"));

		String nombreUnico = archivo.getOriginalFilename();
		Path ruta = Paths.get(directorioBase, nombreUnico);
		Files.createDirectories(ruta.getParent());
		Files.write(ruta, archivo.getBytes());

		Documento documento = new Documento();
		documento.setNombre(archivo.getOriginalFilename());
		documento.setRuta(ruta.toString());
		documento.setOrigen("S");
		documento.setSolicitud(solicitud);
		documentoRepository.save(documento);

		solicitud.getListaDocumentos().add(documento);
		solicitud.setEstado(estadoRepository.findById(7).get());
		solicitudRepository.save(solicitud);

		Asignacion asignacion = asignacionRepository.findBySolicitud(solicitud);

		Historico historico = new Historico();
		historico.setEvento("Se sube archivo de resolución");
		historico.setSolicitud(solicitud);
		historico.setEstado(solicitud.getEstado());
		historico.setFechaRegistro(new Date());
		if (asignacion != null) {
			UsuarioTrabajador trabajador = asignacion.getUsuarioTrabajador();
			historico.setUsuarioTrabajador(trabajador);
			asignacionRepository.delete(asignacion);
		}
		historicoRepository.save(historico);

	}

	public Asignacion buscarAsignacionPorSolicitud(Integer idSolicitud) {
		Optional<Solicitud> solicitud = solicitudRepository.findById(idSolicitud);
		if (solicitud.isPresent()) {
			return asignacionRepository.findBySolicitud(solicitud.get());
		}
		return null;		
	}
}
