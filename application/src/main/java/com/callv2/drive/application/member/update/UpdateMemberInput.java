package com.callv2.drive.application.member.update;

public record UpdateMemberInput(
        String id,
        String username,
        String nickname) {

    public static UpdateMemberInput from(String id, String username, String nickname) {
        return new UpdateMemberInput(id, username, nickname);
    }

}