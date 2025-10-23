package com.callv2.drive.application.file.usecase.retrieve.list;

import java.util.UUID;

import com.callv2.drive.domain.pagination.SearchQuery;

public record FileListInput(
        UUID actorId,
        SearchQuery query) {

}