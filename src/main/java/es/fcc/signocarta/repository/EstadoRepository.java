package es.fcc.signocarta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.fcc.signocarta.modelo.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer>{
	
	Estado getReferenceById(Integer id);

}
