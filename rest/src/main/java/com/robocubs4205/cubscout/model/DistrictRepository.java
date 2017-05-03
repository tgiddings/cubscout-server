package com.robocubs4205.cubscout.model;

import java.util.Set;

public interface DistrictRepository {
    District find(String code);
    District save(District district);
    void delete(District district);
    Set<District> findAll();
}
