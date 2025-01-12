package com.callv2.drive.application.file.create;

import java.io.InputStream;

public record CreateFileInput(String name, String contentType, InputStream content) {

}
