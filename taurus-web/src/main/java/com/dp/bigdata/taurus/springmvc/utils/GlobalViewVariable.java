package com.dp.bigdata.taurus.springmvc.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.restlet.resource.ClientResource;

import com.dp.bigdata.taurus.restlet.shared.UserDTO;

/**
 * @author chenchongze
 *
 */
public class GlobalViewVariable {
	public String currentUser = null;
	public String host = null;
	public int userId = -1;
	public boolean isAdmin = false;
	public ClientResource cr = null;
	public ArrayList<UserDTO> users = null;
	public HashMap<String, UserDTO> userMap = null;
	
	public GlobalViewVariable(){};
}
