package com.dp.bigdata.taurus.generated.mapper;

import com.dp.bigdata.taurus.generated.module.UserGroupMapping;
import com.dp.bigdata.taurus.generated.module.UserGroupMappingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserGroupMappingMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusUserGroupMapping
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    int countByExample(UserGroupMappingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusUserGroupMapping
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    int deleteByExample(UserGroupMappingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusUserGroupMapping
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusUserGroupMapping
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    int insert(UserGroupMapping record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusUserGroupMapping
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    int insertSelective(UserGroupMapping record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusUserGroupMapping
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    List<UserGroupMapping> selectByExample(UserGroupMappingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusUserGroupMapping
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    UserGroupMapping selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusUserGroupMapping
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    int updateByExampleSelective(@Param("record") UserGroupMapping record, @Param("example") UserGroupMappingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusUserGroupMapping
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    int updateByExample(@Param("record") UserGroupMapping record, @Param("example") UserGroupMappingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusUserGroupMapping
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    int updateByPrimaryKeySelective(UserGroupMapping record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TaurusUserGroupMapping
     *
     * @mbggenerated Thu May 15 16:53:11 HKT 2014
     */
    int updateByPrimaryKey(UserGroupMapping record);
}