package com.tajinsurance.service;

import com.tajinsurance.domain.Partner;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by berz on 24.03.14.
 */
@Service
@Transactional
public class PartnerServiceImpl implements PartnerService {
    @Autowired
    EntityManager entityManager;

    @Override
    public List<Partner> getPartners() {
        Session session = entityManager.unwrap(Session.class);
        List<Partner> pts = session.createCriteria(Partner.class).list();
        return pts;
    }

    @Override
    public Partner getPartnerById(Long id){
        Session session = entityManager.unwrap(Session.class);
        Partner p = (Partner) session.get(Partner.class, id);
        return p;
    }
}
