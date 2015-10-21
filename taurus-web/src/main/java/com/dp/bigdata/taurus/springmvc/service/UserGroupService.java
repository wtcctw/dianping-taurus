package com.dp.bigdata.taurus.springmvc.service;

import java.util.List;

import com.dp.bigdata.taurus.generated.module.UserGroup;
import com.dp.bigdata.taurus.springmvc.bean.JqGridRespBean;

/**
 * @author chenchongze
 *
 */
public interface UserGroupService {

	public List<UserGroup> retrieveByPageAndRows(int page, int rows);
	
	public JqGridRespBean retrieveByJqGrid(int page, int rows);
	
	public int deleteByIdSplitByComma(String idsComma);
	
	public int deleteById(Integer id);

	/**
	 * @author chenchongze
	 * @param userGroup
	 */
	public int updateById(UserGroup userGroup);
	
	
}
