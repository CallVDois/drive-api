package com.callv2.drive.infrastructure.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.callv2.drive.domain.pagination.Pagination;
import com.callv2.drive.domain.pagination.SearchQuery;
import com.callv2.drive.infrastructure.api.controller.ApiError;
import com.callv2.drive.infrastructure.member.model.MemberQuotaResponse;
import com.callv2.drive.infrastructure.member.model.QuotaRequestListResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Members Admin")
@RequestMapping("admin/members")
public interface MemberAdminAPI {

    @GetMapping("{id}/quotas")
    @Operation(summary = "Request drive quota", description = "This method request a drive ammount quota", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retrieve successfuly"),
            @ApiResponse(responseCode = "404", description = "Member not found", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    ResponseEntity<MemberQuotaResponse> getQuota(@PathVariable(value = "id", required = true) String id);

    @PostMapping("{id}/quotas/requests/approve")
    @Operation(summary = "Approve drive quota request", description = "This method approve a drive ammount quota request", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Approved successfuly"),
            @ApiResponse(responseCode = "404", description = "Member not found", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    ResponseEntity<Void> approveQuotaRequest(
            @PathVariable(value = "id", required = true) String id,
            @RequestParam(value = "approved", defaultValue = "true") boolean approved);

    @GetMapping(value = "quotas/requests", produces = { MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "List quotas requests", description = "This method list quotas requests", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Quotas requests listed successfully", content = @Content(schema = @Schema(implementation = Pagination.class, subTypes = {
                    QuotaRequestListResponse.class }))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    ResponseEntity<Pagination<QuotaRequestListResponse>> listQuotaRequests(
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "orderField", required = false, defaultValue = "requestedAt") String orderField,
            @RequestParam(name = "orderDirection", required = false, defaultValue = "DESC") SearchQuery.Order.Direction orderDirection);

}