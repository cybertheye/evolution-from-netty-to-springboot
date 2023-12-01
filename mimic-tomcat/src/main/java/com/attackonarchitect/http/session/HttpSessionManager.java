package com.attackonarchitect.http.session;

import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;
import com.attackonarchitect.http.cookie.MTCookie;
import com.attackonarchitect.http.cookie.MTCookieBuilder;
import io.netty.util.internal.StringUtil;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiong
 * @date 2023/11/29 17:20
 */
public class HttpSessionManager implements SessionManager{

    private static volatile HttpSessionManager instance = null;
    private  Map<String,HttpSession> sessionMap = new ConcurrentHashMap<>();

    private HttpSessionManager() {

    }
    public static HttpSessionManager getInstance(){
        if (instance == null){
            synchronized (HttpSessionManager.class){
                if (instance == null){
                    instance = new HttpSessionManager();
                }
            }
        }
        return instance;
    }

    // 能否将方法放入HttpSession中？
    public void invalidate(String sessionId) {
        getSessionMap().remove(sessionId);
    }

    public void initializeSession(MTRequest request, MTResponse response) {
        // 从cookie中获取sessionId
        String sessionId = request.getCookie("JSESSIONID");
        if(StringUtil.isNullOrEmpty(sessionId)){
            // 创建session
            HttpSession session = new MTHttpSession();
            // 将session放入sessionMap
            getSessionMap().put(session.getSessionId(),session);
            // 将sessionId放入cookie
            MTCookie cookie = MTCookieBuilder.newBuilder()
                    .name("JSESSIONID")
                    .value(session.getSessionId())
                    .domain("*")
                    .path("/")
                    .build();

            // Set-Cookie: JSESSIONID=xxx;Path=/; .......
            response.addHeader("Set-Cookie",cookie.toString());
        }

    }

    @Override
    public Map<String, HttpSession> getSessionMap() {
        return getInstance().sessionMap;
    }
}
