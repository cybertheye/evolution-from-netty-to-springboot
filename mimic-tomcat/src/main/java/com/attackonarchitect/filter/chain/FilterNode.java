package com.attackonarchitect.filter.chain;

import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;

import java.io.UnsupportedEncodingException;

/**
 * @description:
 */
public class FilterNode  {
    Filter filter;
    FilterNode next;

    public boolean exec(MTRequest request, MTResponse response) throws UnsupportedEncodingException {
        return filter.doFilter(request,response);
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public FilterNode getNext() {
        return next;
    }

    public void setNext(FilterNode next) {
        this.next = next;
    }
}
