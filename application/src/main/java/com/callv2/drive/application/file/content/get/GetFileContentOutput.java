package com.callv2.drive.application.file.content.get;

import java.io.InputStream;

public record GetFileContentOutput(String name, InputStream content) {

    public static GetFileContentOutput with(final String name, final InputStream content) {
        return new GetFileContentOutput(name, content);
    }

}
