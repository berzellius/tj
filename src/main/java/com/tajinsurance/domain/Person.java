package com.tajinsurance.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by berz on 24.03.14.
 */
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "person_id_generator")
    @SequenceGenerator(name = "person_id_generator", sequenceName = "person_id_seq")
    @NotNull
    @Column(updatable = false, columnDefinition = "bigint")
    private Long id;

    public Person() {
    }

    @Version
    @Column(name = "version")
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String surname;

    private String name;

    private String middle;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date born;

    @Column(name = "doc_series")
    private String docSeries;

    @Column(name = "doc_number")
    private String docNumber;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @Column(name = "doc_date")
    private Date docDate;

    @Column(name = "doc_department")
    private String docDepartment;

    @Column(name = "index_regisrt")
    private String indexRegistr;

    @Column(name = "addr_registr")
    private String addrRegistr;

    @Column(name = "index_resident")
    private String indexResident;

    @Column(name = "addr_resident")
    private String addrResident;

    @Column(name = "phone_home")
    private String phoneHome;

    @Column(name = "phone_mod")
    private String phoneMob;

    @Column(name = "phone_work")
    private String phoneWork;

    private String email;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public Date getBorn() {
        return born;
    }

    public void setBorn(Date born) {
        this.born = born;
    }

    public String getDocSeries() {
        return docSeries;
    }

    public void setDocSeries(String docSeries) {
        this.docSeries = docSeries;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public Date getDocDate() {
        return docDate;
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate;
    }

    public String getDocDepartment() {
        return docDepartment;
    }

    public void setDocDepartment(String docDepartment) {
        this.docDepartment = docDepartment;
    }

    public String getIndexRegistr() {
        return indexRegistr;
    }

    public void setIndexRegistr(String indexRegistr) {
        this.indexRegistr = indexRegistr;
    }

    public String getAddrRegistr() {
        return addrRegistr;
    }

    public void setAddrRegistr(String addrRegistr) {
        this.addrRegistr = addrRegistr;
    }

    public String getIndexResident() {
        return indexResident;
    }

    public void setIndexResident(String indexResident) {
        this.indexResident = indexResident;
    }

    public String getAddrResident() {
        return addrResident;
    }

    public void setAddrResident(String addrResident) {
        this.addrResident = addrResident;
    }

    public String getPhoneHome() {
        return phoneHome;
    }

    public void setPhoneHome(String phoneHome) {
        this.phoneHome = phoneHome;
    }

    public String getPhoneMob() {
        return phoneMob;
    }

    public void setPhoneMob(String phoneMob) {
        this.phoneMob = phoneMob;
    }

    public String getPhoneWork() {
        return phoneWork;
    }

    public void setPhoneWork(String phoneWork) {
        this.phoneWork = phoneWork;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString(){
        SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy");
        String s = this.getSurname() + " " + this.getName() + " " + this.getMiddle() + ", " + (this.getBorn() != null ? f.format(this.getBorn()) : "") + "; ";
        s += this.getDocSeries() + " № " + this.getDocNumber() + " " + this.getDocDepartment() + " / " + (this.getDocDate() != null?  f.format(this.getDocDate()) : "");
        return s;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
