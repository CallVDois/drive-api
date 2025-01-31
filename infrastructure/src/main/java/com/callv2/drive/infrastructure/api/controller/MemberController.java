package com.callv2.drive.infrastructure.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.callv2.drive.application.member.quota.request.create.CreateRequestQuotaInput;
import com.callv2.drive.application.member.quota.request.create.CreateRequestQuotaUseCase;
import com.callv2.drive.application.member.quota.retrieve.get.GetQuotaInput;
import com.callv2.drive.application.member.quota.retrieve.get.GetQuotaUseCase;
import com.callv2.drive.domain.member.QuotaUnit;
import com.callv2.drive.infrastructure.api.MemberAPI;
import com.callv2.drive.infrastructure.member.model.MemberQuotaResponse;
import com.callv2.drive.infrastructure.member.presenter.MemberPresenter;
import com.callv2.drive.infrastructure.security.SecurityContext;

@Controller
public class MemberController implements MemberAPI {

    private final CreateRequestQuotaUseCase createRequestQuotaUseCase;
    private final GetQuotaUseCase getQuotaUseCase;

    public MemberController(
            final CreateRequestQuotaUseCase createRequestQuotaUseCase,
            final GetQuotaUseCase getQuotaUseCase) {
        this.createRequestQuotaUseCase = createRequestQuotaUseCase;
        this.getQuotaUseCase = getQuotaUseCase;
    }

    @Override
    public ResponseEntity<Void> requestQuota(final long amount, final QuotaUnit unit) {

        final String memberId = SecurityContext.getAuthenticatedUser();

        this.createRequestQuotaUseCase.execute(CreateRequestQuotaInput.of(memberId, amount, unit));

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<MemberQuotaResponse> getQuota() {
        return ResponseEntity
                .ok(MemberPresenter.present(
                        this.getQuotaUseCase.execute(GetQuotaInput.of(SecurityContext.getAuthenticatedUser()))));
    }

}
