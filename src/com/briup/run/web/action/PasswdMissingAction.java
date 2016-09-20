package com.briup.run.web.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.briup.run.common.exception.MemberServiceException;
import com.briup.run.common.util.BeanFactory;
import com.briup.run.service.IMemberService;
import com.opensymphony.xwork2.ActionSupport;

public class PasswdMissingAction extends ActionSupport implements SessionAware
{
	private String userName;
	private String passwdQuestion;
	private String passwdAnswer;
	
	private String msg;

	private IMemberService memberService;
	private Map<String, Object> session;
	
	public String execute()
	{
		try
		{
			msg=memberService.getBackPasswd(userName, passwdQuestion, passwdAnswer);
			return SUCCESS;
		} 
		catch (MemberServiceException e)
		{
			msg=e.getMessage();
			e.printStackTrace();
			return "error";
		}
	}

	@Override
	public void setSession(Map<String, Object> arg0)
	{
		this.session =arg0;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPasswdQuestion()
	{
		return passwdQuestion;
	}

	public void setPasswdQuestion(String passwdQuestion)
	{
		this.passwdQuestion = passwdQuestion;
	}

	public String getPasswdAnswer()
	{
		return passwdAnswer;
	}

	public void setPasswdAnswer(String passwdAnswer)
	{
		this.passwdAnswer = passwdAnswer;
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
