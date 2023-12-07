package com.attackonarchitect;

import com.attackonarchitect.filter.WebFilter;
import com.attackonarchitect.filter.chain.Filter;
import com.attackonarchitect.listener.EventListener;
import com.attackonarchitect.listener.request.ServletRequestAttributeListener;
import com.attackonarchitect.listener.webcontext.ServletContextListener;
import com.attackonarchitect.servlet.Servlet;
import com.attackonarchitect.servlet.ServletInformation;
import com.attackonarchitect.servlet.ServletInformationBuilder;
import com.attackonarchitect.servlet.WebServlet;

import java.util.*;
import java.util.stream.Collectors;

/**
 * spi组件扫描
 *
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2023/11/14
 * @since 1.8
 **/
public class SpiComponentScanner implements ComponentScanner {
    private final List<EventListener> webListenerComponents = new ArrayList<>();

    private final Map<String, String> webServletComponents = new HashMap<>();

    private final Map<String, ServletInformation> servletInformationMap = new HashMap<>();

    private final Map<String, Set<String>> webFilterComponents = new HashMap<>();

    private final ClassLoader classLoader;
    // 记录filter 顺序
    private final Map<String, Integer> webFilterComponentsOrder = new HashMap<>();

    public SpiComponentScanner(ClassLoader classLoader) {
        this.classLoader = Optional.ofNullable(classLoader).orElseGet(Thread.currentThread()::getContextClassLoader);
        this.init();
    }

    private void init() {
        this.initListeners();

        this.initServlet();

        this.initFilters();
    }

    private void initListeners() {
        ServiceLoader<ServletContextListener> listenerLoader = ServiceLoader.load(ServletContextListener.class, this.classLoader);

        for (ServletContextListener listener : listenerLoader) {
            // 解析出监听器相关信息
            this.webListenerComponents.add(listener);
        }
    }

    private void initServlet() {
        ServiceLoader<Servlet> servletLoader = ServiceLoader.load(Servlet.class, this.classLoader);

        for (Servlet servlet : servletLoader) {
            // 解析出监听器相关信息
            String servletClassName = servlet.getClass().getName();
            WebServlet webServlet = servlet.getClass().getAnnotation(WebServlet.class);
            if (Objects.isNull(webServlet)) {
                System.err.println(servletClassName + " 需要被 @WebServlet 注解");
                continue;
            }

            ServletInformation servletInformation = new ServletInformationBuilder()
                    .setClassName(servletClassName)
                    .setUrlPattern(webServlet.value())
                    .setLoadOnStartup(webServlet.loadOnStartup())
                    .setInitParams(webServlet.initParams())
                    .build();

            for (String uri : webServlet.value()) {
                this.webServletComponents.put(uri, servletClassName);
                this.servletInformationMap.put(uri, servletInformation);
            }
        }
    }

    private void initFilters() {
        ServiceLoader<Filter> filterLoader = ServiceLoader.load(Filter.class, this.classLoader);

        for (Filter filter : filterLoader) {
            String filterClassName = filter.getClass().getName();
            WebFilter webFilter = filter.getClass().getAnnotation(WebFilter.class);
            if (Objects.isNull(webFilter)) {
                System.err.println(filterClassName + " 需要被 @WebFilter 注解");
                continue;
            }

            for (String uri : webFilter.value()) {
                this.webFilterComponents.computeIfAbsent(uri, key -> new HashSet<>());
                this.webFilterComponents.get(uri).add(filterClassName);
            }
        }
    }


    @Override
    public List<String> getWebListenerComponents() {
        return this.webListenerComponents.stream().map(EventListener::getClass).map(Class::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> getWebServletComponents() {
        return this.webServletComponents;
    }

    @Override
    public Map<String, ServletInformation> getServletInformationMap() {
        return this.servletInformationMap;
    }

    @Override
    public Map<String, Set<String>> getWebFilterComponents() {
        return this.webFilterComponents;
    }

    @Override
    public Map<String, Integer> getWebFilterComponentsOrder() {
        return this.webFilterComponentsOrder;
    }
}
