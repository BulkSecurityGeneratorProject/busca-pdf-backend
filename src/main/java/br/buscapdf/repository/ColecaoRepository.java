package br.buscapdf.repository;

import br.buscapdf.domain.Colecao;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Colecao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ColecaoRepository extends JpaRepository<Colecao, Long> {

}
