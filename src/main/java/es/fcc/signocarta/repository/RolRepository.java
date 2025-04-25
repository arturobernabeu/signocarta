package es.fcc.signocarta.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.fcc.signocarta.modelo.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer>{

	
}
