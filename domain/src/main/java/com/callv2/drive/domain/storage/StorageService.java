package com.callv2.drive.domain.storage;

import java.io.InputStream;

public interface StorageService {

    String store(String name, InputStream content);

    void delete(String name);

}
