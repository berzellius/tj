package com.tajinsurance.service;

import com.tajinsurance.domain.*;
import com.tajinsurance.dto.ContractPremiumAjax;
import com.tajinsurance.exceptions.CalculatePremiumException;
import com.tajinsurance.exceptions.NoEntityException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by berz on 30.03.14.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PremiumServiceImpl implements PremiumService {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    RiskService riskService;

    @Autowired
    PartnerService partnerService;

    public PremiumServiceImpl() {
    }

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
        if (cp == null) throw new NoEntityException("ContractPremuim width id = " + id + " not found");

        return cp;
    }

    @Override
    public List<ContractPremium> getValidatedCPremiums(Contract contract) {
        return entityManager.createQuery("SELECT o FROM ContractPremium o WHERE contract = :c AND validated = true AND deleted = false", ContractPremium.class)
                .setParameter("c", contract)
                .getResultList();
    }


    @Override
    public List<ContractPremiumAjax> getValidatedPremiums(Contract contract) {
        Session session = entityManager.unwrap(Session.class);

        List<ContractPremium> contractPremiumList = session.createCriteria(ContractPremium.class)
                .add(Restrictions.eq("contract", contract))
                .add(Restrictions.eq("validated", true))
                .add(Restrictions.eq("deleted", false)).list();
        if (contractPremiumList.size() == 0) return null;

        List<ContractPremiumAjax> contractPremiumAjaxes = new LinkedList<ContractPremiumAjax>();

        for (ContractPremium contractPremium : contractPremiumList) {
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
        if (contractPremiumList.size() == 0) return null;

        List<ContractPremiumAjax> contractPremiumAjaxes = new LinkedList<ContractPremiumAjax>();

        for (ContractPremium contractPremium : contractPremiumList) {
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
    @Transactional(propagation = Propagation.MANDATORY)
    public ContractPremium calculatePremium(ContractPremium cp, Integer l) throws CalculatePremiumException {
        return calculatePremium(cp, l, BigDecimal.ONE);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public ContractPremium calculatePremium(ContractPremium cp, Integer l, BigDecimal q) throws CalculatePremiumException {

        assert (cp.getContract() != null);
        assert (cp.getRisk() != null);
        assert (cp.getInsuredSum() != null);
        assert (cp.getContract().getCatContract() != null);


        //BigDecimal rate = riskService.getRateForRisk(cp.getRisk(), cp.getContract().getCatContract());
        CatContractRisk ccr = riskService.getRisk(cp.getRisk(), cp.getContract().getCatContract(), cp.getContract().getPartner());
        if (ccr == null) throw new CalculatePremiumException("risk_not_set");
        BigDecimal rate = ccr.getRate();
        BigDecimal monthTarif = ccr.getMonthTarif().divide(BigDecimal.valueOf(100));

        assert (ccr.getMaxSum() != null);
        assert (ccr.getMinSum() != null);

        cp.setInsuredSum(cp.getInsuredSum().multiply(rate).setScale(0, RoundingMode.HALF_UP));

        if (cp.getContract().getCatContract().getMinTerm() != null && cp.getContract().getCatContract().getMinTerm() > l) {
            throw new CalculatePremiumException("minimum_term_failed", riskService.getMinSum(cp.getContract()), riskService.getMaxSum(cp.getContract()), riskService.getMinTerm(cp.getContract()));
        }

        if (ccr.getMaxSum() != null) {
            if (cp.getInsuredSum().compareTo(ccr.getMaxSum()) == 1)
                throw new CalculatePremiumException("maximum_sum_failed", riskService.getMinSum(cp.getContract()), riskService.getMaxSum(cp.getContract()), riskService.getMinTerm(cp.getContract()));
        }

        if (ccr.getMinSum() != null) {
            if (cp.getInsuredSum().compareTo(ccr.getMinSum()) == -1)
                throw new CalculatePremiumException("minimum_sum_failed", riskService.getMinSum(cp.getContract()), riskService.getMaxSum(cp.getContract()), riskService.getMinTerm(cp.getContract()));
        }
        cp.setPremium(
                cp.getInsuredSum()
                        .multiply(monthTarif)
                        .multiply(BigDecimal.valueOf(l)).setScale(0, RoundingMode.HALF_UP)
                        .multiply(q)
                        .multiply(
                                getCorrellation(
                                        cp.getRisk(),
                                        cp.getContract().getPartner(),
                                        cp.getContract().getCatContract()
                                )
                        )

        );

        ContractPremium p = savePremium(cp);

        /*p.setPremium(
                p.getInsuredSum().multiply(rate)
        );


        session.saveOrUpdate(p);*/

        return p;
    }

    @Override
    public boolean deletePremium(Long id) throws NoEntityException {
        /*Session session = entityManager.unwrap(Session.class);
        assert (id != 0l);
        //TODO переписать. Не очень понятно, зачем блокировка.
        LockOptions lo = new LockOptions();
        lo.setLockMode(LockMode.PESSIMISTIC_WRITE);

        ContractPremium cp = getPremiumById(id);

        session.buildLockRequest(lo).lock(cp);

        cp.setDeleted(true);

        session.update(cp);

        session.flush();
        session.refresh(cp);


        return true;*/
        ContractPremium cp = entityManager.find(ContractPremium.class, id);
        if (cp == null) throw new NoEntityException("no contractPremium entity with id=" + id);
        cp.setDeleted(true);
        entityManager.merge(cp);
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public BigDecimal calculatePremiumWOSaving(ContractPremium cp, Integer l) throws CalculatePremiumException {
        return calculatePremiumWOSaving(cp, l, BigDecimal.ONE);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public BigDecimal calculatePremiumWOSaving(ContractPremium cp, Integer l, BigDecimal q) throws CalculatePremiumException {



        assert (cp.getContract() != null);
        assert (cp.getRisk() != null);
        assert (cp.getInsuredSum() != null);
        assert (cp.getContract().getCatContract() != null);


        //BigDecimal rate = riskService.getRateForRisk(cp.getRisk(), cp.getContract().getCatContract());
        CatContractRisk ccr = riskService.getRisk(cp.getRisk(), cp.getContract().getCatContract(), cp.getContract().getPartner());
        // Тариф для данного риска был удален
        if(ccr == null) return BigDecimal.ZERO;

        BigDecimal rate = ccr.getRate();
        BigDecimal monthTarif = ccr.getMonthTarif().divide(BigDecimal.valueOf(100));

        assert (rate != null);
        assert (l != null);


        cp.setInsuredSum(
                cp.getInsuredSum()
                        .multiply(rate));

        assert (ccr.getMaxSum() != null);
        assert (ccr.getMinSum() != null);

        if (cp.getContract().getCatContract().getMinTerm() != null && cp.getContract().getCatContract().getMinTerm() > l) {
            throw new CalculatePremiumException("minimum_term_failed", riskService.getMinSum(cp.getContract()), riskService.getMaxSum(cp.getContract()), riskService.getMinTerm(cp.getContract()));
        }

        if (ccr.getMaxSum() != null) {
            if (cp.getInsuredSum().compareTo(ccr.getMaxSum()) == 1)
                throw new CalculatePremiumException("maximum_sum_failed", riskService.getMinSum(cp.getContract()), riskService.getMaxSum(cp.getContract()), riskService.getMinTerm(cp.getContract()));
        }

        if (ccr.getMinSum() != null) {
            if (cp.getInsuredSum().compareTo(ccr.getMinSum()) == -1)
                throw new CalculatePremiumException("minimum_sum_failed", riskService.getMinSum(cp.getContract()), riskService.getMaxSum(cp.getContract()), riskService.getMinTerm(cp.getContract()));
        }

        return cp.getInsuredSum()
                .multiply(monthTarif)
                .multiply(BigDecimal.valueOf(l))
                .multiply(q)
                .multiply(
                    getCorrellation(
                        cp.getRisk(),
                        cp.getContract().getPartner(),
                        cp.getContract().getCatContract())
                    )
                .setScale(0, RoundingMode.HALF_UP);


    }

    @Override
    public BigDecimal getCorrellation(Risk risk, Partner partner, CatContract catContract) {
        BigDecimal c = BigDecimal.ONE;

        if(partner != null && partner.getCorrelation() != null)
            c = c.multiply(partner.getCorrelation());

        PartnerProductMoneyProperty partnerProductMoneyPropertyCorrelation =
                (PartnerProductMoneyProperty)
                        partnerService
                                .getPartnerProductMoneyPropertyByPropertyName(
                                        partner,
                                        ProductMoneyProperty.PropertyType.correlation,
                                        catContract
                                );

        if(
                partnerProductMoneyPropertyCorrelation != null &&
                        partnerProductMoneyPropertyCorrelation.getMoneyValue() != null
                )
            c = c.multiply(
                    partnerProductMoneyPropertyCorrelation.getMoneyValue()
            );

        PartnerRiskCorrelation partnerRiskCorrelation = partnerService.getPartnerRiskCorrelationByPartnerAndRisk(
                partner,
                risk,
                catContract
        );

        if(
                partnerRiskCorrelation != null &&
                        partnerRiskCorrelation.getCorrelation() != null

                )
            c = c.multiply(partnerRiskCorrelation.getCorrelation());

        return c;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void premiumCorrection(Long premiumId, Contract contract) throws CalculatePremiumException, NoEntityException {
        premiumCorrection(premiumId, contract, BigDecimal.ONE);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void premiumCorrection(Long premiumId, Contract contract, BigDecimal q) throws CalculatePremiumException, NoEntityException {
        ContractPremium cpExists = getPremiumById(premiumId);
        BigDecimal properPremium;
        if (cpExists.getInsuranceObject() == null) {
            properPremium = calculatePremiumWOSaving(
                    new ContractPremium(cpExists.getRisk(), contract, contract.getSum()),
                    contract.getLength(), q
            );
        } else {
            properPremium = calculatePremiumWOSaving(
                    new ContractPremium(cpExists.getRisk(), contract, cpExists.getInsuranceObject().getSum()),
                    contract.getLength(), q
            );
        }

        // Должна ли премия по данному риску быть пересчитана?
        if (!cpExists.getPremium().equals(properPremium)) {

            // Удаляем премию
            entityManager.remove(cpExists);


            ContractPremium cp;
            // Рассчитываем
            if(cpExists.getInsuranceObject()!= null){
                cp = new ContractPremium(cpExists.getRisk(), contract, cpExists.getInsuranceObject().getSum());
                cp.setInsuranceObject(cpExists.getInsuranceObject());
            }
            else{
                cp = new ContractPremium(cpExists.getRisk(), contract, contract.getSum());
            }
            cp.setValidated(true);
            calculatePremium(cp, contract.getLength(), q);
        }
    }

    @Override
    public BigDecimal getAllInsuredSumForContractPremiums(Contract contract) {
        BigDecimal sum = BigDecimal.ZERO;

        List<ContractPremium> contractPremiums = getValidatedCPremiums(contract);

        if(contractPremiums == null) return sum;

        for(ContractPremium contractPremium : contractPremiums){
            if(contractPremium.isValidated() && !contractPremium.isDeleted()){
                sum = sum.add(contractPremium.getInsuredSum());
            }
        }

        return sum;
    }


}
