package com.callv2.drive.infrastructure.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.callv2.drive.application.member.quota.request.create.CreateRequestQuotaInput;
import com.callv2.drive.application.member.quota.request.create.CreateRequestQuotaUseCase;
import com.callv2.drive.domain.member.QuotaUnit;
import com.callv2.drive.infrastructure.api.MemberAPI;
import com.callv2.drive.infrastructure.security.SecurityContext;

@Controller
public class MemberController implements MemberAPI {

    private final CreateRequestQuotaUseCase createRequestQuotaUseCase;

    public MemberController(
            final CreateRequestQuotaUseCase createRequestQuotaUseCase) {
        this.createRequestQuotaUseCase = createRequestQuotaUseCase;
    }

    @Override
    public ResponseEntity<Void> requestQuota(final long amount, final QuotaUnit unit) {

        final String memberId = SecurityContext.getAuthenticatedUser();

        this.createRequestQuotaUseCase.execute(CreateRequestQuotaInput.of(memberId, amount, unit));

        return ResponseEntity.noContent().build();
    }

}
