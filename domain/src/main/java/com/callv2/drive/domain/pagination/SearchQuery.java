package com.callv2.drive.domain.pagination;

import java.util.List;

public record SearchQuery(
        Pagination pagination,
        Filter.Operator filterMethod,
        List<Filter> filters) {

    public static SearchQuery of(
            final Pagination pagination,
            final Filter.Operator filterMethod,
            final List<Filter> filters) {
        return new SearchQuery(pagination, filterMethod, filters);
    }

}
