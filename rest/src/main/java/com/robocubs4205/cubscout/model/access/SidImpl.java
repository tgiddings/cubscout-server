package com.robocubs4205.cubscout.model.access;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.security.acls.model.Sid;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SidImpl implements Sid{
    @Id
    private long id;
    @Override
    public boolean equals(Object o){
        if(o==null||!(o instanceof SidImpl)) return false;
        SidImpl sid = (SidImpl)o;
        return new EqualsBuilder().append(id,sid.id).build();
    }
    @Override
    public int hashCode(){
        return new HashCodeBuilder().append(id).build();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
