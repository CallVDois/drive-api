package com.callv2.drive.infrastructure.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.callv2.drive.application.member.quota.request.approve.ApproveRequestQuotaInput;
import com.callv2.drive.application.member.quota.request.approve.ApproveRequestQuotaUseCase;
import com.callv2.drive.application.member.quota.request.list.ListRequestQuotaUseCase;
import com.callv2.drive.application.member.quota.retrieve.get.GetQuotaInput;
import com.callv2.drive.application.member.quota.retrieve.get.GetQuotaUseCase;
import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.Pagination;
import com.callv2.drive.domain.pagination.SearchQuery;
import com.callv2.drive.infrastructure.api.MemberAdminAPI;
import com.callv2.drive.infrastructure.member.model.MemberQuotaResponse;
import com.callv2.drive.infrastructure.member.model.QuotaRequestListResponse;
import com.callv2.drive.infrastructure.member.presenter.MemberPresenter;

@Controller
public class MemberAdminController implements MemberAdminAPI {

    private final ApproveRequestQuotaUseCase approveRequestQuotaUseCase;
    private final ListRequestQuotaUseCase listRequestQuotaUseCase;
    private final GetQuotaUseCase getQuotaUseCase;

    public MemberAdminController(
            final ApproveRequestQuotaUseCase approveRequestQuotaUseCase,
            final ListRequestQuotaUseCase listRequestQuotaUseCase,
            final GetQuotaUseCase getQuotaUseCase) {
        this.approveRequestQuotaUseCase = approveRequestQuotaUseCase;
        this.listRequestQuotaUseCase = listRequestQuotaUseCase;
        this.getQuotaUseCase = getQuotaUseCase;
    }

    @Override
    public ResponseEntity<MemberQuotaResponse> getQuota(String id) {
        return ResponseEntity.ok(MemberPresenter.present(getQuotaUseCase.execute(GetQuotaInput.of(id))));
    }

    @Override
    public ResponseEntity<Void> approveQuotaRequest(final String id, final boolean approved) {
        this.approveRequestQuotaUseCase.execute(ApproveRequestQuotaInput.of(id, approved));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Page<QuotaRequestListResponse>> listQuotaRequests(
            final int page,
            final int perPage,
            final String orderField,
            final Pagination.Order.Direction orderDirection) {

        final SearchQuery query = SearchQuery.of(
                Pagination.of(page, perPage, Pagination.Order.of(orderField, orderDirection)),
                null,
                List.of());

        return ResponseEntity.ok(listRequestQuotaUseCase.execute(query).map(MemberPresenter::present));

    }

}
