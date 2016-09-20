package com.briup.run.web.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.briup.run.common.bean.Memberinfo;
import com.briup.run.common.bean.Messagerecord;
import com.briup.run.common.exception.MessengerServiceException;
import com.briup.run.common.util.BeanFactory;
import com.briup.run.service.IMessengerService;
import com.opensymphony.xwork2.ActionSupport;

public class MessageAction extends ActionSupport implements SessionAware
{
	// 接受从页面传来的对象
	private Messagerecord message;
	private List<Messagerecord> messageList;

	private Map<String, Object> session;
	
	private IMessengerService messengerService;
	
	// 写消息的Action
	public String writeMessage()
	{
		Memberinfo memberinfo = (Memberinfo) session.get("memberinfo");
		
		// 初始化message
		message.setSenddate(new Date());
		message.setSender(memberinfo.getNickname());
		message.setStatus(0L);
		message.setSenderstatus(0L);
		message.setReceiverstatus(0L);
		try
		{
			messengerService.saveMessage(message);
		} 
		catch (MessengerServiceException e)
		{
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	// 发件箱Action
	public String listSendMessage()
	{
		Memberinfo memberinfo = (Memberinfo) session.get("memberinfo");
		try
		{
			messageList=messengerService.listSendMessage(memberinfo.getNickname());
		} 
		catch (MessengerServiceException e)
		{
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	// 收件箱Action
	public String listRecieveMessage()
	{
		Memberinfo memberinfo = (Memberinfo) session.get("memberinfo");
		try
		{
			messageList=messengerService.listRecieveMessage(memberinfo.getNickname());
		} 
		catch (MessengerServiceException e)
		{
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	// 显示消息Action
	public String showMessage()
	{
		try
		{
			message= messengerService.findMessage(message.getId());
		} 
		catch (MessengerServiceException e)
		{
			e.printStackTrace();
		}
		return SUCCESS;
	}
			

	// 提供Get、Set方法
	public Messagerecord getMessage()
	{
		return message;
	}

	public void setMessage(Messagerecord message)
	{
		this.message = message;
	}

	@Override
	public void setSession(Map<String, Object> arg0)
	{
		this.session=arg0;
	}

	public List<Messagerecord> getMessageList()
	{
		return messageList;
	}

	public void setMessageList(List<Messagerecord> messageList)
	{
		this.messageList = messageList;
	}

	public IMessengerService getMessengerService()
	{
		return messengerService;
	}

	public void setMessengerService(IMessengerService messengerService)
	{
		this.messengerService = messengerService;
	}
	
}
