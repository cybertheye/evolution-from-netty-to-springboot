package project.servlet;

import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;
import com.attackonarchitect.servlet.MimicServlet;
import com.attackonarchitect.servlet.WebServlet;

import java.io.UnsupportedEncodingException;

/**
 * @description:
 */
@WebServlet("/*")
public class DefaultServlet extends MimicServlet {


    @Override
    public void service(MTRequest req, MTResponse response) throws UnsupportedEncodingException {
        super.service(req, response);

    }

    @Override
    protected void doPost(MTRequest req, MTResponse response) {

    }

    @Override
    protected void doGet(MTRequest req, MTResponse response) throws UnsupportedEncodingException {
        response.write(this.getClass().getName() + " inside /*");
    }
}
