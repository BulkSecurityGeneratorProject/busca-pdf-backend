package br.buscapdf.web.rest;

import br.buscapdf.security.AuthoritiesConstants;
import com.codahale.metrics.annotation.Timed;
import br.buscapdf.domain.TipoDocumento;

import br.buscapdf.repository.TipoDocumentoRepository;
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
 * REST controller for managing TipoDocumento.
 */
@RestController
@RequestMapping("/api")
public class TipoDocumentoResource {

    private final Logger log = LoggerFactory.getLogger(TipoDocumentoResource.class);

    private static final String ENTITY_NAME = "tipoDocumento";

    private final TipoDocumentoRepository tipoDocumentoRepository;

    public TipoDocumentoResource(TipoDocumentoRepository tipoDocumentoRepository) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    /**
     * POST  /tipo-documentos : Create a new tipoDocumento.
     *
     * @param tipoDocumento the tipoDocumento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tipoDocumento, or with status 400 (Bad Request) if the tipoDocumento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tipo-documentos")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<TipoDocumento> createTipoDocumento(@Valid @RequestBody TipoDocumento tipoDocumento) throws URISyntaxException {
        log.debug("REST request to save TipoDocumento : {}", tipoDocumento);
        if (tipoDocumento.getId() != null) {
            throw new BadRequestAlertException("A new tipoDocumento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoDocumento result = tipoDocumentoRepository.save(tipoDocumento);
        return ResponseEntity.created(new URI("/api/tipo-documentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tipo-documentos : Updates an existing tipoDocumento.
     *
     * @param tipoDocumento the tipoDocumento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tipoDocumento,
     * or with status 400 (Bad Request) if the tipoDocumento is not valid,
     * or with status 500 (Internal Server Error) if the tipoDocumento couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tipo-documentos")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<TipoDocumento> updateTipoDocumento(@Valid @RequestBody TipoDocumento tipoDocumento) throws URISyntaxException {
        log.debug("REST request to update TipoDocumento : {}", tipoDocumento);
        if (tipoDocumento.getId() == null) {
            return createTipoDocumento(tipoDocumento);
        }
        TipoDocumento result = tipoDocumentoRepository.save(tipoDocumento);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tipoDocumento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tipo-documentos : get all the tipoDocumentos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tipoDocumentos in body
     */
    @GetMapping("/tipo-documentos")
    @Timed
    public List<TipoDocumento> getAllTipoDocumentos() {
        log.debug("REST request to get all TipoDocumentos");
        return tipoDocumentoRepository.findAll();
        }

    /**
     * GET  /tipo-documentos/:id : get the "id" tipoDocumento.
     *
     * @param id the id of the tipoDocumento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tipoDocumento, or with status 404 (Not Found)
     */
    @GetMapping("/tipo-documentos/{id}")
    @Timed
    public ResponseEntity<TipoDocumento> getTipoDocumento(@PathVariable Long id) {
        log.debug("REST request to get TipoDocumento : {}", id);
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tipoDocumento));
    }

    /**
     * DELETE  /tipo-documentos/:id : delete the "id" tipoDocumento.
     *
     * @param id the id of the tipoDocumento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tipo-documentos/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteTipoDocumento(@PathVariable Long id) {
        log.debug("REST request to delete TipoDocumento : {}", id);
        tipoDocumentoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
