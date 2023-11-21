package project;

import com.attackonarchitect.MimicTomcatServer;
import com.attackonarchitect.WebScanPackage;
import com.sun.tools.javac.util.ServiceLoader;

/**
 * @description:
 */

@WebScanPackage
public class StartUp {
    public static void main(String[] args) {
        // 注解启动
//        new MimicTomcatServer(9999).start(StartUp.class);

        // xml启动
        new MimicTomcatServer(9999).start("/WEB-INF/web.xml");

        // SPI 启动
//        new MimicTomcatServer(9999).start(StartUp.class.getClassLoader());
    }
}
