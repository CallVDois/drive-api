package com.callv2.drive.domain.folder;

import com.callv2.drive.domain.ValueObject;

public record SubFolder(FolderID id, FolderName name) implements ValueObject {

    public static SubFolder with(final FolderID id, final FolderName name) {
        return new SubFolder(id, name);
    }

    public static SubFolder from(final Folder folder) {
        return new SubFolder(folder.getId(), folder.getName());
    }

}
