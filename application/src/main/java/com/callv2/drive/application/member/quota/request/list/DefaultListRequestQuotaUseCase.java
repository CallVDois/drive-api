package com.callv2.drive.application.member.quota.request.list;

import java.util.Objects;

import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.pagination.Pagination;
import com.callv2.drive.domain.pagination.SearchQuery;

public class DefaultListRequestQuotaUseCase extends ListRequestQuotaUseCase {

    private final MemberGateway memberGateway;

    public DefaultListRequestQuotaUseCase(final MemberGateway memberGateway) {
        this.memberGateway = Objects.requireNonNull(memberGateway);
    }

    @Override
    public Pagination<RequestQuotaListOutput> execute(final SearchQuery searchQuery) {
        return memberGateway
                .findAllQuotaRequests(searchQuery)
                .map(RequestQuotaListOutput::from);
    }

}
