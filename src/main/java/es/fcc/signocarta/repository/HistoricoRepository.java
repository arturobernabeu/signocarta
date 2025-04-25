package es.fcc.signocarta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.fcc.signocarta.modelo.Historico;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Integer>{

}
