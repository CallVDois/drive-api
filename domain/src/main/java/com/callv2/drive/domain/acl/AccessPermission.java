package com.callv2.drive.domain.acl;

public enum AccessPermission implements Permission<AccessPermission> {
    MANAGE(0),
    WRITE(1),
    READ(2);

    private static final Permission.Type TYPE = Permission.Type.ACCESS;

    private final Integer level;

    AccessPermission(int level) {
        this.level = level;
    }

    @Override
    public Permission.Type type() {
        return TYPE;
    }

    @Override
    public Integer getLevel() {
        return level;
    }

    public Boolean canShare() {
        return this.level <= MANAGE.level;
    }

    public Boolean canWrite() {
        return this.level <= WRITE.level;
    }

    public Boolean canRead() {
        return this.level <= READ.level;
    }

    public Boolean fitsWithin(final AccessPermission maximum) {
        return this.level >= maximum.level;
    }

    public static AccessPermission mostPrivileged() {
        return MANAGE;
    }

    public Boolean allows(final AccessPermission permission) {
        return this.level <= permission.level;
    }

}
