package br.buscapdf.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A UserExtra.
 */
@Entity
@Table(name = "user_extra")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserExtra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull
    @Size(max = 11)
    @Column(name = "cpf", length = 11, nullable = false)
    private String cpf;

    @NotNull
    @Column(name = "rg", nullable = false)
    private Integer rg;

    @NotNull
    @Column(name = "functional_number", nullable = false)
    private Integer functionalNumber;

    @OneToOne
    @MapsId
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public UserExtra cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getRg() {
        return rg;
    }

    public UserExtra rg(Integer rg) {
        this.rg = rg;
        return this;
    }

    public void setRg(Integer rg) {
        this.rg = rg;
    }

    public Integer getFunctionalNumber() {
        return functionalNumber;
    }

    public UserExtra functionalNumber(Integer functionalNumber) {
        this.functionalNumber = functionalNumber;
        return this;
    }

    public void setFunctionalNumber(Integer functionalNumber) {
        this.functionalNumber = functionalNumber;
    }

    public User getUser() {
        return user;
    }

    public UserExtra user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        UserExtra userExtra = (UserExtra) o;
        if (userExtra.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userExtra.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserExtra{" +
            "id=" + getId() +
            ", cpf='" + getCpf() + "'" +
            ", rg=" + getRg() +
            ", functionalNumber=" + getFunctionalNumber() +
            "}";
    }
}
