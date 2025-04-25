package es.fcc.signocarta.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.fcc.signocarta.modelo.UsuarioAplicacion;

@Repository
public interface UsuarioAppRepository extends JpaRepository<UsuarioAplicacion, Integer>{
	
	 boolean existsByEmail(String email);// para comprobar si un email ya existe
	 
	 //recuperar usuario por email
	 Optional<UsuarioAplicacion> findByEmail(String email);
	 
	 

}
