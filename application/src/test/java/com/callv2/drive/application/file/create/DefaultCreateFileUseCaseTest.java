package com.callv2.drive.application.file.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.callv2.drive.domain.access.AccessPermission;
import com.callv2.drive.domain.access.Acl;
import com.callv2.drive.domain.access.AclGateway;
import com.callv2.drive.domain.access.AclID;
import com.callv2.drive.domain.access.Entry;
import com.callv2.drive.domain.access.Resource;
import com.callv2.drive.domain.event.EventDispatcher;
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
import com.callv2.drive.domain.member.Nickname;
import com.callv2.drive.domain.member.Quota;
import com.callv2.drive.domain.member.QuotaUnit;
import com.callv2.drive.domain.member.Username;
import com.callv2.drive.domain.storage.StorageGateway;
import com.callv2.drive.domain.storage.StorageKeyGenerator;

@ExtendWith(MockitoExtension.class)
class DefaultCreateFileUseCaseTest {

    @InjectMocks
    DefaultCreateFileUseCase useCase;

    @Mock
    EventDispatcher eventDispatcher;

    @Mock
    MemberGateway memberGateway;

    @Mock
    FolderGateway folderGateway;

    @Mock
    StorageKeyGenerator storageKeyGenerator;

    @Mock
    StorageGateway storageService;

    @Mock
    FileGateway fileGateway;

    @Mock
    AclGateway aclGateway;

