package com.tajinsurance.service;

import com.tajinsurance.domain.CatContract;
import com.tajinsurance.domain.CatContractLocaleEntity;
import com.tajinsurance.domain.CatContractRisk;
import com.tajinsurance.domain.User;
import com.tajinsurance.dto.RiskAjax;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by berz on 20.03.14.
 */
@Service
@Transactional
public class CatContractServiceImpl implements CatContractService {

    /*@PersistenceContext
    transient EntityManager entityManager;*/
    @Autowired
    EntityManager entityManager;

    @Override
    public List<CatContract> getAllCatContracts(String locale) {
        Session session = entityManager.unwrap(Session.class);
        List<CatContract> cts = session.createCriteria(CatContract.class).list();
        return getLocalizations(cts, locale);
    }

    private List<CatContract> getLocalizations(List<CatContract> cts, String locale){
        for(CatContract ct : cts){
            for(CatContractLocaleEntity ctl : ct.getLocaleEntityList()){
                if(ctl.getLocale().equals(locale)){
                    ct.setName(ctl.getName());
                    ct.setValue(ctl.getValue());
                }
            }
        }
        return cts;
    }

    @Override
    public CatContract getCatContractById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        CatContract ct = (CatContract) session.get(CatContract.class, id);
        if(ct == null) throw new EntityNotFoundException("cant find CatContract object with id: "+id);
        return ct;
    }

    @Override
    public List<CatContract> getAllowedCatContractsForUser(User u, String locale) {
        return getLocalizations(u.getPartner().getCatContracts(),locale);
    }

    @Override
    public List<RiskAjax> getAllowedRisksForCatContract(CatContract cc) {
        Session session = entityManager.unwrap(Session.class);

        List<RiskAjax> ra = new LinkedList<RiskAjax>();
            List<CatContractRisk> ccr = (List<CatContractRisk>) session.createCriteria(CatContractRisk.class)
                    .add(Restrictions.eq("catContract", cc))
                    .list();
            for(CatContractRisk cr : ccr){
                ra.add(new RiskAjax(cr.getRisk().getValue(), cr.getRisk().getId(), cr.getRate()));
            }
        return ra;
    }


}
