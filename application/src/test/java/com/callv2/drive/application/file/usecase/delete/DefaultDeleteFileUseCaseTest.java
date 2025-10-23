package com.callv2.drive.application.file.usecase.delete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.callv2.drive.domain.acl.Acl;
import com.callv2.drive.domain.acl.AclGateway;
import com.callv2.drive.domain.acl.Resource;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.event.EventSource;
import com.callv2.drive.domain.exception.NotAllowedException;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.Content;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.file.FileName;
import com.callv2.drive.domain.folder.entity.FolderID;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.member.Nickname;
import com.callv2.drive.domain.member.Quota;
import com.callv2.drive.domain.member.QuotaUnit;
import com.callv2.drive.domain.member.Username;

@ExtendWith(MockitoExtension.class)
public class DefaultDeleteFileUseCaseTest {

    @InjectMocks
    DefaultDeleteFileUseCase useCase;

    @Mock
    AclGateway aclGateway;

    @Mock
    MemberGateway memberGateway;

    @Mock
    FileGateway fileGateway;

    @Mock
    EventDispatcher eventDispatcher;

    @Test
    void givenAValidParam_whenCallsExecute_thenShouldDeleteFile() {

        final var deleter = Member.with(
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

        final FileID expectedFileId = FileID.unique();
        final MemberID expectedCreatorId = deleter.getId();
        final MemberID expectedDeleterId = deleter.getId();
        final FolderID expectedFolderId = FolderID.unique();
        final FileName expectedFileName = FileName.of("file.txt");
        final Content expectedContent = Content.of(
                "file.txt",
                "text/plain",
                100L);
        final Instant expectedCreatedAt = Instant.now().minus(
                java.time.Duration.ofDays(1));
        final Instant expectedUpdatedAt = Instant.now();
        final var file = File.with(
                expectedFileId,
                expectedCreatorId,
                expectedDeleterId,
                expectedFolderId,
                expectedFileName,
                expectedContent,
                expectedCreatorId,
                null,
                expectedCreatedAt,
                expectedUpdatedAt,
                null,
                false,
                null);

        final Resource<FileID> resource = Resource.file(file);
        final Acl fileAcl = Acl.create(resource, file.getOwner());

        when(memberGateway.findById(any()))
                .thenReturn(Optional.of(deleter));

        when(memberGateway.findById(expectedDeleterId))
                .thenReturn(Optional.of(deleter));

        when(fileGateway.findByIdWithMemberAccess(expectedFileId, expectedDeleterId))
                .thenReturn(Optional.of(file));

        when(fileGateway.findByIdWithMemberAccess(expectedFileId, deleter.getId()))
                .thenReturn(Optional.of(file));

        when(aclGateway.findByResource(resource))
                .thenReturn(Optional.of(fileAcl));

        when(fileGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final DeleteFileInput input = DeleteFileInput.of(
                expectedDeleterId.getValue(),
                expectedFileId.getValue());

        useCase.execute(input);

        verify(memberGateway, times(1)).findById(any());
        verify(memberGateway, times(1)).findById(eq(expectedDeleterId));
        verify(fileGateway, times(1)).findByIdWithMemberAccess(any(), any());
        verify(fileGateway, times(1)).findByIdWithMemberAccess(eq(expectedFileId), eq(deleter.getId()));
        verify(fileGateway, times(0)).deleteById(any());
        verify(eventDispatcher, times(1)).notify(any(File.class));
        verify(eventDispatcher, times(1)).notify(any(EventSource.class));
    }

    @Test
    void givenAInvalidMemberId_whenCallsExecute_thenShouldThrowNotFoundException() {

        final MemberID expectedDeleterId = MemberID.of(UUID.randomUUID());
        final FileID expectedFileId = FileID.unique();

        final String expectedExceptionMessage = "[Member] not found.";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "[Member] with id [%s] not found."
                .formatted(expectedDeleterId.getValue());

        when(memberGateway.findById(any()))
                .thenReturn(Optional.empty());

        when(memberGateway.findById(expectedDeleterId))
                .thenReturn(Optional.empty());

        final DeleteFileInput input = DeleteFileInput.of(
                expectedDeleterId.getValue(),
                expectedFileId.getValue());

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(memberGateway, times(1)).findById(any());
        verify(memberGateway, times(1)).findById(eq(expectedDeleterId));
        verify(fileGateway, never()).findByIdWithMemberAccess(any(), any());
        verify(fileGateway, never()).deleteById(any());
        verify(eventDispatcher, never()).notify(any(EventSource.class));
    }

    @Test
    void givenAInvalidFileId_whenCallsExecute_thenShouldThrowNotFoundException() {

        final FileID expectedFileId = FileID.unique();

        final String expectedExceptionMessage = "[File] not found.";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "[File] with id [%s] not found.".formatted(expectedFileId.getValue());

        final var deleter = Member.with(
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

        final MemberID expectedDeleterId = deleter.getId();

        when(memberGateway.findById(expectedDeleterId))
                .thenReturn(Optional.of(deleter));

        when(fileGateway.findByIdWithMemberAccess(expectedFileId, deleter.getId()))
                .thenReturn(Optional.empty());

        final var input = DeleteFileInput.of(
                expectedDeleterId.getValue(),
                expectedFileId.getValue());

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(memberGateway, times(1)).findById(any());
        verify(memberGateway, times(1)).findById(eq(expectedDeleterId));
        verify(fileGateway, times(1)).findByIdWithMemberAccess(any(), any());
        verify(fileGateway, times(1)).findByIdWithMemberAccess(eq(expectedFileId), eq(deleter.getId()));
        verify(fileGateway, never()).deleteById(any());
        verify(eventDispatcher, never()).notify(any(EventSource.class));

    }

    @Test
    void givenAValidMemberIdButNotHaveSystemAccess_whenCallsExecute_thenShouldThrowNotFoundException() {

        final MemberID expectedDeleterId = MemberID.of(UUID.randomUUID());
        final FileID expectedFileId = FileID.unique();

        final String expectedExceptionMessage = "The requested action is not allowed.";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Member does not have permission to delete files.";

        final var deleter = Member.with(
                MemberID.of(UUID.randomUUID()),
                Username.of("username"),
                Nickname.of("nickname"),
                Quota.of(0, QuotaUnit.BYTE),
                null,
                false,
                Instant.now(),
                Instant.now(),
                0L)
                .requestQuota(Quota.of(1, QuotaUnit.GIGABYTE))
                .approveQuotaRequest();

        when(memberGateway.findById(expectedDeleterId))
                .thenReturn(Optional.of(deleter));

        final var input = DeleteFileInput.of(
                expectedDeleterId.getValue(),
                expectedFileId.getValue());

        final var actualException = assertThrows(NotAllowedException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(memberGateway, times(1)).findById(any());
        verify(memberGateway, times(1)).findById(eq(expectedDeleterId));
        verify(fileGateway, never()).findByIdWithMemberAccess(any(), any());
        verify(fileGateway, never()).deleteById(any());
        verify(eventDispatcher, never()).notify(any(EventSource.class));

    }

}
