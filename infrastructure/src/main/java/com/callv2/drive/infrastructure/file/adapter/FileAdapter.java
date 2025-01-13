package com.callv2.drive.infrastructure.file.adapter;

import org.springframework.web.multipart.MultipartFile;

import com.callv2.drive.application.file.create.CreateFileInput;
import com.callv2.drive.domain.exception.InternalErrorException;

public interface FileAdapter {

    static CreateFileInput adaptCreateFileInput(final MultipartFile aFile) {
        try {
            return CreateFileInput.of(
                    aFile.getOriginalFilename(),
                    aFile.getContentType(),
                    aFile.getInputStream(),
                    aFile.getSize());
        } catch (Exception e) {
            throw InternalErrorException.with("An Error ocurred on adapt MultipartFile to CreateFileInput", e);
        }
    }

}
