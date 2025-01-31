package com.callv2.drive.application.folder.retrieve.list;

import com.callv2.drive.application.UseCase;
import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.SearchQuery;

public abstract class ListFoldersUseCase extends UseCase<SearchQuery, Page<FolderListOutput>> {

}
