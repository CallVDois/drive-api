package com.callv2.drive.infrastructure.member;

import java.util.Optional;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;
import com.callv2.drive.domain.member.QuotaRequestPreview;
import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.SearchQuery;
import com.callv2.drive.infrastructure.filter.adapter.QueryAdapter;
import com.callv2.drive.infrastructure.member.persistence.MemberJpaEntity;
import com.callv2.drive.infrastructure.member.persistence.MemberJpaRepository;

@Component
public class DefaultMemberGateway implements MemberGateway {

    private final MemberJpaRepository memberJpaRepository;

    public DefaultMemberGateway(final MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public Member create(final Member member) {
        return this.memberJpaRepository.save(MemberJpaEntity.fromDomain(member)).toDomain();
    }

    @Override
    public Optional<Member> findById(final MemberID id) {
        return this.memberJpaRepository
                .findById(id.getValue())
                .map(MemberJpaEntity::toDomain);
    }

    @Transactional
    @Override
    public Member update(final Member member) {

        if (!this.memberJpaRepository.existsById(member.getId().getValue()))
            throw NotFoundException.with(Member.class, member.getId().getValue());

        final MemberJpaEntity memberJpa = MemberJpaEntity.fromDomain(member);
        final Integer rowsUpdated = memberJpaRepository.update(
                memberJpa.getId(),
                memberJpa.getUsername(),
                memberJpa.getNickname(),
                memberJpa.getQuotaAmmount(),
                memberJpa.getQuotaUnit(),
                memberJpa.getQuotaRequestAmmount(),
                memberJpa.getQuotaRequestUnit(),
                memberJpa.getQuotaRequestedAt(),
                memberJpa.getCreatedAt(),
                memberJpa.getUpdatedAt(),
                memberJpa.getSynchronizedVersion());

        if (rowsUpdated != 1)
            throw new OptimisticLockingFailureException(
                    "Member update failed due to version conflict for id: " + member.getId().getValue());

        return member;
    }

    @Override
    public Page<QuotaRequestPreview> findAllQuotaRequests(SearchQuery searchQuery) {

        final org.springframework.data.domain.Page<QuotaRequestPreview> pageResult = this.memberJpaRepository
                .findAllQuotaRequests(QueryAdapter.of(searchQuery.pagination()));

        return new Page<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalPages(),
                pageResult.getTotalElements(),
                pageResult.toList());
    }

    @Override
    public Boolean existsById(MemberID id) {
        return this.memberJpaRepository.existsById(id.getValue());
    }

}
