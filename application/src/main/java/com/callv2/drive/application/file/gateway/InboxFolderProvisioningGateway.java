package com.callv2.drive.application.file.gateway;

import com.callv2.drive.domain.folder.entity.Folder;
import com.callv2.drive.domain.member.MemberID;

public interface InboxFolderProvisioningGateway {

    Folder inboxFolder(MemberID member);

}
