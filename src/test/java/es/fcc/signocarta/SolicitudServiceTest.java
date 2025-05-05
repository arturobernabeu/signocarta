package es.fcc.signocarta;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.fcc.signocarta.modelo.Asignacion;
import es.fcc.signocarta.modelo.Solicitud;
import es.fcc.signocarta.repository.AsignacionRepository;
import es.fcc.signocarta.repository.SolicitudRepository;
import es.fcc.signocarta.service.SolicitudService;

@ExtendWith(MockitoExtension.class)
public class SolicitudServiceTest {
	
	 @Mock
	 private SolicitudRepository solicitudRepository;
	 
	 @Mock
	 private AsignacionRepository asignacionRepository;
	 
	 @InjectMocks
	 private SolicitudService solicitudService;
	
	
	@DisplayName("Debe eliminar solicitud y su asignación si existen")
	@Test
	void testBorrarPorIdCuandoExiste() {
	    Solicitud solicitud = new Solicitud();
	    solicitud.setId(1);
	    Asignacion asignacionMock = new Asignacion();
	    asignacionMock.setSolicitud(solicitud);

	    when(solicitudRepository.findById(1)).thenReturn(Optional.of(solicitud));
	    when(asignacionRepository.findBySolicitud(solicitud)).thenReturn(asignacionMock);

	    solicitudService.borrarPorId(1);

	    verify(asignacionRepository).delete(asignacionMock);
	    verify(solicitudRepository).delete(solicitud);
	}

}
