package com.tajinsurance.service;

import com.tajinsurance.domain.CatContract;
import com.tajinsurance.domain.Risk;
import com.tajinsurance.exceptions.NoEntityException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by berz on 30.03.14.
 */
@Service
public interface RiskService {
    Risk getRiskById(Long id) throws NoEntityException;

    BigDecimal getRateForRisk(Risk risk, CatContract cc);
}
