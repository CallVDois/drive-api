package com.callv2.drive.application.file.create;

import java.util.List;
import java.util.Objects;

import com.callv2.drive.domain.access.AccessPermission;
import com.callv2.drive.domain.access.Acl;
import com.callv2.drive.domain.access.AclGateway;
import com.callv2.drive.domain.access.Resource;
import com.callv2.drive.domain.exception.InternalErrorException;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.exception.QuotaExceededException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.file.Content;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileName;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.storage.StorageGateway;
import com.callv2.drive.domain.storage.StorageKeyGenerator;
import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.validation.handler.Notification;

public class DefaultCreateFileUseCase extends CreateFileUseCase {

    private final MemberGateway memberGateway;
    private final FolderGateway folderGateway;
    private final FileGateway fileGateway;
    private final StorageKeyGenerator storageKeyGenerator;
    private final StorageGateway storageGateway;
    private final AclGateway aclGateway;

    public DefaultCreateFileUseCase(
            final MemberGateway memberGateway,
            final FolderGateway folderGateway,
            final FileGateway fileGateway,
            final StorageKeyGenerator storageKeyGenerator,
            final StorageGateway storageGateway,
            final AclGateway aclGateway) {
        this.memberGateway = Objects.requireNonNull(memberGateway);
        this.folderGateway = Objects.requireNonNull(folderGateway);
        this.fileGateway = Objects.requireNonNull(fileGateway);
        this.storageKeyGenerator = Objects.requireNonNull(storageKeyGenerator);
        this.storageGateway = Objects.requireNonNull(storageGateway);
        this.aclGateway = Objects.requireNonNull(aclGateway);
    }

    @Override
    public CreateFileOutput execute(final CreateFileInput input) {

        final MemberID creatorId = memberGateway
                .findById(MemberID.of(input.ownerId()))
                .map(Member::getId)
                .orElseThrow(() -> NotFoundException.with(Member.class, input.ownerId().toString()));

        final FolderID folderId = FolderID.of(input.folderId());
        final Folder folder = folderGateway
                .findById(folderId)
                .orElseThrow(() -> NotFoundException.with(Folder.class, input.folderId().toString()));

        final Acl folderAcl = this.aclGateway
                .findByResource(Resource.folder(folderId))
                .orElseThrow();// TODO throw some exception

        checkAccessPermission(creatorId, folderAcl);

        final Member owner = memberGateway
                .findById(folder.getOwner())
                .orElseThrow(() -> NotFoundException.with(Member.class, folder.getOwner().toString()));

        checkQuota(owner, input.size());

        final Notification notification = Notification.create();

        final FileName fileName = FileName.of(input.name());
        fileName.validate(notification);
        if (notification.hasError())
            throw ValidationException.with("Could not create Aggregate File", notification);

        final List<File> filesOnSameFolder = fileGateway.findByFolder(folderId);
        if (filesOnSameFolder.stream().map(File::getName).anyMatch(fileName::equals))
            throw ValidationException.with("Could not create Aggregate File",
                    ValidationError.with("File with same name already exists on this folder"));

        final Content content = storeContentFile(input);

        final File file = notification
                .validate(() -> File.create(creatorId, folder.getOwner(), folderId, fileName, content));

        if (notification.hasError())
            throw ValidationException.with("Could not create Aggregate File", notification);

        final Acl fileInheritedAcl = folderAcl.createInherited(Resource.file(file.getId()));

        aclGateway.create(fileInheritedAcl);

        storeFile(file);

        return CreateFileOutput.from(file);
    }

    private void checkAccessPermission(final MemberID memberId, final Acl folderAcl) {

        final AccessPermission folderAclPermission = folderAcl
                .effectiveAccessPermission(memberId)
                .orElseThrow();// TODO throw some exception

        if (!folderAclPermission.canWrite())
            throw new RuntimeException("You don't have permission to create files in this folder");// TODO domain

    }

    private void checkQuota(final Member owner, final Long newFileSize) {

        final Long actualUsedQuota = fileGateway
                .findByOwner(owner.getId())
                .stream()
                .map(File::getContent)
                .mapToLong(Content::size)
                .sum();

        if (actualUsedQuota + newFileSize > owner.getQuota().sizeInBytes())
            throw QuotaExceededException.with(owner.getQuota());

    }

    private void storeFile(final File file) {
        try {
            fileGateway.create(file);
        } catch (Exception e) {
            deleteContentFile(file.getContent().storageKey());
            throw InternalErrorException.with("Could not store File", e);
        }
    }

    private Content storeContentFile(final CreateFileInput input) {

        try {

            final String storageKey = storageKeyGenerator.generate();
            final String contentType = input.contentType();
            final Long contentSize = input.size();

            storageGateway.store(storageKey, input.content());

            return Content.of(storageKey, contentType, contentSize);

        } catch (Exception e) {
            throw InternalErrorException.with("Could not store BinaryContent", e);
        }

    }

    private void deleteContentFile(final String contentLocation) {
        try {
            storageGateway.delete(contentLocation);
        } catch (Exception e) {
            throw InternalErrorException.with("Could not delete BinaryContent", e);
        }
    }

}
