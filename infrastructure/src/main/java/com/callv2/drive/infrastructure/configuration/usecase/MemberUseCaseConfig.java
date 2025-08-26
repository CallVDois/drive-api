package com.callv2.drive.infrastructure.configuration.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.drive.application.member.quota.request.approve.ApproveRequestQuotaUseCase;
import com.callv2.drive.application.member.quota.request.approve.DefaultApproveRequestQuotaUseCase;
import com.callv2.drive.application.member.quota.request.create.CreateRequestQuotaUseCase;
import com.callv2.drive.application.member.quota.request.create.DefaultCreateRequestQuotaUseCase;
import com.callv2.drive.application.member.quota.request.list.DefaultListRequestQuotaUseCase;
import com.callv2.drive.application.member.quota.request.list.ListRequestQuotaUseCase;
import com.callv2.drive.application.member.quota.retrieve.get.DefaultGetQuotaUseCase;
import com.callv2.drive.application.member.quota.retrieve.get.GetQuotaUseCase;
import com.callv2.drive.application.member.quota.retrieve.list.DefaultListQuotasUseCase;
import com.callv2.drive.application.member.quota.retrieve.list.ListQuotasUseCase;
import com.callv2.drive.application.member.quota.retrieve.summary.DefaultGetQuotasSummaryUseCase;
import com.callv2.drive.application.member.quota.retrieve.summary.GetQuotasSummaryUseCase;
import com.callv2.drive.application.member.synchronize.DefaultSynchronizeMemberUseCase;
import com.callv2.drive.application.member.synchronize.SynchronizeMemberUseCase;
import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.member.MemberGateway;

@Configuration
public class MemberUseCaseConfig {

    private final MemberGateway memberGateway;
    private final FileGateway fileGateway;

    public MemberUseCaseConfig(final MemberGateway memberGateway, final FileGateway fileGateway) {
        this.memberGateway = memberGateway;
        this.fileGateway = fileGateway;
    }

    @Bean
    CreateRequestQuotaUseCase createRequestQuotaUseCase() {
        return new DefaultCreateRequestQuotaUseCase(memberGateway);
    }

    @Bean
    ApproveRequestQuotaUseCase approveRequestQuotaUseCase() {
        return new DefaultApproveRequestQuotaUseCase(memberGateway);
    }

    @Bean
    ListRequestQuotaUseCase listRequestQuotaUseCase() {
        return new DefaultListRequestQuotaUseCase(memberGateway);
    }

    @Bean
    GetQuotaUseCase getQuotaUseCase() {
        return new DefaultGetQuotaUseCase(memberGateway, fileGateway);
    }

    @Bean
    SynchronizeMemberUseCase synchronizeMemberUseCase() {
        return new DefaultSynchronizeMemberUseCase(memberGateway);
    }

    @Bean
    ListQuotasUseCase listQuotasUseCase() {
        return new DefaultListQuotasUseCase(memberGateway);
    }

    @Bean
    GetQuotasSummaryUseCase getQuotasSummaryUseCase() {
        return new DefaultGetQuotasSummaryUseCase(memberGateway, fileGateway);
    }

}
