package com.callv2.drive.application.file.content.get;

public record GetFileContentOutput(String name, String location, long size) {

    public static GetFileContentOutput with(final String name, final String location, final long size) {
        return new GetFileContentOutput(name, location, size);
    }

}
