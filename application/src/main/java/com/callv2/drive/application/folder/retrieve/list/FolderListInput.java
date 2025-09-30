package com.callv2.drive.application.folder.retrieve.list;

import java.util.UUID;

import com.callv2.drive.domain.pagination.SearchQuery;

public record FolderListInput(
        UUID actorId,
        SearchQuery searchQuery) {

}
