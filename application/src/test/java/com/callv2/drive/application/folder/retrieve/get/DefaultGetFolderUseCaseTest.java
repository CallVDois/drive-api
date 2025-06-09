package com.callv2.drive.application.folder.retrieve.get;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.folder.FolderName;
import com.callv2.drive.domain.member.MemberID;

@ExtendWith(MockitoExtension.class)
public class DefaultGetFolderUseCaseTest {

    @InjectMocks
    DefaultGetFolderUseCase useCase;

    @Mock
    FolderGateway folderGateway;

    @Mock
    FileGateway fileGateway;

    @Test
    void givenAValidFolderId_whenCallsExecute_thenShouldReturnFolder() {

        final var ownerId = MemberID.of("owner");

        final var expectedFolderName = "folder";
        final var expectedFolder = Folder.create(
                ownerId,
                FolderName.of(expectedFolderName),
                Folder.createRoot(ownerId));

        final var expectedFolderId = expectedFolder.getId();
        final var expectedSubFolders = expectedFolder.getSubFolders();
        final var expectedCreatedAt = expectedFolder.getCreatedAt();
        final var expectedUpdatedAt = expectedFolder.getUpdatedAt();
        final var expectedDeletedAt = expectedFolder.getDeletedAt();

        when(folderGateway.findById(expectedFolderId))
                .thenReturn(Optional.of(expectedFolder));

        final var input = GetFolderInput.with(expectedFolderId.getValue());

        final var actualOutput = assertDoesNotThrow(() -> useCase.execute(input));

        assertEquals(expectedFolderId.getValue(), actualOutput.id());
        assertEquals(expectedFolderName, actualOutput.name());
        assertEquals(expectedFolder.getParentFolder().getValue(), actualOutput.parentFolder());
        assertEquals(expectedSubFolders.size(), actualOutput.subFolders().size());
        assertEquals(expectedCreatedAt, actualOutput.createdAt());
        assertEquals(expectedUpdatedAt, actualOutput.updatedAt());
        assertEquals(expectedDeletedAt, actualOutput.deletedAt());

        verify(folderGateway, times(1)).findById(any());
        verify(folderGateway, times(1)).findById(eq(expectedFolderId));

    }

    @Test
    void givenNotExistentFolderId_whenCallsExecute_thenShouldThorwsNotFoundException() {

        final var expectedFolderId = FolderID.unique();

        final var expectedExceptionMessage = "[Folder] not found.";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "[Folder] with id [%s] not found.".formatted(expectedFolderId.getValue());

        when(folderGateway.findById(expectedFolderId))
                .thenReturn(Optional.empty());

        final var input = GetFolderInput.with(expectedFolderId.getValue());

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(folderGateway, times(1)).findById(any());
        verify(folderGateway, times(1)).findById(eq(expectedFolderId));

    }

}
