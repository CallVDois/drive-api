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
        final var expectedContentLocation = "location";
        final var expectedContentSize = 109L;
        final var content = Content.of(expectedContentLocation, expectedContentType, expectedContentSize);

        final var actualFile = assertDoesNotThrow(() -> File.create(FileName.of(expectedName), content));

        assertEquals(expectedName, actualFile.getName().value());
        assertEquals(expectedContentType, actualFile.getContent().type());
        assertEquals(expectedContentLocation, actualFile.getContent().location());
        assertEquals(expectedContentSize, actualFile.getContent().size());
        assertNotNull(actualFile.getCreatedAt());
        assertNotNull(actualFile.getUpdatedAt());
        assertEquals(actualFile.getCreatedAt(), actualFile.getUpdatedAt());
    }

    @Test
    void givenEmptyName_whenCallsCreate_thenShouldThrowsValidationException() {

        final var expectedName = "";

        final var expectedContentType = "image/jpeg";
        final var expectedContentLocation = "location";
        final var expectedContentSize = 109L;
        final var content = Content.of(expectedContentLocation, expectedContentType, expectedContentSize);

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' cannot be empty.";

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(FileName.of(expectedName), content));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenNullName_whenCallsCreate_thenShouldThrowsValidationException() {

        final String expectedName = null;

        final var expectedContentType = "image/jpeg";
        final var expectedContentLocation = "location";
        final var expectedContentSize = 109L;
        final var content = Content.of(expectedContentLocation, expectedContentType, expectedContentSize);

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' cannot be null.";

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        content));

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
        final var expectedContentLocation = "location";
        final var expectedContentSize = 109L;
        final var content = Content.of(expectedContentLocation, expectedContentType, expectedContentSize);

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' must be between 1 and 64 characters.";

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        content));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    void givenReservedName_whenCallsCreate_thenShouldThrowsValidationException() {

        final var expectedName = "nul";

        final var expectedContentType = "image/jpeg";
        final var expectedContentLocation = "location";
        final var expectedContentSize = 109L;
        final var content = Content.of(expectedContentLocation, expectedContentType, expectedContentSize);

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' cannot be a reserved name: %s".formatted(expectedName);

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        content));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    void givenMultipleInvalidParams_whenCallsCreate_thenShouldThrowsValidationExceptionWithMultipleErrors() {

        final String expectedName = "nul";

        final var expectedContentType = "image/jpeg";
        final var expectedContentLocation = "location";
        final var expectedContentSize = 109L;
        final var content = Content.of(expectedContentLocation, expectedContentType, expectedContentSize);

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedNameErrorMessage = "'name' cannot be a reserved name: %s".formatted(expectedName);

        final var actualException = assertThrows(
                ValidationException.class,
                () -> File.create(
                        FileName.of(expectedName),
                        content));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());

        assertTrue(actualException
                .getErrors().stream().anyMatch(e -> e.message().equals(expectedNameErrorMessage)));
    }

    @Test
    void givenAValidParams_whenCallsUpdate_thenShouldCreateFile() {

        final var expectedName = FileName.of("File");

        final var expectedContentType = "image/jpeg";
        final var expectedContentLocation = "location";
        final var expectedContentSize = 109L;
        final var content = Content.of(expectedContentLocation, expectedContentType, expectedContentSize);

        final var aFile = File.create(
                FileName.of("file"),
                Content.of("loc", "typo", 0));

        final var actualUpdatedFile = assertDoesNotThrow(() -> File.with(aFile).update(expectedName, content));

        assertEquals(expectedName, actualUpdatedFile.getName());
        assertEquals(expectedContentType, actualUpdatedFile.getContent().type());
        assertEquals(expectedContentLocation, actualUpdatedFile.getContent().location());
        assertEquals(expectedContentSize, actualUpdatedFile.getContent().size());
        assertNotNull(actualUpdatedFile.getCreatedAt());
        assertNotNull(actualUpdatedFile.getUpdatedAt());
        assertTrue(aFile.getCreatedAt().isBefore(actualUpdatedFile.getUpdatedAt()));
    }

    @Test
    void givenEmptyName_whenCallsUpdate_thenShouldThrowsValidationException() {

        final var expectedName = "";

        final var expectedContentType = "image/jpeg";
        final var expectedContentLocation = "location";
        final var expectedContentSize = 109L;
        final var content = Content.of(expectedContentLocation, expectedContentType, expectedContentSize);

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' cannot be empty.";

        final var aFile = File.create(FileName.of("file"), content);

        final var actualException = assertThrows(ValidationException.class,
                () -> File.with(aFile).update(FileName.of(expectedName), content));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    void givenNullName_whenCallsUpdate_thenShouldThrowsValidationException() {

        final String expectedName = null;

        final var expectedContentType = "image/jpeg";
        final var expectedContentLocation = "location";
        final var expectedContentSize = 109L;
        final var content = Content.of(expectedContentLocation, expectedContentType, expectedContentSize);

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' cannot be null.";

        final var aFile = File.create(FileName.of("file"), content);

        final var actualException = assertThrows(ValidationException.class,
                () -> File.with(aFile).update(FileName.of(expectedName), content));

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
        final var expectedContentLocation = "location";
        final var expectedContentSize = 109L;
        final var content = Content.of(expectedContentLocation, expectedContentType, expectedContentSize);

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' must be between 1 and 64 characters.";

        final var aFile = File.create(FileName.of("file"), content);

        final var actualException = assertThrows(ValidationException.class,
                () -> File.with(aFile).update(FileName.of(expectedName), content));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    void givenReservedName_whenCallsUpdate_thenShouldThrowsValidationException() {

        final var expectedName = "nul";

        final var expectedContentType = "image/jpeg";
        final var expectedContentLocation = "location";
        final var expectedContentSize = 109L;
        final var content = Content.of(expectedContentLocation, expectedContentType, expectedContentSize);

        final var expectedExceptionMessage = "Validation fail has occoured";
        final var expectedErrorsCount = 1;
        final var expectedErrorMessage = "'name' cannot be a reserved name: %s".formatted(expectedName);

        final var aFile = File.create(FileName.of("file"), content);

        final var actualException = assertThrows(ValidationException.class,
                () -> File.with(aFile).update(FileName.of(expectedName), content));

        assertEquals(expectedExceptionMessage, actualException.getMessage());
        assertEquals(expectedErrorsCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

}
