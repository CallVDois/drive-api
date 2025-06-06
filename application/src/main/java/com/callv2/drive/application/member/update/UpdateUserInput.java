package com.callv2.drive.application.member.update;

public record UpdateUserInput(
        String id,
        String username,
        String nickname) {

    public static UpdateUserInput from(String id, String username, String nickname) {
        return new UpdateUserInput(id, username, nickname);
    }

}