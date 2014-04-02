package com.tajinsurance.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by berz on 27.03.14.
 */
@Entity
@Table(name = "cat_contract_risk")
public class CatContractRisk {
    public CatContractRisk() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "cat_contract_risk_id_generator")
    @SequenceGenerator(name = "cat_contract_risk_id_generator", sequenceName = "cat_contract_risk_id_seq")
    @NotNull
    @Column(updatable = false, columnDefinition = "bigint")
    private Long id;

    @OneToOne
    @JoinColumn(name = "cat_contract_id")
    private CatContract catContract;

    @OneToOne
    @JoinColumn(name = "risk_id")
    private Risk risk;

    private BigDecimal rate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public CatContract getCatContract() {
        return catContract;
    }

    public void setCatContract(CatContract catContract) {
        this.catContract = catContract;
    }

    public Risk getRisk() {
        return risk;
    }

    public void setRisk(Risk risk) {
        this.risk = risk;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
