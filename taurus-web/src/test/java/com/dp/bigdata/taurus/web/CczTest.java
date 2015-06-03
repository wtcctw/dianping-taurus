package com.dp.bigdata.taurus.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jodd.util.StringUtil;

import org.junit.Test;

public class CczTest {

	@Test
	public void test1() throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = "";
		
		if (StringUtil.isBlank(dateStr)) { dateStr = formatter.format(new Date()); }
		
		String endTime = formatter.format(formatter.parse(dateStr));
		System.out.println(endTime);
		System.out.println(dateStr);
	}
}
