package com.attackonarchitect;

import com.attackonarchitect.servlet.ServletInformation;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.*;

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
            this.webServletComponents.put(urlPattern, servletClass);
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
            this.servletInformationMap.put(urlPattern, servletInformation);
            this.webServletComponents.put(urlPattern, servletInformation.getClazzName());
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
}
