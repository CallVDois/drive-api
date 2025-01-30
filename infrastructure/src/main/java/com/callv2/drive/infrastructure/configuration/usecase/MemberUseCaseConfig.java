package com.callv2.drive.infrastructure.configuration.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.callv2.drive.application.member.quota.request.approve.ApproveRequestQuotaUseCase;
import com.callv2.drive.application.member.quota.request.approve.DefaultApproveRequestQuotaUseCase;
import com.callv2.drive.application.member.quota.request.create.CreateRequestQuotaUseCase;
import com.callv2.drive.application.member.quota.request.create.DefaultCreateRequestQuotaUseCase;
import com.callv2.drive.application.member.quota.request.list.DefaultListRequestQuotaUseCase;
import com.callv2.drive.application.member.quota.request.list.ListRequestQuotaUseCase;
import com.callv2.drive.domain.member.MemberGateway;

@Configuration
public class MemberUseCaseConfig {

    private final MemberGateway memberGateway;

    public MemberUseCaseConfig(final MemberGateway memberGateway) {
        this.memberGateway = memberGateway;
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

}
