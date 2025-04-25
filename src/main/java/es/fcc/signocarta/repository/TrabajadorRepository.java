package es.fcc.signocarta.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.fcc.signocarta.modelo.UsuarioTrabajador;

@Repository
public interface TrabajadorRepository extends JpaRepository<UsuarioTrabajador, Integer> {

	boolean existsByCodTrabajador(String codTrabajador);

	Optional<UsuarioTrabajador> findByCodTrabajador(String codTrabajador);
	
	
	

}
