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

        final var updatedMember = Member.with(
                memberId,
                username,
                nickname,
                null,
                null,
                input.createdAt(),
                input.updatedAt(),
                input.synchronizedVersion());

        this.memberGateway
                .findById(memberId)
                .map(member -> member.synchronize(updatedMember))
                .ifPresentOrElse(this::update, () -> memberGateway.create(updatedMember));
    }

    private void update(final Member member) {
        memberGateway.update(member.synchronize(member));
    }

}
