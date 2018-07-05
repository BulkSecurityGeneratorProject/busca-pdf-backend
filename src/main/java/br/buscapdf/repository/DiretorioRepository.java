package br.buscapdf.repository;

import br.buscapdf.domain.Diretorio;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Diretorio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiretorioRepository extends JpaRepository<Diretorio, Long> {

}
