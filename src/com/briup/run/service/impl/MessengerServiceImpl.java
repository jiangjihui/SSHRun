package com.briup.run.service.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.briup.run.common.bean.Friendrecord;
import com.briup.run.common.bean.Memberinfo;
import com.briup.run.common.bean.Messagerecord;
import com.briup.run.common.exception.DataAccessException;
import com.briup.run.common.exception.MessengerServiceException;
import com.briup.run.common.util.BeanFactory;
import com.briup.run.common.util.HibernateSessionFactory;
import com.briup.run.dao.IMemberDao;
import com.briup.run.dao.IMessengerDao;
import com.briup.run.service.IMessengerService;

public class MessengerServiceImpl implements IMessengerService
{
	private IMessengerDao messengerDao;
	private IMemberDao memberDao;
	
	public IMessengerDao getMessengerDao()
	{
		return messengerDao;
	}

	public void setMessengerDao(IMessengerDao messengerDao)
	{
		this.messengerDao = messengerDao;
	}

	public IMemberDao getMemberDao()
	{
		return memberDao;
	}

	public void setMemberDao(IMemberDao memberDao)
	{
		this.memberDao = memberDao;
	}

	@Override
	public Integer findNewMessageNum(String nickname) throws MessengerServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Memberinfo findOneMemberinfo() throws MessengerServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Memberinfo> findFriends(String age, String gender, String city) throws MessengerServiceException
	{
		Session session=HibernateSessionFactory.getSession();
		// from Memberinfo where (age>=:min and age<=max) and gender=:sex and city=:city
		Long min=0L;
		Long max=200L;
		
		if (!"unlimited".equals(age))
		{
			if ("1".equals(age))
			{
				min=10L;
				max=19L;
			}
			else if ("2".equals(age))
			{
				min=20L;
				max=29L;
			}
			else if ("3".equals(age))
			{
				min=30L;
				max=39L;
			}
		}
		String hql="from Memberinfo where (age>=:min and age<=:max)";

		if (!"unlimited".equals(gender))
		{
			hql+=" and gender = :gender";
		}

		if (!"unlimited".equals(city))
		{
			hql+=" and provincecity = :city";
		}
		System.out.println("\n+++++++hql:"+hql);
		Query query=session.createQuery(hql);
		query.setLong("min", min);
		query.setLong("max", max);
		if (!"unlimited".equals(gender))
		{
			query.setString("gender", gender);
		}
		if (!"unlimited".equals(city))
		{
			query.setString("city", city);
		}
		return query.list();
	}

	@Override
	public void saveMessage(Messagerecord message) throws MessengerServiceException
	{
		
		try
		{
			messengerDao.saveMessage(message);
		}
		catch (DataAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<Messagerecord> listSendMessage(String senderName) throws MessengerServiceException
	{
		try
		{
			return messengerDao.listSendMessage(senderName);
		} 
		catch (DataAccessException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Messagerecord> listRecieveMessage(String recieveName) throws MessengerServiceException
	{
		try
		{
			return messengerDao.listRecieveMessage(recieveName);
		} 
		catch (DataAccessException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Messagerecord findMessage(Long id) throws MessengerServiceException
	{
		// 消息标记已阅读
		try
		{
			Messagerecord message=messengerDao.findMessage(id);
			message.setStatus(1L);
			messengerDao.saveMessage(message);
			return message;
		} 
		catch (DataAccessException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void delRecieveMessage(Long id) throws MessengerServiceException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delSendMessage(Long id) throws MessengerServiceException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Memberinfo> randFriend(String selfname,List<Memberinfo> memberList) throws MessengerServiceException
	{
		try
		{
			List<Memberinfo> listFriend = memberDao.listFriend(selfname);
			if (listFriend.size()==memberList.size())		// 如果所有的会员都是好友，返回null
			{
				return null;
			}
			while(true)
			{
				int rand=(int)(Math.random()*memberList.size());		// 随机数
				String friendname=memberList.get(rand).getNickname();
				// 随机找到的会员是自己则进入下次循环
				if (friendname.equals(selfname))
				{
					continue;
				}
				Friendrecord friendrecord=memberDao.findfriend(selfname, friendname);
				if (friendrecord==null)		// 互相不是好友
				{
					// 将该会员加入list
					Memberinfo m=memberList.get(rand);
					memberList.clear();
					memberList.add(m);
					return memberList;
				}
			}
		} 
		catch (DataAccessException e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
