package com.briup.run.web.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.briup.run.common.bean.Memberinfo;
import com.briup.run.common.bean.Memberspace;
import com.briup.run.common.exception.MemberServiceException;
import com.briup.run.common.util.BeanFactory;
import com.briup.run.common.util.Util;
import com.briup.run.service.IMemberService;
import com.opensymphony.xwork2.ActionSupport;

public class MemberSpaceAction extends ActionSupport implements SessionAware
{
	private Memberspace memberspace;

	private Map<String, Object> session; // 得到用户

	private IMemberService memberService;

	public String execute()
	{
		// 得到用户对象
		Memberinfo m = (Memberinfo) session.get("memberinfo");
		try
		{
			if (memberService.isMemberspace(m.getId()))
			{
				// 将用户的城市转换后放入session
				session.put("memberCity", Util.getProvinceNameById(m.getProvincecity()));
				
				memberspace=memberService.findSpace(m); 
				session.put("memberspace", memberspace);
				return SUCCESS;
			}
			else
			{
				return "error";
			}
		} catch (MemberServiceException e)
		{
			e.printStackTrace();
			return "error";
		}
	}

	@Override
	public void setSession(Map<String, Object> arg0)
	{
		this.session = arg0;
	}

	public Memberspace getMemberspace()
	{
		return memberspace;
	}

	public void setMemberspace(Memberspace memberspace)
	{
		this.memberspace = memberspace;
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
