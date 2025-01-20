package com.callv2.drive.application.folder.retrieve.list;

import com.callv2.drive.application.UseCase;
import com.callv2.drive.domain.pagination.Pagination;
import com.callv2.drive.domain.pagination.SearchQuery;

public abstract class ListFoldersUseCase extends UseCase<SearchQuery, Pagination<FolderListOutput>> {

}
