package com.attackonarchitect.http.session;

import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;
import com.attackonarchitect.http.cookie.MTCookie;
import com.attackonarchitect.http.cookie.MTCookieBuilder;
import io.netty.util.internal.StringUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiong
 * @date 2023/11/29 17:20
 */
public class HttpSessionManager implements SessionManager,Runnable{

    private static volatile HttpSessionManager instance = null;
    private  Map<String,HttpSession> sessionMap = new ConcurrentHashMap<>();

    private HttpSessionManager() {

    }
    public static HttpSessionManager getInstance(){
        if (instance == null){
            synchronized (HttpSessionManager.class){
                if (instance == null){
                    instance = new HttpSessionManager();
                    // 开启session过期检查线程
                    new Thread(instance).start();
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
            HttpSession session = new MTHttpSession(30 * 60);
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
        }else {
            // 更新session最后访问时间
            HttpSession session = getSessionMap().get(sessionId);
            if(session != null){
               session.setLastAccessedTime(System.currentTimeMillis());
            }
        }


    }

    @Override
    public Map<String, HttpSession> getSessionMap() {
        return getInstance().sessionMap;
    }

    @Override
    public void run() {
        for (;;){
            try {
                Thread.sleep(1000 * 30);
                for (HttpSession session : getSessionMap().values()) {
                    if(session.getLastAccessedTime() + session.getMaxInactiveInterval() * 1000L < System.currentTimeMillis()){
                        invalidate(session.getSessionId());
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
