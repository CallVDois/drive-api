package com.callv2.drive.infrastructure.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.callv2.drive.application.member.quota.request.approve.ApproveRequestQuotaInput;
import com.callv2.drive.application.member.quota.request.approve.ApproveRequestQuotaUseCase;
import com.callv2.drive.application.member.quota.request.create.CreateRequestQuotaInput;
import com.callv2.drive.application.member.quota.request.create.CreateRequestQuotaUseCase;
import com.callv2.drive.application.member.quota.request.list.ListRequestQuotaUseCase;
import com.callv2.drive.domain.member.QuotaUnit;
import com.callv2.drive.domain.pagination.Pagination;
import com.callv2.drive.domain.pagination.SearchQuery;
import com.callv2.drive.domain.pagination.SearchQuery.Order.Direction;
import com.callv2.drive.infrastructure.api.MemberAPI;
import com.callv2.drive.infrastructure.member.model.QuotaRequestListResponse;
import com.callv2.drive.infrastructure.member.presenter.MemberPresenter;
import com.callv2.drive.infrastructure.security.SecurityContext;

@Controller
public class MemberController implements MemberAPI {

    private final CreateRequestQuotaUseCase createRequestQuotaUseCase;
    private final ApproveRequestQuotaUseCase approveRequestQuotaUseCase;
    private final ListRequestQuotaUseCase listRequestQuotaUseCase;

    public MemberController(
            final CreateRequestQuotaUseCase createRequestQuotaUseCase,
            final ApproveRequestQuotaUseCase approveRequestQuotaUseCase,
            final ListRequestQuotaUseCase listRequestQuotaUseCase) {
        this.createRequestQuotaUseCase = createRequestQuotaUseCase;
        this.approveRequestQuotaUseCase = approveRequestQuotaUseCase;
        this.listRequestQuotaUseCase = listRequestQuotaUseCase;
    }

    @Override
    public ResponseEntity<Void> requestQuota(final long amount, final QuotaUnit unit) {

        final String memberId = SecurityContext.getAuthenticatedUser();

        this.createRequestQuotaUseCase.execute(CreateRequestQuotaInput.of(memberId, amount, unit));

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> approveQuotaRequest(final String id, final boolean approved) {
        this.approveRequestQuotaUseCase.execute(ApproveRequestQuotaInput.of(id, approved));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Pagination<QuotaRequestListResponse>> list(
            final int page,
            final int perPage,
            final String orderField,
            final Direction orderDirection) {

        final SearchQuery query = SearchQuery.of(
                page,
                perPage,
                SearchQuery.Order.of(orderField, orderDirection),
                null,
                List.of());

        return ResponseEntity.ok(listRequestQuotaUseCase.execute(query).map(MemberPresenter::present));

    }

}
