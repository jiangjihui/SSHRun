package com.briup.run.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils
{
	// 将日期转化为字符串
	public static String getDateStr(Date date)
	{
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}
	
/*	public static void main(String[] args)
	{
		DateUtils dateUtils=new DateUtils();
		System.out.println(dateUtils.getDateStr(new Date()));
	}*/
}

