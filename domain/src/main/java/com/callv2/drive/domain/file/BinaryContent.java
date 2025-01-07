package com.callv2.drive.domain.file;

import com.callv2.drive.domain.ValueObject;

public record BinaryContent(byte[] bytes) implements ValueObject {

    public static BinaryContent of(final byte[] bytes) {
        return new BinaryContent(bytes);
    }

    @Override
    public byte[] bytes() {
        return bytes == null ? new byte[0] : bytes;
    }

}