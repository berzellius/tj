package com.tajinsurance.service;

import com.tajinsurance.domain.CatContract;
import com.tajinsurance.domain.User;
import com.tajinsurance.dto.RiskAjax;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by berz on 20.03.14.
 */
@Service
public interface CatContractService {
    List<CatContract> getAllCatContracts(String locale);

    CatContract getCatContractById(Long id);

    List<CatContract> getAllowedCatContractsForUser(User u, String locale);

    List<RiskAjax> getAllowedRisksForCatContract(CatContract cc);


}
