package com.example.hateoas;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CORSFilter implements Filter
{

    @Override
    public void init( FilterConfig filterConfig ) throws ServletException
    {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain )
            throws IOException, ServletException
    {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String origin = (String) servletRequest.getRemoteHost() + ":" + servletRequest.getRemotePort();
        response.setHeader( "Access-Control-Allow-Origin", "*" );
        response.setHeader( "Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS" );
        response.setHeader( "Access-Control-Max-Age", "3600" );
        //you can also use * here to allow all types header
        response.setHeader( "Access-Control-Allow-Headers", "Content-Type, x-requested-with, Authorization, refresh-token" );
        response.setHeader( "Access-Control-Allow-Credentials", "false" );
        filterChain.doFilter( servletRequest, servletResponse );
    }

    @Override
    public void destroy()
    {
    }
}