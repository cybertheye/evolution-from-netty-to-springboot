package com.attackonarchitect.filter.chain;

import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;

/**
 * @description:
 */

public interface Chain {
    void start(MTRequest request, MTResponse response);
}
