import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @description:
 */
public class TestMimicTomcat {

    @Test
    public void test() throws IOException, URISyntaxException {
        URL systemResources = ClassLoader.getSystemResource("xyz/cybertheye/test/project/servlet");
        File file1 = new File(systemResources.toURI());
        System.out.println(file1.getPath());

    }

}
