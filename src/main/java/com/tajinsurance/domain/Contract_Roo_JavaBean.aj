// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.tajinsurance.domain;

import java.util.Date;

privileged aspect Contract_Roo_JavaBean {

    public String Contract.getC_number() {
        return this.c_number;
    }

    public void Contract.setC_number(String c_number) {
        this.c_number = c_number;
    }

    public String Contract.getC_memo() {
        return this.c_memo;
    }

    public void Contract.setC_memo(String c_memo) {
        this.c_memo = c_memo;
    }

    public CatContract Contract.getCatContract() {
        return this.catContract;
    }

    public void Contract.setCatContract(CatContract catContract) {
        this.catContract = catContract;
    }

    public CatContractStatus Contract.getCatContractStatus() {
        return this.catContractStatus;
    }

    public void Contract.setCatContractStatus(CatContractStatus catContractStatus) {
        this.catContractStatus = catContractStatus;
    }

    public Partner Contract.getPartner() {
        return partner;
    }

    public void Contract.setPartner(Partner partner) {
        this.partner = partner;
    }

    public Date Contract.getPrintDate() {
        return printDate;
    }

    public void Contract.setPrintDate(Date printDate) {
        this.printDate = printDate;
    }

    public Date Contract.getAppDate() {
        return appDate;
    }

    public void Contract.setAppDate(Date appDate) {
        this.appDate = appDate;
    }

    public Date Contract.getPayDate() {
        return payDate;
    }

    public void Contract.setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date Contract.getStartDate() {
        return startDate;
    }

    public void Contract.setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date Contract.getEndDate() {
        return endDate;
    }

    public void Contract.setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Person Contract.getPerson() {
        return person;
    }

    public void Contract.setPerson(Person person) {
        this.person = person;
    }

    public Integer Contract.getLength() {
        return length;
    }

    public void Contract.setLength(Integer length) {
        this.length = length;
    }
}
