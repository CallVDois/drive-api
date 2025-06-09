package com.callv2.drive.infrastructure.member.presenter;

import com.callv2.drive.application.member.quota.request.list.ListRequestQuotaOutput;
import com.callv2.drive.application.member.quota.retrieve.get.GetQuotaOutput;
import com.callv2.drive.infrastructure.member.model.MemberQuotaResponse;
import com.callv2.drive.infrastructure.member.model.QuotaRequestListResponse;

public interface MemberPresenter {

    static QuotaRequestListResponse present(final ListRequestQuotaOutput output) {
        return new QuotaRequestListResponse(
                new QuotaRequestListResponse.Member(
                        output.memberId(),
                        output.memberUsername()),
                output.quotaAmount(),
                output.quotaUnit(),
                output.quotaRequestedAt());
    }

    static MemberQuotaResponse present(final GetQuotaOutput output) {
        return new MemberQuotaResponse(output.memberId(), output.used(), output.total(), output.available());
    }

}
