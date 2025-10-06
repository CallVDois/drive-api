package com.callv2.drive.application.file.sharing.remove;

import java.util.UUID;

public record RemoveFileSharingInput(
        UUID fileId,
        UUID revoker,
        UUID revokedMember) {

}
