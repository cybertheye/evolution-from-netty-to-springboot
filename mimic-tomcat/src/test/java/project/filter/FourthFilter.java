package project.filter;

import com.attackonarchitect.filter.WebFilter;
import com.attackonarchitect.filter.chain.Filter;
import com.attackonarchitect.filter.chain.FilterChain;
import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;

import java.io.UnsupportedEncodingException;

/**
 * @description:
 */
@WebFilter("/hello/a/b/*")
public class FourthFilter implements Filter {

    @Override
    public boolean doFilter(MTRequest request, MTResponse response, FilterChain filterChain) throws UnsupportedEncodingException {

        response.write("pass filter /hello/a/b/*");
        filterChain.start(request, response);

        response.write("after pass");
        return true;
    }
}
