package com.callv2.drive.application.member.quota.retrieve.summary;

import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.member.MemberGateway;

public class DefaultGetQuotasSummaryUseCase extends GetQuotasSummaryUseCase {

    private final MemberGateway memberGateway;
    private final FileGateway fileGateway;

    public DefaultGetQuotasSummaryUseCase(
            final MemberGateway memberGateway,
            final FileGateway fileGateway) {
        this.memberGateway = memberGateway;
        this.fileGateway = fileGateway;
    }

    @Override
    public GetQuotasSummaryOutput execute() {

        final Long totalMembers = this.memberGateway.count();
        final Long totalAllocatedQuota = this.memberGateway.sumAllQuota();
        final Long totalUsedStorage = this.fileGateway.sumAllContentSize();

        return new GetQuotasSummaryOutput(
                totalMembers,
                totalAllocatedQuota,
                totalUsedStorage,
                totalAllocatedQuota - totalUsedStorage);

    }

}
