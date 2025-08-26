package com.callv2.drive.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.callv2.drive.domain.member.QuotaUnit;
import com.callv2.drive.infrastructure.member.model.MemberQuotaResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Members")
@RequestMapping("members")
public interface MemberAPI {

    @Operation(summary = "Request drive quota", description = "This method request a drive amount quota", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Requested successfuly")
    @ApiResponse(responseCode = "404", description = "Member not found", content = @Content(schema = @Schema(implementation = Void.class)))
    @PostMapping("quotas/requests/{amount}")
    ResponseEntity<Void> requestQuota(
            @PathVariable(value = "amount", required = true) long amount,
            @RequestParam(value = "unit", defaultValue = "GIGABYTE") QuotaUnit unit);

    @Operation(summary = "Retrieve actual drive quota", description = "This method retrieve a drive amount quota", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Retrieve successfuly")
    @ApiResponse(responseCode = "404", description = "Member not found", content = @Content(schema = @Schema(implementation = Void.class)))
    @GetMapping("quotas")
    ResponseEntity<MemberQuotaResponse> getQuota();

}