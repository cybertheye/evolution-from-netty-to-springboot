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
@WebServlet("/hello/a/*")
public class SecondServlet extends MimicServlet {
    @Override
    protected void doPost(MTRequest req, MTResponse response) {

    }

    @Override
    protected void doGet(MTRequest req, MTResponse response) throws UnsupportedEncodingException {
        response.writeAndFlush(this.getClass().getName() + " inside /hello/a/*");
        ServletContext servletContext = getServletContext();
        Object name = servletContext.getAttribute("name");
        System.out.println("get attribute from servletcontext: "+name);
    }
}
