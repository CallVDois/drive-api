package com.callv2.drive.infrastructure.member.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.callv2.drive.domain.member.QuotaRequestPreview;

public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, String> {

    @Query("""
            select distinct new com.callv2.drive.domain.member.QuotaRequestPreview(
                m.id as memberId,
                m.quotaRequestAmmount as amount,
                m.quotaRequestUnit as unit,
                m.quotaRequestedAt as requestedAt
            )
            from Member m
            where
                m.quotaRequestedAt is not null
            """)
    Page<QuotaRequestPreview> findAllQuotaRequests(Pageable page);

}
