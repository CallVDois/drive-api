package com.callv2.drive.application.file.retrieve.get;

public record GetFileInput(String id) {

    public static GetFileInput from(String id) {
        return new GetFileInput(id);
    }

}
