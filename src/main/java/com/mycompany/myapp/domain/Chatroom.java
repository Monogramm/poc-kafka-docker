package com.mycompany.myapp.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * The Chatroom entity.
 */
@ApiModel(description = "The Chatroom entity.")
@Entity
@Table(name = "chatroom")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "chatroom")
public class Chatroom extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The chatroom name attribute.
     */
    @ApiModelProperty(value = "The chatroom name attribute.")
    @Size(min = 1, max = 255)
    @Column(name = "name", length = 255, unique = true, nullable = false)
    private String name;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Chatroom name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Chatroom)) {
            return false;
        }
        return id != null && id.equals(((Chatroom) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Chatroom{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
