package com.callv2.drive.domain.access;

public enum AccessPermission {
    WRITE(0),
    READ(1);

    private final Integer level;

    AccessPermission(int level) {
        this.level = level;
    }

    public Integer getLevel() {
        return level;
    }

}
