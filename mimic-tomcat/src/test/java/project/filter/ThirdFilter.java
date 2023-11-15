package project.filter;

import com.attackonarchitect.filter.WebFilter;
import com.attackonarchitect.filter.chain.Filter;
import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;

import java.io.UnsupportedEncodingException;

/**
 * @description:
 */
@WebFilter("/hello/a/*")
public class ThirdFilter implements Filter {

    @Override
    public boolean doFilter(MTRequest request, MTResponse response) throws UnsupportedEncodingException {
        response.write("pass filter third /hello/a/*");
        return true;
    }
}
