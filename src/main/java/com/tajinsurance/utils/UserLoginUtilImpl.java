package com.tajinsurance.utils;

import com.tajinsurance.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by berz on 27.03.14.
 */
public class UserLoginUtilImpl implements UserLoginUtil {
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    public User getCurrentLogInUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        if(name == null) return null;

        return (User) userDetailsService.loadUserByUsername(name);
    }
}
