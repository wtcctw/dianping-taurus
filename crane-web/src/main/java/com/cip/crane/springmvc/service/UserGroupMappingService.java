package com.cip.crane.springmvc.service;

import com.cip.crane.generated.module.UserGroupMapping;
import com.cip.crane.springmvc.bean.JqGridRespBean;

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
