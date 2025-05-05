package es.fcc.signocarta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.fcc.signocarta.modelo.Historico;
import es.fcc.signocarta.modelo.Solicitud;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Integer>{

	List<Historico> findBySolicitud(Solicitud solicitud);
	
}
