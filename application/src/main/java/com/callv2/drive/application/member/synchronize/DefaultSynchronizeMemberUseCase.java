package com.callv2.drive.application.member.synchronize;

import java.util.Objects;

import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.member.Nickname;
import com.callv2.drive.domain.member.Username;

public class DefaultSynchronizeMemberUseCase extends SynchronizeMemberUseCase {

    private final MemberGateway memberGateway;

    public DefaultSynchronizeMemberUseCase(final MemberGateway memberGateway) {
        this.memberGateway = Objects.requireNonNull(memberGateway);
    }

    @Override
    public void execute(final SynchronizeMemberInput input) {

        final MemberID memberId = MemberID.of(input.id());
        final Username username = Username.of(input.username());
        final Nickname nickname = Nickname.of(input.nickname());

        final Member member = this.memberGateway
                .findById(memberId)
                .orElse(Member.create(
                        memberId,
                        username,
                        nickname,
                        input.createdAt(),
                        input.updatedAt(),
                        input.synchronizedVersion()));

        memberGateway.update(member.synchronize(member));
    }

}
