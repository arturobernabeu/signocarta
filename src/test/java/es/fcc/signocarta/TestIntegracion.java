package es.fcc.signocarta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import es.fcc.signocarta.modelo.Documento;
import es.fcc.signocarta.modelo.Historico;
import es.fcc.signocarta.modelo.Rol;
import es.fcc.signocarta.modelo.Solicitud;
import es.fcc.signocarta.modelo.UsuarioAplicacion;
import es.fcc.signocarta.repository.AsignacionRepository;
import es.fcc.signocarta.repository.DocumentoRepository;
import es.fcc.signocarta.repository.HistoricoRepository;
import es.fcc.signocarta.repository.SolicitudRepository;
import es.fcc.signocarta.repository.UsuarioAppRepository;
import es.fcc.signocarta.service.SolicitudService;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TestIntegracion {

	@Autowired
	private SolicitudService solicitudService;

	@Autowired
	private DocumentoRepository documentoRepository;

	@Autowired
	private SolicitudRepository solicitudRepository;

	@Autowired
	private UsuarioAppRepository usuarioAppRepository;

	@Autowired
	private AsignacionRepository asignacionRepository;

	@Autowired
	private HistoricoRepository historicoRepository;

	@BeforeEach
	public void limpiarBaseDeDatos() {
		asignacionRepository.deleteAll();
		historicoRepository.deleteAll();
		documentoRepository.deleteAll();
		solicitudRepository.deleteAll();
		usuarioAppRepository.deleteAll();
	}

	@Test
	public void testCrearSolicitud_integracionCompleta() throws IOException {
		// Preparar archivo simulado
		MockMultipartFile archivo = new MockMultipartFile("archivo", "test.pdf", "application/pdf",
				"Contenido de prueba".getBytes());

		// Preparar usuario simulado
		UsuarioAplicacion usuario = new UsuarioAplicacion();
		usuario.setNombre("Prueba");
		usuario.setApellidos("Integración");
		usuario.setEmail("test@example.com");
		usuario.setPassword("1234");
		Rol rol = new Rol();
		rol.setId(3);
		usuario.setRol(rol);
		usuario = usuarioAppRepository.save(usuario);

		// Ejecutar método
		solicitudService.crearSolicitud(archivo, "Comentario de prueba", usuario.getId());

		// Verificaciones
		List<Solicitud> solicitudes = solicitudRepository.findAll();
		assertEquals(1, solicitudes.size());

		Solicitud solicitud = solicitudes.get(0);
		assertEquals("Comentario de prueba", solicitud.getObservaciones());
		assertEquals(usuario.getId(), solicitud.getUsuarioApp().getId());

		List<Documento> documentos = documentoRepository.findAll();
		assertEquals(1, documentos.size());
		assertEquals("test.pdf", documentos.get(0).getNombre());

		List<Historico> historicos = historicoRepository.findAll();
		assertEquals(1, historicos.size());
		assertEquals("Solicitud creada", historicos.get(0).getEvento());
	}

}
