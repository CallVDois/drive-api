package com.callv2.drive.application.folder.retrieve.list;

import java.util.Objects;

import com.callv2.drive.domain.folder.FolderGateway;
import com.callv2.drive.domain.pagination.Pagination;
import com.callv2.drive.domain.pagination.SearchQuery;

public class DefaultListFoldersUseCase extends ListFoldersUseCase {

    private final FolderGateway folderGateway;

    public DefaultListFoldersUseCase(final FolderGateway folderGateway) {
        this.folderGateway = Objects.requireNonNull(folderGateway);
    }

    @Override
    public Pagination<FolderListOutput> execute(final SearchQuery input) {
        return folderGateway
                .findAll(input)
                .map(FolderListOutput::from);
    }

}
