package com.attackonarchitect.http;

import java.io.UnsupportedEncodingException;

/**
 * @description:
 */

public interface MTResponse {
    void write(String content);

    void writeAndFlush(String content) throws UnsupportedEncodingException;
}
