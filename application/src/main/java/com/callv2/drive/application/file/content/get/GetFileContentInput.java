package com.callv2.drive.application.file.content.get;

public record GetFileContentInput(String fileId) {

    public static GetFileContentInput with(final String fileId) {
        return new GetFileContentInput(fileId);
    }

}
