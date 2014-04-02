package com.tajinsurance.service;

import com.tajinsurance.domain.Contract;
import com.tajinsurance.domain.ContractPremium;
import com.tajinsurance.dto.ContractPremiumAjax;
import com.tajinsurance.exceptions.NoEntityException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by berz on 30.03.14.
 */
@Service
public interface PremiumService {

    ContractPremium savePremium(ContractPremium cp);

    ContractPremium getPremiumById(Long id) throws NoEntityException;

    List<ContractPremiumAjax> getValidatedPremiums(Contract contract);

    List<ContractPremiumAjax> getNotValidatedPremiums(Contract contract);

    ContractPremium calculatePremium(ContractPremium cp);

    boolean deletePremium(Long id) throws NoEntityException;

}
