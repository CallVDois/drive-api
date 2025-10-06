package com.callv2.drive.domain.access;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.callv2.drive.domain.Identifier;
import com.callv2.drive.domain.ValueObject;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileID;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderID;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.validation.ValidationError;
import com.callv2.drive.domain.validation.ValidationHandler;

public record Resource<I extends Identifier<?>>(I id, ResourceType type, MemberID owner) implements ValueObject {

    public static <I extends Identifier<?>> Resource<I> of(final I id, final ResourceType type, final MemberID owner) {
        return new Resource<>(id, type, owner);
    }

    public Resource<FolderID> folder() {
        if (!ResourceType.FOLDER.equals(this.type))
            throw ValidationException.with(
                    "Resource is not of type FOLDER",
                    ValidationError.with("'type' must be FOLDER when calling folder()"));

        return new Resource<>((FolderID) this.id, this.type, this.owner);
    }

    public Resource<FileID> file() {
        if (!ResourceType.FILE.equals(this.type))
            throw ValidationException.with(
                    "Resource is not of type FILE",
                    ValidationError.with("'type' must be FILE when calling file()"));

        return new Resource<>((FileID) this.id, this.type, this.owner);
    }

    public static Resource<FolderID> folder(final Folder folder) {
        return new Resource<>(folder.getId(), ResourceType.FOLDER, folder.getOwner());
    }

    public static Resource<FileID> file(final File file) {
        return new Resource<>(file.getId(), ResourceType.FILE, file.getOwner());
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(id))
            handler.append(ValidationError.with("'id' cannot be null"));

        if (isNull(type))
            handler.append(ValidationError.with("'type' cannot be null"));

        if (nonNull(id))
            id.validate(handler);

    }

}
