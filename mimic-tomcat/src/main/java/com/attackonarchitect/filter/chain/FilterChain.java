package com.attackonarchitect.filter.chain;

import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;

import java.util.List;

/**
 * @description:
 */

public interface FilterChain {

    void addFirst(List<Filter> filters);

    void addLast(List<Filter> filters);

    void doFilter(MTRequest request, MTResponse response);

}
