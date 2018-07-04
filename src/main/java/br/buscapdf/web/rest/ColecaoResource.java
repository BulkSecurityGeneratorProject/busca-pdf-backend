package br.buscapdf.web.rest;

import br.buscapdf.security.AuthoritiesConstants;
import com.codahale.metrics.annotation.Timed;
import br.buscapdf.domain.Colecao;

import br.buscapdf.repository.ColecaoRepository;
import br.buscapdf.web.rest.errors.BadRequestAlertException;
import br.buscapdf.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Colecao.
 */
@RestController
@RequestMapping("/api")
public class ColecaoResource {

    private final Logger log = LoggerFactory.getLogger(ColecaoResource.class);

    private static final String ENTITY_NAME = "colecao";

    private final ColecaoRepository colecaoRepository;

    public ColecaoResource(ColecaoRepository colecaoRepository) {
        this.colecaoRepository = colecaoRepository;
    }

    /**
     * POST  /colecaos : Create a new colecao.
     *
     * @param colecao the colecao to create
     * @return the ResponseEntity with status 201 (Created) and with body the new colecao, or with status 400 (Bad Request) if the colecao has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/colecaos")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Colecao> createColecao(@Valid @RequestBody Colecao colecao) throws URISyntaxException {
        log.debug("REST request to save Colecao : {}", colecao);
        if (colecao.getId() != null) {
            throw new BadRequestAlertException("A new colecao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Colecao result = colecaoRepository.save(colecao);
        return ResponseEntity.created(new URI("/api/colecaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /colecaos : Updates an existing colecao.
     *
     * @param colecao the colecao to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated colecao,
     * or with status 400 (Bad Request) if the colecao is not valid,
     * or with status 500 (Internal Server Error) if the colecao couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/colecaos")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Colecao> updateColecao(@Valid @RequestBody Colecao colecao) throws URISyntaxException {
        log.debug("REST request to update Colecao : {}", colecao);
        if (colecao.getId() == null) {
            return createColecao(colecao);
        }
        Colecao result = colecaoRepository.save(colecao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, colecao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /colecaos : get all the colecaos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of colecaos in body
     */
    @GetMapping("/colecaos")
    @Timed
    public List<Colecao> getAllColecaos() {
        log.debug("REST request to get all Colecaos");
        return colecaoRepository.findAll();
        }

    /**
     * GET  /colecaos/:id : get the "id" colecao.
     *
     * @param id the id of the colecao to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the colecao, or with status 404 (Not Found)
     */
    @GetMapping("/colecaos/{id}")
    @Timed
    public ResponseEntity<Colecao> getColecao(@PathVariable Long id) {
        log.debug("REST request to get Colecao : {}", id);
        Colecao colecao = colecaoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(colecao));
    }

    /**
     * DELETE  /colecaos/:id : delete the "id" colecao.
     *
     * @param id the id of the colecao to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/colecaos/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteColecao(@PathVariable Long id) {
        log.debug("REST request to delete Colecao : {}", id);
        colecaoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
