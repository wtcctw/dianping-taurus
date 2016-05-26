package com.cip.crane.springmvc.service.impl;

import com.cip.crane.generated.mapper.UserMapper;
import com.cip.crane.generated.module.User;
import com.cip.crane.generated.module.UserExample;
import com.cip.crane.springmvc.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author chenchongze
 *
 */
@Service
public class UserService implements IUserService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean checkExists(String dpAccount) {
        UserExample example = new UserExample();
        example.createCriteria().andNameEqualTo(dpAccount);
        List<User> users = new ArrayList<User>();
        try {
            users = userMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("init database error", e);
        }

        if(users.size() > 0) {
            return true;
        }

        return false;
    }
}
