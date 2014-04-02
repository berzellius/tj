package com.tajinsurance.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by berz on 20.03.14.
 */
@Entity(name = "CatContract")
@Table(name = "cat_contract")
public class CatContract {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "cat_contract_id_generator")
    @SequenceGenerator(name = "cat_contract_id_generator", sequenceName = "cat_contract_id_seq")
    @NotNull
    @Column(updatable = false, columnDefinition = "bigint")
    private Long id;

    private String name;

    private String value;

    /*@ManyToMany
    @JoinTable(
            name = "cat_contract_risk_map",
            joinColumns = {
                    @JoinColumn(name = "cat_contract_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "risk_id")
            }
    )
    private Set<Risk> risks;*/

    @OneToMany(mappedBy = "catContract", fetch = FetchType.LAZY)
    private List<CatContractLocaleEntity> localeEntityList;

    @Override
    public String toString(){
        return name + ": "+ value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocaleEntityList(List<CatContractLocaleEntity> localeEntityList) {
        this.localeEntityList = localeEntityList;
    }

    public List<CatContractLocaleEntity> getLocaleEntityList(){
        return localeEntityList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /*public Set<Risk> getRisks() {
        return risks;
    }

    public void setRisks(Set<Risk> risks) {
        this.risks = risks;
    }*/
}
