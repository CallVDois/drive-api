package com.callv2.drive.domain.file;

public interface FileContentGateway {

    void store(File file, Content content);

    Content load(final File file);

}
