package com.callv2.drive.infrastructure.storage;

import java.io.InputStream;

public interface StorageService {

    String store(String name, InputStream content);

    void delete(String name);

}
