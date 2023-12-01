package project.servlet.subservlet;

import com.attackonarchitect.context.ServletContext;
import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;
import com.attackonarchitect.servlet.MimicServlet;
import com.attackonarchitect.servlet.WebServlet;

import java.io.UnsupportedEncodingException;

/**
 * @description:
 */
@WebServlet("/hello/a/b")
public class FourhServlet extends MimicServlet {
    @Override
    protected void doPost(MTRequest req, MTResponse response) {

    }

    @Override
    protected void doGet(MTRequest req, MTResponse response) throws UnsupportedEncodingException {
        ServletContext servletContext = getServletContext();
        servletContext.setAttribute("name","cy");
        req.getParameters().forEach((key,value)->{
            System.out.println(key+"="+value);
        });
        response.write(this.getClass().getName() +  " /hello/a/b : uri="+req.uri());
    }
}
