package project.filter;

import com.attackonarchitect.filter.WebFilter;
import com.attackonarchitect.filter.chain.Filter;
import com.attackonarchitect.filter.chain.FilterChain;
import com.attackonarchitect.http.MTRequest;
import com.attackonarchitect.http.MTResponse;
import com.attackonarchitect.http.session.HttpSession;

import java.io.UnsupportedEncodingException;

/**
 * @description:
 * 模拟业务场景: 通过过滤器判断用户是否登录
 */
@WebFilter(value = "/*",order = 999)
public class SessionFilter implements Filter {

    @Override

    public boolean doFilter(MTRequest request, MTResponse response, FilterChain filterChain) throws UnsupportedEncodingException {

        response.write("pass Session filter /*");
        HttpSession session = request.getSession();
        if(session == null){
            // 无sessionid,或session过期,假设为登录过期
            response.write("not login");
            filterChain.start(request, response);
            return false;
        }else {
            // 有sessionid,假设为登录状态仍在
            response.write("have logged on:" + session.getSessionId());
            filterChain.start(request, response);
            return true;
        }

    }
}
