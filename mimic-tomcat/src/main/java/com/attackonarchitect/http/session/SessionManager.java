package com.attackonarchitect.http.session;

import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;

import java.util.Map;

/**
 * @author xiong
 * @date 2023/12/1 14:12
 */
public interface SessionManager {

    void initializeSession(MTRequest request, MTResponse response);
    void invalidate(String sessionId);

    Map<String,HttpSession> getSessionMap();
}
