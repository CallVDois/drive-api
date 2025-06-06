package com.callv2.drive.application.member.update;

import java.util.Objects;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.Username;
import com.callv2.drive.domain.member.Nickname;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;

public class DefaultUpdateMemberUseCase extends UpdateMemberUseCase {

    private final MemberGateway memberGateway;

    public DefaultUpdateMemberUseCase(final MemberGateway memberGateway) {
        this.memberGateway = Objects.requireNonNull(memberGateway);
    }

    @Override
    public void execute(final UpdateUserInput input) {

        MemberID memberId = MemberID.of(input.id());
        Username username = Username.of(input.username());
        Nickname nickname = Nickname.of(input.nickname());

        final Member member = memberGateway.findById(memberId)
                .orElseThrow(() -> NotFoundException.with(Member.class, memberId.getValue()));

        member.changeUsername(username);
        member.changeNickname(nickname);

        memberGateway.update(member);
    }

}