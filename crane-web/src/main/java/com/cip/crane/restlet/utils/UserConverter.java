package com.cip.crane.restlet.utils;

import com.cip.crane.generated.module.User;
import com.cip.crane.restlet.shared.UserDTO;

public class UserConverter {

	public static UserDTO toUserDTO(User user){
		UserDTO result = new UserDTO();
		
		result.setId(user.getId());
		result.setName(user.getName());
		result.setMail(user.getMail());
		result.setTel(user.getTel());
		result.setQq(user.getQq());
		
		return result;
	}
	
	public static User toUser(UserDTO userDTO){
		User result = new User();
		
		result.setId(userDTO.getId());
		result.setName(userDTO.getName());
		result.setMail(userDTO.getMail());
		result.setTel(userDTO.getTel());
		result.setQq(userDTO.getQq());
		
		return result;
	}
}
