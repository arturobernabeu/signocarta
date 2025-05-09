package es.fcc.signocarta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.fcc.signocarta.modelo.Documento;
import es.fcc.signocarta.modelo.Solicitud;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento,Integer>{

	Documento findBySolicitud(Solicitud solicitud);
	
	

}
