package com.tajinsurance.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by berz on 24.03.14.
 */
@Entity
@Table(name = "risk")
public class Risk implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "risk_id_generator")
    @SequenceGenerator(name = "risk_id_generator", sequenceName = "risk_id_seq")
    @NotNull
    @Column(updatable = false, columnDefinition = "bigint")
    private Long id;

    public Risk() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String value;

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Transient
    private Integer version;

    /*
    * Буквенная комбинация, позволяющая определить риск
    */
    private String det;

    private Boolean deleted;

    @JoinColumn(name = "tpr_id")
    @OneToOne
    private TypeOfRisk typeOfRisk;

    @Override
    public String toString() {
        return getValue();
    }

    @Override
    public boolean equals(Object obj) {
        return getId().equals(((Risk) obj).getId()) && obj instanceof Risk;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));

        return result;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDet() {
        return det;
    }

    public void setDet(String det) {
        this.det = det;
    }

    public TypeOfRisk getTypeOfRisk() {
        return typeOfRisk;
    }

    public void setTypeOfRisk(TypeOfRisk typeOfRisk) {
        this.typeOfRisk = typeOfRisk;
    }
}
