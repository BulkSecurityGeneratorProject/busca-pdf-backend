package br.buscapdf.web.rest;

import br.buscapdf.security.AuthoritiesConstants;
import com.codahale.metrics.annotation.Timed;
import br.buscapdf.domain.Diretorio;

import br.buscapdf.repository.DiretorioRepository;
import br.buscapdf.web.rest.errors.BadRequestAlertException;
import br.buscapdf.web.rest.util.HeaderUtil;
import br.buscapdf.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Diretorio.
 */
@RestController
@RequestMapping("/api")
public class DiretorioResource {

    private final Logger log = LoggerFactory.getLogger(DiretorioResource.class);

    private static final String ENTITY_NAME = "diretorio";

    private final DiretorioRepository diretorioRepository;

    public DiretorioResource(DiretorioRepository diretorioRepository) {
        this.diretorioRepository = diretorioRepository;
    }

    /**
     * POST  /diretorios : Create a new diretorio.
     *
     * @param diretorio the diretorio to create
     * @return the ResponseEntity with status 201 (Created) and with body the new diretorio, or with status 400 (Bad Request) if the diretorio has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/diretorios")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Diretorio> createDiretorio(@Valid @RequestBody Diretorio diretorio) throws URISyntaxException {
        log.debug("REST request to save Diretorio : {}", diretorio);
        if (diretorio.getId() != null) {
            throw new BadRequestAlertException("A new diretorio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Diretorio result = diretorioRepository.save(diretorio);
        return ResponseEntity.created(new URI("/api/diretorios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /diretorios : Updates an existing diretorio.
     *
     * @param diretorio the diretorio to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated diretorio,
     * or with status 400 (Bad Request) if the diretorio is not valid,
     * or with status 500 (Internal Server Error) if the diretorio couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/diretorios")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Diretorio> updateDiretorio(@Valid @RequestBody Diretorio diretorio) throws URISyntaxException {
        log.debug("REST request to update Diretorio : {}", diretorio);
        if (diretorio.getId() == null) {
            return createDiretorio(diretorio);
        }
        Diretorio result = diretorioRepository.save(diretorio);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, diretorio.getId().toString()))
            .body(result);
    }

    /**
     * GET  /diretorios : get all the diretorios.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of diretorios in body
     */
    @GetMapping("/diretorios")
    @Timed
    public ResponseEntity<List<Diretorio>> getAllDiretorios(Pageable pageable) {
        log.debug("REST request to get a page of Diretorios");
        Page<Diretorio> page = diretorioRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/diretorios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /diretorios/:id : get the "id" diretorio.
     *
     * @param id the id of the diretorio to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the diretorio, or with status 404 (Not Found)
     */
    @GetMapping("/diretorios/{id}")
    @Timed
    public ResponseEntity<Diretorio> getDiretorio(@PathVariable Long id) {
        log.debug("REST request to get Diretorio : {}", id);
        Diretorio diretorio = diretorioRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(diretorio));
    }

    /**
     * DELETE  /diretorios/:id : delete the "id" diretorio.
     *
     * @param id the id of the diretorio to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/diretorios/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteDiretorio(@PathVariable Long id) {
        log.debug("REST request to delete Diretorio : {}", id);
        diretorioRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
