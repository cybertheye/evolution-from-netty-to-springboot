package com.attackonarchitect;

import com.attackonarchitect.filter.WebFilter;
import com.attackonarchitect.listener.WebListener;
import com.attackonarchitect.servlet.ServletInformation;
import com.attackonarchitect.servlet.ServletInformationBuilder;
import com.attackonarchitect.servlet.WebServlet;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * @description:
 */
public class WebComponentScanner implements ComponentScanner {

    private Class<?> clazz;


    public WebComponentScanner(Class<?> clazz) {
        this.clazz = clazz;
        WebScanPackage annotation = clazz.getAnnotation(WebScanPackage.class);
        String[] scanPackages = null;

        if(null != annotation) {
            scanPackages = annotation.value();
            if (scanPackages.length == 1 && scanPackages[0].equals("")) {
                String clazzName = clazz.getName();
                String packagePath = clazzName.substring(0, clazzName.lastIndexOf("."));
                scanPackages[0] = packagePath;
            }
        }

        //为了 支持 Spring MVC 特地弄的
        enlist();

        scan(scanPackages);
    }

    /**
     * 注册 Servlet 用
     */
    private void enlist(){

        WebEnlistServlet enlistServlet = clazz.getAnnotation(WebEnlistServlet.class);
        if (enlistServlet != null) {
            ServletInformation servletInformation = new ServletInformationBuilder()
                    .setClassName(enlistServlet.servletClass())
                    .setUrlPattern(enlistServlet.urlPattern())
                    .setLoadOnStartup(enlistServlet.loadOnStartup())
                    .setInitParams(enlistServlet.initParams())
                    .build();
            Arrays.stream(enlistServlet.urlPattern())
                    .forEach(url -> {
                        getServletInformationMap().put(url, servletInformation);
                    });
        }
    }



    private void scan(String[] scanPackages) {
        if(scanPackages == null){
            return;
        }
        try {
            for (String scanPackage : scanPackages) {
                scanSingle(scanPackage);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void scanSingle(String scanPackage) throws IOException, URISyntaxException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String scanPath = scanPackage.replaceAll("\\.", "/");
        URL systemResources = ClassLoader.getSystemResource(scanPath);
        URI resourceUri = systemResources.toURI();
        File file = new File(resourceUri);
        iterateScan(file, scanPackage);
    }


    private void iterateScan(File file, String scanPackage) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        for (File listFile : file.listFiles()) {
            if (listFile.isDirectory()) {
                iterateScan(listFile, scanPackage + "." + listFile.getName());
            } else {
                String fileName = listFile.getName();
                if (fileName.endsWith(".class")) {
                    //拼接全限定类名
                    Class<?> clazz = Class.forName(scanPackage + "." + fileName.split("\\.")[0]);
                    WebServlet webServletAnno = clazz.getAnnotation(WebServlet.class);
                    WebFilter webFilterAnno = clazz.getAnnotation(WebFilter.class);
                    WebListener webListenerAnno = clazz.getAnnotation(WebListener.class);
                    if (webServletAnno != null) {
                        String[] uris = webServletAnno.value();

                        ServletInformationBuilder builder= new ServletInformationBuilder();
                        ServletInformation servletInformation = builder.setClassName(clazz.getName())
                                .setUrlPattern(webServletAnno.value())
                                .setLoadOnStartup(webServletAnno.loadOnStartup())
                                .setInitParams(webServletAnno.initParams())
                                .build();

                        for (String uri : uris) {
                            webServletComponents.put(uri, clazz.getName());
                            servletInformationMap.put(uri,servletInformation);
                        }
                    }
                    if (webFilterAnno != null) {
                        String[] uris = webFilterAnno.value();
                        webFilterComponentsOrder.put(clazz.getName(),webFilterAnno.order());
                        for (String uri : uris) {
                            Set<String> uriSet = webFilterComponents.getOrDefault(uri, new HashSet<>());
                            uriSet.add(clazz.getName());
                            webFilterComponents.put(uri,uriSet);
                        }
                    }
                    if (webListenerAnno != null) {
                        webListenerComponents.add(clazz.getName());
                    }
                }
            }
        }
    }


    private Map<String, ServletInformation> servletInformationMap = new HashMap<>();
    private List<String> webListenerComponents = new ArrayList<>();
    private Map<String, String> webServletComponents = new HashMap<>();
    private Map<String, Set<String>> webFilterComponents = new HashMap<>();
    // 记录filter 顺序
    private Map<String, Integer> webFilterComponentsOrder = new HashMap<>();

    @Override
    public Map<String, ServletInformation> getServletInformationMap() {
        return this.servletInformationMap;
    }

    @Override
    public List<String> getWebListenerComponents() {
        return this.webListenerComponents;
    }

    @Override
    public Map<String, String> getWebServletComponents() {
        return this.webServletComponents;
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
