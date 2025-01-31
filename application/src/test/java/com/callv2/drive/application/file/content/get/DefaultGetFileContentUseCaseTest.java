package com.callv2.drive.application.file.content.get;

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
import com.callv2.drive.domain.file.Content;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.file.FileName;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.member.MemberID;

@ExtendWith(MockitoExtension.class)
public class DefaultGetFileContentUseCaseTest {

    @InjectMocks
    DefaultGetFileContentUseCase useCase;

    @Mock
    FileGateway fileGateway;

    @Test
    void givenAValidParam_whenCallsExecute_shouldReturnContent() {

        final var ownerId = MemberID.of("owner");
        final var expectedFolder = FolderID.unique();
        final var expectedFileName = FileName.of("file.txt");
        final var expectedContent = Content.of("location", "text", 10);
        final var expectedFile = File.create(ownerId, expectedFolder, expectedFileName, expectedContent);
        final var expectedFileId = expectedFile.getId();

        when(fileGateway.findById(any()))
                .thenReturn(Optional.of(expectedFile));

        final var input = GetFileContentInput.with(expectedFileId.getValue());

        final var actualOutput = useCase.execute(input);

        assertEquals(expectedFileName.value(), actualOutput.name());
        assertEquals(expectedContent.location(), actualOutput.location());
        assertEquals(expectedContent.size(), actualOutput.size());

        verify(fileGateway, times(1)).findById(any());
        verify(fileGateway, times(1)).findById(eq(expectedFileId));

    }

    @Test
    void givenAnInexistentId_whenCallsExecute_shouldThrowNotFoundException() {

        final var expectedFileId = FileID.unique();

        final var expectedExceptionMessage = "File with id '%s' not found"
                .formatted(expectedFileId.getValue().toString());

        when(fileGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var input = GetFileContentInput.with(expectedFileId.getValue());

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(input));

        assertEquals(expectedExceptionMessage, actualException.getMessage());

        verify(fileGateway, times(1)).findById(any());
        verify(fileGateway, times(1)).findById(eq(expectedFileId));

    }

}
