package com.briup.run.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.hibernate.Session;

import com.briup.run.common.util.HibernateSessionFactory;

/**
 * Servlet Filter implementation class LoginFilter
 */
//@WebFilter("/LoginFilter")
public class CommitFilter implements Filter {

    /**
     * Default constructor. 
     */
    public CommitFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		// 设置为原子操作
		Session session = HibernateSessionFactory.getSession();
		session.beginTransaction();
		System.out.println("事物开始");
		
		chain.doFilter(request, response);
		
		// 提交事务
		session.getTransaction().commit();
		System.out.println("事物结束"); 
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
