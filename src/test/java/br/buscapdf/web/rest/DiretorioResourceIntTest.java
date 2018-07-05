package br.buscapdf.web.rest;

import br.buscapdf.BuscapdfApp;

import br.buscapdf.domain.Diretorio;
import br.buscapdf.repository.DiretorioRepository;
import br.buscapdf.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static br.buscapdf.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DiretorioResource REST controller.
 *
 * @see DiretorioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BuscapdfApp.class)
public class DiretorioResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    @Autowired
    private DiretorioRepository diretorioRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDiretorioMockMvc;

    private Diretorio diretorio;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DiretorioResource diretorioResource = new DiretorioResource(diretorioRepository);
        this.restDiretorioMockMvc = MockMvcBuilders.standaloneSetup(diretorioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diretorio createEntity(EntityManager em) {
        Diretorio diretorio = new Diretorio()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .path(DEFAULT_PATH);
        return diretorio;
    }

    @Before
    public void initTest() {
        diretorio = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiretorio() throws Exception {
        int databaseSizeBeforeCreate = diretorioRepository.findAll().size();

        // Create the Diretorio
        restDiretorioMockMvc.perform(post("/api/diretorios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diretorio)))
            .andExpect(status().isCreated());

        // Validate the Diretorio in the database
        List<Diretorio> diretorioList = diretorioRepository.findAll();
        assertThat(diretorioList).hasSize(databaseSizeBeforeCreate + 1);
        Diretorio testDiretorio = diretorioList.get(diretorioList.size() - 1);
        assertThat(testDiretorio.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testDiretorio.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testDiretorio.getPath()).isEqualTo(DEFAULT_PATH);
    }

    @Test
    @Transactional
    public void createDiretorioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = diretorioRepository.findAll().size();

        // Create the Diretorio with an existing ID
        diretorio.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiretorioMockMvc.perform(post("/api/diretorios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diretorio)))
            .andExpect(status().isBadRequest());

        // Validate the Diretorio in the database
        List<Diretorio> diretorioList = diretorioRepository.findAll();
        assertThat(diretorioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = diretorioRepository.findAll().size();
        // set the field null
        diretorio.setNome(null);

        // Create the Diretorio, which fails.

        restDiretorioMockMvc.perform(post("/api/diretorios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diretorio)))
            .andExpect(status().isBadRequest());

        List<Diretorio> diretorioList = diretorioRepository.findAll();
        assertThat(diretorioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = diretorioRepository.findAll().size();
        // set the field null
        diretorio.setDescricao(null);

        // Create the Diretorio, which fails.

        restDiretorioMockMvc.perform(post("/api/diretorios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diretorio)))
            .andExpect(status().isBadRequest());

        List<Diretorio> diretorioList = diretorioRepository.findAll();
        assertThat(diretorioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = diretorioRepository.findAll().size();
        // set the field null
        diretorio.setPath(null);

        // Create the Diretorio, which fails.

        restDiretorioMockMvc.perform(post("/api/diretorios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diretorio)))
            .andExpect(status().isBadRequest());

        List<Diretorio> diretorioList = diretorioRepository.findAll();
        assertThat(diretorioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiretorios() throws Exception {
        // Initialize the database
        diretorioRepository.saveAndFlush(diretorio);

        // Get all the diretorioList
        restDiretorioMockMvc.perform(get("/api/diretorios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(diretorio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())));
    }

    @Test
    @Transactional
    public void getDiretorio() throws Exception {
        // Initialize the database
        diretorioRepository.saveAndFlush(diretorio);

        // Get the diretorio
        restDiretorioMockMvc.perform(get("/api/diretorios/{id}", diretorio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(diretorio.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDiretorio() throws Exception {
        // Get the diretorio
        restDiretorioMockMvc.perform(get("/api/diretorios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiretorio() throws Exception {
        // Initialize the database
        diretorioRepository.saveAndFlush(diretorio);
        int databaseSizeBeforeUpdate = diretorioRepository.findAll().size();

        // Update the diretorio
        Diretorio updatedDiretorio = diretorioRepository.findOne(diretorio.getId());
        // Disconnect from session so that the updates on updatedDiretorio are not directly saved in db
        em.detach(updatedDiretorio);
        updatedDiretorio
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .path(UPDATED_PATH);

        restDiretorioMockMvc.perform(put("/api/diretorios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDiretorio)))
            .andExpect(status().isOk());

        // Validate the Diretorio in the database
        List<Diretorio> diretorioList = diretorioRepository.findAll();
        assertThat(diretorioList).hasSize(databaseSizeBeforeUpdate);
        Diretorio testDiretorio = diretorioList.get(diretorioList.size() - 1);
        assertThat(testDiretorio.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testDiretorio.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testDiretorio.getPath()).isEqualTo(UPDATED_PATH);
    }

    @Test
    @Transactional
    public void updateNonExistingDiretorio() throws Exception {
        int databaseSizeBeforeUpdate = diretorioRepository.findAll().size();

        // Create the Diretorio

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDiretorioMockMvc.perform(put("/api/diretorios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(diretorio)))
            .andExpect(status().isCreated());

        // Validate the Diretorio in the database
        List<Diretorio> diretorioList = diretorioRepository.findAll();
        assertThat(diretorioList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDiretorio() throws Exception {
        // Initialize the database
        diretorioRepository.saveAndFlush(diretorio);
        int databaseSizeBeforeDelete = diretorioRepository.findAll().size();

        // Get the diretorio
        restDiretorioMockMvc.perform(delete("/api/diretorios/{id}", diretorio.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Diretorio> diretorioList = diretorioRepository.findAll();
        assertThat(diretorioList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Diretorio.class);
        Diretorio diretorio1 = new Diretorio();
        diretorio1.setId(1L);
        Diretorio diretorio2 = new Diretorio();
        diretorio2.setId(diretorio1.getId());
        assertThat(diretorio1).isEqualTo(diretorio2);
        diretorio2.setId(2L);
        assertThat(diretorio1).isNotEqualTo(diretorio2);
        diretorio1.setId(null);
        assertThat(diretorio1).isNotEqualTo(diretorio2);
    }
}
