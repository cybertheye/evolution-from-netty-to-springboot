package com.attackonarchitect.filter.chain;

import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;
import com.attackonarchitect.servlet.Servlet;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @description:
 * filter责任链实现
 */
public class FilterChainImpl implements FilterChain {

    private FilterNode head = new FilterNode(); //soldier
    private FilterNode tail;

    private FilterNode currNode = null;

    private Servlet targetServlet;

    private FilterChainImpl(Servlet targetServlet) {
        this.targetServlet = targetServlet;
        tail = head;
    }

    public static FilterChain createFilterChain(Servlet targetServlet,List<Filter> filters){
        FilterChainImpl filterChain = new FilterChainImpl(targetServlet);
        filterChain.addLast(filters);
        return filterChain;
    }

    @Override
    public void addFirst(List<Filter> filters) {
        FilterNode pre = head.getNext();
        for (Filter filter : filters) {
            FilterNode filterNode = new FilterNode();
            filterNode.setFilter(filter);
            filterNode.setNext(pre);
            pre = filterNode;
        }

        head.setNext(pre);
    }

    @Override
    public void addLast(List<Filter> filters) {
        for (Filter filter : filters) {
            FilterNode filterNode = new FilterNode();
            filterNode.setFilter(filter);

            tail.setNext(filterNode);
            tail = filterNode;
        }

    }

    /**
     * 依次执行所有的filter
     * @param request
     * @param response
     */
    @Override
    public void start(MTRequest request, MTResponse response) {
        // 获取下一个过滤器
        // 如果没有过滤器, 则执行对应的servlet
        currNode = Optional.ofNullable(currNode).orElse(head).getNext();
        try {
//            while (traveler != null && traveler.exec(request, response)) {
//                traveler=traveler.next;
//            }
            if (Objects.isNull(currNode)) {
                targetServlet.service(request,response);
            } else {
                currNode.exec(request, response, this);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
