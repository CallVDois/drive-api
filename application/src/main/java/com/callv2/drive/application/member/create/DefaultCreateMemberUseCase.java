package com.callv2.drive.application.member.create;

import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.member.Nickname;
import com.callv2.drive.domain.member.Username;
import com.callv2.drive.domain.validation.handler.Notification;

public class DefaultCreateMemberUseCase extends CreateMemberUseCase {

    private final MemberGateway memberGateway;

    public DefaultCreateMemberUseCase(MemberGateway memberGateway) {
        this.memberGateway = memberGateway;
    }

    @Override
    public void execute(CreateMemberInput input) {

        final MemberID id = MemberID.of(input.id());
        final Nickname nickname = Nickname.of(input.nickname());
        final Username username = Username.of(input.username());

        final Member member = Member.create(id, nickname, username);

        final Notification notification = Notification.create();
        member.validate(notification);

        if (notification.hasError())
            throw ValidationException.with("Validation error", notification);

        this.memberGateway.create(member);
    }

}