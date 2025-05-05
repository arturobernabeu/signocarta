package es.fcc.signocarta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.fcc.signocarta.modelo.Presupuesto;
import es.fcc.signocarta.modelo.Solicitud;

@Repository
public interface PresupuestoRepository extends JpaRepository<Presupuesto,Integer>{
	
	Presupuesto findAllBySolicitud(Solicitud solicitud);


}
