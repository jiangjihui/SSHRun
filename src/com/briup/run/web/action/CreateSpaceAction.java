package com.briup.run.web.action;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.briup.run.common.bean.Memberinfo;
import com.briup.run.common.bean.Memberspace;
import com.briup.run.common.exception.MemberServiceException;
import com.briup.run.common.util.BeanFactory;
import com.briup.run.service.IMemberService;
import com.opensymphony.xwork2.ActionSupport;

public class CreateSpaceAction extends ActionSupport implements SessionAware
{
	private String opinion;
	private String runtime;
	private String runplace;
	private String runstar;
	private String runhabit;
	private String cellphone;
	
	private String msg;
	
	// icon头像
	private File  icon;
	private String iconFileName;
	private String iconContentType;
	
	private Map<String, Object> session;
	private IMemberService memberService;
	
	public String execute()
	{
		// 将上传的文件放入服务器
		String path=ServletActionContext.getServletContext().getRealPath("/upload");
		if (icon!=null)
		{
			File savedir=new File(path);
			if (!savedir.exists())
			{
				savedir.mkdirs();
			}
			File dest=new File(savedir,iconFileName);
			try
			{
				FileUtils.copyFile(icon, dest);
			} 
			catch (IOException e)
			{
				msg="文件上传失败~";
				e.printStackTrace();
				return "error";
			}
		}

		// 得到创建用户空间的用户对象
		Memberinfo memberinfo = (Memberinfo) session.get("memberinfo");
		
		Memberspace memberspace = memberinfo.getMemberSpace();

		// 判断是否存在用户空间
		if (memberspace==null)
		{
			memberspace=new Memberspace();		// 不存在则创建用户空间
		}
		
		memberspace.setOpinion(opinion);
		memberspace.setRunhabit(runhabit);
		memberspace.setRunplace(runplace);
		memberspace.setRunstar(runstar);
		memberspace.setRuntime(runtime);
		memberspace.setCellphone(cellphone);
		
		//将文件路径放入空间对象
		memberspace.setIcon(iconFileName);
		
		// 调用service层的函数，写入数据库中
		try
		{
			memberService.createMemberSpace(memberinfo, memberspace);
		} 
		catch (MemberServiceException e)
		{
			msg="创建用户空间失败~";
			e.printStackTrace();
			return "error";
		}
		// 更新MemberInfo对象
		session.put("memberinfo", memberinfo);
		session.put("memberspace", memberspace);
		return SUCCESS;
	}

	public File getFile()
	{
		return icon;
	}

	public void setFile(File icon)
	{
		this.icon = icon;
	}

	public String getFileFileName()
	{
		return iconFileName;
	}

	public void setFileFileName(String iconFileName)
	{
		this.iconFileName = iconFileName;
	}

	public String getFileContentType()
	{
		return iconContentType;
	}

	public void setFileContentType(String iconContentType)
	{
		this.iconContentType = iconContentType;
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

	public String getOpinion()
	{
		return opinion;
	}

	public void setOpinion(String opinion)
	{
		this.opinion = opinion;
	}

	public String getRuntime()
	{
		return runtime;
	}

	public void setRuntime(String runtime)
	{
		this.runtime = runtime;
	}

	public String getRunplace()
	{
		return runplace;
	}

	public void setRunplace(String runplace)
	{
		this.runplace = runplace;
	}

	public String getRunstar()
	{
		return runstar;
	}

	public void setRunstar(String runstar)
	{
		this.runstar = runstar;
	}

	public String getRunhabit()
	{
		return runhabit;
	}

	public void setRunhabit(String runhabit)
	{
		this.runhabit = runhabit;
	}

	public String getCellphone()
	{
		return cellphone;
	}

	public void setCellphone(String cellphone)
	{
		this.cellphone = cellphone;
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
