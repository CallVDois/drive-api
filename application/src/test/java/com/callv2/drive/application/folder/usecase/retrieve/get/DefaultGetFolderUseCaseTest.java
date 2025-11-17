package com.callv2.drive.application.folder.usecase.retrieve.get;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.callv2.drive.application.folder.service.PathResolutionApplicationService;
import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.folder.entity.FolderID;
import com.callv2.drive.domain.folder.valueobject.FolderName;
import com.callv2.drive.domain.member.MemberID;

@ExtendWith(MockitoExtension.class)
public class DefaultGetFolderUseCaseTest {

    @InjectMocks
    DefaultGetFolderUseCase useCase;

    @Mock
    FolderGateway folderGateway;

    @Mock
    FileGateway fileGateway;

    @Mock
    PathResolutionApplicationService pathResolutionService;

    @Test
    void givenAValidFolderId_whenCallsExecute_thenShouldReturnFolder() {

        final var ownerId = MemberID.of(UUID.randomUUID());
        final var actorId = ownerId;

        final var expectedRootFolder = Folder.createRoot(ownerId);

        final var expectedFolderName = "folder";
        final var expectedFolder = Folder.create(
                ownerId,
                ownerId,
                FolderName.of(expectedFolderName),
                expectedRootFolder);

        final var expectedSubFolder1 = Folder.create(
                ownerId,
                ownerId,
                FolderName.of("subFolder1"),
                expectedFolder);
        final var expectedSubFolder2 = Folder.create(
                ownerId,
                ownerId,
                FolderName.of("subFolder2"),
                expectedFolder);

        final var expectedSubFolders = Set.of(expectedSubFolder1, expectedSubFolder2);

        final var expectedFolderId = expectedFolder.getId();
        final var expectedCreatedAt = expectedFolder.getCreatedAt();
        final var expectedUpdatedAt = expectedFolder.getUpdatedAt();
        final var expectedDeletedAt = expectedFolder.getDeletedAt();

        when(folderGateway.findByIdWithMemberAccess(expectedFolderId, ownerId))
                .thenReturn(Optional.of(expectedFolder));

        when(folderGateway.findByParentFolderIdWithMemberAccess(expectedFolder.getId(), actorId))
                .thenReturn(expectedSubFolders);

        when(pathResolutionService.resolvePath(expectedFolder, actorId))
                .thenReturn(List.of(expectedFolder, expectedRootFolder));

        final var input = GetFolderInput.with(expectedFolderId.getValue(), ownerId.getValue());

        final var actualOutput = assertDoesNotThrow(() -> useCase.execute(input));

        assertEquals(expectedFolderId.getValue(), actualOutput.id());
        assertEquals(expectedFolderName, actualOutput.name());
        assertEquals(expectedFolder.getParentFolder().getValue(), actualOutput.parentFolder());
        assertEquals(expectedSubFolders.size(), actualOutput.subFolders().size());
        assertEquals(expectedRootFolder.getId().getValue(), actualOutput.path().get(1).id());
        assertEquals(expectedRootFolder.getName().value(), actualOutput.path().get(1).name());
        assertEquals(expectedFolder.getId().getValue(), actualOutput.path().get(0).id());
        assertEquals(expectedFolder.getName().value(), actualOutput.path().get(0).name());
        assertEquals(expectedCreatedAt, actualOutput.createdAt());
        assertEquals(expectedUpdatedAt, actualOutput.updatedAt());
        assertEquals(expectedDeletedAt, actualOutput.deletedAt());

        verify(folderGateway, times(1)).findByIdWithMemberAccess(any(), any());
        verify(folderGateway, times(1)).findByIdWithMemberAccess(eq(expectedFolderId), eq(ownerId));

        verify(folderGateway, times(1)).findByParentFolderIdWithMemberAccess(any(), any());
        verify(folderGateway, times(1)).findByParentFolderIdWithMemberAccess(eq(expectedFolder.getId()), eq(actorId));

        verify(pathResolutionService, times(1)).resolvePath(any(), any());
        verify(pathResolutionService, times(1)).resolvePath(eq(expectedFolder), eq(actorId));

    }

    @Test
    void givenNotExistentFolderId_whenCallsExecute_thenShouldThorwsNotFoundException() {

        final var expectedFolderId = FolderID.unique();
        final var expectActorId = MemberID.of(UUID.randomUUID());

        final var expectedExceptionMessage = "[Folder] not found.";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "[Folder] with id [%s] not found."
                .formatted(expectedFolderId.getValue());

        when(folderGateway.findByIdWithMemberAccess(expectedFolderId, expectActorId))
                .thenReturn(Optional.empty());

        final var input = GetFolderInput.with(expectedFolderId.getValue(), expectActorId.getValue());

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(folderGateway, times(1)).findByIdWithMemberAccess(any(), any());
        verify(folderGateway, times(1)).findByIdWithMemberAccess(eq(expectedFolderId), eq(expectActorId));

        verify(pathResolutionService, times(0)).resolvePath(any(), any());

    }

}
