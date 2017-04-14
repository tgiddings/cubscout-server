package com.robocubs4205.cubscout.model.access;

import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class AceImpl implements AccessControlEntry{
    @ManyToOne(optional = false)
    Acl acl;
    @ManyToOne(optional = false)
    SidImpl sid;
    @Id
    @GeneratedValue
    private long id;
    private boolean isGranting;
    @Embedded
    private PermissionImpl permission;

    @Override
    public Acl getAcl() {
        return acl;
    }

    public void setAcl(Acl acl) {
        this.acl = acl;
    }

    @Override
    public Serializable getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public Permission getPermission() {
        return permission;
    }

    public void setPermission(PermissionImpl permission) {
        this.permission = permission;
    }

    @Override
    public Sid getSid() {
        return sid;
    }

    public void setSid(SidImpl sid) {
        this.sid = sid;
    }

    @Override
    public boolean isGranting() {
        return isGranting;
    }

    public void setGranting(boolean granting) {
        isGranting = granting;
    }
}
