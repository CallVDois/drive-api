package com.callv2.drive.infrastructure.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.callv2.drive.domain.pagination.Filter;
import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.Pagination;
import com.callv2.drive.infrastructure.api.controller.ApiError;
import com.callv2.drive.infrastructure.folder.model.CreateFolderRequest;
import com.callv2.drive.infrastructure.folder.model.CreateFolderResponse;
import com.callv2.drive.infrastructure.folder.model.FolderListResponse;
import com.callv2.drive.infrastructure.folder.model.GetFolderResponse;
import com.callv2.drive.infrastructure.folder.model.MoveFolderRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Folders")
@RequestMapping("folders")
public interface FolderAPI {

    @GetMapping(value = "root", produces = { MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Retrive a folder", description = "This method retrive a root folder", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Root folder retrieved successfully", content = @Content(schema = @Schema(implementation = GetFolderResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    ResponseEntity<GetFolderResponse> getRoot();

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Create a folder", description = "This method creates a folder", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Folder created successfully", content = @Content(schema = @Schema(implementation = CreateFolderResponse.class))),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    ResponseEntity<CreateFolderResponse> create(@RequestBody CreateFolderRequest request);

    @GetMapping(value = "{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Retrive a folder", description = "This method retrive a folder", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Folder retrieved successfully", content = @Content(schema = @Schema(implementation = GetFolderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Folder not found", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    ResponseEntity<GetFolderResponse> getById(@PathVariable(required = true) UUID id);

    @PatchMapping(value = "{id}/move", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Move a folder", description = "This method moves a folder to a new location", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Folder moved successfully", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "Folder not found", content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    ResponseEntity<Void> move(@PathVariable(required = true) UUID id, @RequestBody MoveFolderRequest request);

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "List folders", description = "This method list folders", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Folders listed successfully", content = @Content(schema = @Schema(implementation = Page.class, subTypes = {
                    FolderListResponse.class }))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    ResponseEntity<Page<FolderListResponse>> list(
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "orderField", required = false, defaultValue = "createdAt") String orderField,
            @RequestParam(name = "orderDirection", required = false, defaultValue = "DESC") Pagination.Order.Direction orderDirection,
            @RequestParam(name = "filterOperator", required = false, defaultValue = "AND") Filter.Operator filterOperator,
            @RequestParam(name = "filters", required = false) List<String> filters);

}
