package com.callv2.drive.domain.acl;

public interface Permission<T extends Enum<T> & Permission<T>> {

    Type type();

    String name();

    Integer getLevel();

    Boolean allows(T permission);

    public enum Type {
        ACCESS,
    }

}
