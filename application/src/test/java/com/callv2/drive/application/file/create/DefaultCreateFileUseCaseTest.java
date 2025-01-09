package com.callv2.drive.application.file.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.callv2.drive.domain.file.BinaryContent;
import com.callv2.drive.domain.file.FileExtension;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileName;

@ExtendWith(MockitoExtension.class)
public class DefaultCreateFileUseCaseTest {

    @InjectMocks
    DefaultCreateFileUseCase useCase;

    @Mock
    FileGateway fileGateway;

    @Test
    void givenAValidParams_whenCallsExecute_thenShouldCreateFile() {

        final var expectedFileName = FileName.of("file");
        final var expectedFileExtension = FileExtension.of("txt");
        final var expectedContent = BinaryContent.of("content".getBytes());

        when(fileGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var input = new CreateFileInput(
                expectedFileName.value(),
                expectedFileExtension.value(),
                expectedContent.bytes());

        final var actualOuptut = useCase.execute(input);

        assertNotNull(actualOuptut.id());

        verify(fileGateway, times(1)).create(any());
        verify(fileGateway, times(1)).create(argThat(file -> {

            assertEquals(actualOuptut.id(), file.getId().getValue().toString());
            assertEquals(expectedFileName, file.getName());
            assertEquals(expectedFileExtension, file.getExtension());
            assertEquals(expectedContent, file.getContent());
            assertNotNull(file.getCreatedAt());
            assertNotNull(file.getUpdatedAt());
            assertEquals(file.getCreatedAt(), file.getUpdatedAt());

            return true;

        }));

    }

}
