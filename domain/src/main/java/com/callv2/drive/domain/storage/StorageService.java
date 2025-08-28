package com.callv2.drive.domain.storage;

import java.io.InputStream;

public interface StorageService {

    void store(String key, InputStream content);

    void delete(String key);

    InputStream retrieve(String key);

}
