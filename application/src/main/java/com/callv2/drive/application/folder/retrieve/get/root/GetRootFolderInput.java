package com.callv2.drive.application.folder.retrieve.get.root;
public record GetRootFolderInput(String ownerId) {

    public static GetRootFolderInput from(final String ownerId) {
        return new GetRootFolderInput(ownerId);
    }

}
