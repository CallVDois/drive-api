package com.callv2.drive.infrastructure.member;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.callv2.drive.domain.exception.AlreadyExistsException;
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

        if (this.memberJpaRepository.existsById(member.getId().getValue()))
            throw AlreadyExistsException.with(Member.class, member.getId().getValue());

        return save(member);
    }

    @Override
    public Optional<Member> findById(final MemberID id) {
        return this.memberJpaRepository
                .findById(id.getValue())
                .map(MemberJpaEntity::toDomain);
    }

    @Override
    public Member update(final Member member) {

        if (!this.memberJpaRepository.existsById(member.getId().getValue()))
            throw NotFoundException.with(Member.class, member.getId().getValue());

        return save(member);
    }

    private Member save(final Member member) {
        return memberJpaRepository
                .save(MemberJpaEntity.fromDomain(member))
                .toDomain();
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
