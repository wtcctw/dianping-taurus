package com.cip.crane.core;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.cip.crane.generated.mapper.TaskMapper;
import com.cip.crane.generated.module.Host;
import com.cip.crane.generated.module.TaskAttempt;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cip.crane.generated.mapper.HostMapper;
import com.cip.crane.generated.mapper.TaskAttemptMapper;

/**
 * 
 * ImportDataTest
 * 
 * @author damon.zhu
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext-test.xml" })
public class HostMapperTest {

	@Autowired
	private HostMapper hostMapper;

	@Autowired
	private TaskAttemptMapper taskAttemptMapper;

	@Autowired
	private TaskMapper taskMapper;
    @Ignore
	//@Test
	public void insertHostData() {
		Host record = hostMapper.selectByPrimaryKey("HADOOP");
		String ip = record.getIp();
		assertEquals("10.1.77.84", ip);
	}
    @Ignore
	//@Test
	public void selectHostData() {
		List<Host> results = hostMapper.selectByExample(null);
		assertEquals(1, results.size());
	}
    @Ignore
	protected void loadData() {
		Host record = new Host();
		record.setIp("10.1.77.84");
		record.setName("HADOOP");
		record.setPoolid(1);
		hostMapper.insertSelective(record);
	}
    //@Ignore
	//@Test
	public void insertTaskData() {
		List<TaskAttempt> selectByGroupAndStatus = taskAttemptMapper
				.selectByGroupAndStatus();

		for(TaskAttempt at : selectByGroupAndStatus){
			System.out.println(at.getTaskid() + " at " + at.getStarttime());

		}
	}
}
