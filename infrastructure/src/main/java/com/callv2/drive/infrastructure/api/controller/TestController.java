package com.callv2.drive.infrastructure.api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.pagination.SearchQuery;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Controller
public class TestController {

    private final FileGateway fileGateway;

    public TestController(FileGateway fileGateway) {
        this.fileGateway = fileGateway;
    }

    @PostMapping("path")
    public ResponseEntity<?> postMethodName(@RequestBody SearchQuery query) {
        // TODO: process POST request

        final var aham = fileGateway.findAll(query);

        return ResponseEntity.ok(aham);
    }

}
