package com.callv2.drive.infrastructure.storage;

public interface StorageService {

    void store(String name, byte[] content);

    byte[] load(String name);

    void delete(String name);

}
