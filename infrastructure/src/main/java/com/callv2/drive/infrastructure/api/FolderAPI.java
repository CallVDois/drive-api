package com.callv2.drive.infrastructure.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.callv2.drive.infrastructure.api.controller.ApiError;
import com.callv2.drive.infrastructure.folder.model.CreateFolderRequest;
import com.callv2.drive.infrastructure.folder.model.CreateFolderResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Folders")
@RequestMapping("folders")
public interface FolderAPI {

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Create a folder", description = "This method creates a folder")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Folder created successfully", content = @Content(schema = @Schema(implementation = CreateFolderResponse.class))),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    ResponseEntity<CreateFolderResponse> create(@RequestBody CreateFolderRequest request);

}
