package com.callv2.drive.infrastructure.file.adapter;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.callv2.drive.application.file.create.CreateFileInput;
import com.callv2.drive.domain.exception.InternalErrorException;

public interface FileAdapter {

    static CreateFileInput adapt(UUID folderId, final MultipartFile aFile) {
        try {
            return CreateFileInput.of(
                    folderId,
                    aFile.getOriginalFilename(),
                    aFile.getContentType(),
                    aFile.getInputStream(),
                    aFile.getSize());
        } catch (Exception e) {
            throw InternalErrorException.with("An Error ocurred on adapt MultipartFile to CreateFileInput", e);
        }
    }

}
