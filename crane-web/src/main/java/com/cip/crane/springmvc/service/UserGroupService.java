package com.cip.crane.springmvc.service;

import java.util.List;

import com.cip.crane.springmvc.bean.JqGridRespBean;
import com.cip.crane.generated.module.UserGroup;

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
