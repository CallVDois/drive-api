package com.callv2.drive.application.file.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.callv2.drive.domain.file.ContentGateway;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileName;

@ExtendWith(MockitoExtension.class)
public class DefaultCreateFileUseCaseTest {

    @InjectMocks
    DefaultCreateFileUseCase useCase;

    @Mock
    ContentGateway contentGateway;

    @Mock
    FileGateway fileGateway;

    @Test
    void givenAValidParams_whenCallsExecute_thenShouldCreateFile() {

        final var expectedFileName = FileName.of("file");
        final var expectedContentType = "image/jpeg";
        final var expectedContent = "content".getBytes();

        doNothing()
                .when(contentGateway)
                .store(any());

        when(fileGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var input = new CreateFileInput(
                expectedFileName.value(),
                expectedContentType,
                expectedContent);

        final var actualOuptut = useCase.execute(input);

        assertNotNull(actualOuptut.id());

        verify(contentGateway, times(1)).store(any());
        verify(contentGateway, times(1)).store(argThat(content -> {

            assertEquals(expectedContent, content.bytes());
            assertNotNull(content.getId());

            return true;
        }));

        verify(fileGateway, times(1)).create(any());
        verify(fileGateway, times(1)).create(argThat(file -> {

            assertEquals(actualOuptut.id().getValue(), file.getId().getValue());
            assertEquals(expectedFileName, file.getName());
            assertEquals(expectedContentType, file.getContentType());
            assertNotNull(file.getCreatedAt());
            assertNotNull(file.getUpdatedAt());
            assertNotNull(file.getContent());
            assertEquals(file.getCreatedAt(), file.getUpdatedAt());

            return true;

        }));

    }

}
