package com.briup.run.web.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.Session;

import com.briup.run.common.bean.Memberinfo;
import com.briup.run.common.exception.MemberServiceException;
import com.briup.run.common.util.BeanFactory;
import com.briup.run.service.IMemberService;
import com.opensymphony.xwork2.ActionSupport;

public class RegisterAction extends ActionSupport implements SessionAware
{
	private Memberinfo memberinfo;
	
	private String authCode;		//获取用户输入的验证码

	Map<String, Object> session;		//存储servlet生成的验证码
	
	private String msg;		//显示在页面上的异常信息
	
	private IMemberService memberService;
	
	public String execute()
	{
		// 判断用户输入 的验证码是否正确
		String code=(String) session.get("authCode");
		if (!code.equals(authCode))
		{
			msg="输入验证码错误";
			return "error";
		}
		
		try
		{
			memberService.registerMemberinfo(memberinfo);
			return SUCCESS;
		}
		catch (MemberServiceException e)
		{
			e.printStackTrace();
			msg="注册失败，"+e.getMessage();
			return "error";
		}
	}

	public Memberinfo getMemberinfo()
	{
		return memberinfo;
	}
	public void setMemberinfo(Memberinfo memberinfo)
	{
		this.memberinfo = memberinfo;
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

	public IMemberService getMemberService()
	{
		return memberService;
	}

	public void setMemberService(IMemberService memberService)
	{
		this.memberService = memberService;
	}
	
}
