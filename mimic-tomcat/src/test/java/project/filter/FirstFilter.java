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
@WebFilter(value = "/*",order = -1)
public class FirstFilter implements Filter {

    @Override

    public boolean doFilter(MTRequest request, MTResponse response, FilterChain filterChain) throws UnsupportedEncodingException {
        response.write("pass filter /*");
        filterChain.start(request, response);

        return true;
    }
}
