package com.callv2.drive.application.file.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.callv2.drive.domain.file.FileContentGateway;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileName;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;

@ExtendWith(MockitoExtension.class)
public class DefaultCreateFileUseCaseTest {

    @InjectMocks
    DefaultCreateFileUseCase useCase;

    @Mock
    FolderGateway folderGateway;

    @Mock
    FileContentGateway contentGateway;

    @Mock
    FileGateway fileGateway;

    @Test
    void givenAValidParams_whenCallsExecute_thenShouldCreateFile() {

        final var folder = Folder.createRoot();

        final var expectedFolderId = folder.getId();

        final var expectedFileName = FileName.of("file");
        final var expectedContentType = "image/jpeg";
        final var contentBytes = "content".getBytes();

        final var expectedContent = new ByteArrayInputStream(contentBytes);
        final var expectedContentSize = (long) contentBytes.length;

        when(fileGateway.findByFolder(any()))
                .thenReturn(List.of());

        when(folderGateway.findById(any()))
                .thenReturn(Optional.of(folder));

        when(contentGateway.store(any(), any()))
                .then(returnsFirstArg());

        when(fileGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var input = CreateFileInput.of(
                expectedFolderId.getValue(),
                expectedFileName.value(),
                expectedContentType,
                expectedContent,
                expectedContentSize);

        final var actualOuptut = useCase.execute(input);

        assertNotNull(actualOuptut.id());

        verify(folderGateway, times(1)).findById(any());
        verify(folderGateway, times(1)).findById(eq(expectedFolderId));
        verify(contentGateway, times(1)).store(any(), any());
        verify(contentGateway, times(1)).store(any(), eq(expectedContent));
        verify(fileGateway, times(1)).findByFolder(any());
        verify(fileGateway, times(1)).findByFolder(eq(folder.getId()));
        verify(fileGateway, times(1)).create(any());
        verify(fileGateway, times(1)).create(argThat(file -> {

            assertEquals(actualOuptut.id().getValue(), file.getId().getValue());
            assertEquals(expectedFileName, file.getName());
            assertEquals(expectedContentType, file.getContent().type());
            assertNotNull(file.getCreatedAt());
            assertNotNull(file.getUpdatedAt());
            assertNotNull(file.getContent().location());
            assertEquals(file.getCreatedAt(), file.getUpdatedAt());

            return true;
        }));

    }

}
