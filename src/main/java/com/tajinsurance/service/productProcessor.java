package com.tajinsurance.service;

import com.tajinsurance.domain.Contract;
import com.tajinsurance.domain.ContractImage;
import com.tajinsurance.domain.InsuranceArea;
import com.tajinsurance.exceptions.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by berz on 15.09.14.
 */
@Service
public interface ProductProcessor {

    public ProductProcessor getProductProcessorImplementation(Contract contract);

    public void updateContract(Contract contract, Boolean managerMode) throws NoEntityException, CalculatePremiumException, NoPersonToContractException, BadContractDataException, NoRelatedContractNumber, IOException, IllegalDataException;

    public void updateContract(Contract contract, MultipartFile file, ContractImage contractImage, Boolean managerMode) throws NoEntityException, CalculatePremiumException, NoPersonToContractException, BadContractDataException, NoRelatedContractNumber, IOException, IllegalDataException;

    public BigDecimal getIntegralInsuredSumForContract(Contract contract);

    BigDecimal getAllPremiumForContract(Contract contract) throws CalculatePremiumException;

    BigDecimal getFullCost(Contract contract);

    BigDecimal getAllPremiumForContractWithoutSaving(Contract contract, List<InsuranceArea> insuranceAreas) throws CalculatePremiumException;

    BigDecimal getIntegralInsuredSumForContractWithoutSaving(Contract contract, List<InsuranceArea> insuranceAreas);

    Integer getMaxAgeForMale();

    Integer getMaxAgeForFemale();
}
