package com.callv2.drive.application.file.content.get;

import java.io.InputStream;

public record GetFileContentOutput(String name, long size, InputStream inputStream) {

    public static GetFileContentOutput with(final String name, final long size, InputStream inputStream) {
        return new GetFileContentOutput(name, size, inputStream);
    }

}
