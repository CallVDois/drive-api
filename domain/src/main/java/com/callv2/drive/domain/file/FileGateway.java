package com.callv2.drive.domain.file;

import java.util.Optional;

public interface FileGateway {

    File create(File file);

    Optional<File> findById(FileID id);

}
