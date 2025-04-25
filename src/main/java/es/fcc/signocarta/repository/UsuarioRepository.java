package es.fcc.signocarta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.fcc.signocarta.modelo.Usuario;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
	
	

}
