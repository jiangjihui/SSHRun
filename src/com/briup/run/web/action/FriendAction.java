package com.briup.run.web.action;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.Session;

import com.briup.run.common.bean.Memberinfo;
import com.briup.run.common.exception.MemberServiceException;
import com.briup.run.common.exception.MessengerServiceException;
import com.briup.run.common.util.BeanFactory;
import com.briup.run.service.IMemberService;
import com.briup.run.service.IMessengerService;
import com.opensymphony.xwork2.ActionSupport;

public class FriendAction extends ActionSupport implements SessionAware
{
	// 从页面得到的参数
	private String friendname;
	private String blackname;
	private String msg;
	private Map<String, Object> session;
	
	private IMessengerService messengerService;
	private IMemberService memberService;

	// 需要传给页面的参数
	private List<Memberinfo> memberList;
	private List<Memberinfo> friendList;
	private List<Memberinfo> blackList;

	// 获得所有的会员列表
	public String listMembers()
	{
		try
		{
			memberList = messengerService.findFriends("unlimited", "unlimited", "unlimited");
		} 
		catch (MessengerServiceException e)
		{
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	// 添加好友
	public String addFriend()
	{
		Memberinfo memberinfo = (Memberinfo) session.get("memberinfo");
		
		if (memberinfo==null)
		{
			return "error";
		}
		try
		{
			memberService.saveOrUpdate(memberinfo.getNickname(), friendname);
			return SUCCESS;
		}
		catch (MemberServiceException e)
		{
			msg=e.getMessage();
			e.printStackTrace();
			return "error";
		}
	}
	
	// 列出好友
	public String listFriend()
	{
		Memberinfo memberinfo = (Memberinfo) session.get("memberinfo");
		if (memberinfo!=null)
		{
			try
			{
				friendList=memberService.listFriend(memberinfo.getNickname());
			} 
			catch (MemberServiceException e)
			{
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	// 按条件查找会员
	private String age="unlimited";
	private String gender="unlimited";
	private String provinceCity="unlimited";
	
	public String matchFriend()
	{
		try
		{
			memberList = messengerService.findFriends(age, gender, provinceCity);
		} 
		catch (MessengerServiceException e)
		{
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	// 随机找好友
	public String randFriend()
	{
		Memberinfo memberinfo = (Memberinfo) session.get("memberinfo");
		listMembers();
		try
		{
			memberList = messengerService.randFriend(memberinfo.getNickname(),memberList);
		} 
		catch (MessengerServiceException e)
		{
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	// 删除好友，并且将其移入黑名单
	public String removeFriend()
	{
		Memberinfo memberinfo = (Memberinfo) session.get("memberinfo");
		
		if (memberinfo==null)
		{
			return "error";
		}
		try
		{
			memberService.removeFriend(memberinfo.getNickname(), friendname);
			return SUCCESS;
		}
		catch (MemberServiceException e)
		{
			msg=e.getMessage();
			e.printStackTrace();
			return "error";
		}
	}
	
	// 列出黑名单
	public String listBlackFriend()
	{
		Memberinfo memberinfo = (Memberinfo) session.get("memberinfo");
		if (memberinfo!=null)
		{
			try
			{
				blackList=memberService.listBlackFriend(memberinfo.getNickname());
			} 
			catch (MemberServiceException e)
			{
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}
	
	// 删除黑名单
	public String removeBlack()
	{
		Memberinfo memberinfo = (Memberinfo) session.get("memberinfo");
		
		if (memberinfo==null)
		{
			return "error";
		}
		try
		{
			memberService.deleleBlack(memberinfo.getNickname(), blackname);
			return SUCCESS;
		}
		catch (MemberServiceException e)
		{
			msg=e.getMessage();
			e.printStackTrace();
			return "error";
		}
	}

	
	// 提供Get、Set方法
	public List<Memberinfo> getMemberList()
	{
		return memberList;
	}

	public void setMemberList(List<Memberinfo> memberList)
	{
		this.memberList = memberList;
	}

	public String getFriendname()
	{
		return friendname;
	}

	public void setFriendname(String friendname)
	{
		this.friendname = friendname;
	}

	public String getBlackname()
	{
		return blackname;
	}

	public void setBlackname(String blackname)
	{
		this.blackname = blackname;
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
		this.session=arg0;
	}

	public List<Memberinfo> getFriendList()
	{
		return friendList;
	}

	public void setFriendList(List<Memberinfo> friendList)
	{
		this.friendList = friendList;
	}

	public List<Memberinfo> getBlackList()
	{
		return blackList;
	}

	public void setBlackList(List<Memberinfo> blackList)
	{
		this.blackList = blackList;
	}

	public String getAge()
	{
		return age;
	}

	public void setAge(String age)
	{
		this.age = age;
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

	public IMessengerService getMessengerService()
	{
		return messengerService;
	}

	public void setMessengerService(IMessengerService messengerService)
	{
		this.messengerService = messengerService;
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
