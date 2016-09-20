package com.briup.run.service.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.briup.run.common.bean.Blackrecord;
import com.briup.run.common.bean.Friendrecord;
import com.briup.run.common.bean.Graderecord;
import com.briup.run.common.bean.Memberinfo;
import com.briup.run.common.bean.Memberspace;
import com.briup.run.common.bean.Pointaction;
import com.briup.run.common.bean.Pointrecord;
import com.briup.run.common.exception.DataAccessException;
import com.briup.run.common.exception.MemberServiceException;
import com.briup.run.common.util.BeanFactory;
import com.briup.run.common.util.DateUtils;
import com.briup.run.common.util.HibernateSessionFactory;
import com.briup.run.common.util.RandomChar;
import com.briup.run.dao.IMemberDao;
import com.briup.run.dao.IMessengerDao;
import com.briup.run.dao.impl.MemberDaoImpl;
import com.briup.run.service.IMemberService;

import oracle.net.aso.s;

public class MemberServiceImpl implements IMemberService
{
	private IMemberDao memberDao;
	private IMessengerDao messengerDao;

	public IMemberDao getMemberDao()
	{
		return memberDao;
	}

	public void setMemberDao(IMemberDao memberDao)
	{
		this.memberDao = memberDao;
		System.out.println("setMemberDao "+this);
		System.out.println(memberDao);
	}

	public IMessengerDao getMessengerDao()
	{
		return messengerDao;
	}

	public void setMessengerDao(IMessengerDao messengerDao)
	{
		this.messengerDao = messengerDao;
		System.out.println(messengerDao);
	}

