package com.callv2.drive.application.file.content.get;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.Content;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.file.FileName;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.storage.StorageGateway;

@ExtendWith(MockitoExtension.class)
public class DefaultGetFileContentUseCaseTest {

    @InjectMocks
    DefaultGetFileContentUseCase useCase;

    @Mock
    FileGateway fileGateway;

    @Mock
    StorageGateway storageService;

    @Test
    void givenAValidParam_whenCallsExecute_shouldReturnContent() {

        final var creatorId = MemberID.of(UUID.randomUUID());
        final var ownerId = MemberID.of(UUID.randomUUID());
        final var expectedFolder = FolderID.unique();
        final var expectedFileName = FileName.of("file.txt");
        final var expectedContent = Content.of("key", "text", 10);
        final var expectedFile = File.create(creatorId, ownerId, expectedFolder, expectedFileName, expectedContent);
        final var expectedFileId = expectedFile.getId();
        final var expectedInputStream = new ByteArrayInputStream(new byte[] {});

        when(fileGateway.findByIdWithMemberAccess(any(), any()))
                .thenReturn(Optional.of(expectedFile));

        when(storageService.retrieve(expectedContent.storageKey()))
                .thenReturn(expectedInputStream);

        final var input = GetFileContentInput.with(expectedFileId.getValue(), creatorId.getValue());

        final var actualOutput = useCase.execute(input);

        assertEquals(expectedFileName.value(), actualOutput.name());
        assertEquals(expectedContent.size(), actualOutput.size());
        assertEquals(expectedInputStream, actualOutput.inputStream());

        verify(fileGateway, times(1)).findByIdWithMemberAccess(any(), any());
        verify(fileGateway, times(1)).findByIdWithMemberAccess(eq(expectedFileId), eq(creatorId));

    }

    @Test
    void givenAnInexistentId_whenCallsExecute_shouldThrowNotFoundException() {

        final var actorId = MemberID.of(UUID.randomUUID());

        final var expectedFileId = FileID.unique();

        final var expectedExceptionMessage = "[File] not found.";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "[File] with id [%s] not found.".formatted(expectedFileId.getValue());

        when(fileGateway.findByIdWithMemberAccess(any(), any()))
                .thenReturn(Optional.empty());

        final var input = GetFileContentInput.with(expectedFileId.getValue(), actorId.getValue());

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(fileGateway, times(1)).findByIdWithMemberAccess(any(), any());
        verify(fileGateway, times(1)).findByIdWithMemberAccess(eq(expectedFileId), eq(actorId));

    }

}
