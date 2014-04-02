package com.tajinsurance.service;

import com.tajinsurance.domain.Contract;
import com.tajinsurance.domain.ContractPremium;
import com.tajinsurance.dto.ContractPremiumAjax;
import com.tajinsurance.exceptions.NoEntityException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by berz on 30.03.14.
 */
@Service
@Transactional
public class PremiumServiceImpl implements PremiumService {
    @Autowired
    EntityManager entityManager;

    @Autowired
    RiskService riskService;

    @Override
    public ContractPremium savePremium(ContractPremium cp) {
        Session session = entityManager.unwrap(Session.class);


        // Готовим advisory lock
        /*byte[] b = cp.toString().getBytes();
        b = "01".toString().getBytes();
        CRC32 crc = new CRC32();
        crc.update(b);
        Long crc32 = crc.getValue();
        System.out.println("key :"+crc32);

        Query q = session.createSQLQuery("SELECT pg_try_advisory_xact_lock(" + crc32.toString() + ")");*/

        session.saveOrUpdate(cp);
        session.flush();
        session.refresh(cp);

        return cp;

    }

    @Override
    public ContractPremium getPremiumById(Long id) throws NoEntityException {
        Session session = entityManager.unwrap(Session.class);


        ContractPremium cp = (ContractPremium) session.get(ContractPremium.class, id);
        if(cp == null) throw new NoEntityException("ContractPremuim width id = " + id + " not found");

        return cp;
    }

    @Override
    public List<ContractPremiumAjax> getValidatedPremiums(Contract contract) {
        Session session = entityManager.unwrap(Session.class);

        List<ContractPremium> contractPremiumList = session.createCriteria(ContractPremium.class)
                .add(Restrictions.eq("contract", contract))
                .add(Restrictions.eq("validated", true))
                .add(Restrictions.eq("deleted", false)).list();
        if(contractPremiumList.size() == 0) return null;

        List<ContractPremiumAjax> contractPremiumAjaxes = new LinkedList<ContractPremiumAjax>();

        for(ContractPremium contractPremium : contractPremiumList){
            contractPremiumAjaxes.add(new ContractPremiumAjax(
                    contractPremium.getRisk().getValue(),
                    contractPremium.getInsuredSum(),
                    contractPremium.getPremium(),
                    contractPremium.getId()
            ));
        }

        return contractPremiumAjaxes;
    }

    @Override
    public List<ContractPremiumAjax> getNotValidatedPremiums(Contract contract) {
        Session session = entityManager.unwrap(Session.class);

        List<ContractPremium> contractPremiumList = session.createCriteria(ContractPremium.class)
                .add(Restrictions.eq("contract", contract))
                .add(Restrictions.eq("validated", false))
                .add(Restrictions.eq("deleted", false)).list();
        if(contractPremiumList.size() == 0) return null;

        List<ContractPremiumAjax> contractPremiumAjaxes = new LinkedList<ContractPremiumAjax>();

        for(ContractPremium contractPremium : contractPremiumList){
            contractPremiumAjaxes.add(new ContractPremiumAjax(
                    contractPremium.getRisk().getValue(),
                    contractPremium.getInsuredSum(),
                    contractPremium.getPremium(),
                    contractPremium.getId()
            ));
        }

        return contractPremiumAjaxes;
    }

    @Override
    public ContractPremium calculatePremium(ContractPremium cp) {
        Session session = entityManager.unwrap(Session.class);

        assert(cp.getContract() != null);
        assert (cp.getRisk() != null);
        assert (cp.getInsuredSum() != null);
        assert (cp.getContract().getCatContract() != null);

        BigDecimal rate = riskService.getRateForRisk(cp.getRisk(), cp.getContract().getCatContract());
        if(rate == null) return null;

        ContractPremium p = savePremium(cp);

        p.setPremium(
                p.getInsuredSum().multiply(rate)
        );


        session.saveOrUpdate(p);

        return cp;
    }

    @Override
    public boolean deletePremium(Long id) throws NoEntityException {
        Session session = entityManager.unwrap(Session.class);
        assert (id != 0l);

        LockOptions lo = new LockOptions();
        lo.setLockMode(LockMode.PESSIMISTIC_WRITE);

        ContractPremium cp = getPremiumById(id);

        session.buildLockRequest(lo).lock(cp);

        cp.setDeleted(true);

        session.update(cp);

        session.flush();
        session.refresh(cp);


        return true;
    }
}
