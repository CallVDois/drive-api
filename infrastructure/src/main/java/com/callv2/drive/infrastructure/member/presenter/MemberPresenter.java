package com.callv2.drive.infrastructure.member.presenter;

import com.callv2.drive.application.member.quota.request.list.RequestQuotaListOutput;
import com.callv2.drive.infrastructure.member.model.QuotaRequestListResponse;

public interface MemberPresenter {

    static QuotaRequestListResponse present(RequestQuotaListOutput output) {
        return new QuotaRequestListResponse(
                output.memberId(),
                output.amount(),
                output.unit(),
                output.requestedAt());
    }

}
