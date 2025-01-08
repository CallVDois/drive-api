package com.callv2.drive.domain.file;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class BinaryContentTest {

    @Test
    void givenABytes_whenCallsBytes_thenReturnsBytes() {
        final byte[] expectedBytes = new byte[] { 1, 2, 3, 4, 5 };

        final var aContent = BinaryContent.of(expectedBytes);

        final var actualBytes = aContent.bytes();

        assertNotNull(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    void givenANullBytes_whenCallsBytes_thenReturnsNonNullByteArray() {
        final byte[] expectedBytes = new byte[] {};

        final var aContent = BinaryContent.of(null);

        final var actualBytes = aContent.bytes();

        assertNotNull(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
    }

}
