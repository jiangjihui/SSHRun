package com.briup.run.web.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.CookiesAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.briup.run.common.bean.Memberinfo;
import com.briup.run.common.exception.MemberServiceException;
import com.briup.run.common.util.BeanFactory;
import com.briup.run.service.IMemberService;
import com.opensymphony.xwork2.ActionSupport;

import oracle.net.aso.a;

public class CheckLoginAction extends ActionSupport implements SessionAware, ServletRequestAware
{
	private Memberinfo memberinfo;			// 实现当前用户的自动登录

	private Map<String, Object> session;

	private HttpServletRequest request;		// 实现Cookie用户的自动登录

	private IMemberService memberService;

	public String execute()
	{
		System.out.println("CheckLoginAction: "+this);
		Cookie[] cookies = request.getCookies();
		
		if (memberinfo != null)
		{
			System.out.println("\n+++++++++++++++++++++++++++memberinfo\n");
		}
		
		// 实现当前用户的自动登录
		if (memberinfo != null)
		{
			try
			{
				Memberinfo member = memberService.login(memberinfo.getNickname(), memberinfo.getPassword());
				if (member != null)
				{
					return SUCCESS;
				}
			} 
			catch (MemberServiceException e)
			{
				e.printStackTrace();
			}
		}
		
		// 实现Cookie用户的自动登录
		// 检查是否存在Cookie
		if (cookies == null)
		{
			return "error";
		}

		String name = null;
		String passwd = null;

		// 从Cookie中得到用户名和密码
		try
		{
			for (Cookie c : cookies)
			{
				if (c.getName().equals("name"))
				{
					name = URLDecoder.decode(c.getValue(), "utf-8");
				}
				else if (c.getName().equals("passwd"))
				{
					passwd = c.getValue();
				}
			}
			Memberinfo m = memberService.login(name, passwd);
			if (m != null)
			{
				session.put("memberinfo", m);
				return SUCCESS;
			}
			return "error";
		} catch (UnsupportedEncodingException | MemberServiceException e1)
		{
			e1.printStackTrace();
		}
		return "error";
	}

	@Override
	public void setSession(Map<String, Object> arg0)
	{
		this.session = arg0;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
	}

	public Memberinfo getMemberinfo()
	{
		return memberinfo;
	}

	public void setMemberinfo(Memberinfo memberinfo)
	{
		this.memberinfo = memberinfo;
	}

	public IMemberService getMemberService()
	{
		return memberService;
	}

	public void setMemberService(IMemberService memberService)
	{
		this.memberService = memberService;
	}
	
}
