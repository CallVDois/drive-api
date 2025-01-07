package com.callv2.drive.domain.file;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.callv2.drive.domain.exception.ValidationException;

public class FileTest {

    @Test
    void givenAValidParams_whenCallsCreate_thenShouldCreateFile() {

        final var expectedName = "file";
        final var expectedExtension = "txt";
        final var expectedContent = new byte[] {};

        final var actualFile = assertDoesNotThrow(() -> File.create(
                FileName.of(expectedName),
                FileExtension.of(expectedExtension),
                BinaryContent.of(expectedContent)));

        assertEquals(expectedName, actualFile.getName().value());
        assertEquals(expectedExtension, actualFile.getExtension().value());
        assertEquals(expectedContent, actualFile.getContent().bytes());
        assertNotNull(actualFile.getCreatedAt());
        assertNotNull(actualFile.getUpdatedAt());
        assertEquals(actualFile.getCreatedAt(), actualFile.getUpdatedAt());
    }

    @Test
    void givenEmptyName_whenCallsCreate_thenShouldThrowsValidationException() {

        final var expectedName = "";
        final var expectedExtension = "txt";
        final var expectedContent = new byte[] {};

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "File name cannot be null or empty.";

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        FileExtension.of(expectedExtension),
                        BinaryContent.of(expectedContent)));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenNullName_whenCallsCreate_thenShouldThrowsValidationException() {

        final String expectedName = null;
        final var expectedExtension = "txt";
        final var expectedContent = new byte[] {};

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "File name cannot be null or empty.";

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        FileExtension.of(expectedExtension),
                        BinaryContent.of(expectedContent)));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenBadCharName_whenCallsCreate_thenShouldThrowsValidationException() {

        final var expectedName = "/file";
        final var expectedExtension = "txt";
        final var expectedContent = new byte[] {};

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "File name contains invalid characters. Allowed: letters, digits, dots, underscores, and hyphens.";

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        FileExtension.of(expectedExtension),
                        BinaryContent.of(expectedContent)));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    void givenNameWithMoreThan64Chars_whenCallsCreate_thenShouldThrowsValidationException() {

        final var expectedName = """
                filefilefilefilefilefilefilefilefilefilefilefilefilefilefilefilefilefile
                """;
        final var expectedExtension = "txt";
        final var expectedContent = new byte[] {};

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "File name must be between 1 and 64 characters.";

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        FileExtension.of(expectedExtension),
                        BinaryContent.of(expectedContent)));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    void givenReservedName_whenCallsCreate_thenShouldThrowsValidationException() {

        final var expectedName = "nul";
        final var expectedExtension = "txt";
        final var expectedContent = new byte[] {};

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "File name cannot be a reserved name: %s".formatted(expectedName);

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        FileExtension.of(expectedExtension),
                        BinaryContent.of(expectedContent)));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    void givenEmptyExtension_whenCallsCreate_thenShouldThrowsValidationException() {

        final var expectedName = "file";
        final var expectedExtension = "";
        final var expectedContent = new byte[] {};

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "File extension cannot be null or empty.";

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        FileExtension.of(expectedExtension),
                        BinaryContent.of(expectedContent)));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenExtensionWithMoreThan16Chars_whenCallsCreate_thenShouldThrowsValidationException() {

        final var expectedName = "file";
        final String expectedExtension = null;
        final var expectedContent = new byte[] {};

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "File extension cannot be null or empty.";

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        FileExtension.of(expectedExtension),
                        BinaryContent.of(expectedContent)));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenNullExtension_whenCallsCreate_thenShouldThrowsValidationException() {

        final var expectedName = "file";
        final var expectedExtension = """
                txttxttxttxttxttxttxttxt
                """;
        final var expectedContent = new byte[] {};

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "File extension must be between 1 and 16 characters.";

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        FileExtension.of(expectedExtension),
                        BinaryContent.of(expectedContent)));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenMultipleInvalidParams_whenCallsCreate_thenShouldThrowsValidationExceptionWithMultipleErrors() {

        final String expectedName = "nul";
        final String expectedExtension = "";
        final var expectedContent = new byte[] {};

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 2;
        final var expectedNameErrorMessage = "File name cannot be a reserved name: %s".formatted(expectedName);
        final var expectedExtensionErrorMessage = "File extension cannot be null or empty.";

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        FileExtension.of(expectedExtension),
                        BinaryContent.of(expectedContent)));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());

        assertTrue(actualException
                .getErrors().stream().anyMatch(e -> e.message().equals(expectedNameErrorMessage)));
        assertTrue(actualException
                .getErrors().stream().anyMatch(e -> e.message().equals(expectedExtensionErrorMessage)));
    }

}
