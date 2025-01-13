package com.callv2.drive.application.file.create;

import java.io.InputStream;

public record CreateFileInput(String name, String contentType, InputStream content, long size) {

    public static CreateFileInput of(String name, String contentType, InputStream content, long size) {
        return new CreateFileInput(name, contentType, content, size);
    }

}
