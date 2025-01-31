package com.callv2.drive.domain.member;

import java.util.Optional;

import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.SearchQuery;

public interface MemberGateway {

    Member create(Member member);

    Optional<Member> findById(MemberID id);

    Member update(Member member);

    Page<QuotaRequestPreview> findAllQuotaRequests(final SearchQuery searchQuery);

}
