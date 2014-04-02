package com.tajinsurance.service;

import com.tajinsurance.domain.Partner;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by berz on 24.03.14.
 */
@Service
public interface PartnerService {
    List<Partner> getPartners();

    Partner getPartnerById(Long id);
}
