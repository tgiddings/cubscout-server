package com.robocubs4205.cubscout.model.access;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GrantedAuthorityImpl implements GrantedAuthority{
    @Id
    private long id;

    private String authority;

    public GrantedAuthorityImpl(){}

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
