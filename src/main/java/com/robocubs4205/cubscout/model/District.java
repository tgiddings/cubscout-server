package com.robocubs4205.cubscout.model;

import org.springframework.hateoas.Identifiable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Created by trevor on 2/14/17.
 */
@Entity
public class District implements Identifiable<String> {
    @Id
    private String code;

    private String name;

    @OneToMany(mappedBy = "district")
    private Set<Event> events;

    public District(){}

    public District(String code){
        setCode(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return getCode();
    }

    public Set<Event> getEvents() {
        return events;
    }
}
