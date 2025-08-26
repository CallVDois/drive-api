package com.callv2.drive.application.member.quota.retrieve.list;

import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.SearchQuery;

public class DefaultListQuotasUseCase extends ListQuotasUseCase {

    private final MemberGateway memberGateway;

    public DefaultListQuotasUseCase(final MemberGateway memberGateway) {
        this.memberGateway = memberGateway;
    }

    @Override
    public Page<ListQuotaOutput> execute(final SearchQuery query) {

        return memberGateway
                .findAll(query)
                .map(ListQuotaOutput::of);

    }

}
