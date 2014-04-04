package com.tajinsurance.service;

import com.tajinsurance.domain.*;
import com.tajinsurance.dto.ContractPremiumAjax;
import com.tajinsurance.dto.RiskAjax;
import com.tajinsurance.exceptions.EntityNotSavedException;
import com.tajinsurance.exceptions.NoEntityException;
import com.tajinsurance.utils.CodeUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

/**
 * Created by berz on 27.02.14.
 */
@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    @Autowired
    CodeUtils codeUtils;

    @Autowired
    EntityManager entityManager;

    @Autowired
    CatContractStatusService catContractStatusService;

    @Autowired
    CatContractService catContractService;

    @Autowired
    PremiumService premiumService;

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }



    @Override
    public Contract getContractById(Long id) throws NoEntityException {
        Contract c = Contract.findContract(id);
        if (c == null) throw new NoEntityException("there is no such Contract!");
        else return c;
    }

    @Override
    public Contract getContractById(Long id, String locale) throws NoEntityException {


        Contract c = Contract.findContract(id);
        if (c == null) throw new NoEntityException("there is no such Contract!");
        else return c;
    }

    @Override
    public List<Contract> getContracts(int first, int count, String sortFieldName, String sortOrder) {
        return Contract.findContractEntries(first, count, sortFieldName, sortOrder);
    }

    @Override
    public List<Contract> getContracts(int first, int count) {
        return Contract.findContractEntries(first, count);
    }

    @Override
    public List<Contract> getContractsPage(Integer numPage, Integer itemsToPage, String sortFieldName, String sortOrder) {
        int page = (numPage == null) ? 1 : numPage.intValue();
        int size = (itemsToPage == null) ? 10 : itemsToPage.intValue();

        int first = (page - 1) * size;


        if (sortFieldName != null) {
            return getContracts(first, size, sortFieldName, sortOrder);
        } else {
            return getContracts(first, size);
        }
    }

    @Override
    public Long countContracts() {
        return Contract.countContracts();
    }

    @Override
    public Contract preparedContract(User u, CatContract cc) {
        Session session = entityManager.unwrap(Session.class);

        // TODO блокировка!!
        String code = prepareCode(session);

        Contract c = new Contract();
        c.setC_number(code);
        c.setPartner(u.getPartner());
        c.setStartDate(new Date());
        c.setCatContractStatus(catContractStatusService.getCatContractStatusByCode(CatContractStatus.StatusCode.BEGIN));

        c.setCatContract(cc);
        // Здесь блок снимется

        session.save(c);
        session.flush();
        session.refresh(c);

        return c;
    }

    @Override
    public boolean checkIfTheRiskIsAllowed(Risk risk, Contract contract) {
        List<RiskAjax> ra = catContractService.getAllowedRisksForCatContract(contract.getCatContract());
        for(RiskAjax r : ra) if(r.riskId == risk.getId()) return true;
        return false;
    }

    @Override
    public Date printContract(Contract c) {

        if(c.getPrintDate() == null){
            c.setPrintDate(new Date());
        }

        c.persist();

        return c.getPrintDate();
    }

    private String prepareCode(Session session){
        String code = null;
        List<Contract> ct;
        do{
            code = codeUtils.getDigitCode(10);
            System.out.println(code);
            ct = session.createCriteria(Contract.class)
                    .add(Restrictions.eq("c_number", code))
                    .list();

        }
        while(ct.size() > 0);
        return code;
    }

    @Override
    public Integer countPages(Integer size) {
        if (size == null || size <= 0) size = 10;
        float fp = countContracts() / size;
        return (int) ((fp == 0.0 || fp > (int) fp) ? fp + 1 : fp);
    }

    @Override
    public void save(Contract contract) throws EntityNotSavedException {


        contract.persist();
        contract.flush();
        Long id = contract.getId();
        if (id == null) throw new EntityNotSavedException("не удалось создать объект " + contract.toString());
    }

    @Override
    public void update(Contract contract) throws NoEntityException {
        Contract old = getContractById(contract.getId());
        if (old == null) throw new NoEntityException("не найден контракт с id = " + contract.getId().toString());
        /*
            Код, который определяет, что никакие служебные поля не изменены
            Изменение фискальной информации
        */
        contract.merge();

        Session session = entityManager.unwrap(Session.class);

        List<ContractPremiumAjax> notValidatedPremiums = premiumService.getNotValidatedPremiums(contract);


        if(notValidatedPremiums != null){
            for(ContractPremiumAjax cp : notValidatedPremiums){
                ContractPremium cpr = (ContractPremium) session.get(ContractPremium.class, cp.premiumId);
                if(cpr.getInsuredSum() != null) cpr.setValidated(true);
                else cpr.setDeleted(true);
                session.update(cpr);
            }
            session.flush();
        }

    }

    @Override
    public void delete(Long id) throws NoEntityException {
        Contract c = getContractById(id);



        if (c == null) throw new NoEntityException("не найден контракт с id = " + c.getId().toString());

        //c.remove();
        c.setDeleted(true);
        c.persist();
    }


}
