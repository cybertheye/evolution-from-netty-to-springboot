package com.attackonarchitect.webmvc.handler;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 */
public class MimicHandlerMethodManagerImpl implements MimicHandlerMethodManager{
    private Map<String,MimicHandlerMethod> methodMapping = new HashMap<>();

    MimicHandlerMethodManagerImpl(ApplicationContext context) {
        initMethod(context);
    }

    private void initMethod(ApplicationContext context) {
        Map<String, Object> controllerBeans = context.getBeansWithAnnotation(Controller.class);

        controllerBeans.forEach((controllerName,controllerBean)->{
            Class<?> beanClazz = controllerBean.getClass();
            String prefixs[] =null;
            RequestMapping typeRequestMappingAnno= beanClazz.getAnnotation(RequestMapping.class);
            if(typeRequestMappingAnno!=null){
                prefixs = typeRequestMappingAnno.value();
                for(int i = 0;i<prefixs.length;i++){
                    if(!prefixs[i].startsWith("/")){
                        prefixs[i] = "/"+prefixs[i];
                    }
                    if(prefixs[i].endsWith("/")){
                        prefixs[i] = prefixs[i].substring(0, prefixs.length-1);
                    }
                }
            }
            Method[] declaredMethods = beanClazz.getDeclaredMethods();

            for(Method declaredMethod: declaredMethods){
                RequestMapping methodRequestmappingAnno = declaredMethod.getAnnotation(RequestMapping.class);
                String[] paths = methodRequestmappingAnno.value();
                for(String path: paths){
                    String url = path;
                    if(!url.startsWith("/")) url = "/"+url;
                    if(prefixs != null) {
                        for (String prefix : prefixs) {
                            String finalUrl = prefix + url;

                            //todo 看一下 springmvc里面是怎么处理 handlermapping映射的
                            wrapAndPutMethod(controllerBean, declaredMethod, finalUrl);
                        }
                        continue;
                    }
                    wrapAndPutMethod(controllerBean,declaredMethod,url);
                }
            }

        });

    }

    private void wrapAndPutMethod(Object controllerBean, Method declaredMethod, String url) {
        MimicHandlerMethod mimicHandlerMethod = new MimicHandlerMethod();
        mimicHandlerMethod.setUrl(url);
        mimicHandlerMethod.setHandlerMethod(declaredMethod);
        mimicHandlerMethod.setTargetBean(controllerBean);

        methodMapping.put(url,mimicHandlerMethod);
    }

    @Override
    public Map<String, MimicHandlerMethod> getMethodMapping() {
        return this.methodMapping;
    }

    @Override
    public MimicHandlerMethod getSpecifiedMethod(String uri) {
        return methodMapping.get(uri);
    }
}
