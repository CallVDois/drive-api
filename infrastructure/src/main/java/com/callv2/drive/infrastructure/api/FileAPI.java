package com.callv2.drive.infrastructure.api;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.callv2.drive.infrastructure.file.model.CreateFileResponse;
import com.callv2.drive.infrastructure.file.model.GetFileResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Files")
@RequestMapping("files")
public interface FileAPI {

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Upload a file", description = "This method uploads a file")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "File uploaded successfully"),
            @ApiResponse(responseCode = "413", description = "File is too large"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<CreateFileResponse> create(@RequestPart("file") MultipartFile file);

    @GetMapping(value = "{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Retrive a file", description = "This method retrive a file")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "File retrived successfully"),
            @ApiResponse(responseCode = "404", description = "File not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<GetFileResponse> get(@PathVariable(required = true) String id);

    @GetMapping(value = "{id}/download", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
    @Operation(summary = "Download a file", description = "This method downloads a file")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "File downloaded successfully"),
            @ApiResponse(responseCode = "404", description = "File not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    ResponseEntity<Resource> download(@PathVariable(required = true) String id);

}
