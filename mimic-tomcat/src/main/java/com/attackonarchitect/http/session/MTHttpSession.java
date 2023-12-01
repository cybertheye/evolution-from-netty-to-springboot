package com.attackonarchitect.http.session;

import com.attackonarchitect.context.ServletContext;

import java.util.UUID;
import java.util.jar.Attributes;


/**
 * @author xiong
 * @date 2023/11/29 17:04
 */
public class MTHttpSession implements HttpSession {

    private final String sessionId;
    private final long creationTime;
    private long lastAccessedTime;
    /**
     * 单位：秒
     */
    private final int maxInactiveInterval;
    private Attributes attributes;

    private static final int DEFAULT_MAX_INACTIVE_INTERVAL = 30 * 60;


    public MTHttpSession() {
        this.sessionId = UUID.randomUUID().toString();
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = creationTime;
        this.maxInactiveInterval = DEFAULT_MAX_INACTIVE_INTERVAL;
        this.attributes = new Attributes();
    }
    public MTHttpSession(int maxInactiveInterval) {
        this.sessionId = UUID.randomUUID().toString();
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = creationTime;
        this.maxInactiveInterval = maxInactiveInterval;
        this.attributes = new Attributes();
    }
    @Override
    public String getSessionId() {
        return this.sessionId;
    }


    @Override
    public long getCreationTime() {
        return this.creationTime;
    }

    @Override
    public long getLastAccessedTime() {
        return this.lastAccessedTime;
    }

    @Override
    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    @Override
    public int getMaxInactiveInterval() {
        return this.maxInactiveInterval;
    }

    @Override
    public Attributes getAttributes() {
        return this.attributes;
    }
}
