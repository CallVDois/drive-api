package com.callv2.drive.infrastructure.member.presenter;

import com.callv2.drive.application.member.quota.request.list.RequestQuotaListOutput;
import com.callv2.drive.application.member.quota.retrieve.get.GetQuotaOutput;
import com.callv2.drive.infrastructure.member.model.MemberQuotaResponse;
import com.callv2.drive.infrastructure.member.model.QuotaRequestListResponse;

public interface MemberPresenter {

    static QuotaRequestListResponse present(RequestQuotaListOutput output) {
        return new QuotaRequestListResponse(
                output.memberId(),
                output.amount(),
                output.unit(),
                output.requestedAt());
    }

    static MemberQuotaResponse present(GetQuotaOutput output) {
        return new MemberQuotaResponse(output.used(), output.total(), output.available());
    }

}
