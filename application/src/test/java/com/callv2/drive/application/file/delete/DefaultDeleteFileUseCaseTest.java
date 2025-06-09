package com.callv2.drive.application.file.delete;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.*;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.member.*;
import com.callv2.drive.domain.storage.StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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

        final Member owner = Member.create(MemberID.of("owner"))
                .requestQuota(Quota.of(1, QuotaUnit.GIGABYTE))
                .approveQuotaRequest();

        final FileID expectedFileId = FileID.unique();
        final MemberID expectedOwnerId = owner.getId();
        final FolderID expectedFolderId = FolderID.unique();
        final FileName expectedFileName = FileName.of("file.txt");
        final Content expectedContent = Content.of(
                "file.txt",
                "text/plain",
                100L
        );
        final Instant expectedCreatedAt = Instant.now().minus(
                java.time.Duration.ofDays(1)
        );
        final Instant expectedUpdatedAt = Instant.now();
        final var file = File.with(
                expectedFileId,
                expectedOwnerId,
                expectedFolderId,
                expectedFileName,
                expectedContent,
                expectedCreatedAt,
                expectedUpdatedAt
        );

        when(memberGateway.findById(expectedOwnerId))
                .thenReturn(Optional.of(owner));

        when(fileGateway.findById(expectedFileId))
                .thenReturn(Optional.of(file));

        final DeleteFileInput input = DeleteFileInput.of(
                expectedOwnerId.getValue(),
                expectedFileId.getValue()
        );

        useCase.execute(input);

        verify(memberGateway, times(1)).findById(any());
        verify(memberGateway, times(1)).findById(eq(expectedOwnerId));
        verify(fileGateway, times(1)).findById(any());
        verify(fileGateway, times(1)).findById(eq(expectedFileId));
        verify(fileGateway, times(1)).deleteById(any());
        verify(fileGateway, times(1)).deleteById(eq(expectedFileId));
        verify(storageService, times(1)).delete(any());
        verify(storageService, times(1)).delete(eq(expectedContent.location()));
    }

    @Test
    void givenAInvalidMemberId_whenCallsExecute_thenShouldThrowNotFoundException() {

        final MemberID expectedOwnerId = MemberID.of("owner");
        final FileID expectedFileId = FileID.unique();

        final String expectedExceptionMessage = "Member with id '%s' not found"
                .formatted(expectedOwnerId.getValue());

        when(memberGateway.findById(expectedOwnerId))
                .thenReturn(Optional.empty());

        final DeleteFileInput input = DeleteFileInput.of(
                expectedOwnerId.getValue(),
                expectedFileId.getValue()
        );

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());

        verify(memberGateway, times(1)).findById(any());
        verify(memberGateway, times(1)).findById(eq(expectedOwnerId));
        verify(fileGateway, never()).findById(any());
        verify(fileGateway, never()).deleteById(any());
        verify(storageService, never()).delete(any());
    }

    @Test
    void givenAInvalidFileId_whenCallsExecute_thenShouldThrowNotFoundException(){

    }



}
