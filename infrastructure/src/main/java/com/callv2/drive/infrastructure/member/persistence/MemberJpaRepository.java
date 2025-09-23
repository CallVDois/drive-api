package com.callv2.drive.infrastructure.member.persistence;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.callv2.drive.domain.member.QuotaRequestPreview;
import com.callv2.drive.domain.member.QuotaUnit;

public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, String> {

    Page<MemberJpaEntity> findAll(Specification<MemberJpaEntity> whereClause, Pageable page);

    @Query("""
            select distinct new com.callv2.drive.domain.member.QuotaRequestPreview(
                m.id as memberId,
                m.username as memberUsername,
                m.nickname as memberNickname,
                m.quotaRequestAmount as quotaAmount,
                m.quotaRequestUnit as quotaUnit,
                m.quotaRequestedAt as quotaRequestedAt
            )
            from Member m
            where
                m.quotaRequestedAt is not null
            """)
    Page<QuotaRequestPreview> findAllQuotaRequests(Pageable page);

    @Modifying
    @Query("""
            update Member m
            set
                m.username = :username,
                m.nickname = :nickname,
                m.quotaAmount = :quotaAmount,
                m.quotaUnit = :quotaUnit,
                m.quotaRequestAmount = :quotaRequestAmount,
                m.quotaRequestUnit = :quotaRequestUnit,
                m.quotaRequestedAt = :quotaRequestedAt,
                m.createdAt = :createdAt,
                m.updatedAt = :updatedAt,
                m.synchronizedVersion = :synchronizedVersion
            where m.id = :id
            and (m.synchronizedVersion is null or :synchronizedVersion >= m.synchronizedVersion)
            """)
    Integer update(
            @Param("id") String id,
            @Param("username") String username,
            @Param("nickname") String nickname,
            @Param("quotaAmount") Long quotaAmount,
            @Param("quotaUnit") QuotaUnit quotaUnit,
            @Param("quotaRequestAmount") Long quotaRequestAmount,
            @Param("quotaRequestUnit") QuotaUnit quotaRequestUnit,
            @Param("quotaRequestedAt") Instant quotaRequestedAt,
            @Param("createdAt") Instant createdAt,
            @Param("updatedAt") Instant updatedAt,
            @Param("synchronizedVersion") Long synchronizedVersion);

    @Query("select coalesce(sum(m.quotaInBytes), 0) from Member m")
    Long sumAllQuota();

}
