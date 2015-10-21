package com.dp.bigdata.taurus.springmvc.service;

import com.dp.bigdata.taurus.generated.module.UserGroupMapping;
import com.dp.bigdata.taurus.springmvc.bean.JqGridRespBean;

/**
 * @author chenchongze
 *
 */
public interface UserGroupMappingService {

	public JqGridRespBean retrieveByJqGrid(int page, int rows);
	
	public int deleteByIdSplitByComma(String idsComma);

	public int updateById(UserGroupMapping userGroupMapping);

	/**
	 * @author chenchongze
	 * @param userGroupMapping
	 */
	public int create(UserGroupMapping userGroupMapping);
}
