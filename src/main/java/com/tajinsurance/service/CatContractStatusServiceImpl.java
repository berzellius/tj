package com.tajinsurance.service;

import com.tajinsurance.domain.CatContractStatus;
import com.tajinsurance.domain.CatContractStatusLocaleEntity;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * Created by berz on 22.03.14.
 */
@Service
@Transactional
public class CatContractStatusServiceImpl implements CatContractStatusService {
    @Autowired
    EntityManager entityManager;


    @Override
    public List<CatContractStatus> getAllCatContractStatuses(String locale) {
        Session session = entityManager.unwrap(Session.class);
        List<CatContractStatus> cts = session.createCriteria(CatContractStatus.class).list();
        for(CatContractStatus ct : cts){
            for(CatContractStatusLocaleEntity ctl : ct.getLocaleEntityList()){
                if(ctl.getLocale().equals(locale)){
                    ct.setValue(ctl.getValue());
                }
            }
        }
        return cts;
    }

    @Override
    public CatContractStatus getCatContractStatusById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        CatContractStatus ct = (CatContractStatus) session.get(CatContractStatus.class, id);
        if(ct == null) throw new EntityNotFoundException("cant find CatContractStatus object with id: "+id);
        return ct;
    }

    @Override
    public CatContractStatus getCatContractStatusByCode(CatContractStatus.StatusCode code) {
        Session session = entityManager.unwrap(Session.class);
        CatContractStatus ccs = (CatContractStatus) session.createCriteria(CatContractStatus.class)
                .add(Restrictions.eq("code", code)).list().get(0);
        return ccs;
    }


}
