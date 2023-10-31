package com.attackonarchitect.servlet;

import com.attackonarchitect.ComponentScanner;
import com.attackonarchitect.context.ServletContext;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @description:
 */
public class ServletManagerImpl implements ServletManager{
    private ServletManagerImpl(){}

    private ComponentScanner provider;

    private ServletContext servletContext;


    private static class ServletManagerImplHolder{
        static ServletManagerImpl instance = new ServletManagerImpl();
    }
    static ServletManager getInstance(ComponentScanner provider,ServletContext servletContext){
        ServletManagerImpl impl =  ServletManagerImplHolder.instance;
//        impl.setScanPackages(scanPackages);
        impl.setProvider(provider);
        impl.setServletContext(servletContext);
        impl.preInit();
        return impl;
    }

    private void preInit() {
       this.getServletMapping().put("default",new DefaultMimicServlet());
       this.getServletMapping().put("error",new ErrorMimicServlet());
    }

    //////////////////////





    //////

    /**
     * clazzName - Servlet
     */
    private Map<String,Servlet> servletMapping;

    private Map<String, Servlet> getServletMapping() {
        if(servletMapping == null){
            servletMapping = new HashMap<>();
        }
        return servletMapping;
    }

    @Override
    public Servlet getSpecifedServlet(String uri) {
        Map<String, String> webServletComponents = provider.getWebServletComponents();
        String clazzName = webServletComponents.get(uri);
        Servlet ret = null;
        try {
            if (!servletMapping.containsKey(clazzName)) {
                Class<?> clazz = Class.forName(clazzName);
                Servlet instance = (Servlet)clazz.newInstance();

                Field servletContextField = null;

                while(true){
                    try {
                        servletContextField = clazz.getDeclaredField("servletContext");
                        servletContextField.setAccessible(true);
                        break;
                    }catch (NoSuchFieldException e) {
                        clazz = clazz.getSuperclass();
                    }
                }

                servletContextField.set(instance,servletContext);
                servletMapping.put(clazzName,instance);
            }

            ret = servletMapping.get(clazzName);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return ret;
    }

    @Override
    public Map<String, Servlet> getAllServletMapping(boolean init) {

        //TODO
        return null;
    }


    @Override
    public Set<String> getAllRequestUri() {
        Map<String, String> webServletComponents = getProvider().getWebServletComponents();
        return webServletComponents.keySet();
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
