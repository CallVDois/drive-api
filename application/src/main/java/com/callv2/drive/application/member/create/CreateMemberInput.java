package com.callv2.drive.application.member.create;

public record CreateMemberInput(
        String id,
        String username,
        String nickname) {

    public static CreateMemberInput from(
            final String id,
            final String username,
            final String nickname) {
        return new CreateMemberInput(id, username, nickname);
    }

}
