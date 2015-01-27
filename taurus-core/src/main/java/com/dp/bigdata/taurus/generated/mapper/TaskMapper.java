package com.dp.bigdata.taurus.generated.mapper;

import com.dp.bigdata.taurus.generated.module.Host;
import com.dp.bigdata.taurus.generated.module.Task;
import com.dp.bigdata.taurus.generated.module.TaskExample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TaskMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusTask
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    int countByExample(TaskExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusTask
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    int deleteByExample(TaskExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusTask
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    int deleteByPrimaryKey(String taskid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusTask
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    int insert(Task record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusTask
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    int insertSelective(Task record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusTask
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    List<Task> selectByExample(TaskExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusTask
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    Task selectByPrimaryKey(String taskid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusTask
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    int updateByExampleSelective(@Param("record") Task record, @Param("example") TaskExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusTask
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    int updateByExample(@Param("record") Task record, @Param("example") TaskExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusTask
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    int updateByPrimaryKeySelective(Task record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusTask
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    int updateByPrimaryKey(Task record);
    public Task getTaskByAttemptId(@Param("attemptId")String attemptId);
    public Task getTaskById(@Param("taskId")String taskId);

    public Task getTaskByAppNameIP(@Param("appName")String appName,@Param("ip")String ip);

    public ArrayList<Task> getTasks();
    ArrayList<Host> getTasksByHost(String hostname);
    public HashMap<String, String> isExitTaskName(@Param("taskName")String taskName);
    int updateCreator(@Param("creator")String creator,@Param("taskName")String taskName);
}