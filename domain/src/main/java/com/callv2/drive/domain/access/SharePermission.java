package com.callv2.drive.domain.access;

public enum SharePermission implements Permission<SharePermission> {
    SHARE_TO_SHARE(0, AccessPermission.WRITE),
    SHARE_WRITE(1, AccessPermission.WRITE),
    SHARE_READ(2, AccessPermission.READ);

    private final Integer level;
    private final AccessPermission shareAccessPermissionsAllowed;

    private SharePermission(
            final Integer level,
            final AccessPermission shareAccessPermissionsAllowed) {
        this.level = level;
        this.shareAccessPermissionsAllowed = shareAccessPermissionsAllowed;
    }

    public Integer getLevel() {
        return level;
    }

    public Boolean allows(final SharePermission permission) {
        return this.level <= permission.level;
    }

    public Boolean canShare(final AccessPermission accessPermissionToBeShared) {
        return this.shareAccessPermissionsAllowed.allows(accessPermissionToBeShared);
    }

    public Boolean canShare(final SharePermission sharePermissionToBeShared) {
        return allows(sharePermissionToBeShared);
    }

    public static SharePermission mostPrivileged() {
        return SHARE_TO_SHARE;
    }

}
