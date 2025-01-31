package com.callv2.drive.application.member.quota.request.approve;

import java.util.Objects;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;

public class DefaultApproveRequestQuotaUseCase extends ApproveRequestQuotaUseCase {

    private final MemberGateway memberGateway;

    public DefaultApproveRequestQuotaUseCase(final MemberGateway memberGateway) {
        this.memberGateway = Objects.requireNonNull(memberGateway);
    }

    @Override
    public void execute(ApproveRequestQuotaInput input) {

        final Member member = memberGateway
                .findById(MemberID.of(input.memberId()))
                .orElseThrow(() -> NotFoundException.with(Member.class, input.memberId()));

        if (input.approved())
            member.approveQuotaRequest();
        else
            member.reproveQuotaRequest();

        memberGateway.update(member);
    }

}
