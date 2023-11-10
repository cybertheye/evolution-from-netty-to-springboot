package com.attackonarchitect.http;

import java.io.UnsupportedEncodingException;

/**
 * @description:
 */

public interface MTResponse {
    void write(String content);

    /**
     * Developers shouldn't call this method
     * @throws UnsupportedEncodingException
     */
    void flush() throws UnsupportedEncodingException;
}
