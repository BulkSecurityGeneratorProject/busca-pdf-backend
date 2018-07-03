package br.buscapdf.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A TipoDocumento.
 */
@Entity
@Table(name = "tipo_documento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoDocumento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "nome", length = 255, nullable = false)
    private String nome;

    @NotNull
    @Size(max = 255)
    @Column(name = "descricao", length = 255, nullable = false)
    private String descricao;

    @NotNull
    @Size(max = 4096)
    @Column(name = "diretorio", length = 4096, nullable = false)
    private String diretorio;

    @NotNull
    @Column(name = "divisao_anual", nullable = false)
    private Boolean divisaoAnual;

    @NotNull
    @Column(name = "pesquisa_automatica", nullable = false)
    private Boolean pesquisaAutomatica;

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

    public TipoDocumento nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public TipoDocumento descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDiretorio() {
        return diretorio;
    }

    public TipoDocumento diretorio(String diretorio) {
        this.diretorio = diretorio;
        return this;
    }

    public void setDiretorio(String diretorio) {
        this.diretorio = diretorio;
    }

    public Boolean isDivisaoAnual() {
        return divisaoAnual;
    }

    public TipoDocumento divisaoAnual(Boolean divisaoAnual) {
        this.divisaoAnual = divisaoAnual;
        return this;
    }

    public void setDivisaoAnual(Boolean divisaoAnual) {
        this.divisaoAnual = divisaoAnual;
    }

    public Boolean isPesquisaAutomatica() {
        return pesquisaAutomatica;
    }

    public TipoDocumento pesquisaAutomatica(Boolean pesquisaAutomatica) {
        this.pesquisaAutomatica = pesquisaAutomatica;
        return this;
    }

    public void setPesquisaAutomatica(Boolean pesquisaAutomatica) {
        this.pesquisaAutomatica = pesquisaAutomatica;
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
        TipoDocumento tipoDocumento = (TipoDocumento) o;
        if (tipoDocumento.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoDocumento.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoDocumento{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", diretorio='" + getDiretorio() + "'" +
            ", divisaoAnual='" + isDivisaoAnual() + "'" +
            ", pesquisaAutomatica='" + isPesquisaAutomatica() + "'" +
            "}";
    }
}
