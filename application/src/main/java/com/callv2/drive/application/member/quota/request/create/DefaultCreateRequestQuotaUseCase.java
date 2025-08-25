package com.callv2.drive.application.member.quota.request.create;

import java.util.Objects;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.exception.ValidationException;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.member.Quota;
import com.callv2.drive.domain.validation.handler.Notification;

public class DefaultCreateRequestQuotaUseCase extends CreateRequestQuotaUseCase {

    private final MemberGateway memberGateway;

    public DefaultCreateRequestQuotaUseCase(final MemberGateway memberGateway) {
        this.memberGateway = Objects.requireNonNull(memberGateway);
    }

    @Override
    public void execute(final CreateRequestQuotaInput input) {

        final MemberID memberId = MemberID.of(input.memberId());

        final Member member = memberGateway
                .findById(memberId)
                .orElseThrow(() -> NotFoundException.with(Member.class, input.memberId()));

        final Notification notification = Notification.create();
        notification.validate(() -> member.requestQuota(Quota.of(input.ammount(), input.unit())));

        if (notification.hasError())
            throw ValidationException.with("Request Quota Error", notification);

        memberGateway.update(member);
    }

}
