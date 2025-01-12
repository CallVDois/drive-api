package com.callv2.drive.infrastructure.storage;

import java.io.InputStream;
import java.io.OutputStream;

public interface StorageService {

    void store(String name, InputStream content);

    InputStream load(final String name);

    int writeTo(final InputStream inputStream, final OutputStream outputStream);

    void delete(String name);

}
