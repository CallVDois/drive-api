package com.callv2.drive.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.Pagination;
import com.callv2.drive.infrastructure.api.controller.ApiError;
import com.callv2.drive.infrastructure.member.model.MemberQuotaResponse;
import com.callv2.drive.infrastructure.member.model.QuotaRequestListResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Members Admin")
@RequestMapping("admin/members")
public interface MemberAdminAPI {

    @Operation(summary = "Request drive quota", description = "This method request a drive ammount quota", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Retrieve successfuly")
    @ApiResponse(responseCode = "404", description = "Member not found", content = @Content(schema = @Schema(implementation = Void.class)))
    @GetMapping("{id}/quotas")
    ResponseEntity<MemberQuotaResponse> getQuota(@PathVariable(value = "id", required = true) String id);

    @Operation(summary = "Approve drive quota request", description = "This method approve a drive ammount quota request", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Approved successfuly")
    @ApiResponse(responseCode = "404", description = "Member not found", content = @Content(schema = @Schema(implementation = Void.class)))
    @PatchMapping("{id}/quotas/requests")
    ResponseEntity<Void> approveQuotaRequest(
            @PathVariable(value = "id", required = true) String id,
            @RequestParam(value = "approved", defaultValue = "true") boolean approved);

    @Operation(summary = "List quotas requests", description = "This method list quotas requests", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Quotas requests listed successfully", content = @Content(schema = @Schema(implementation = Page.class, subTypes = {
            QuotaRequestListResponse.class })))
    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiError.class)))
    @GetMapping("quotas/requests")
    ResponseEntity<Page<QuotaRequestListResponse>> listQuotaRequests(
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "orderField", required = false, defaultValue = "quotaRequestedAt") String orderField,
            @RequestParam(name = "orderDirection", required = false, defaultValue = "DESC") Pagination.Order.Direction orderDirection);

}