	@Override
	public void registerMemberinfo(Memberinfo memberinfo) throws MemberServiceException
	{
		try
		{
			// 查找用户是否存在
			Memberinfo m = memberDao.findMemberinfoByName(memberinfo.getNickname());
			if (m != null)
			{
				throw new MemberServiceException("该昵称已被占用，请重新注册");
			}

			// 通过REGISTER关键字查找注册所对应的积分对象
			Pointaction pointaction = memberDao.findPointactionByPointAction("REGISTER");

			// 从对象pointaction中获取注册动作赢得的积分，并给当前用户设置注册积分
			Long point = pointaction.getPoint();
			memberinfo.setPoint(point);

			// 根据注册获取对应的积分，查找对应的等级
			Graderecord graderecord = memberDao.findMemberinfoLevel(memberinfo.getPoint());

			// 给当前用户设置等级
			memberinfo.setGraderecord(graderecord);

			// 添加当前用户注册动作的记录
			Pointrecord pointrecord = new Pointrecord();
			pointrecord.setNickname(memberinfo.getNickname());
			pointrecord.setPointaction(pointaction);
			pointrecord.setReceivedate(new Date());
			memberDao.saveOrUpdatePointrecord(pointrecord);

			// 查找是否存在推荐人
			String recommender = memberinfo.getRecommender();
			if (!"".equals(recommender) && recommender != null)
			{
				// 如果用户输入了推荐人信息，则查看推荐人是否存在
				Memberinfo recMemberinfo = memberDao.findMemberinfoByName(recommender);
				if (!"".equals(recommender) && recommender != null)
				{
					// 存在推荐人
					// 通过RECOMMEND关键字查找注册所对应的积分对象
					Pointaction recpointaction = memberDao.findPointactionByPointAction("RECOMMEND");
					// 从对象recpointaction中获取注册动作赢得的积分，并给推荐用户设置推荐积分
					Long recpoint = recpointaction.getPoint() + recMemberinfo.getPoint();
					recMemberinfo.setPoint(recpoint);
					// 根据推荐获取对应的积分，查找对应的等级
					Graderecord recgraderecord = memberDao.findMemberinfoLevel(recpoint);
					// 给当前用户设置等级
					memberinfo.setGraderecord(recgraderecord);
					// 更新推荐人信息
					memberDao.saveOrUpdateMemberinfo(recMemberinfo);

					// 添加推荐人用户动作的记录
					Pointrecord recpointrecord = new Pointrecord();
					recpointrecord.setNickname(recMemberinfo.getNickname());
					recpointrecord.setPointaction(recpointaction);
					recpointrecord.setReceivedate(new Date());
					memberDao.saveOrUpdatePointrecord(recpointrecord);
				}
				else
				{
					// 没找到推荐人则将推荐人置空
					memberinfo.setRecommender(null);
				}
			}

			// 设置注册用户的其他信息
			// 设置账户状态
			memberinfo.setStatus(0L);
			// 设置在线状态
			memberinfo.setIsonline(0L);
			// 设置时间
			memberinfo.setRegisterdate(new Date());

			// 保存注册用户信息
			memberDao.saveOrUpdateMemberinfo(memberinfo);

		} catch (DataAccessException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public Memberinfo findMemberinfoByName(String nickname) throws MemberServiceException
	{
		return null;
	}

	@Override
	public Memberinfo login(String name, String passwd) throws MemberServiceException
	{
		System.out.println("MessengerServiceImpl： "+this);
		System.out.println(memberDao);
		try
		{
			Memberinfo m = memberDao.findMemberinfoByName(name);
			if (m == null)
			{
				throw new MemberServiceException("用户名不存在~");
			}
			System.out.println("name "+m.getNickname()+"passwd "+m.getPassword());
			if (!m.getPassword().equals(passwd))
			{
				throw new MemberServiceException("密码输入错误，请重新输入~");
			}

			// 如果用户存在，判断该账户是否已经注销，如果注销，则抛出异常
			if (m.getStatus() == 1L)
			{
				throw new MemberServiceException("该账户已经注销，请联系管理员或者重新注册~");
			}

			// 查找登录日期是否是今天，如果不是或者为null，则加积分，否则不加
			String nowDate = DateUtils.getDateStr(new Date());
			String lastDate = null;
			Date LDate = m.getLatestdate();
			if (LDate != null)
			{
				// 如果这里没有这个判断语句，而是直接执行的话，m.getLatestdate()会得到一个空值，
				// 这时候使用DateUtils.getDateStr会导致读取空值而抛出异常，中断程序执行
				lastDate = DateUtils.getDateStr(m.getLatestdate());
			}
			if (lastDate == null || !nowDate.equals(lastDate))
			{
				// 通过LOGIN关键字查找登录所对应的积分对象
				Pointaction pointaction = memberDao.findPointactionByPointAction("LOGIN");

				// 从对象pointaction中获取注册动作赢得的积分，并给推荐用户设置推荐积分
				Long point = pointaction.getPoint() + m.getPoint();
				m.setPoint(point);

				// 根据推荐获取对应的积分，查找对应的等级
				Graderecord graderecord = memberDao.findMemberinfoLevel(point);

				// 给当前用户设置等级
				m.setGraderecord(graderecord);

				// 添加用户动作积分的记录
				Pointrecord pointrecord = new Pointrecord();
				pointrecord.setNickname(m.getNickname());
				pointrecord.setPointaction(pointaction);
				pointrecord.setReceivedate(new Date());
				memberDao.saveOrUpdatePointrecord(pointrecord);
			}

			// 设置在线状态
			m.setIsonline(1L);
			// 设置时间
			m.setLatestdate(new Date());
			// 更新用户信息
			memberDao.saveOrUpdateMemberinfo(m);

			return m;

		} catch (DataAccessException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void logout(String nickname) throws MemberServiceException
	{

	}

	@Override
	public List<Memberinfo> findMemberinfoByNum(int number) throws MemberServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int findMemberinfoOnline() throws MemberServiceException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Graderecord findMemberinfoLevel(Long point) throws MemberServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Memberinfo saveOrUpDate(Memberinfo memberinfo, String oldPasswd) throws MemberServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}

	// 通过问题找回密码
	@Override
	public String getBackPasswd(String nickname, String pwdQuestion, String pwdAnswer) throws MemberServiceException
	{
		try
		{
			// 查找用户是否存在
			Memberinfo m = memberDao.findMemberinfoByName(nickname);

			if (m == null)
			{
				throw new MemberServiceException("用户名不存在~");
			}

			// 判断用户是否已经注销
			if (m.getStatus() == 1L)
			{
				throw new MemberServiceException("该账户已注销，请重新去注册吧~");
			}

			// 判断用户是否已经注销
			if (!m.getPasswordquestion().equals(pwdQuestion))
			{
				throw new MemberServiceException("输入的密码提示问题不正确");
			}

			// 判断用户是否已经注销
			if (!m.getPasswordanswer().equals(pwdAnswer))
			{
				throw new MemberServiceException("输入的密码提示答案不正确");
			}

			// 生成随机密码
			String randpasswd = new RandomChar().getChars(4, 6);

			// 将随机密码写入数据库
			m.setPassword(randpasswd);
			memberDao.saveOrUpdateMemberinfo(m);

			return randpasswd;
		} catch (DataAccessException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void saveOrUpdate(Memberinfo memberinfo) throws MemberServiceException
	{
		try
		{
			memberDao.saveOrUpdateMemberinfo(memberinfo);
		} catch (DataAccessException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void saveOrUpdate(String selfname, String friendname) throws MemberServiceException
	{
		try
		{
			// 查找需要添加的人是否已经是好友
			Friendrecord friendrecord=memberDao.findfriend(selfname, friendname);
			if (friendrecord!=null)
			{
				throw new MemberServiceException("该会员已经是你的好友啦~");
			}
			Blackrecord blackrecord=memberDao.findBlack(selfname, friendname);
			if (blackrecord!=null)
			{
				throw new MemberServiceException("该会员已经被你加入黑名单，请去黑名单解锁~");
			}
			Friendrecord friend=new Friendrecord();
			friend.setSelfname(selfname);
			friend.setFriendname(friendname);
			memberDao.saveOrUpdateFriend(friend);
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public List<Memberinfo> listFriend(String selfname) throws MemberServiceException
	{
		Session session=HibernateSessionFactory.getSession();
		try
		{
			List<Memberinfo> list=memberDao.listFriend(selfname);
			return list;
		} 
		catch (DataAccessException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void moveToBlack(String selfname, String blackname) throws MemberServiceException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public List<Memberinfo> listBlack(String selfname) throws MemberServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Friendrecord findFriend(String friend) throws MemberServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}

	// 判断是否有个人空间
	@Override
	public Boolean isMemberspace(Long id) throws MemberServiceException
	{
		try
		{
			Memberspace space = memberDao.findSpace(id);
			
			if (space == null)
			{
				return false;
			}
			else
			{
				return true;
			}

		} catch (DataAccessException e)
		{
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public void moveToFriend(String selfName, String name_searching) throws MemberServiceException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void deleleBlack(String selfname, String blackname) throws MemberServiceException
	{
		try
		{
			// 查找需要添加的人是否已经已被加入黑名单
			Blackrecord blackrecord=memberDao.findBlack(selfname, blackname);
			if (blackrecord==null)
			{
				throw new MemberServiceException("该会员已经移出黑名单~");
			}
			else 
			{
				memberDao.deleleBlack(blackrecord);
			}
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public void deleleFriend(String selfName, String friend) throws MemberServiceException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void delSpace(Long id) throws MemberServiceException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Pointaction findPointactionByPointAction(String actionName) throws MemberServiceException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Pointrecord pointrecord) throws MemberServiceException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void createMemberSpace(Memberinfo memberinfo, Memberspace memberspace) throws MemberServiceException
	{
		
		// 问题：采用级联保存
		// 在内存中建立memberspace--》memberinfo 之间的关系。【外键在memberinfo中】
		
		// 在内存中建立对象之间的关系
		memberspace.setMemberinfo(memberinfo);
		memberinfo.setMemberSpace(memberspace);
		
		try
		{
			Pointaction pointaction=memberDao.findPointactionByPointAction("CREATEPERSONALSPACE");
			Long point=pointaction.getPoint()+memberinfo.getPoint();
			memberinfo.setPoint(point);
			
			// 设置等级
			Graderecord createrecord=memberDao.findMemberinfoLevel(memberinfo.getPoint());
			memberinfo.setGraderecord(createrecord);
			
			//记录动作
			Pointrecord record=new Pointrecord();
			record.setNickname(memberinfo.getNickname());
			record.setPointaction(pointaction);
			record.setReceivedate(new Date());
			memberDao.saveOrUpdatePointrecord(record);
			
			memberDao.saveOrUpdateMemberinfo(memberinfo);
			memberDao.saveOrUpdateSpace(memberspace);
		} 
		catch (DataAccessException e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public Memberspace findSpace(Memberinfo memberinfo) throws MemberServiceException
	{
		try
		{
			return memberDao.findSpace(memberinfo.getId());
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public void removeFriend(String selfname, String friendname) throws MemberServiceException
	{
		try
		{
			// 查找需要添加的人是否已经是好友
			Friendrecord friendrecord=memberDao.findfriend(selfname, friendname);
			if (friendrecord==null)
			{
				throw new MemberServiceException("该会员不是你的好友~");
			}
			else 
			{
				memberDao.deleleFriend(friendrecord);
			}
			Blackrecord blackrecord=memberDao.findBlack(selfname, friendname);
			if (blackrecord!=null)
			{
				throw new MemberServiceException("该会员已经被你加入黑名单~");
			}
			else 
			{
				Blackrecord blackfriend=new Blackrecord();
				blackfriend.setSelfname(selfname);
				blackfriend.setBlackname(friendname);
				memberDao.saveOrUpdateFriend(blackfriend);
			}
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public List<Memberinfo> listBlackFriend(String selfname) throws MemberServiceException
	{
		try
		{
			List<Memberinfo> list=memberDao.listBlack(selfname);
			return list;
		} 
		catch (DataAccessException e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
