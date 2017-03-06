package com.robocubs4205.cubscout.rest.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

/**
 * Created by trevor on 2/18/17.
 */
public class RobotResource extends ResourceSupport{
    private long id;
    public RobotResource(){}

    @JsonProperty("id")
    public long getRobotId() {
        return id;
    }

    public void setRobotId(long id) {
        this.id = id;
    }
}
