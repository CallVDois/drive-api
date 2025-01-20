package com.callv2.drive.application.file.retrieve.list;

import java.util.Objects;

import com.callv2.drive.domain.file.FileGateway;
import com.callv2.drive.domain.pagination.Pagination;
import com.callv2.drive.domain.pagination.SearchQuery;

public class DefaultListFilesUseCase extends ListFilesUseCase {

    private final FileGateway fileGateway;

    public DefaultListFilesUseCase(final FileGateway fileGateway) {
        this.fileGateway = Objects.requireNonNull(fileGateway);
    }

    @Override
    public Pagination<FileListOutput> execute(final SearchQuery query) {
        return this.fileGateway
                .findAll(query)
                .map(FileListOutput::from);
    }

}
