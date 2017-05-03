package com.robocubs4205.cubscout.model;

import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DistrictRepository {
    District find(String code);
    District save(District district);
    void delete(District district);
    Set<District> findAll();
}
