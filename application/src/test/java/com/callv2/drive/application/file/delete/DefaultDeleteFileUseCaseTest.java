package com.callv2.drive.application.file.delete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.callv2.drive.domain.exception.NotAllowedException;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.Content;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.file.FileName;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.member.Nickname;
import com.callv2.drive.domain.member.Quota;
import com.callv2.drive.domain.member.QuotaUnit;
import com.callv2.drive.domain.member.Username;
import com.callv2.drive.domain.storage.StorageService;

@ExtendWith(MockitoExtension.class)
public class DefaultDeleteFileUseCaseTest {

    @InjectMocks
    DefaultDeleteFileUseCase useCase;

    @Mock
    MemberGateway memberGateway;

    @Mock
    FileGateway fileGateway;

    @Mock
    StorageService storageService;

    @Test
    void givenAValidParam_whenCallsExecute_thenShouldDeleteFile() {

        final var deleter = Member.with(
                MemberID.of("deleter"),
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
                expectedDeleterId,
                expectedFolderId,
                expectedFileName,
                expectedContent,
                expectedCreatedAt,
                expectedUpdatedAt);

        when(memberGateway.findById(expectedDeleterId))
                .thenReturn(Optional.of(deleter));

        when(fileGateway.findById(expectedFileId))
                .thenReturn(Optional.of(file));

        final DeleteFileInput input = DeleteFileInput.of(
                expectedDeleterId.getValue(),
                expectedFileId.getValue());

        useCase.execute(input);

        verify(memberGateway, times(1)).findById(any());
        verify(memberGateway, times(1)).findById(eq(expectedDeleterId));
        verify(fileGateway, times(1)).findById(any());
        verify(fileGateway, times(1)).findById(eq(expectedFileId));
        verify(fileGateway, times(1)).deleteById(any());
        verify(fileGateway, times(1)).deleteById(eq(expectedFileId));
        verify(storageService, times(1)).delete(any());
        verify(storageService, times(1)).delete(eq(expectedContent.location()));
    }

    @Test
    void givenAInvalidMemberId_whenCallsExecute_thenShouldThrowNotFoundException() {

        final MemberID expectedDeleterId = MemberID.of("deleter");
        final FileID expectedFileId = FileID.unique();

        final String expectedExceptionMessage = "[Member] not found.";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "[Member] with id [%s] not found.".formatted(expectedDeleterId.getValue());

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
        verify(fileGateway, never()).findById(any());
        verify(fileGateway, never()).deleteById(any());
        verify(storageService, never()).delete(any());
    }

    @Test
    void givenAInvalidFileId_whenCallsExecute_thenShouldThrowNotFoundException() {

        final MemberID expectedDeleterId = MemberID.of("deleter");
        final FileID expectedFileId = FileID.unique();

        final String expectedExceptionMessage = "[File] not found.";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "[File] with id [%s] not found.".formatted(expectedFileId.getValue());

        final var deleter = Member.with(
                MemberID.of("owner"),
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

        when(memberGateway.findById(expectedDeleterId))
                .thenReturn(Optional.of(deleter));

        when(fileGateway.findById(expectedFileId))
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
        verify(fileGateway, times(1)).findById(any());
        verify(fileGateway, times(1)).findById(eq(expectedFileId));
        verify(fileGateway, never()).deleteById(any());
        verify(storageService, never()).delete(any());

    }

    @Test
    void givenAValidMemberIdButNotHaveSystemAccess_whenCallsExecute_thenShouldThrowNotFoundException() {

        final MemberID expectedDeleterId = MemberID.of("deleter");
        final FileID expectedFileId = FileID.unique();

        final String expectedExceptionMessage = "The requested action is not allowed.";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Member does not have permission to delete files.";

        final var deleter = Member.with(
                MemberID.of("owner"),
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
        verify(fileGateway, never()).findById(any());
        verify(fileGateway, never()).deleteById(any());
        verify(storageService, never()).delete(any());

    }

}
