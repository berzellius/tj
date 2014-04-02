package com.tajinsurance.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * Created by berz on 24.03.14.
 */
@Entity
@Table(name = "partner")
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "partner_id_generator")
    @SequenceGenerator(name = "partner_id_generator", sequenceName = "partner_id_seq")
    @NotNull
    @Column(updatable = false, columnDefinition = "bigint")
    private Long id;

    public Partner() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    protected String value;

    public String getValue(){
        return this.value;
    }

    public void setValue(String value){
        this.value = value;
    }

    @ManyToMany
    @JoinTable(
        name = "partner_cat_contract",
        joinColumns = {
                @JoinColumn(name="partner_id")
        },
        inverseJoinColumns = {
                @JoinColumn(name = "cat_contract_id")
        }
    )
    protected List<CatContract> catContracts;

    public List<CatContract> getCatContracts(){
        return this.catContracts;
    }

    public void setCatContracts(List<CatContract> catContracts){
        this.catContracts = catContracts;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
