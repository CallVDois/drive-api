package com.callv2.drive.infrastructure.file.adapter;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.callv2.drive.application.file.content.delete.DeleteFileContentInput;
import com.callv2.drive.application.file.create.CreateFileInput;
import com.callv2.drive.application.file.sharing.create.CreateFileSharingInput;
import com.callv2.drive.domain.exception.InternalErrorException;
import com.callv2.drive.infrastructure.file.model.DeleteFileContentMessage;
import com.callv2.drive.infrastructure.file.model.GrantFilePermissionRequest;

public interface FileAdapter {

    static CreateFileInput adapt(UUID ownerId, UUID folderId, final MultipartFile aFile) {
        try {
            return CreateFileInput.of(
                    ownerId,
                    folderId,
                    aFile.getOriginalFilename(),
                    aFile.getContentType(),
                    aFile.getInputStream(),
                    aFile.getSize());
        } catch (Exception e) {
            throw InternalErrorException.with("An Error ocurred on adapt MultipartFile to CreateFileInput", e);
        }
    }

    static DeleteFileContentInput adapt(DeleteFileContentMessage message) {
        return DeleteFileContentInput.of(message.data().fileId(), message.data().deleterId());
    }

    static CreateFileSharingInput adapt(
            UUID fileId,
            UUID granterId,
            GrantFilePermissionRequest request) {
        return CreateFileSharingInput.with(
                fileId,
                granterId,
                request.grantee(),
                request.accessPermission(),
                request.sharePermission());
    }

}
