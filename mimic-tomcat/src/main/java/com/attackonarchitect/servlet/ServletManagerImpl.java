package com.attackonarchitect.servlet;

import com.attackonarchitect.ComponentScanner;
import com.attackonarchitect.context.ServletContext;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @description:
 */
public class ServletManagerImpl implements ServletManager {
    private ServletManagerImpl() {
    }

    private ComponentScanner provider;

    private ServletContext servletContext;


    private static class ServletManagerImplHolder {
        static ServletManagerImpl instance = new ServletManagerImpl();
    }

    static ServletManager getInstance(ComponentScanner provider, ServletContext servletContext) {
        ServletManagerImpl impl = ServletManagerImplHolder.instance;
//        impl.setScanPackages(scanPackages);
        impl.setProvider(provider);
        impl.setServletContext(servletContext);
        impl.preInit();
        return impl;
    }

    private void preInit() {
        this.getServletMapping().put("default", new DefaultMimicServlet());
        this.getServletMapping().put("error", new ErrorMimicServlet());

        //新增 loadOnStartup 初始化功能
        Collection<ServletInformation> values = provider.getServletInformationMap().values();

        values.forEach(information -> {
            if (information.getLoadOnStartup() > 0) {
                instantiate(information);
            }
        });


    }

    //////////////////////


    //////

    /**
     * clazzName - Servlet
     */
    private Map<String, Servlet> servletMapping;

    private Map<String, Servlet> getServletMapping() {
        if (servletMapping == null) {
            servletMapping = new HashMap<>();
        }
        return servletMapping;
    }

    @Override
    public Servlet getSpecifedServlet(String uri) {
        if (servletMapping.containsKey(uri)) {
            return servletMapping.get(uri);
        }
        Map<String, ServletInformation> servletInformationMap = provider.getServletInformationMap();
        ServletInformation servletInformation = servletInformationMap.get(uri);
//        Map<String, String> webServletComponents = provider.getWebServletComponents();
//        String clazzName = webServletComponents.get(uri);
        Servlet ret = null;
        ret = instantiate(servletInformation);
        return ret;
    }


    /**
     * 实例化Servlet
     * @param servletInformation
     * @return
     */
    private Servlet instantiate(ServletInformation servletInformation) {
        String clazzName = servletInformation.getClazzName();
        Servlet instance = null;
        try {
            if (!servletMapping.containsKey(clazzName)) {

                Class<?> clazz = Class.forName(clazzName);
                instance = (Servlet) clazz.newInstance();
                Field servletContextField = null;

                Class<?> traveler = clazz;
                while (true) {
                    try {
                        servletContextField = traveler.getDeclaredField("servletContext");
                        servletContextField.setAccessible(true);
                        break;
                    } catch (NoSuchFieldException e) {
                        traveler= traveler.getSuperclass();
                    }
                }
                //Field 直接 set，和 通过 setter 方法的区别在于 setter可以有逻辑处理
                servletContextField.set(instance, servletContext);

                // 对于 注解 @WebInitParams 传过来的参数处理
                Map<String, String> initParams = servletInformation.getInitParams();

                Class<?> finalClazz = clazz;
                Servlet finalInstance = instance;
                initParams.forEach((key, value) -> {
                    try {
                        String setterName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
                        Method method = finalClazz.getMethod(setterName,value.getClass());
                        method.invoke(finalInstance,value);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });


                String[] urlPatterns = servletInformation.getUrlPattern();
                for(String urlPattern : urlPatterns){
                    servletMapping.put(urlPattern, instance);
                }
            }

            return instance;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public Map<String, Servlet> getAllServletMapping(boolean init) {

        //TODO
        return null;
    }


    @Override
    public Set<String> getAllRequestUri() {
        return getProvider().getServletInformationMap().keySet();
    }


    /////////getter ,setter
    public ComponentScanner getProvider() {
        return provider;
    }

    public void setProvider(ComponentScanner provider) {
        this.provider = provider;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
