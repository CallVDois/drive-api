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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.callv2.drive.domain.file.FileContentGateway;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.file.FileName;

@ExtendWith(MockitoExtension.class)
public class DefaultCreateFileUseCaseTest {

    @InjectMocks
    DefaultCreateFileUseCase useCase;

    @Mock
    FileContentGateway contentGateway;

    @Mock
    FileGateway fileGateway;

    @Test
    void givenAValidParams_whenCallsExecute_thenShouldCreateFile() {

        final var expectedFileName = FileName.of("file");
        final var expectedContentType = "image/jpeg";
        final var contentBytes = "content".getBytes();

        final var expectedContent = new ByteArrayInputStream(contentBytes);
        final var expectedContentSize = (long) contentBytes.length;

        when(contentGateway.store(any(), any()))
                .then(returnsFirstArg());

        when(fileGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var input = CreateFileInput.of(
                expectedFileName.value(),
                expectedContentType,
                expectedContent,
                expectedContentSize);

        final var actualOuptut = useCase.execute(input);

        assertNotNull(actualOuptut.id());

        verify(contentGateway, times(1)).store(any(), any());
        verify(contentGateway, times(1)).store(any(), eq(expectedContent));
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
