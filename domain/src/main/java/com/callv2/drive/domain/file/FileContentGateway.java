package com.callv2.drive.domain.file;

import java.io.InputStream;

public interface FileContentGateway {

    void store(File file, InputStream inputStream);

    InputStream load(final File file);

}
