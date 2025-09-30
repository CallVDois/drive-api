package com.callv2.drive.application.folder.move;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderName;
import com.callv2.drive.domain.member.MemberID;

@ExtendWith(MockitoExtension.class)
public class DefaultMoveFolderUseCaseTest {

    @InjectMocks
    DefaultMoveFolderUseCase useCase;

    @Mock
    FolderGateway folderGateway;

    @Test
    void givenVAlidInput_whenCallsExecute_thenMoveFolder() {

        final var ownerId = MemberID.of(UUID.randomUUID());
        final var actorId = ownerId;

        final var expectedRootFolder = Folder.createRoot(ownerId);

        final var expectedFolderToMove = Folder.create(ownerId, ownerId, FolderName.of("folder1"), expectedRootFolder);
        final var expectedFolderTarget = Folder.create(ownerId, ownerId, FolderName.of("folder2"), expectedRootFolder);

        when(folderGateway.findByIdWithMemberAccess(expectedFolderToMove.getId(), ownerId))
                .thenReturn(Optional.of(expectedFolderToMove));

        when(folderGateway.findByIdWithMemberAccess(expectedFolderTarget.getId(), ownerId))
                .thenReturn(Optional.of(expectedFolderTarget));

        when(folderGateway.findByIdWithMemberAccess(expectedRootFolder.getId(), ownerId))
                .thenReturn(Optional.of(expectedRootFolder));

        when(folderGateway.findByParentFolderIdWithMemberAccess(expectedFolderTarget.getId(), actorId))
                .thenReturn(Set.of());

        final var input = new MoveFolderInput(
                expectedFolderToMove.getId().getValue(),
                expectedFolderTarget.getId().getValue(),
                actorId.getValue());

        assertDoesNotThrow(() -> useCase.execute(input));

        verify(folderGateway, never()).updateAll(anyList());
        verify(folderGateway, times(1)).update(eq(expectedFolderToMove));
        verify(folderGateway, times(1)).findByParentFolderIdWithMemberAccess(any(), any());

    }

}
