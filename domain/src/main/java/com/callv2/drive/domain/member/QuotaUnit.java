package com.callv2.drive.domain.member;

public enum QuotaUnit {
    BYTE(1),
    KILOBYTE(1024),
    MEGABYTE(1024 * 1024),
    GIGABYTE(1024 * 1024 * 1024),
    TERABYTE(1024L * 1024L * 1024L * 1024L);

    private final long multiplier;

    private QuotaUnit(long multiplier) {
        this.multiplier = multiplier;
    }

    public long toBytes(long size) {
        return size * multiplier;
    }

}
