package es.fcc.signocarta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.fcc.signocarta.modelo.Asignacion;
import es.fcc.signocarta.modelo.Historico;
import es.fcc.signocarta.modelo.Solicitud;
import es.fcc.signocarta.repository.AsignacionRepository;
import es.fcc.signocarta.repository.HistoricoRepository;
import es.fcc.signocarta.repository.SolicitudRepository;
import es.fcc.signocarta.service.SolicitudAdmService;

@ExtendWith(MockitoExtension.class)
public class SolicitudAdmServiceTest {
	
	
	 @Mock
	 private SolicitudRepository solicitudRepository;
	 
	 @Mock
	 private AsignacionRepository asignacionRepository;
	 
	 @Mock
	 private HistoricoRepository historicoRepository;
	 
	 @InjectMocks
	 private SolicitudAdmService solicitudAdmService;
	 
	
	@DisplayName("Debe devolver una lista de históricos asociados a una solicitud")
	@Test
	void testListarHistoricoXSolicitud() {
	    Solicitud solicitud = new Solicitud();
	    solicitud.setId(1);
	    
	    List<Historico> historicosMock = List.of(new Historico(), new Historico());

	    when(solicitudRepository.findById(1)).thenReturn(Optional.of(solicitud));
	    when(historicoRepository.findBySolicitud(solicitud)).thenReturn(historicosMock);

	    List<Historico> resultado = solicitudAdmService.listarHistoricoXSolicitud(1);

	    assertEquals(2, resultado.size());
	    verify(historicoRepository).findBySolicitud(solicitud);
	}
	
	@DisplayName("Debe devolver asignación cuando existe para una solicitud")
	@Test
	void testBuscarAsignacionPorSolicitudCuandoExiste() {
	    Solicitud solicitud = new Solicitud();
	    solicitud.setId(1);
	    Asignacion asignacionMock = new Asignacion();

	    when(solicitudRepository.findById(1)).thenReturn(Optional.of(solicitud));
	    when(asignacionRepository.findBySolicitud(solicitud)).thenReturn(asignacionMock);

	    Asignacion resultado = solicitudAdmService.buscarAsignacionPorSolicitud(1);

	    assertNotNull(resultado);
	    verify(asignacionRepository).findBySolicitud(solicitud);
	}

}
