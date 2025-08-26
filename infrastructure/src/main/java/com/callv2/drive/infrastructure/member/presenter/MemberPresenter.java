package com.callv2.drive.infrastructure.member.presenter;

import com.callv2.drive.application.member.quota.request.list.ListRequestQuotaOutput;
import com.callv2.drive.application.member.quota.retrieve.get.GetQuotaOutput;
import com.callv2.drive.application.member.quota.retrieve.list.ListQuotaOutput;
import com.callv2.drive.application.member.quota.retrieve.summary.GetQuotasSummaryOutput;
import com.callv2.drive.infrastructure.member.model.MemberQuotaListResponse;
import com.callv2.drive.infrastructure.member.model.MemberQuotaResponse;
import com.callv2.drive.infrastructure.member.model.QuotaRequestListResponse;
import com.callv2.drive.infrastructure.member.model.QuotaSummaryResponse;

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
        return new MemberQuotaResponse(
                output.memberId(),
                output.username(),
                output.used(),
                output.total(),
                output.available());
    }

    static MemberQuotaListResponse present(final ListQuotaOutput output) {
        return new MemberQuotaListResponse(
                output.memberId(),
                output.username(),
                output.total());
    }

    static QuotaSummaryResponse present(final GetQuotasSummaryOutput output) {
        return new QuotaSummaryResponse(
                output.membersCount(),
                output.totalAllocatedQuota(),
                output.totalUsedQuota(),
                output.totalAvailableQuota());
    }

}
