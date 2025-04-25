package es.fcc.signocarta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.fcc.signocarta.modelo.Solicitud;
import es.fcc.signocarta.modelo.UsuarioAplicacion;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud,Integer>{

	List<Solicitud> findAllByUsuarioApp(UsuarioAplicacion usuario);



}
