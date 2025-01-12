package com.callv2.drive.infrastructure.storage;

import com.callv2.drive.domain.file.Content;

public interface StorageService {

    void store(String name, Content content);

    Content load(final String name);

    void delete(String name);

}
