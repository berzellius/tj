package com.tajinsurance.domain;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by berz on 26.03.14.
 */
@Entity
@Table(name = "user_roles")
public class UserRole implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_roles_id_generator")
    @SequenceGenerator(name = "user_roles_id_generator", sequenceName = "user_roles_id_seq")
    @NotNull
    @Column(updatable = false, columnDefinition = "bigint")
    private Long id;

    private String authority;

    public UserRole(){}

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
