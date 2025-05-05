package es.fcc.signocarta.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.fcc.signocarta.modelo.Asignacion;
import es.fcc.signocarta.modelo.Solicitud;
import es.fcc.signocarta.modelo.UsuarioTrabajador;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Integer>{
	
	Asignacion findBySolicitud(Solicitud solicitud);

	Asignacion findByUsuarioTrabajador(UsuarioTrabajador trabajador);

}
