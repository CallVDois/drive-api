package com.callv2.drive.application.member.quota.retrieve.get;

import com.callv2.drive.domain.exception.NotFoundException;
import com.callv2.drive.domain.file.Content;
import com.callv2.drive.domain.file.File;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.member.Member;
import com.callv2.drive.domain.member.MemberGateway;
import com.callv2.drive.domain.member.MemberID;

public class DefaultGetQuotaUseCase extends GetQuotaUseCase {

    private final MemberGateway memberGateway;
    private final FileGateway fileGateway;

    public DefaultGetQuotaUseCase(final MemberGateway memberGateway, final FileGateway fileGateway) {
        this.memberGateway = memberGateway;
        this.fileGateway = fileGateway;
    }

    @Override
    public GetQuotaOutput execute(GetQuotaInput input) {

        final MemberID ownerId = MemberID.of(input.id());

        final Member owner = memberGateway
                .findById(ownerId)
                .orElseThrow(() -> NotFoundException.with(Member.class, input.id().toString()));

        final Long actualUsedQuota = fileGateway
                .findByOwner(ownerId)
                .stream()
                .map(File::getContent)
                .mapToLong(Content::size)
                .sum();

        final Long available = owner.getQuota().sizeInBytes() - actualUsedQuota;

        return GetQuotaOutput.from(actualUsedQuota, owner.getQuota().sizeInBytes(), available);
    }

}
