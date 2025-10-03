package com.callv2.drive.domain.access;

public interface Permission<T extends Enum<T> & Permission<T>> {

    String name();

    Integer getLevel();

    Boolean allows(T permission);

}
