import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

/**
 * @description:
 */
public class TestMimicTomcat {

    @Test
    public void test() throws IOException, URISyntaxException {
        URL systemResources = ClassLoader.getSystemResource("test.properties");
        File file1 = new File(systemResources.toURI());
        System.out.println(file1.getPath());

        Properties properties = new Properties();
        properties.load(ClassLoader.getSystemResourceAsStream("test.properties"));
        properties.stringPropertyNames().forEach(x -> {
            System.out.println( x + " -> " + properties.getProperty(x));
        });

    }

}
