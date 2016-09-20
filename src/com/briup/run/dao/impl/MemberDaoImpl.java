package com.briup.run.dao.impl;

/**
 * 用户数据库操作类
 * @author jjh
 * 2016-09-20
 */

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.briup.run.common.bean.Blackrecord;
import com.briup.run.common.bean.Friendrecord;
import com.briup.run.common.bean.Graderecord;
import com.briup.run.common.bean.Memberinfo;
import com.briup.run.common.bean.Memberspace;
import com.briup.run.common.bean.Pointaction;
import com.briup.run.common.bean.Pointrecord;
import com.briup.run.common.exception.DataAccessException;
import com.briup.run.dao.IMemberDao;

public class MemberDaoImpl implements IMemberDao
{
	private HibernateTemplate hibernateTemplate;
	
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
		System.out.println("sessionFactory: "+sessionFactory);
		System.out.println("create：hibernateTemplate"+hibernateTemplate);
		System.out.println("MemberDaoImpl "+this);
	}

	@Override
	public Memberinfo findMemberinfoByName(String name) throws DataAccessException
	{
//		Session session=HibernateSessionFactory.getSession();
//		Query query=session.createQuery("from Memberinfo m where m.nickname=?");
//		query.setString(0, name);
//		List<Memberinfo> list=query.list();
		System.out.println("MemberDaoImpl "+this);
		System.out.println("findMemberinfoByName: hibernateTemplate: "+hibernateTemplate);
		List list = hibernateTemplate.find("from Memberinfo m where m.nickname=?",name);
		if (list.size()>0)
		{
			return (Memberinfo) list.get(0);
		}
		return null;
	}

	@Override
	public void saveOrUpdateMemberinfo(Memberinfo memberinfo) throws DataAccessException
	{
//		Session session=HibernateSessionFactory.getSession();
//		session.saveOrUpdate(memberinfo);
		hibernateTemplate.save(memberinfo);
	}

	@Override
	public List<Memberinfo> findMemberinfoByNum(int number) throws DataAccessException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer findMemberinfoOnline() throws DataAccessException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Graderecord findMemberinfoLevel(Long point) throws DataAccessException
	{
		// 查找用户在其所拥有的积分所对应的等级
		// hql: from Graderecord where minpoint<=? and maxpoint>=?;
//		Session session = HibernateSessionFactory.getSession();
//		Query query = session.createQuery("from Graderecord where minpoint<=? and maxpoint>=?");
//		query.setLong(0, point);
//		query.setLong(1, point);
//		Graderecord g=(Graderecord) query.uniqueResult();
		String hql="from Graderecord where minpoint<=? and maxpoint>=?";
		List list = hibernateTemplate.find(hql,new Long []{point,point});
		if (list==null||list.isEmpty())
		{
			return null;
		}
		return (Graderecord) list.get(0);
	}

	@Override
	public Pointaction findPointactionByPointAction(String pointAction) throws DataAccessException
	{
		// 通过ActionName查找对应的积分
//		Session session=HibernateSessionFactory.getSession();
		// hql语句：不是SQL语句，不包含表名，只包含类名以及 其属性名
//		Query query=session.createQuery("from Pointaction where actionname=?");
//		query.setString(0, pointAction);
//		List<Pointaction> list=query.list();
		String hql="from Pointaction where actionname=?";
		List list = hibernateTemplate.find(hql,pointAction);
		if (list.size()>0)
		{
			return (Pointaction) list.get(0);
		}
		
		return null;
	}

	@Override
	public void saveOrUpdatePointrecord(Pointrecord pointrecord) throws DataAccessException
	{
		// 添加当前用户动作记录
//		Session session=HibernateSessionFactory.getSession();
//		session.save(pointrecord);
		hibernateTemplate.save(pointrecord);
	}

	@Override
	public void saveOrUpdateFriend(Friendrecord friend) throws DataAccessException
	{
//		Session session=HibernateSessionFactory.getSession();
//		session.saveOrUpdate(friend);
		hibernateTemplate.save(friend);
	}

	@Override
	public void saveOrUpdateFriend(Blackrecord friend) throws DataAccessException
	{
//		Session session=HibernateSessionFactory.getSession();
//		session.saveOrUpdate(friend);
		hibernateTemplate.save(friend);
	}

	@Override
	public List<Memberinfo> listFriend(String selfname) throws DataAccessException
	{
//		Session session=HibernateSessionFactory.getSession();
//		Query query=session.createQuery("from Memberinfo where nickname in (select friendname from Friendrecord where selfname = ?)");
//		query.setString(0, selfname);
//		return query.list();
		String hql="from Memberinfo where nickname in (select friendname from Friendrecord where selfname = ?)";
		return hibernateTemplate.find(hql,selfname);
	}

	@Override
	public List<Memberinfo> listBlack(String selfname) throws DataAccessException
	{
//		Session session=HibernateSessionFactory.getSession();
//		Query query=session.createQuery("from Memberinfo where nickname in (select blackname from Blackrecord where selfname = ?)");
//		query.setString(0, selfname);
//		return query.list();
		String hql="from Memberinfo where nickname in (select blackname from Blackrecord where selfname = ?)";
		return hibernateTemplate.find(hql,selfname);
	}

	@Override
	public void deleleBlack(Blackrecord black) throws DataAccessException
	{
//		Session session=HibernateSessionFactory.getSession();
//		session.delete(black);
		hibernateTemplate.delete(black);
	}

	@Override
	public void deleleFriend(Friendrecord friend) throws DataAccessException
	{
//		Session session=HibernateSessionFactory.getSession();
//		session.delete(friend);
		hibernateTemplate.delete(friend);
	}

	@Override
	public Friendrecord findfriend(String selfName, String friendName) throws DataAccessException
	{
//		Session session=HibernateSessionFactory.getSession();
//		Query query=session.createQuery("from Friendrecord where selfname=? and friendname=?");
//		query.setString(0, selfName);
//		query.setString(1, friendName);
//		List<Friendrecord> list=query.list();
		String hql="from Friendrecord where selfname=? and friendname=?";
		List list = hibernateTemplate.find(hql,new String[]{selfName,friendName});
		if (list.size()>0)
		{
			return (Friendrecord) list.get(0);
		}
		return null;
	}

	@Override
	public Blackrecord findBlack(String selfName, String blackName) throws DataAccessException
	{
//		Session session=HibernateSessionFactory.getSession();
//		Query query=session.createQuery("from Blackrecord where selfname=? and blackname=?");
//		query.setString(0, selfName);
//		query.setString(1, blackName);
//		List<Blackrecord> list=query.list();
		String hql="from Blackrecord where selfname=? and blackname=?";
		List list = hibernateTemplate.find(hql,new String []{selfName,blackName});
		if (list.size()>0)
		{
			return (Blackrecord) list.get(0);
		}
		return null;
	}

	@Override
	public Memberspace findSpace(Long id) throws DataAccessException
	{
//		Session session=HibernateSessionFactory.getSession();
//		Query query=session.createQuery("from Memberspace where memberid=?");
//		query.setLong(0, id);
//		List<Memberspace> list=query.list();
		String hql="from Memberspace where memberid=?";
		List list = hibernateTemplate.find(hql, id);
		if (list.size()>0)
		{
			return (Memberspace) list.get(0);
		}
		return null;
	}

	@Override
	public void delSpace(Memberspace space) throws DataAccessException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveOrUpdateSpace(Memberspace memberspace) throws DataAccessException
	{
//		Session session=HibernateSessionFactory.getSession();
//		session.saveOrUpdate(memberspace);
		hibernateTemplate.saveOrUpdate(memberspace);
	}

}
