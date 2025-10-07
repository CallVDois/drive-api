package com.callv2.drive.application.acl;

import com.callv2.drive.domain.access.Acl;
import com.callv2.drive.domain.access.AclGateway;
import com.callv2.drive.domain.access.AclID;
import com.callv2.drive.domain.access.Resource;
import com.callv2.drive.domain.access.ResourceType;
import com.callv2.drive.domain.event.EventDispatcher;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.folder.Folder;
import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.folder.FolderID;

public class DefaultRecalculateAclInheritanceUseCase extends RecalculateAclInheritanceUseCase {

    private final EventDispatcher eventDispatcher;

    private final AclGateway aclGateway;
    private final FileGateway fileGateway;
    private final FolderGateway folderGateway;

    public DefaultRecalculateAclInheritanceUseCase(
            final EventDispatcher eventDispatcher,
            final AclGateway aclGateway,
            final FileGateway fileGateway,
            final FolderGateway folderGateway) {
        this.eventDispatcher = eventDispatcher;
        this.aclGateway = aclGateway;
        this.fileGateway = fileGateway;
        this.folderGateway = folderGateway;
    }

    @Override
    public void execute(final RecalculateAclInheritanceInput input) {

        final Acl parentAcl = aclGateway
                .findById(AclID.of(input.parentAclId()))
                .orElseThrow(); // TODO domain exception

        if (ResourceType.FILE.equals(parentAcl.getResource().type()))
            return; // nothing to do for files

        final Resource<FolderID> parentResource = parentAcl.getResource().folder();

        fileGateway
                .findAllByFolder(parentResource.id())
                .forEach(file -> recalculateFileAcl(file, parentAcl));

        folderGateway
                .findByParentFolderId(parentResource.id())
                .forEach(folder -> recalculateFolderAcl(folder, parentAcl));
    }

    private void recalculateFileAcl(final File file, final Acl parentAcl) {
        final Acl fileAcl = aclGateway
                .findByResource(Resource.file(file))
                .orElseThrow(); // TODO throws?? domain exception

        final Acl updatedFileAcl = fileAcl.inheritFrom(parentAcl);
        eventDispatcher.notify(aclGateway.update(updatedFileAcl));
    }

    private void recalculateFolderAcl(final Folder folder, final Acl parentAcl) {
        final Acl folderAcl = aclGateway
                .findByResource(Resource.folder(folder))
                .orElseThrow(); // TODO throws?? domain exception'

        final Acl updatedFolderAcl = folderAcl.inheritFrom(parentAcl);
        eventDispatcher.notify(aclGateway.update(updatedFolderAcl));
    }

}
