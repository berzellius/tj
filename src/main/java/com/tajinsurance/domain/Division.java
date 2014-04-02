package com.tajinsurance.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by berz on 24.03.14.
 */
@Entity
@Table(name = "division")
public class Division {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "division_id_generator")
    @SequenceGenerator(name = "division_id_generator", sequenceName = "division_id_seq")
    @NotNull
    @Column(updatable = false, columnDefinition = "bigint")
    private Long id;

    public Division() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String value;

    @JoinColumn(name = "partner_id")
    @OneToOne
    private Partner partner;

    public Partner getPartners() {
        return partner;
    }

    public void setPartners(Partner partners) {
        this.partner = partners;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
