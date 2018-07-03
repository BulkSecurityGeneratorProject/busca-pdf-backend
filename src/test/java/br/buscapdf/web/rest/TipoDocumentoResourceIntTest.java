package br.buscapdf.web.rest;

import br.buscapdf.BuscapdfApp;

import br.buscapdf.domain.TipoDocumento;
import br.buscapdf.repository.TipoDocumentoRepository;
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
 * Test class for the TipoDocumentoResource REST controller.
 *
 * @see TipoDocumentoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BuscapdfApp.class)
public class TipoDocumentoResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_DIRETORIO = "AAAAAAAAAA";
    private static final String UPDATED_DIRETORIO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DIVISAO_ANUAL = false;
    private static final Boolean UPDATED_DIVISAO_ANUAL = true;

    private static final Boolean DEFAULT_PESQUISA_AUTOMATICA = false;
    private static final Boolean UPDATED_PESQUISA_AUTOMATICA = true;

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTipoDocumentoMockMvc;

    private TipoDocumento tipoDocumento;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipoDocumentoResource tipoDocumentoResource = new TipoDocumentoResource(tipoDocumentoRepository);
        this.restTipoDocumentoMockMvc = MockMvcBuilders.standaloneSetup(tipoDocumentoResource)
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
    public static TipoDocumento createEntity(EntityManager em) {
        TipoDocumento tipoDocumento = new TipoDocumento()
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .diretorio(DEFAULT_DIRETORIO)
            .divisaoAnual(DEFAULT_DIVISAO_ANUAL)
            .pesquisaAutomatica(DEFAULT_PESQUISA_AUTOMATICA);
        return tipoDocumento;
    }

    @Before
    public void initTest() {
        tipoDocumento = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipoDocumento() throws Exception {
        int databaseSizeBeforeCreate = tipoDocumentoRepository.findAll().size();

        // Create the TipoDocumento
        restTipoDocumentoMockMvc.perform(post("/api/tipo-documentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoDocumento)))
            .andExpect(status().isCreated());

        // Validate the TipoDocumento in the database
        List<TipoDocumento> tipoDocumentoList = tipoDocumentoRepository.findAll();
        assertThat(tipoDocumentoList).hasSize(databaseSizeBeforeCreate + 1);
        TipoDocumento testTipoDocumento = tipoDocumentoList.get(tipoDocumentoList.size() - 1);
        assertThat(testTipoDocumento.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTipoDocumento.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testTipoDocumento.getDiretorio()).isEqualTo(DEFAULT_DIRETORIO);
        assertThat(testTipoDocumento.isDivisaoAnual()).isEqualTo(DEFAULT_DIVISAO_ANUAL);
        assertThat(testTipoDocumento.isPesquisaAutomatica()).isEqualTo(DEFAULT_PESQUISA_AUTOMATICA);
    }

    @Test
    @Transactional
    public void createTipoDocumentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoDocumentoRepository.findAll().size();

        // Create the TipoDocumento with an existing ID
        tipoDocumento.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoDocumentoMockMvc.perform(post("/api/tipo-documentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoDocumento)))
            .andExpect(status().isBadRequest());

        // Validate the TipoDocumento in the database
        List<TipoDocumento> tipoDocumentoList = tipoDocumentoRepository.findAll();
        assertThat(tipoDocumentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoDocumentoRepository.findAll().size();
        // set the field null
        tipoDocumento.setNome(null);

        // Create the TipoDocumento, which fails.

        restTipoDocumentoMockMvc.perform(post("/api/tipo-documentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoDocumento)))
            .andExpect(status().isBadRequest());

        List<TipoDocumento> tipoDocumentoList = tipoDocumentoRepository.findAll();
        assertThat(tipoDocumentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoDocumentoRepository.findAll().size();
        // set the field null
        tipoDocumento.setDescricao(null);

        // Create the TipoDocumento, which fails.

        restTipoDocumentoMockMvc.perform(post("/api/tipo-documentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoDocumento)))
            .andExpect(status().isBadRequest());

        List<TipoDocumento> tipoDocumentoList = tipoDocumentoRepository.findAll();
        assertThat(tipoDocumentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDiretorioIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoDocumentoRepository.findAll().size();
        // set the field null
        tipoDocumento.setDiretorio(null);

        // Create the TipoDocumento, which fails.

        restTipoDocumentoMockMvc.perform(post("/api/tipo-documentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoDocumento)))
            .andExpect(status().isBadRequest());

        List<TipoDocumento> tipoDocumentoList = tipoDocumentoRepository.findAll();
        assertThat(tipoDocumentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDivisaoAnualIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoDocumentoRepository.findAll().size();
        // set the field null
        tipoDocumento.setDivisaoAnual(null);

        // Create the TipoDocumento, which fails.

        restTipoDocumentoMockMvc.perform(post("/api/tipo-documentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoDocumento)))
            .andExpect(status().isBadRequest());

        List<TipoDocumento> tipoDocumentoList = tipoDocumentoRepository.findAll();
        assertThat(tipoDocumentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPesquisaAutomaticaIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoDocumentoRepository.findAll().size();
        // set the field null
        tipoDocumento.setPesquisaAutomatica(null);

        // Create the TipoDocumento, which fails.

        restTipoDocumentoMockMvc.perform(post("/api/tipo-documentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoDocumento)))
            .andExpect(status().isBadRequest());

        List<TipoDocumento> tipoDocumentoList = tipoDocumentoRepository.findAll();
        assertThat(tipoDocumentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipoDocumentos() throws Exception {
        // Initialize the database
        tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get all the tipoDocumentoList
        restTipoDocumentoMockMvc.perform(get("/api/tipo-documentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoDocumento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].diretorio").value(hasItem(DEFAULT_DIRETORIO.toString())))
            .andExpect(jsonPath("$.[*].divisaoAnual").value(hasItem(DEFAULT_DIVISAO_ANUAL.booleanValue())))
            .andExpect(jsonPath("$.[*].pesquisaAutomatica").value(hasItem(DEFAULT_PESQUISA_AUTOMATICA.booleanValue())));
    }

    @Test
    @Transactional
    public void getTipoDocumento() throws Exception {
        // Initialize the database
        tipoDocumentoRepository.saveAndFlush(tipoDocumento);

        // Get the tipoDocumento
        restTipoDocumentoMockMvc.perform(get("/api/tipo-documentos/{id}", tipoDocumento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipoDocumento.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.diretorio").value(DEFAULT_DIRETORIO.toString()))
            .andExpect(jsonPath("$.divisaoAnual").value(DEFAULT_DIVISAO_ANUAL.booleanValue()))
            .andExpect(jsonPath("$.pesquisaAutomatica").value(DEFAULT_PESQUISA_AUTOMATICA.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTipoDocumento() throws Exception {
        // Get the tipoDocumento
        restTipoDocumentoMockMvc.perform(get("/api/tipo-documentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoDocumento() throws Exception {
        // Initialize the database
        tipoDocumentoRepository.saveAndFlush(tipoDocumento);
        int databaseSizeBeforeUpdate = tipoDocumentoRepository.findAll().size();

        // Update the tipoDocumento
        TipoDocumento updatedTipoDocumento = tipoDocumentoRepository.findOne(tipoDocumento.getId());
        // Disconnect from session so that the updates on updatedTipoDocumento are not directly saved in db
        em.detach(updatedTipoDocumento);
        updatedTipoDocumento
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .diretorio(UPDATED_DIRETORIO)
            .divisaoAnual(UPDATED_DIVISAO_ANUAL)
            .pesquisaAutomatica(UPDATED_PESQUISA_AUTOMATICA);

        restTipoDocumentoMockMvc.perform(put("/api/tipo-documentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTipoDocumento)))
            .andExpect(status().isOk());

        // Validate the TipoDocumento in the database
        List<TipoDocumento> tipoDocumentoList = tipoDocumentoRepository.findAll();
        assertThat(tipoDocumentoList).hasSize(databaseSizeBeforeUpdate);
        TipoDocumento testTipoDocumento = tipoDocumentoList.get(tipoDocumentoList.size() - 1);
        assertThat(testTipoDocumento.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTipoDocumento.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testTipoDocumento.getDiretorio()).isEqualTo(UPDATED_DIRETORIO);
        assertThat(testTipoDocumento.isDivisaoAnual()).isEqualTo(UPDATED_DIVISAO_ANUAL);
        assertThat(testTipoDocumento.isPesquisaAutomatica()).isEqualTo(UPDATED_PESQUISA_AUTOMATICA);
    }

    @Test
    @Transactional
    public void updateNonExistingTipoDocumento() throws Exception {
        int databaseSizeBeforeUpdate = tipoDocumentoRepository.findAll().size();

        // Create the TipoDocumento

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTipoDocumentoMockMvc.perform(put("/api/tipo-documentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoDocumento)))
            .andExpect(status().isCreated());

        // Validate the TipoDocumento in the database
        List<TipoDocumento> tipoDocumentoList = tipoDocumentoRepository.findAll();
        assertThat(tipoDocumentoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTipoDocumento() throws Exception {
        // Initialize the database
        tipoDocumentoRepository.saveAndFlush(tipoDocumento);
        int databaseSizeBeforeDelete = tipoDocumentoRepository.findAll().size();

        // Get the tipoDocumento
        restTipoDocumentoMockMvc.perform(delete("/api/tipo-documentos/{id}", tipoDocumento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TipoDocumento> tipoDocumentoList = tipoDocumentoRepository.findAll();
        assertThat(tipoDocumentoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoDocumento.class);
        TipoDocumento tipoDocumento1 = new TipoDocumento();
        tipoDocumento1.setId(1L);
        TipoDocumento tipoDocumento2 = new TipoDocumento();
        tipoDocumento2.setId(tipoDocumento1.getId());
        assertThat(tipoDocumento1).isEqualTo(tipoDocumento2);
        tipoDocumento2.setId(2L);
        assertThat(tipoDocumento1).isNotEqualTo(tipoDocumento2);
        tipoDocumento1.setId(null);
        assertThat(tipoDocumento1).isNotEqualTo(tipoDocumento2);
    }
}
