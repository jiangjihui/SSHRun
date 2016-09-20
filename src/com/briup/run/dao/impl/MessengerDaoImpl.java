package com.briup.run.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.briup.run.common.bean.Memberinfo;
import com.briup.run.common.bean.Messagerecord;
import com.briup.run.common.exception.DataAccessException;
import com.briup.run.common.util.HibernateSessionFactory;
import com.briup.run.dao.IMessengerDao;

public class MessengerDaoImpl implements IMessengerDao
{
	private HibernateTemplate hibernateTemplate;
	
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}
	
	@Override
	public Integer findNewMessageNum(String nickname) throws DataAccessException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer findMemberinfoNum() throws DataAccessException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Memberinfo findOneMemberinfo(int sum) throws DataAccessException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List findFriends(String age, String gender, String city) throws DataAccessException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveMessage(Messagerecord message) throws DataAccessException
	{
//		Session session=HibernateSessionFactory.getSession();
//		session.saveOrUpdate(message);
		hibernateTemplate.saveOrUpdate(message);
	}

	@Override
	public List<Messagerecord> listSendMessage(String senderName) throws DataAccessException
	{
//		Session session=HibernateSessionFactory.getSession();
//		Query query=session.createQuery("from Messagerecord where sender=? and senderstatus=?");
//		query.setString(0, senderName);
//		query.setString(1, "0");		// 筛选未删除的消息
//		return query.list();
		String hql="from Messagerecord where sender=? and senderstatus=?";
		return hibernateTemplate.find(hql, new String[]{senderName,"0"});
		
	}

	@Override
	public List<Messagerecord> listRecieveMessage(String recieveName) throws DataAccessException
	{
//		Session session=HibernateSessionFactory.getSession();
//		Query query=session.createQuery("from Messagerecord where receiver=? and receiverstatus=?");
//		query.setString(0, recieveName);
//		query.setString(1, "0");		// 筛选未删除的消息
//		return query.list();
		String hql="from Messagerecord where receiver=? and receiverstatus=?";
		return hibernateTemplate.find(hql, recieveName,"0");
	}

	@Override
	public Messagerecord findMessage(Long id) throws DataAccessException
	{
//		Session session=HibernateSessionFactory.getSession();
//		Query query=session.createQuery("from Messagerecord where id=?");
//		query.setLong(0, id);
//		List<Messagerecord> list=query.list();
		String hql="from Messagerecord where id=?";
		List list = hibernateTemplate.find(hql, id);
		if (list.size()>0)
		{
			return (Messagerecord) list.get(0);
		}
		return null;
	}

	@Override
	public void deleteRecieveMessage(Long id) throws DataAccessException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSendMessage(Long id) throws DataAccessException
	{
		// TODO Auto-generated method stub
		
	}

}
