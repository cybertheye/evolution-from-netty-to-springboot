package project.servlet;

import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;
import com.attackonarchitect.http.cookie.MTCookieBuilder;
import com.attackonarchitect.servlet.MimicServlet;
import com.attackonarchitect.servlet.WebServlet;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 */

@WebServlet("/hello/*")
public class FirstServlet extends MimicServlet {

    @Override
    protected void doPost(MTRequest req, MTResponse response) {

    }
    @Override
    protected void doGet(MTRequest req, MTResponse response) throws UnsupportedEncodingException {
        // 打印Header
        System.out.println("当前请求Header: ");
        for (Iterator<String> headerNames = req.getHeaderNames(); headerNames.hasNext(); ) {
            String name = headerNames.next();
            System.out.printf("%s: %s%s", name, req.getHeader(name), System.lineSeparator());
        }

        System.out.println("当前请求的Cookie: ");
        for (Iterator<String> cookieNames = req.getCookieNames(); cookieNames.hasNext(); ) {
            String name = cookieNames.next();
            System.out.printf("%s: %s%s", name, req.getCookie(name), System.lineSeparator());
        }

        response.write(this.getClass().getName() + " inside /hello/*");
        response.setCookie("servlet-name", "FirstServlet");

        // servlet-class 两分钟后失效
        response.setCookie(MTCookieBuilder.newBuilder()
                        .name("servlet-class")
                        .value(this.getClass().getName())
                        .httpOnly()
                        .expireAfter(2, TimeUnit.MINUTES)
                        .build());

        response.addHeader("china", "niubi");
    }
}
