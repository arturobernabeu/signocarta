package es.fcc.signocarta.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.fcc.signocarta.controller.salida.SalidaHistorico;
import es.fcc.signocarta.modelo.Documento;
import es.fcc.signocarta.modelo.Historico;
import es.fcc.signocarta.modelo.Solicitud;
import es.fcc.signocarta.modelo.UsuarioAplicacion;
import es.fcc.signocarta.repository.DocumentoRepository;
import es.fcc.signocarta.repository.EstadoRepository;
import es.fcc.signocarta.repository.HistoricoRepository;
import es.fcc.signocarta.repository.SolicitudRepository;
import es.fcc.signocarta.repository.UsuarioAppRepository;

@Service
public class SolicitudService {

	private final DocumentoRepository documentoRepository;
	private final SolicitudRepository solicitudRepository;
	private final EstadoRepository estadoRepository;
	private final UsuarioAppRepository usuarioAppRepository;
	private final HistoricoRepository historicoRepository;

	private final String directorioBase = "archivosUsuarios/"; // directorio donde se guardarán los archivos

	public SolicitudService(DocumentoRepository documentoRepository, SolicitudRepository solicitudRepository,
			EstadoRepository estadoRepository, UsuarioAppRepository usuarioAppRepository, HistoricoRepository historicoRepository) {
		super();
		this.documentoRepository = documentoRepository;
		this.solicitudRepository = solicitudRepository;
		this.estadoRepository = estadoRepository;
		this.usuarioAppRepository = usuarioAppRepository;
		this.historicoRepository = historicoRepository;
	}

	public void crearSolicitud(MultipartFile archivo, String comentario, Integer idUsuarioLogado) throws IOException {
		if (archivo.isEmpty())
			return;

		// primero almacenamos el archivo en la base de datos
		String nombreUnico = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
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
		solicitud.setEstado(estadoRepository.getReferenceById(1));
		solicitud.getListaDocumentos().add(documentoRepository.findById(documento.getId()).get());
		solicitud.setFechaRegistro(fecha);
		solicitud.setUsuarioApp(usuarioAppRepository.findById(idUsuarioLogado).get());
		solicitudRepository.save(solicitud); // guardamos la solicitud
		
		// Tras crear la solicitud crearemos un histórico para esa solicitud donde se irán resgistrando los cambios de estado y demás de dicha solicitud
		Historico historico = new Historico();
		historico.setEvento("Solicitud creada");
		historico.setSolicitud(solicitud);
		historico.setEstado(solicitud.getEstado());
		historico.setFechaRegistro(solicitud.getFechaRegistro());
		historicoRepository.save(historico);
		
	}
	//método que devuelve los datos necesarios de cada solicitud para que se pinten en pantalla
	public SalidaHistorico datosSolicitudHistorico(Solicitud solicitud) {
		
		List<Documento> listDocumentos= solicitud.getListaDocumentos();
		Documento archivoEntrada = new Documento();
		
		for(int i=0; i<listDocumentos.size();i++) {
			Documento archivo = listDocumentos.get(i); 
			if(archivo.getOrigen().toLowerCase().startsWith("e")) {// localizo el documento que tenga origen de salida E o si empieza por e en el caso de que sea la palaba "entrada"
				archivoEntrada = archivo;
			}
		}		
		SalidaHistorico salida = new SalidaHistorico();
		salida.setFecha(solicitud.getFechaRegistro());
		salida.setDocEntrada(archivoEntrada);
		salida.setEstado(solicitud.getEstado().getNombre());
		
		return salida;
	}
	
	// método que devuelve una lista de las solicitudes que tiene un mismo usuario
	public List<Solicitud> solicitudesPorUsuario(Integer idUsuario) {
		UsuarioAplicacion usuario= usuarioAppRepository.findById(idUsuario).get();
		 List <Solicitud> listaSolicitudes = solicitudRepository.findAllByUsuarioApp(usuario);
		return listaSolicitudes;
	}
	

}
