package com.callv2.drive.application.file.content.get;

import java.io.InputStream;

public record GetFileContentOutput(String name, InputStream content, long size) {

    public static GetFileContentOutput with(final String name, final InputStream content, final long size) {
        return new GetFileContentOutput(name, content, size);
    }

}
