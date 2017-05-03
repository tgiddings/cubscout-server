package com.robocubs4205.cubscout.model.access;

import org.springframework.security.acls.domain.AclFormattingUtils;
import org.springframework.security.acls.model.Permission;

public class PermissionImpl implements Permission{
    private int mask;

    @Override
    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    @Override
    public String getPattern() {
        return AclFormattingUtils.printBinary(mask,'x');
    }
}
