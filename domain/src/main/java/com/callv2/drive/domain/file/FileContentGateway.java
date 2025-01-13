package com.callv2.drive.domain.file;

import java.io.InputStream;

public interface FileContentGateway {

    String store(String contentName, InputStream inputStream);

    void delete(String contentLocation);

}
