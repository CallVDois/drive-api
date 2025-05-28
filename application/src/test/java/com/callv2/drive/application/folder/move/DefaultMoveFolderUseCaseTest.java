package com.callv2.drive.application.folder.move;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

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

        final var ownerId = MemberID.of("owner");

        final var expectedRootFolder = Folder.createRoot(ownerId);

        final var expectedFolderToMove = Folder.create(ownerId, FolderName.of("folder1"), expectedRootFolder);
        final var expectedFolderTarget = Folder.create(ownerId, FolderName.of("folder2"), expectedRootFolder);

        when(folderGateway.findById(expectedFolderToMove.getId()))
                .thenReturn(Optional.of(expectedFolderToMove));

        when(folderGateway.findById(expectedFolderTarget.getId()))
                .thenReturn(Optional.of(expectedFolderTarget));

        when(folderGateway.findById(expectedRootFolder.getId()))
                .thenReturn(Optional.of(expectedRootFolder));

        doNothing()
                .when(folderGateway)
                .updateAll(anyList());

        final var input = new MoveFolderInput(
                expectedFolderToMove.getId().getValue(),
                expectedFolderTarget.getId().getValue());

        assertDoesNotThrow(() -> useCase.execute(input));

        verify(folderGateway).updateAll(anyList());
    }

}
