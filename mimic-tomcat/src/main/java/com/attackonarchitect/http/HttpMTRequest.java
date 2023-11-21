package com.attackonarchitect.http;

import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import com.attackonarchitect.context.ServletContext;
import com.attackonarchitect.listener.request.ServletRequestAttributeEvent;
import com.attackonarchitect.listener.request.ServletRequestAttributeListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 */
public class HttpMTRequest implements MTRequest{

    @Override
    public HttpMethod getMethod() {
        return null;
    }

    @Override
    public HttpMethod method() {
        return null;
    }

    @Override
    public HttpRequest setMethod(HttpMethod method) {
        return null;
    }

    @Override
    public String getUri() {
        return null;
    }

    @Override
    public String uri() {
        return null;
    }

    @Override
    public HttpRequest setUri(String uri) {
        return null;
    }

    @Override
    public HttpVersion getProtocolVersion() {
        return null;
    }

    @Override
    public HttpVersion protocolVersion() {
        return null;
    }

    @Override
    public HttpRequest setProtocolVersion(HttpVersion version) {
        return null;
    }

    @Override
    public HttpHeaders headers() {
        return null;
    }

    @Override
    public DecoderResult getDecoderResult() {
        return null;
    }

    @Override
    public DecoderResult decoderResult() {
        return null;
    }

    @Override
    public void setDecoderResult(DecoderResult result) {

    }

    private ServletContext context;

    private Map<String,Object> attributeDepot = new HashMap<>();

    public Object getAttribute(String name){
        return attributeDepot.get(name);
    }


    public void setAttribute(String name, Object obj){
        attributeDepot.put(name,obj);
        ServletRequestAttributeEvent srae = new ServletRequestAttributeEvent(name, obj);
        context.getNotifiler().notifyListeners(srae);
    }

    //////////////////// parameters
    private Map<String,String> parametersDepot ;

    public void setParametersDepot(Map<String, String> parametersDepot) {
        this.parametersDepot = parametersDepot;
    }

    @Override
    public Map<String, String> getParameters() {
        return this.parametersDepot;
    }

    @Override
    public String getParameter(String name) {
        return this.parametersDepot.get(name);
    }

    public HttpMTRequest(ServletContext context) {
        this.context = context;
    }



    @Override
    public ServletContext getServletContext() {
        return this.context;
    }
}
