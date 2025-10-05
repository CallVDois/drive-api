package com.callv2.drive.application.sharing.file.retrieve.list.received;

import java.util.UUID;

import com.callv2.drive.domain.pagination.SearchQuery;

public record FileSharingReceivedListInput(
        UUID actorId,
        SearchQuery query) {

}