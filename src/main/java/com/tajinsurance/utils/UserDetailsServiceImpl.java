package com.tajinsurance.utils;

import com.tajinsurance.domain.User;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by berz on 26.03.14.
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    EntityManager entityManager;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Session session = entityManager.unwrap(Session.class);
        List<User> ul = session.createCriteria(User.class).add(Restrictions.eq("username",s)).list();
        if(ul.size() == 1) return ul.get(0);
        else if(ul.size() == 0){
            throw new UsernameNotFoundException("User " + s + " not found!");
        }
        return null;
    }
}
