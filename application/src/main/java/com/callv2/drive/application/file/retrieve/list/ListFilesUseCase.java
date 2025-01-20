package com.callv2.drive.application.file.retrieve.list;

import com.callv2.drive.application.UseCase;
import com.callv2.drive.domain.pagination.Pagination;
import com.callv2.drive.domain.pagination.SearchQuery;

public abstract class ListFilesUseCase extends UseCase<SearchQuery, Pagination<FileListOutput>> {

}
