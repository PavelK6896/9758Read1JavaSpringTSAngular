package app.web.pavelk.read1.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilterResponse implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request2 = (HttpServletRequest) request;
        HttpServletResponse response2 = (HttpServletResponse) response;

        response2.setHeader("EXemPLE----!!555555555555!!------------", "HEeADERs");
//        response2.setHeader("Access-Control-Allow-Origin", "*");
//        response2.setHeader("Access-Control-Allow-Credentials", "true");
//        response2.setHeader("Access-Control-Allow-Methods",
//                "ACL, CANCELUPLOAD, CHECKIN, CHECKOUT, COPY, " +
//                        "DELETE, GET, HEAD, LOCK, MKCALENDAR, MKCOL, MOVE, OPTIONS, POST, " +
//                        "PROPFIND, PROPPATCH, PUT, REPORT, SEARCH, UNCHECKOUT, UNLOCK, UPDATE, VERSION-CONTROL");
//
//        response2.setHeader("Access-Control-Max-Age", "3600");
//        response2.setHeader("Access-Control-Allow-Headers",
//                "Origin, X-Requested-With, Content-Type, Accept, Key, Authorization");

        chain.doFilter(request2, response2);
    }
}
