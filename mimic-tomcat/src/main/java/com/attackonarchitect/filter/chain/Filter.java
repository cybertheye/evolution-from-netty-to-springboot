package com.attackonarchitect.filter.chain;

import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;

import java.io.UnsupportedEncodingException;

/**
 * @description:
 */

public interface Filter {
    boolean doFilter(MTRequest request, MTResponse response) throws UnsupportedEncodingException;
}
