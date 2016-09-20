package com.briup.run.web.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.CookiesAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.briup.run.common.bean.Memberinfo;
import com.briup.run.common.exception.MemberServiceException;
import com.briup.run.common.util.BeanFactory;
import com.briup.run.service.IMemberService;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport implements SessionAware,CookiesAware,ServletResponseAware
{
	private String name;
	
	private String passwd;
	
	private String authCode;		// 获取用户输入的验证码

	Map<String, Object> session;		// 存储servlet生成的验证码
	
	private String autoLogin;		// 完成自动登陆所使用的变量
	
	private String msg;		// 显示在页面上的异常信息
	
	private Map<String, String> cookie;
	
	private HttpServletResponse response;
	
	private IMemberService memberService;
	
	@Override
	public String execute()
	{
		// 判断用户输入的验证码是否正确
		System.out.println("LoginAction: "+this);
		String code=(String) session.get("authCode");
		if (authCode==null || "".equals(authCode) || !code.equals(authCode))
		{
			msg="输入验证码错误";
			System.out.println("输入验证码错误");
			return "error";
		}
		
		try
		{
			Memberinfo m = memberService.login(name, passwd);
			session.put("memberinfo", m);
			
			// 判断是够是自动登陆
			if ((autoLogin!=null)&&(autoLogin.equals("0")))
			{
				// 创建cookie对象
				// 如果Cookie的值是中文，则需要编码URLEncoder类中的encode（String s）方法
				Cookie nameCookie=new Cookie("name", URLEncoder.encode(m.getNickname(),"utf-8"));
				Cookie passwdCookie=new Cookie("passwd", m.getPassword());
				
				// 设置Cookie的生命时间是2周，将Cookie写回浏览器
				nameCookie.setMaxAge(60*60*24*14);
				passwdCookie.setMaxAge(60*60*24*14);
				
				// 将Cookie写会浏览器
//				ServletActionContext.getResponse().addCookie(nameCookie);
//				ServletActionContext.getResponse().addCookie(passwdCookie);
				response.addCookie(nameCookie);
				response.addCookie(passwdCookie);
				
			}
			
			return SUCCESS;
		}
		catch (MemberServiceException | UnsupportedEncodingException e)
		{
			e.printStackTrace();
			msg=e.getMessage();
			return "error";
		}
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getPasswd()
	{
		return passwd;
	}
	public void setPasswd(String passwd)
	{
		this.passwd = passwd;
	}
	public String getAuthCode()
	{
		return authCode;
	}
	public void setAuthCode(String authCode)
	{
		this.authCode = authCode;
	}
	@Override
	public void setSession(Map<String, Object> arg0)
	{
		this.session=arg0;
	}
	public String getMsg()
	{
		return msg;
	}
	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public String getAutoLogin()
	{
		return autoLogin;
	}

	public void setAutoLogin(String autoLogin)
	{
		this.autoLogin = autoLogin;
	}

	@Override
	public void setCookiesMap(Map<String, String> arg0)
	{
		this.cookie=arg0;
	}

	@Override
	public void setServletResponse(HttpServletResponse arg0)
	{
		this.response=arg0;
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
