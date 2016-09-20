package com.briup.run.web.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.Session;

import com.briup.run.common.bean.Memberinfo;
import com.briup.run.common.exception.MemberServiceException;
import com.briup.run.common.util.BeanFactory;
import com.briup.run.service.IMemberService;
import com.opensymphony.xwork2.ActionSupport;

public class ModifyAction extends ActionSupport implements SessionAware
{
	private String oldPasswd;
	private String newPasswd;
	private String email;
	private String passwdQuestion;
	private String passwdAnswer;
	private String gender;
	private String provinceCity;
	private String phone;
	private String address;
	
	private Map<String, Object> session;
	
	private String msg;		//显示在页面上的异常信息
	
	private IMemberService memberService;
	
	public String execute()
	{
		// 得到正在修改信息的用户对象
		Memberinfo memberinfo = (Memberinfo) session.get("memberinfo");
		
		if ("".equals(oldPasswd)||!memberinfo.getPassword().equals(oldPasswd))
		{
			msg="输入的旧密码不正确";
			return "error";
		}
		
		memberinfo.setAddress(address);
		memberinfo.setEmail(email);
		memberinfo.setGender(gender);
		memberinfo.setPassword(newPasswd);
		memberinfo.setProvincecity(provinceCity);
		memberinfo.setPasswordquestion(passwdQuestion);
		memberinfo.setPasswordanswer(passwdAnswer);
		memberinfo.setPhone(phone);
		
		try
		{
			memberService.saveOrUpdate(memberinfo);
			return SUCCESS;
		}
		catch (MemberServiceException e)
		{
			e.printStackTrace();
			msg="修改信息失败，"+e.getMessage();
			return "error";
		}
	}
	
	public String getOldPasswd()
	{
		return oldPasswd;
	}

	public void setOldPasswd(String oldPasswd)
	{
		this.oldPasswd = oldPasswd;
	}

	public String getNewPasswd()
	{
		return newPasswd;
	}

	public void setNewPasswd(String newPasswd)
	{
		this.newPasswd = newPasswd;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
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

	public String getGender()
	{
		return gender;
	}

	public void setGender(String gender)
	{
		this.gender = gender;
	}

	public String getProvinceCity()
	{
		return provinceCity;
	}

	public void setProvinceCity(String provinceCity)
	{
		this.provinceCity = provinceCity;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getMsg()
	{
		return msg;
	}
	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	@Override
	public void setSession(Map<String, Object> arg0)
	{
		this.session= arg0;
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
