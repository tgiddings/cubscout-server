package com.robocubs4205.cubscout.model.access;

import org.springframework.security.acls.model.ObjectIdentity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class ObjectIdentityImpl implements ObjectIdentity{
    @Id
    @GeneratedValue
    private long id;

    private String type;

    @Override
    public Serializable getIdentifier() {
        return getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
