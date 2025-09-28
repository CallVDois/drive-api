package com.callv2.drive.domain.access;

public enum SharePermission {
    SHARE_TO_SHARE(0),
    SHARE_WRITE(1),
    SHARE_READ(2);

    private final Integer level;

    private SharePermission(int level) {
        this.level = level;
    }

    public Integer getLevel() {
        return level;
    }

    public static SharePermission mostPrivileged() {
        return SHARE_TO_SHARE;
    }

}
