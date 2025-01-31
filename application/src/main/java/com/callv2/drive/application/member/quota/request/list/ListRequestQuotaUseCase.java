package com.callv2.drive.application.member.quota.request.list;

import com.callv2.drive.application.UseCase;
import com.callv2.drive.domain.pagination.Page;
import com.callv2.drive.domain.pagination.SearchQuery;

public abstract class ListRequestQuotaUseCase extends UseCase<SearchQuery, Page<RequestQuotaListOutput>> {

}