    @Test
    void givenAValidParams_whenCallsExecute_thenShouldCreateFile() {

        final var owner = Member.with(
                MemberID.of(UUID.randomUUID()),
                Username.of("username"),
                Nickname.of("nickname"),
                Quota.of(0, QuotaUnit.BYTE),
                null,
                true,
                Instant.now(),
                Instant.now(),
                0L)
                .requestQuota(Quota.of(1, QuotaUnit.GIGABYTE))
                .approveQuotaRequest();

        final var ownerId = owner.getId();

        final var folder = Folder.createRoot(ownerId);
        final var expectedFolderId = folder.getId();

        final var expectedFileName = FileName.of("file");
        final var expectedContentType = "image/jpeg";
        final var contentBytes = "content".getBytes();

        final var expectedContent = new ByteArrayInputStream(contentBytes);
        final var expectedContentSize = (long) contentBytes.length;

        final var expectedAclId = AclID.unique();
        final var expectedAclResource = Resource.folder(folder);
        final var expectedAclDirectEntries = Set.<Entry<?>>of(Entry.create(ownerId, AccessPermission.mostPrivileged()));
        final var expectedAclInheritedEntries = Set.<Entry<?>>of();
        final var expectedAclCreatedAt = Instant.now();
        final var expectedAclUpdatedAt = expectedAclCreatedAt;

        final var expectedStorageKey = UUID.randomUUID().toString();

        when(memberGateway.findById(any()))
                .thenReturn(Optional.of(owner));

        when(fileGateway.findAllByFolder(any()))
                .thenReturn(List.of());

        when(folderGateway.findByIdWithMemberAccess(any(), any()))
                .thenReturn(Optional.of(folder));

        when(aclGateway.findByResource(any()))
                .thenReturn(Optional.of(Acl.with(
                        expectedAclId,
                        expectedAclResource,
                        expectedAclDirectEntries,
                        expectedAclInheritedEntries,
                        expectedAclCreatedAt,
                        expectedAclUpdatedAt,
                        new LinkedList<>())));

        when(storageKeyGenerator.generate())
                .thenReturn(expectedStorageKey);

        doNothing()
                .when(storageService).store(any(), any());

        when(fileGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var input = CreateFileInput.of(
                ownerId.getValue(),
                expectedFolderId.getValue(),
                expectedFileName.value(),
                expectedContentType,
                expectedContent,
                expectedContentSize);

        final var actualOuptut = useCase.execute(input);

        assertNotNull(actualOuptut.id());

        verify(folderGateway, times(1)).findByIdWithMemberAccess(any(), any());
        verify(folderGateway, times(1)).findByIdWithMemberAccess(eq(expectedFolderId), eq(ownerId));
        verify(storageService, times(1)).store(any(), any());
        verify(storageService, times(1)).store(any(), eq(expectedContent));
        verify(storageService, times(0)).delete(any());
        verify(fileGateway, times(1)).findAllByFolder(any());
        verify(fileGateway, times(1)).findAllByFolder(eq(folder.getId()));
        verify(fileGateway, times(1)).create(any());
        verify(fileGateway, times(1)).create(argThat(file -> {

            assertEquals(actualOuptut.id().getValue(), file.getId().getValue());
            assertEquals(expectedFileName, file.getName());
            assertEquals(expectedContentType, file.getContent().type());
            assertNotNull(file.getCreatedAt());
            assertNotNull(file.getUpdatedAt());
            assertNotNull(file.getContent().storageKey());
            assertEquals(file.getCreatedAt(), file.getUpdatedAt());

            return true;
        }));

    }

    @Test
    void givenAnInvalidFolderId_whenCallsExecute_thenShouldThrowNotFoundException() {

        final var owner = Member.with(
                MemberID.of(UUID.randomUUID()),
                Username.of("username"),
                Nickname.of("nickname"),
                Quota.of(0, QuotaUnit.BYTE),
                null,
                true,
                Instant.now(),
                Instant.now(),
                0L)
                .requestQuota(Quota.of(1, QuotaUnit.GIGABYTE))
                .approveQuotaRequest();

        final var ownerId = owner.getId();

        final var folder = Folder.createRoot(ownerId);

        final var expectedFolderId = folder.getId();

        final var expectedFileName = FileName.of("file");
        final var expectedContentType = "image/jpeg";
        final var contentBytes = "content".getBytes();

        final var expectedContent = new ByteArrayInputStream(contentBytes);
        final var expectedContentSize = (long) contentBytes.length;

        final var expectedExceptionMessage = "[Folder] not found.";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "[Folder] with id [%s] not found."
                .formatted(expectedFolderId.getValue());

        when(memberGateway.findById(any()))
                .thenReturn(Optional.of(owner));

        when(folderGateway.findByIdWithMemberAccess(any(), any()))
                .thenReturn(Optional.empty());

        final var input = CreateFileInput.of(
                ownerId.getValue(),
                expectedFolderId.getValue(),
                expectedFileName.value(),
                expectedContentType,
                expectedContent,
                expectedContentSize);

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(folderGateway, times(1)).findByIdWithMemberAccess(any(), any());
        verify(folderGateway, times(1)).findByIdWithMemberAccess(eq(expectedFolderId), eq(ownerId));
        verify(storageService, times(0)).store(any(), any());
        verify(storageService, times(0)).store(any(), eq(expectedContent));
        verify(storageService, times(0)).delete(any());
        verify(fileGateway, times(0)).findAllByFolder(any());
        verify(fileGateway, times(0)).create(any());

    }

    @Test
    void givenAnInvalidMemberId_whenCallsExecute_thenShouldThrowNotFoundException() {

        final var expectedOwnerId = MemberID.of(UUID.randomUUID());
        final var expectedFolderId = FolderID.unique();

        final var expectedFileName = FileName.of("file");
        final var expectedContentType = "image/jpeg";
        final var contentBytes = "content".getBytes();

        final var expectedContent = new ByteArrayInputStream(contentBytes);
        final var expectedContentSize = (long) contentBytes.length;

        final var expectedExceptionMessage = "[Member] not found.";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "[Member] with id [%s] not found."
                .formatted(expectedOwnerId.getValue());

        when(memberGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var input = CreateFileInput.of(
                expectedOwnerId.getValue(),
                expectedFolderId.getValue(),
                expectedFileName.value(),
                expectedContentType,
                expectedContent,
                expectedContentSize);

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(memberGateway, times(1)).findById(any());
        verify(memberGateway, times(1)).findById(eq(expectedOwnerId));
        verify(folderGateway, times(0)).findByIdWithMemberAccess(any(), any());
        verify(storageService, times(0)).store(any(), any());
        verify(storageService, times(0)).store(any(), eq(expectedContent));
        verify(storageService, times(0)).delete(any());
        verify(fileGateway, times(0)).findAllByFolder(any());
        verify(fileGateway, times(0)).create(any());

    }

    @Test
    void givenAValidParamsWithAlreadyExistingFileNameOnSameFolder_whenCallsExecute_thenShouldThrowValidationException() {

        final var creator = Member.with(
                MemberID.of(UUID.randomUUID()),
                Username.of("creator"),
                Nickname.of("creator"),
                Quota.of(0, QuotaUnit.BYTE),
                null,
                true,
                Instant.now(),
                Instant.now(),
                0L);

        final var owner = Member.with(
                MemberID.of(UUID.randomUUID()),
                Username.of("username"),
                Nickname.of("nickname"),
                Quota.of(0, QuotaUnit.BYTE),
                null,
                true,
                Instant.now(),
                Instant.now(),
                0L)
                .requestQuota(Quota.of(1, QuotaUnit.GIGABYTE))
                .approveQuotaRequest();

        final var creatorId = creator.getId();
        final var ownerId = owner.getId();

        final var folder = Folder.createRoot(ownerId);

        final var expectedFolderId = folder.getId();

        final var expectedFileName = FileName.of("file");
        final var expectedContentType = "image/jpeg";
        final var contentBytes = "content".getBytes();

        final var expectedContent = new ByteArrayInputStream(contentBytes);
        final var expectedContentSize = (long) contentBytes.length;

        final var fileWithSameName = File.create(creatorId, ownerId, folder.getId(), expectedFileName,
                Content.of("location", "text", 10));

        final var expectedExceptionMessage = "Could not create Aggregate File";
        final var expectedErrorMessage = "File with same name already exists on this folder";

        final var expectedAclId = AclID.unique();
        final var expectedAclResource = Resource.folder(folder);
        final var expectedAclDirectEntries = Set.<Entry<?>>of(Entry.create(ownerId, AccessPermission.mostPrivileged()));
        final var expectedAclInheritedEntries = Set.<Entry<?>>of();
        final var expectedAclCreatedAt = Instant.now();
        final var expectedAclUpdatedAt = expectedAclCreatedAt;

        when(memberGateway.findById(ownerId))
                .thenReturn(Optional.of(owner));

        when(fileGateway.findAllByFolder(any()))
                .thenReturn(List.of(fileWithSameName));

        when(folderGateway.findByIdWithMemberAccess(any(), any()))
                .thenReturn(Optional.of(folder));

        when(aclGateway.findByResource(any()))
                .thenReturn(Optional.of(Acl.with(
                        expectedAclId,
                        expectedAclResource,
                        expectedAclDirectEntries,
                        expectedAclInheritedEntries,
                        expectedAclCreatedAt,
                        expectedAclUpdatedAt,
                        new LinkedList<>())));

        final var input = CreateFileInput.of(
                ownerId.getValue(),
                expectedFolderId.getValue(),
                expectedFileName.value(),
                expectedContentType,
                expectedContent,
                expectedContentSize);

        final var actualException = assertThrows(ValidationException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(folderGateway, times(1)).findByIdWithMemberAccess(any(), any());
        verify(folderGateway, times(1)).findByIdWithMemberAccess(eq(expectedFolderId), eq(ownerId));
        verify(storageService, times(0)).store(any(), any());
        verify(storageService, times(0)).delete(any());
        verify(fileGateway, times(1)).findAllByFolder(any());
        verify(fileGateway, times(1)).findAllByFolder(eq(folder.getId()));
        verify(fileGateway, times(0)).create(any());

    }

    @Test
    void givenAValidParams_whenCallsExecuteAndFileGatewayCreateThrowsRandomException_thenShouldThrowInternalErrorException() {

        final var owner = Member.with(
                MemberID.of(UUID.randomUUID()),
                Username.of("username"),
                Nickname.of("nickname"),
                Quota.of(0, QuotaUnit.BYTE),
                null,
                true,
                Instant.now(),
                Instant.now(),
                0L)
                .requestQuota(Quota.of(1, QuotaUnit.GIGABYTE))
                .approveQuotaRequest();

        final var ownerId = owner.getId();

        final var folder = Folder.createRoot(ownerId);
        final var expectedFolderId = folder.getId();

        final var expectedFileName = FileName.of("file");
        final var expectedContentType = "image/jpeg";
        final var contentBytes = "content".getBytes();

        final var expectedContent = new ByteArrayInputStream(contentBytes);
        final var expectedContentSize = (long) contentBytes.length;

        final var expectedExceptionMessage = "Could not store File";

        final var expectedAclId = AclID.unique();
        final var expectedAclResource = Resource.folder(folder);
        final var expectedAclDirectEntries = Set.<Entry<?>>of(Entry.create(ownerId, AccessPermission.mostPrivileged()));
        final var expectedAclInheritedEntries = Set.<Entry<?>>of();
        final var expectedAclCreatedAt = Instant.now();
        final var expectedAclUpdatedAt = expectedAclCreatedAt;

        final var expectedStorageKey = UUID.randomUUID().toString();

        when(memberGateway.findById(ownerId))
                .thenReturn(Optional.of(owner));

        when(fileGateway.findAllByFolder(any()))
                .thenReturn(List.of());

        when(folderGateway.findByIdWithMemberAccess(any(), any()))
                .thenReturn(Optional.of(folder));

        doNothing()
                .when(storageService).store(any(), any());

        when(fileGateway.create(any()))
                .thenThrow(new IllegalStateException("FileGateway Exception"));

        doNothing()
                .when(storageService)
                .delete(any());

        when(aclGateway.findByResource(any()))
                .thenReturn(Optional.of(Acl.with(
                        expectedAclId,
                        expectedAclResource,
                        expectedAclDirectEntries,
                        expectedAclInheritedEntries,
                        expectedAclCreatedAt,
                        expectedAclUpdatedAt,
                        new LinkedList<>())));

        when(storageKeyGenerator.generate())
                .thenReturn(expectedStorageKey);

        final var input = CreateFileInput.of(
                ownerId.getValue(),
                expectedFolderId.getValue(),
                expectedFileName.value(),
                expectedContentType,
                expectedContent,
                expectedContentSize);

        final var actualException = assertThrows(InternalErrorException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals("FileGateway Exception", actualException.getCause().getMessage());

        verify(folderGateway, times(1)).findByIdWithMemberAccess(any(), any());
        verify(folderGateway, times(1)).findByIdWithMemberAccess(eq(expectedFolderId), eq(ownerId));
        verify(storageService, times(1)).store(any(), any());
        verify(storageService, times(1)).store(any(), eq(expectedContent));
        verify(storageService, times(1)).delete(any());
        verify(fileGateway, times(1)).findAllByFolder(any());
        verify(fileGateway, times(1)).findAllByFolder(eq(folder.getId()));
        verify(fileGateway, times(1)).create(any());
        verify(fileGateway, times(1)).create(argThat(file -> {

            assertNotNull(file.getId().getValue());
            assertEquals(expectedFileName, file.getName());
            assertEquals(expectedContentType, file.getContent().type());
            assertNotNull(file.getCreatedAt());
            assertNotNull(file.getUpdatedAt());
            assertNotNull(file.getContent().storageKey());
            assertEquals(file.getCreatedAt(), file.getUpdatedAt());

            return true;
        }));

    }

    @Test
    void givenAValidParams_whenCallsExecuteAndFileGatewayCreateAndContentGatewayDeleteThrowsRandomException_thenShouldThrowInternalErrorException() {

        final var owner = Member.with(
                MemberID.of(UUID.randomUUID()),
                Username.of("username"),
                Nickname.of("nickname"),
                Quota.of(0, QuotaUnit.BYTE),
                null,
                true,
                Instant.now(),
                Instant.now(),
                0L)
                .requestQuota(Quota.of(1, QuotaUnit.GIGABYTE))
                .approveQuotaRequest();

        final var ownerId = owner.getId();

        final var folder = Folder.createRoot(ownerId);
        final var expectedFolderId = folder.getId();

        final var expectedFileName = FileName.of("file");
        final var expectedContentType = "image/jpeg";
        final var contentBytes = "content".getBytes();

        final var expectedContent = new ByteArrayInputStream(contentBytes);
        final var expectedContentSize = (long) contentBytes.length;

        final var expectedExceptionMessage = "Could not delete BinaryContent";

        final var expectedAclId = AclID.unique();
        final var expectedAclResource = Resource.folder(folder);
        final var expectedAclDirectEntries = Set.<Entry<?>>of(Entry.create(ownerId, AccessPermission.mostPrivileged()));
        final var expectedAclInheritedEntries = Set.<Entry<?>>of();
        final var expectedAclCreatedAt = Instant.now();
        final var expectedAclUpdatedAt = expectedAclCreatedAt;

        final var expectedStorageKey = UUID.randomUUID().toString();

        when(memberGateway.findById(ownerId))
                .thenReturn(Optional.of(owner));

        when(fileGateway.findAllByFolder(any()))
                .thenReturn(List.of());

        when(folderGateway.findByIdWithMemberAccess(any(), any()))
                .thenReturn(Optional.of(folder));

        doNothing()
                .when(storageService).store(any(), any());

        when(fileGateway.create(any()))
                .thenThrow(new IllegalStateException("FileGateway Exception"));

        doThrow(new IllegalStateException("ContentGateway Exception"))
                .when(storageService)
                .delete(any());

        when(aclGateway.findByResource(any()))
                .thenReturn(Optional.of(Acl.with(
                        expectedAclId,
                        expectedAclResource,
                        expectedAclDirectEntries,
                        expectedAclInheritedEntries,
                        expectedAclCreatedAt,
                        expectedAclUpdatedAt,
                        new LinkedList<>())));

        when(storageKeyGenerator.generate())
                .thenReturn(expectedStorageKey);

        final var input = CreateFileInput.of(
                ownerId.getValue(),
                expectedFolderId.getValue(),
                expectedFileName.value(),
                expectedContentType,
                expectedContent,
                expectedContentSize);

        final var actualException = assertThrows(InternalErrorException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals("ContentGateway Exception", actualException.getCause().getMessage());

        verify(folderGateway, times(1)).findByIdWithMemberAccess(any(), any());
        verify(folderGateway, times(1)).findByIdWithMemberAccess(eq(expectedFolderId), eq(ownerId));
        verify(storageService, times(1)).store(any(), any());
        verify(storageService, times(1)).store(any(), eq(expectedContent));
        verify(storageService, times(1)).delete(any());
        verify(fileGateway, times(1)).findAllByFolder(any());
        verify(fileGateway, times(1)).findAllByFolder(eq(folder.getId()));
        verify(fileGateway, times(1)).create(any());
        verify(fileGateway, times(1)).create(argThat(file -> {

            assertNotNull(file.getId().getValue());
            assertEquals(expectedFileName, file.getName());
            assertEquals(expectedContentType, file.getContent().type());
            assertNotNull(file.getCreatedAt());
            assertNotNull(file.getUpdatedAt());
            assertNotNull(file.getContent().storageKey());
            assertEquals(file.getCreatedAt(), file.getUpdatedAt());

            return true;
        }));

    }

    @Test
    void givenAValidParams_whenCallsExecuteAndContentGatewayStoreThrowsRandomException_thenShouldThrowInternalErrorException() {

        final var owner = Member.with(
                MemberID.of(UUID.randomUUID()),
                Username.of("username"),
                Nickname.of("nickname"),
                Quota.of(0, QuotaUnit.BYTE),
                null,
                true,
                Instant.now(),
                Instant.now(),
                0L)
                .requestQuota(Quota.of(1, QuotaUnit.GIGABYTE))
                .approveQuotaRequest();

        final var ownerId = owner.getId();

        final var folder = Folder.createRoot(ownerId);
        final var expectedFolderId = folder.getId();

        final var expectedFileName = FileName.of("file");
        final var expectedContentType = "image/jpeg";
        final var contentBytes = "content".getBytes();

        final var expectedContent = new ByteArrayInputStream(contentBytes);
        final var expectedContentSize = (long) contentBytes.length;

        final var expectedExceptionMessage = "Could not store BinaryContent";

        final var expectedAclId = AclID.unique();
        final var expectedAclResource = Resource.folder(folder);
        final var expectedAclDirectEntries = Set.<Entry<?>>of(Entry.create(ownerId, AccessPermission.mostPrivileged()));
        final var expectedAclInheritedEntries = Set.<Entry<?>>of();
        final var expectedAclCreatedAt = Instant.now();
        final var expectedAclUpdatedAt = expectedAclCreatedAt;

        final var expectedStorageKey = UUID.randomUUID().toString();

        when(memberGateway.findById(ownerId))
                .thenReturn(Optional.of(owner));

        when(folderGateway.findByIdWithMemberAccess(any(), any()))
                .thenReturn(Optional.of(folder));

        doThrow(new IllegalStateException("ContentGateway Exception"))
                .when(storageService)
                .store(any(), any());

        when(aclGateway.findByResource(any()))
                .thenReturn(Optional.of(Acl.with(
                        expectedAclId,
                        expectedAclResource,
                        expectedAclDirectEntries,
                        expectedAclInheritedEntries,
                        expectedAclCreatedAt,
                        expectedAclUpdatedAt,
                        new LinkedList<>())));

        when(storageKeyGenerator.generate())
                .thenReturn(expectedStorageKey);

        final var input = CreateFileInput.of(
                ownerId.getValue(),
                expectedFolderId.getValue(),
                expectedFileName.value(),
                expectedContentType,
                expectedContent,
                expectedContentSize);

        final var actualException = assertThrows(InternalErrorException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals("ContentGateway Exception", actualException.getCause().getMessage());

        verify(folderGateway, times(1)).findByIdWithMemberAccess(any(), any());
        verify(folderGateway, times(1)).findByIdWithMemberAccess(eq(expectedFolderId), eq(ownerId));
        verify(storageService, times(1)).store(any(), any());
        verify(storageService, times(1)).store(any(), eq(expectedContent));
        verify(fileGateway, times(1)).findAllByFolder(any());
        verify(fileGateway, times(1)).findAllByFolder(eq(expectedFolderId));
        verify(storageService, times(0)).delete(any());
        verify(fileGateway, times(0)).create(any());

    }

    @Test
    void givenAnInvalidFileName_whenCallsExecute_thenShouldThrowValidationException() {

        final var owner = Member.with(
                MemberID.of(UUID.randomUUID()),
                Username.of("username"),
                Nickname.of("nickname"),
                Quota.of(0, QuotaUnit.BYTE),
                null,
                true,
                Instant.now(),
                Instant.now(),
                0L)
                .requestQuota(Quota.of(1, QuotaUnit.GIGABYTE))
                .approveQuotaRequest();

        final var ownerId = owner.getId();

        final var folder = Folder.createRoot(ownerId);
        final var expectedFolderId = folder.getId();

        final var expectedFileName = FileName.of("NUL");
        final var expectedContentType = "image/jpeg";
        final var contentBytes = "content".getBytes();

        final var expectedContent = new ByteArrayInputStream(contentBytes);
        final var expectedContentSize = (long) contentBytes.length;

        final var expectedExceptionMessage = "Could not create Aggregate File";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' cannot be a reserved name: NUL";

        final var expectedAclId = AclID.unique();
        final var expectedAclResource = Resource.folder(folder);
        final var expectedAclDirectEntries = Set.<Entry<?>>of(Entry.create(ownerId, AccessPermission.mostPrivileged()));
        final var expectedAclInheritedEntries = Set.<Entry<?>>of();
        final var expectedAclCreatedAt = Instant.now();
        final var expectedAclUpdatedAt = expectedAclCreatedAt;

        when(memberGateway.findById(ownerId))
                .thenReturn(Optional.of(owner));

        when(folderGateway.findByIdWithMemberAccess(expectedFolderId, ownerId))
                .thenReturn(Optional.of(folder));

        when(aclGateway.findByResource(any()))
                .thenReturn(Optional.of(Acl.with(
                        expectedAclId,
                        expectedAclResource,
                        expectedAclDirectEntries,
                        expectedAclInheritedEntries,
                        expectedAclCreatedAt,
                        expectedAclUpdatedAt,
                        new LinkedList<>())));

        final var input = CreateFileInput.of(
                ownerId.getValue(),
                expectedFolderId.getValue(),
                expectedFileName.value(),
                expectedContentType,
                expectedContent,
                expectedContentSize);

        final var actualException = assertThrows(ValidationException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(folderGateway, times(1)).findByIdWithMemberAccess(any(), any());
        verify(folderGateway, times(1)).findByIdWithMemberAccess(eq(expectedFolderId), eq(ownerId));
        verify(storageService, times(0)).store(any(), any());
        verify(fileGateway, times(0)).findAllByFolder(any());
        verify(storageService, times(0)).delete(any());
        verify(fileGateway, times(0)).create(any());

    }

    @Test
    void givenAValidParams_whenCallsExecuteAndMemberQuotaIsExceeded_thenShouldThrowsQuotaExceededException() {

        final var owner = Member.with(
                MemberID.of(UUID.randomUUID()),
                Username.of("username"),
                Nickname.of("nickname"),
                Quota.of(0, QuotaUnit.BYTE),
                null,
                true,
                Instant.now(),
                Instant.now(),
                0L)
                .requestQuota(Quota.of(1, QuotaUnit.BYTE))
                .approveQuotaRequest();

        final var ownerId = owner.getId();

        final var folder = Folder.createRoot(ownerId);
        final var expectedFolderId = folder.getId();

        final var expectedFileName = FileName.of("NUL");
        final var expectedContentType = "image/jpeg";
        final var contentBytes = "content".getBytes();

        final var expectedContent = new ByteArrayInputStream(contentBytes);
        final var expectedContentSize = (long) contentBytes.length;

        final var expectedExceptionMessage = "Quota exceeded.";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "You have exceeded your current quota of 1 BYTE";

        final var expectedAclId = AclID.unique();
        final var expectedAclResource = Resource.folder(folder);
        final var expectedAclDirectEntries = Set.<Entry<?>>of(Entry.create(ownerId, AccessPermission.mostPrivileged()));
        final var expectedAclInheritedEntries = Set.<Entry<?>>of();
        final var expectedAclCreatedAt = Instant.now();
        final var expectedAclUpdatedAt = expectedAclCreatedAt;

        when(memberGateway.findById(ownerId))
                .thenReturn(Optional.of(owner));

        when(folderGateway.findByIdWithMemberAccess(expectedFolderId, ownerId))
                .thenReturn(Optional.of(folder));

        when(aclGateway.findByResource(any()))
                .thenReturn(Optional.of(Acl.with(
                        expectedAclId,
                        expectedAclResource,
                        expectedAclDirectEntries,
                        expectedAclInheritedEntries,
                        expectedAclCreatedAt,
                        expectedAclUpdatedAt,
                        new LinkedList<>())));

        final var input = CreateFileInput.of(
                ownerId.getValue(),
                expectedFolderId.getValue(),
                expectedFileName.value(),
                expectedContentType,
                expectedContent,
                expectedContentSize);

        final var actualException = assertThrows(QuotaExceededException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(folderGateway, times(1)).findByIdWithMemberAccess(any(), any());
        verify(storageService, times(0)).store(any(), any());
        verify(fileGateway, times(0)).findAllByFolder(any());
        verify(storageService, times(0)).delete(any());
        verify(fileGateway, times(0)).create(any());

    }

    @Test
    void givenAnInvalidParamsWithContentTypeNull_whenCallsExecute_thenShouldThrowsValidationException() {

        final var owner = Member.with(
                MemberID.of(UUID.randomUUID()),
                Username.of("username"),
                Nickname.of("nickname"),
                Quota.of(0, QuotaUnit.BYTE),
                null,
                true,
                Instant.now(),
                Instant.now(),
                0L)
                .requestQuota(Quota.of(1, QuotaUnit.GIGABYTE))
                .approveQuotaRequest();

        final var ownerId = owner.getId();

        final var folder = Folder.createRoot(ownerId);
        final var expectedFolderId = folder.getId();

        final var expectedFileName = FileName.of("file");
        final String expectedContentType = null;
        final var contentBytes = "content".getBytes();

        final var expectedContent = new ByteArrayInputStream(contentBytes);
        final var expectedContentSize = (long) contentBytes.length;

        final var expectedExceptionMessage = "Could not create Aggregate File";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' cannot be null.";

        final var expectedAclId = AclID.unique();
        final var expectedAclResource = Resource.folder(folder);
        final var expectedAclDirectEntries = Set.<Entry<?>>of(Entry.create(ownerId, AccessPermission.mostPrivileged()));
        final var expectedAclInheritedEntries = Set.<Entry<?>>of();
        final var expectedAclCreatedAt = Instant.now();
        final var expectedAclUpdatedAt = expectedAclCreatedAt;

        final var expectedStorageKey = UUID.randomUUID().toString();

        when(memberGateway.findById(any()))
                .thenReturn(Optional.of(owner));

        when(fileGateway.findAllByFolder(any()))
                .thenReturn(List.of());

        when(folderGateway.findByIdWithMemberAccess(any(), any()))
                .thenReturn(Optional.of(folder));

        when(folderGateway.findByIdWithMemberAccess(expectedFolderId, ownerId))
                .thenReturn(Optional.of(folder));

        doNothing()
                .when(storageService).store(any(), any());

        when(aclGateway.findByResource(any()))
                .thenReturn(Optional.of(Acl.with(
                        expectedAclId,
                        expectedAclResource,
                        expectedAclDirectEntries,
                        expectedAclInheritedEntries,
                        expectedAclCreatedAt,
                        expectedAclUpdatedAt,
                        new LinkedList<>())));

        when(storageKeyGenerator.generate())
                .thenReturn(expectedStorageKey);

        final var input = CreateFileInput.of(
                ownerId.getValue(),
                expectedFolderId.getValue(),
                expectedFileName.value(),
                expectedContentType,
                expectedContent,
                expectedContentSize);

        final var actualException = assertThrows(ValidationException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(folderGateway, times(1)).findByIdWithMemberAccess(any(), any());
        verify(folderGateway, times(1)).findByIdWithMemberAccess(eq(expectedFolderId), eq(ownerId));
        verify(storageService, times(1)).store(any(), any());
        verify(storageService, times(1)).store(any(), eq(expectedContent));
        verify(storageService, times(0)).delete(any());
        verify(fileGateway, times(1)).findAllByFolder(any());
        verify(fileGateway, times(1)).findAllByFolder(eq(folder.getId()));
        verify(fileGateway, times(0)).create(any());

    }

}
