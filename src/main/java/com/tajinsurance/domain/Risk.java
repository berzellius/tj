package com.tajinsurance.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by berz on 24.03.14.
 */
@Entity
@Table(name = "risk")
public class Risk {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "risk_id_generator")
    @SequenceGenerator(name = "risk_id_generator", sequenceName = "risk_id_seq")
    @NotNull
    @Column(updatable = false, columnDefinition = "bigint")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String value;

    public String getValue(){
        return this.value;
    }

    public void setValue(String value){
        this.value = value;
    }
}
