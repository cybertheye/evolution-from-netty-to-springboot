package com.attackonarchitect.filter.chain;

import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;
import com.attackonarchitect.servlet.Servlet;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @description:
 */
public class DefaultFilterChain implements FilterChain {

    //servlet
    private Servlet targetServlet;

    //当前filter
    private Filter currentFilter;

    //filters
    private List<Filter> filters;

    private DefaultFilterChain chain;

    public DefaultFilterChain(Servlet targetServlet, List<Filter> filters) {
        this.targetServlet = targetServlet;
        initChain(filters);
    }

    public DefaultFilterChain(Servlet targetServlet, List<Filter> invokerFilters, Filter currentFilter, DefaultFilterChain chain) {
        this.targetServlet = targetServlet;
        this.filters = invokerFilters;
        this.currentFilter = currentFilter;
        this.chain = chain;
    }

    //初始化filter链
    private DefaultFilterChain initChain(List<Filter> filters) {
        DefaultFilterChain chain = new DefaultFilterChain(this.targetServlet, filters, null, null);
        if (!isEmpty(filters)) {
            Collections.reverse(filters);
            Iterator<Filter> iterator = filters.iterator();
            while (iterator.hasNext()) {
                chain = new DefaultFilterChain(null, filters, iterator.next(), chain);
            }
        }
        return chain;
    }

    @Override
    public void addFirst(List<Filter> filters) {

        if (isEmpty(this.filters)) {
            this.filters = filters;
        } else {
            this.filters.addAll(filters);
        }
        initChain(this.filters);

    }

    @Override
    public void addLast(List<Filter> filters) {

        if (isEmpty(this.filters)) {
            this.filters = filters;
        } else {
            this.filters.addAll(this.filters.size() - 1, filters);
        }
        initChain(this.filters);

    }

    @Override
    public void doFilter(MTRequest request, MTResponse response) {

        if (this.currentFilter != null) {
            try {
                this.currentFilter.doFilter(request, response);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        if (this.chain != null) {
            this.chain.doFilter(request, response);
        }

        if (this.targetServlet!= null) {
            try {
                this.targetServlet.service(request, response);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isEmpty(List<Filter> filters) {
        return filters == null || filters.size() == 0;
    }

}
