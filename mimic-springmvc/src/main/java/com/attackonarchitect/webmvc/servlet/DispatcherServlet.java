package com.attackonarchitect.webmvc.servlet;

import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.RequestParam;
import com.attackonarchitect.http.HttpMTRequest;
import com.attackonarchitect.http.HttpMTResponse;
import com.attackonarchitect.webmvc.handler.MimicHandlerMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 */

public class DispatcherServlet extends FrameworkServlet {

    @Override
    public void doGet(MTRequest request, MTResponse response) {
        try {
            doService(request,response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doPost(MTRequest request, MTResponse response) {
        try {
            doService(request,response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    private void doService(MTRequest request, MTResponse response) throws Exception{
        String uri = request.uri();
        String[] split = uri.split("\\?");
        Map<String,String> requestParams = new HashMap<>();
        if(split.length>1) {
            String[] pairParmas = split[1].split("&");
            for (String pairParam : pairParmas) {
                String[] r = pairParam.split("=");
                requestParams.put(r[0], r[1]);
            }
        }
        MimicHandlerMethod targetMethod = getManager().getSpecifiedMethod(split[0]);
        if(targetMethod==null){
            response.write("invalid url");
            return;
        }
        Object result = doDispatch(targetMethod,requestParams);

        if(result!=null){
            Gson gson = new Gson();
            response.write(gson.toJson(result));
        }
    }

    public Object doDispatch(MimicHandlerMethod targetMethod, Map<String, String> params) {
        Method handlerMethod = targetMethod.getHandlerMethod();
        Object targetBean = targetMethod.getTargetBean();

        try {
            return doInvokeMethod(handlerMethod,targetBean,params);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }



    protected void init() {

    }

    private Object doInvokeMethod(Method handlerMethod,
                                  Object targetBean,
                                  Map<String, String> params) throws InvocationTargetException, IllegalAccessException {

        Parameter[] parameters = handlerMethod.getParameters();
        Object[] methodActualParams = new Object[handlerMethod.getParameterCount()];
        int index = 0;
        for(Parameter parameter : parameters){
//            String name = parameter.getName(); arg0
            /*
            demo :
            @RequestMapping("/param")
            public String testParam(String name, @RequestParam("ag") int age){
            处理这个 @RequestParam
             */
            RequestParam paramAnno = parameter.getAnnotation(RequestParam.class);
            String paramName = paramAnno.value();
            Class<?> needType = parameter.getType();
            //params是前面 /a/b?x=1&y=2 , <x,1>,<y,2> 的map 键值对
            String paramValueAtString = params.get(paramName);

            //todo 进行类型转化，String-> needType
            Object setParam = paramValueAtString;

            methodActualParams[index++]=setParam;
        }



        return handlerMethod.invoke(targetBean,methodActualParams);
    }




}
