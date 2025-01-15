package com.callv2.drive.infrastructure.filter.adapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.callv2.drive.domain.pagination.SearchQuery;

public interface QueryAdapter {

    static PageRequest of(final SearchQuery searchQuery) {
        return PageRequest.of(searchQuery.page(), searchQuery.perPage(), of(searchQuery.order()));
    }

    static Sort of(final SearchQuery.Order order) {
        return Sort.by(of(order.direction()), order.field());
    }

    static Direction of(final SearchQuery.Order.Direction direction) {
        return Direction.fromString(direction.name());
    }

}
