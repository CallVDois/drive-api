package com.callv2.drive.application.member.quota.retrieve.list;

import com.callv2.drive.application.UseCase;
import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.SearchQuery;

public abstract class ListQuotasUseCase extends UseCase<SearchQuery, Page<ListQuotaOutput>> {

}
