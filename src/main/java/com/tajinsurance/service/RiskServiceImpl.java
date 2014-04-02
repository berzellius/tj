package com.tajinsurance.service;

import com.tajinsurance.domain.CatContract;
import com.tajinsurance.domain.CatContractRisk;
import com.tajinsurance.domain.Risk;
import com.tajinsurance.exceptions.NoEntityException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by berz on 30.03.14.
 */
@Service
@Transactional
public class RiskServiceImpl implements RiskService {
    @Autowired
    EntityManager entityManager;

    @Override
    public Risk getRiskById(Long id) throws NoEntityException {
        Session session = entityManager.unwrap(Session.class);
        Risk r = (Risk) session.get(Risk.class, id);

        if(r == null) throw new NoEntityException("contract not found: id="+id);
        return r;
    }

    @Override
    public BigDecimal getRateForRisk(Risk risk, CatContract cc) {
        Session session = entityManager.unwrap(Session.class);
        List<CatContractRisk> catContractRiskList =  session.createCriteria(CatContractRisk.class)
                .add(Restrictions.eq("risk", risk))
                .add(Restrictions.eq("catContract", cc))
                .list();
        if(catContractRiskList.size() == 0) return null;

        return catContractRiskList.get(0).getRate();
    }
}
