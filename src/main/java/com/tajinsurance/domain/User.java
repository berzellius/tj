package com.tajinsurance.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * Created by berz on 26.03.14.
 */
@Entity
@Table(name="users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_seq")
    @NotNull
    @Column(updatable = false, columnDefinition = "bigint")
    private Long id;


    private String username;

    private String password;

    private boolean locked;

    private boolean expired;

    @Column(name = "credentials_expired")
    private boolean credentialsExpired;

    @OneToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @ManyToMany
    @JoinTable(
            name = "users_authorities",
            joinColumns = {
                    @JoinColumn(name = "user_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "auth_id")
            }
    )
    private Collection<UserRole> authorities;

    public User() {
    }

    public User(boolean allEnabled){
        if(!allEnabled) return;
        setExpired(false);
        setCredentialsExpired(false);
        setLocked(false);
        setEnabled(true);
    }


    @Override
    public Collection<UserRole> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.expired;
    }

    public void setExpired(boolean e){
        this.expired = e;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    public void setLocked(boolean l){
        this.locked = l;
    }

    public boolean enabled;

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    public void setCredentialsExpired(boolean ce){
        this.credentialsExpired = ce;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean e){
        this.enabled = e;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }
}
