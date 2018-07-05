package br.buscapdf.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Documento.
 */
@Entity
@Table(name = "documento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Documento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 100)
    @Column(name = "nome", length = 100)
    private String nome;

    @Size(max = 255)
    @Column(name = "descricao", length = 255)
    private String descricao;

    @Size(max = 4096)
    @Column(name = "complemento_diretorio", length = 4096)
    private String complementoDiretorio;

    @NotNull
    @Size(max = 255)
    @Column(name = "nome_arquivo", length = 255, nullable = false)
    private String nomeArquivo;

    @ManyToOne
    private Diretorio diretorio;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Documento nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Documento descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getComplementoDiretorio() {
        return complementoDiretorio;
    }

    public Documento complementoDiretorio(String complementoDiretorio) {
        this.complementoDiretorio = complementoDiretorio;
        return this;
    }

    public void setComplementoDiretorio(String complementoDiretorio) {
        this.complementoDiretorio = complementoDiretorio;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public Documento nomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
        return this;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public Diretorio getDiretorio() {
        return diretorio;
    }

    public Documento diretorio(Diretorio diretorio) {
        this.diretorio = diretorio;
        return this;
    }

    public void setDiretorio(Diretorio diretorio) {
        this.diretorio = diretorio;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Documento documento = (Documento) o;
        if (documento.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), documento.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Documento{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", complementoDiretorio='" + getComplementoDiretorio() + "'" +
            ", nomeArquivo='" + getNomeArquivo() + "'" +
            "}";
    }
}
