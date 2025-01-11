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
        final var expectedContentType = "image/jpeg";
        final var expectedContent = BinaryContentID.unique();

        final var actualFile = assertDoesNotThrow(() -> File.create(
                FileName.of(expectedName),
                expectedContentType,
                expectedContent));

        assertEquals(expectedName, actualFile.getName().value());
        assertEquals(expectedContentType, actualFile.getContentType());
        assertEquals(expectedContent, actualFile.getContent());
        assertNotNull(actualFile.getCreatedAt());
        assertNotNull(actualFile.getUpdatedAt());
        assertEquals(actualFile.getCreatedAt(), actualFile.getUpdatedAt());
    }

    @Test
    void givenEmptyName_whenCallsCreate_thenShouldThrowsValidationException() {

        final var expectedName = "";
        final var expectedContentType = "image/jpeg";

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' cannot be empty.";

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        expectedContentType,
                        BinaryContentID.unique()));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenNullName_whenCallsCreate_thenShouldThrowsValidationException() {

        final String expectedName = null;
        final var expectedContentType = "image/jpeg";

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' cannot be null.";

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        expectedContentType,
                        BinaryContentID.unique()));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenBadCharName_whenCallsCreate_thenShouldThrowsValidationException() {

        final var expectedName = "/file";
        final var expectedContentType = "image/jpeg";

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' contains invalid characters. Allowed: letters, digits, dots, underscores, and hyphens.";

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        expectedContentType,
                        BinaryContentID.unique()));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    void givenNameWithMoreThan64Chars_whenCallsCreate_thenShouldThrowsValidationException() {

        final var expectedName = """
                filefilefilefilefilefilefilefilefilefilefilefilefilefilefilefilefilefile
                """;
        final var expectedContentType = "image/jpeg";

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' must be between 1 and 64 characters.";

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        expectedContentType,
                        BinaryContentID.unique()));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    void givenReservedName_whenCallsCreate_thenShouldThrowsValidationException() {

        final var expectedName = "nul";
        final var expectedContentType = "image/jpeg";

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' cannot be a reserved name: %s".formatted(expectedName);

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        expectedContentType,
                        BinaryContentID.unique()));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    void givenEmptyContentType_whenCallsCreate_thenShouldThrowsValidationException() {

        final var expectedName = "file";
        final var expectedContentType = "";

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'contentType' cannot be empty.";

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        expectedContentType,
                        BinaryContentID.unique()));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenContentTypeWithMoreThan16Chars_whenCallsCreate_thenShouldThrowsValidationException() {

        final var expectedName = "file";
        final String expectedContentType = null;

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'contentType' cannot be null.";

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        expectedContentType,
                        BinaryContentID.unique()));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenMultipleInvalidParams_whenCallsCreate_thenShouldThrowsValidationExceptionWithMultipleErrors() {

        final String expectedName = "nul";
        final String expectedContentType = "";

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 2;
        final var expectedNameErrorMessage = "'name' cannot be a reserved name: %s".formatted(expectedName);
        final var expectedContentTypeErrorMessage = "'contentType' cannot be empty.";

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        expectedContentType,
                        BinaryContentID.unique()));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());

        assertTrue(actualException
                .getErrors().stream().anyMatch(e -> e.message().equals(expectedNameErrorMessage)));
        assertTrue(actualException
                .getErrors().stream().anyMatch(e -> e.message().equals(expectedContentTypeErrorMessage)));
    }

    @Test
    void givenAValidParams_whenCallsUpdate_thenShouldCreateFile() {

        final var expectedName = FileName.of("File");
        final var expectedContentType = "image/jpeg";
        final var expectedContent = BinaryContentID.unique();

        final var aFile = File.create(
                FileName.of("file"),
                "video/mp4",
                expectedContent);

        final var actualUpdatedFile = assertDoesNotThrow(() -> File.with(aFile)
                .update(expectedName,
                        expectedContentType));

        assertEquals(expectedName, actualUpdatedFile.getName());
        assertEquals(expectedContentType, actualUpdatedFile.getContentType());
        assertEquals(expectedContent, actualUpdatedFile.getContent());
        assertNotNull(actualUpdatedFile.getCreatedAt());
        assertNotNull(actualUpdatedFile.getUpdatedAt());
        assertTrue(aFile.getCreatedAt().isBefore(actualUpdatedFile.getUpdatedAt()));
    }

    @Test
    void givenEmptyName_whenCallsUpdate_thenShouldThrowsValidationException() {

        final var expectedName = "";
        final var expectedContentType = "image/jpeg";

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' cannot be empty.";

        final var aFile = File.create(
                FileName.of("file"),
                "video/mp4",
                BinaryContentID.unique());

        final var actualException = assertThrows(ValidationException.class,
                () -> File.with(aFile).update(
                        FileName.of(expectedName),
                        expectedContentType));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenNullName_whenCallsUpdate_thenShouldThrowsValidationException() {

        final String expectedName = null;
        final var expectedContentType = "image/jpeg";

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' cannot be null.";

        final var aFile = File.create(
                FileName.of("file"),
                "video/mp4",
                BinaryContentID.unique());

        final var actualException = assertThrows(ValidationException.class,
                () -> File.with(aFile).update(
                        FileName.of(expectedName),
                        expectedContentType));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    void givenBadCharName_whenCallsUpdate_thenShouldThrowsValidationException() {

        final var expectedName = "/file";
        final var expectedContentType = "image/jpeg";

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' contains invalid characters. Allowed: letters, digits, dots, underscores, and hyphens.";

        final var aFile = File.create(
                FileName.of("file"),
                "video/mp4",
                BinaryContentID.unique());

        final var actualException = assertThrows(ValidationException.class,
                () -> File.with(aFile).update(
                        FileName.of(expectedName),
                        expectedContentType));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    void givenNameWithMoreThan64Chars_whenCallsUpdate_thenShouldThrowsValidationException() {

        final var expectedName = """
                filefilefilefilefilefilefilefilefilefilefilefilefilefilefilefilefilefile
                """;
        final var expectedContentType = "image/jpeg";

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' must be between 1 and 64 characters.";

        final var aFile = File.create(
                FileName.of("file"),
                "video/mp4",
                BinaryContentID.unique());

        final var actualException = assertThrows(ValidationException.class,
                () -> File.with(aFile).update(
                        FileName.of(expectedName),
                        expectedContentType));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    void givenReservedName_whenCallsUpdate_thenShouldThrowsValidationException() {

        final var expectedName = "nul";
        final var expectedContentType = "image/jpeg";

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' cannot be a reserved name: %s".formatted(expectedName);

        final var aFile = File.create(
                FileName.of("file"),
                "video/mp4",
                BinaryContentID.unique());

        final var actualException = assertThrows(ValidationException.class,
                () -> File.with(aFile).update(
                        FileName.of(expectedName),
                        expectedContentType));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    void givenEmptyContentType_whenCallsUpdate_thenShouldThrowsValidationException() {

        final var expectedName = "file";
        final var expectedContentType = "";

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'contentType' cannot be empty.";

        final var aFile = File.create(
                FileName.of("file"),
                "video/mp4",
                BinaryContentID.unique());

        final var actualException = assertThrows(ValidationException.class,
                () -> File.with(aFile).update(
                        FileName.of(expectedName),
                        expectedContentType));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    void givenContentTypeWithMoreThan16Chars_whenCallsUpdate_thenShouldThrowsValidationException() {

        final var expectedName = "file";
        final String expectedContentType = null;

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'contentType' cannot be null.";

        final var aFile = File.create(
                FileName.of("file"),
                "video/mp4",
                BinaryContentID.unique());

        final var actualException = assertThrows(ValidationException.class,
                () -> File.with(aFile).update(
                        FileName.of(expectedName),
                        expectedContentType));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    void givenMultipleInvalidParams_whenCallsUpdate_thenShouldThrowsValidationExceptionWithMultipleErrors() {

        final String expectedName = "nul";
        final String expectedContentType = "";

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 2;
        final var expectedNameErrorMessage = "'name' cannot be a reserved name: %s".formatted(expectedName);
        final var expectedContentTypeErrorMessage = "'contentType' cannot be empty.";

        final var aFile = File.create(
                FileName.of("file"),
                "video/mp4",
                BinaryContentID.unique());

        final var actualException = assertThrows(ValidationException.class,
                () -> File.with(aFile).update(
                        FileName.of(expectedName),
                        expectedContentType));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());

        assertTrue(actualException
                .getErrors().stream().anyMatch(e -> e.message().equals(expectedNameErrorMessage)));
        assertTrue(actualException
                .getErrors().stream().anyMatch(e -> e.message().equals(expectedContentTypeErrorMessage)));
    }

}
