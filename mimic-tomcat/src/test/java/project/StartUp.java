package project;

import com.attackonarchitect.MimicTomcatServer;
import com.attackonarchitect.WebScanPackage;

/**
 * @description:
 */

@WebScanPackage
public class StartUp {
    public static void main(String[] args) {
        new MimicTomcatServer(9999).start(StartUp.class);
    }
}
