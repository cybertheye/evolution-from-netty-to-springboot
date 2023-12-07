package com.attackonarchitect;

import com.attackonarchitect.servlet.ServletInformation;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 依据xml扫描当前Tomcat组件
 *
 * @author <a href="mailto:675464934@qq.com">Terrdi</a>
 * @date 2023/11/13
 * @since 1.8
 **/
public class XmlComponentScanner implements ComponentScanner {
    private final List<String> webListenerComponents = new ArrayList<>();

    private final Map<String, String> webServletComponents = new HashMap<>();

    private final Map<String, ServletInformation> servletInformationMap = new HashMap<>();

    private final Map<String, Set<String>> webFilterComponents = new HashMap<>();
    // 记录filter 顺序
    private final Map<String, Integer> webFilterComponentsOrder = new HashMap<>();

    private final String resource;

    public XmlComponentScanner(String resource) {
        this.resource = resource;

        this.init();
    }

    /**
     * 扫描xml结点信息完成初始化
     */
    private void init() {
        // 尝试读取出配置文件信息
        SAXReader reader = new SAXReader();
        Document document = null;

        try {
            URL url = this.getClass().getResource(this.resource);
            document = reader.read(url);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        // 扫描结点
        Iterator<Element> iterator = document.getRootElement().elementIterator();
        while (iterator.hasNext()) {
            Element currElement = iterator.next();

            String nodeName = currElement.getName();
            switch (nodeName) {
                case "display-name":
                    System.out.println("当前显示名称为: " + currElement.getText());
                    break;
                case "description":
                    System.out.println("当前应用描述为： " + currElement.getText());
                    break;
                case "welcome-file-list":
                    this.welcomeFileList(currElement);
                    break;
                case "listener":
                    this.listener(currElement);
                    break;
                case "servlet":
                    this.servlet(currElement);
                    break;
                case "servlet-mapping":
                    this.servletMapping(currElement);
                    break;
                case "filter":
                    this.filter(currElement);
                    break;
                case "filter-mapping":
                    this.filterMapping(currElement);
                    break;
            }
        }
    }

    private void servlet(final Element element) {
        ServletInformation servletInformation = new ServletInformation();

        // 获取到该servlet的相关信息
        final String servletName = element.elementText("servlet-name");
        final String servletClass = element.elementText("servlet-class");
        if (servletName == null || servletName.isEmpty()) {
            throw new IllegalArgumentException("xml 无法解析 servlet-name:\n" + element.asXML());
        }
        if (servletClass == null || servletClass.isEmpty()) {
            throw new IllegalArgumentException("xml 无法解析 servlet-class:\n" + element.asXML());
        }

        servletInformation.setClazzName(servletClass);
        servletInformation.setLoadOnStartup(Integer.parseInt(
                element.elementText("load-on-startup")));
        servletInformation.setInitParams(this.resolveInitParam(element));


        if (this.webServletComponents.containsKey(servletName)) {
            // 优先解析到了 servlet-mapping, 补充servlet信息
            String urlPattern = this.webServletComponents.remove(servletName);
            servletInformation.setUrlPattern(urlPattern.split(";"));
            for (String uri : servletInformation.getUrlPattern()) {
                this.servletInformationMap.put(uri, servletInformation);
                this.webServletComponents.put(uri, servletClass);
            }
        } else {
            this.servletInformationMap.put(servletName, servletInformation);
        }
    }

    private void servletMapping(final Element element) {
        final String servletName = element.elementText("servlet-name");
        String urlPattern = element.elementText("url-pattern");
        if (servletName == null || servletName.isEmpty()) {
            throw new IllegalArgumentException("xml 无法解析 servlet-name:\n" + element.asXML());
        }

        if (this.servletInformationMap.containsKey(servletName)) {
            // 已经解析了 servlet
            ServletInformation servletInformation = this.servletInformationMap.remove(servletName);
            servletInformation.setUrlPattern(urlPattern.split(";"));
            for (String uri : servletInformation.getUrlPattern()) {
                this.servletInformationMap.put(uri, servletInformation);
                this.webServletComponents.put(uri, servletInformation.getClazzName());
            }
        } else {
            // 还没有解析 servlet
            this.webServletComponents.put(servletName, urlPattern);
        }
    }

    /**
     * 解析出初始化参数结点
     * @param element servlet结点
     * @return 返回该servlet结点下所有初始化参数
     */
    private Map<String, String> resolveInitParam(final Element element) {
        List<Element> list = element.elements("init-param");

        Map<String, String> ret = new HashMap<>();
        for (Element element0 : list) {
            String paramName = element0.elementText("param-name");
            String paramValue = element0.elementText("param-value");
            ret.put(paramName, paramValue);
        }
        return ret;
    }

    private void welcomeFileList(final Element element) {
        System.out.println("开始欢迎页面的列表如下: ");
        List<Element> list = element.elements("welcome-file");

        list.stream().map(Node::getText).forEachOrdered(System.out::println);
    }

    private void listener(final Element element) {
        List<Element> list = element.elements("listener-class");

        if (list.isEmpty()) {
            System.err.println("找不到监听器实现类");
        } else {
            this.webListenerComponents.add(list.get(0).getText().trim());
        }
    }

    private void filter(final Element element) {
        final String filterName = element.elementText("filter-name");
        final String filterClass = element.elementText("filter-class");
        if (filterName == null || filterName.isEmpty()) {
            throw new IllegalArgumentException("xml 无法解析 servlet-name:\n" + element.asXML());
        }

        if (this.webFilterComponents.containsKey(filterName)) {
            // 优先解析到 filter-mapping
            // 填充映射
            Set<String> urlPatterns = this.webFilterComponents.remove(filterName);
            for (String uri : urlPatterns) {
                this.webFilterComponents.putIfAbsent(uri, new HashSet<>());
                this.webFilterComponents.get(uri).add(filterClass);
            }
        } else {
            // 还未解析到filter-mapping 存储
            this.webFilterComponents.put(filterName, Collections.singleton(filterClass));
        }
    }

    private void filterMapping(final Element element) {
        final String filterName = element.elementText("filter-name");
        final String urlPattern = element.elementText("url-pattern");
        if (filterName == null || filterName.isEmpty()) {
            throw new IllegalArgumentException("xml 无法解析 servlet-name:\n" + element.asXML());
        }

        if (this.webFilterComponents.containsKey(filterName)) {
            // 优先解析到 filter
            Set<String> singleonSet = this.webFilterComponents.remove(filterName);
            if (singleonSet.size() == 1) {
                singleonSet.forEach(className -> {
                    for (String uri : urlPattern.split(";")) {
                        this.webFilterComponents.putIfAbsent(uri, new HashSet<>());
                        this.webFilterComponents.get(uri).add(className);
                    }
                });
            } else {
                throw new IllegalStateException(filterName + " 过滤器解析失败!");
            }
        } else {
            this.webFilterComponents.put(filterName, Stream.of(urlPattern.split(";")).collect(Collectors.toSet()));
        }
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
