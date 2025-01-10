package com.callv2.drive.domain.file;

import com.callv2.drive.domain.Entity;
import com.callv2.drive.domain.validation.ValidationHandler;

public class BinaryContent extends Entity<BinaryContentID> {

    private byte[] bytes;

    private BinaryContent(final BinaryContentID id, final byte[] bytes) {
        super(id);
        this.bytes = bytes;
    }

    @Override
    public void validate(ValidationHandler handler) {
        return; // TODO verify if it's necessary to validate BinaryContent
    }

    public static BinaryContent create(final byte[] bytes) {
        return new BinaryContent(BinaryContentID.unique(), bytes);
    }

    public static BinaryContent with(final BinaryContentID id, final byte[] bytes) {
        return new BinaryContent(id, bytes);
    }

    public static BinaryContent with(final BinaryContent content) {
        return BinaryContent.with(content.getId(), content.bytes());
    }

    public byte[] bytes() {
        return bytes == null ? new byte[0] : bytes;
    }

}