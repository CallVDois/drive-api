package com.callv2.drive.application.file.usecase.retrieve.get;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.member.MemberID;

@ExtendWith(MockitoExtension.class)
public class DefaultGetFileUseCaseTest {

    @InjectMocks
    DefaultGetFileUseCase useCase;

    @Mock
    FileGateway fileGateway;

    @Test
    void givenAValidId_whenCallsExecute_thenShouldReturnFile() {

        final var creatorId = MemberID.of(UUID.randomUUID());
        final var ownerId = creatorId;

        final var expectedFolder = Folder.createRoot(ownerId);

        final var expectedName = "file.jpeg";

        final var expectedContentLocation = "location";
        final var expectedContentType = "image/jpeg";
        final var expectedContentSize = 10L;

        final var expectedContent = Content.of(expectedContentLocation, expectedContentType, expectedContentSize);

        final var expectedFile = File.create(
                creatorId,
                ownerId,
                expectedFolder.getId(),
                FileName.of(expectedName),
                expectedContent);

        final var expectedId = expectedFile.getId();

        when(fileGateway.findByIdWithMemberAccess(any(), any()))
                .thenReturn(Optional.of(File.with(expectedFile)));

        final var aCommand = GetFileInput.from(expectedId.getValue(), creatorId.getValue());

        final var actualOuput = assertDoesNotThrow(() -> useCase.execute(aCommand));

        assertEquals(expectedName, actualOuput.name());
        assertEquals(expectedContentType, actualOuput.contentType());
        assertEquals(expectedFile.getCreatedAt(), actualOuput.createdAt());
        assertEquals(expectedFile.getUpdatedAt(), actualOuput.updatedAt());

        verify(fileGateway, times(1)).findByIdWithMemberAccess(any(), any());
        verify(fileGateway, times(1)).findByIdWithMemberAccess(eq(expectedId), eq(creatorId));
    }

    @Test
    void givenAInvalidId_whenCallsExecute_thenShouldReturnNotFound() {

        final var expectedId = FileID.unique();

        final var actorId = MemberID.of(UUID.randomUUID());

        final var expectedExceptionMessage = "[File] not found.";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "[File] with id [%s] not found.".formatted(expectedId.getValue().toString());

        when(fileGateway.findByIdWithMemberAccess(any(), any()))
                .thenReturn(Optional.empty());

        final var aCommand = GetFileInput.from(expectedId.getValue(), actorId.getValue());

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(fileGateway, times(1)).findByIdWithMemberAccess(any(), any());
        verify(fileGateway, times(1)).findByIdWithMemberAccess(eq(expectedId), eq(actorId));
    }

}
