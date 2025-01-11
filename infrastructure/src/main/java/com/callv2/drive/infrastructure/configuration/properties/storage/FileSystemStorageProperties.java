package com.callv2.drive.infrastructure.configuration.properties.storage;

public class FileSystemStorageProperties {

    private String location = "upload-dir";

    public FileSystemStorageProperties() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
