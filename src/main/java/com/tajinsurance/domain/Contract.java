package com.tajinsurance.domain;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@Entity
public class Contract {

    /**
     */
    @NotNull
    @Length(max = 10)
    private String c_number;

    /**
     */
    private String c_memo;

    private boolean deleted;

    @Version
    @Column(name = "version")
    private Integer version;

    @JoinColumn(name = "cc_id")
    @OneToOne
    private CatContract catContract;

    @JoinColumn(name = "ccs_id")
    @OneToOne
    private CatContractStatus catContractStatus;

    @JoinColumn(name = "partner_id")
    @OneToOne
    private Partner partner;

    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    @Column(name = "print_date")
    private Date printDate;

    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    @Column(name = "app_date")
    private Date appDate;

    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    @Column(name = "pay_date")
    private Date payDate;

    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    @Column(name = "start_date")
    private Date startDate;

    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    @Column(name = "end_date")
    private Date endDate;

    @JoinColumn(name = "person_id")
    @OneToOne
    private Person person;

    private Integer length;

    @OneToMany(mappedBy = "contract", fetch = FetchType.LAZY)
    private List<ContractPremium> contractPremiums;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<ContractPremium> getContractPremiums() {
        return contractPremiums;
    }

    public void setContractPremiums(List<ContractPremium> contractPremiums) {
        this.contractPremiums = contractPremiums;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
