package com.pollen.pollenweb.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pollen.pollenweb.entity.User;
import com.pollen.pollenweb.exception.PwException;
import com.pollen.pollenweb.mapper.UserMapper;
import com.pollen.pollenweb.result.ResponseEnum;
import com.pollen.pollenweb.service.IUserService;
import com.pollen.pollenweb.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
@Slf4j
public  class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByUsername(User user) {
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq("username",user.getUsername());
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User queryUserByLoginname(User user) {
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq("loginname",user.getLoginname());
        return userMapper.selectOne(wrapper);
    }

    @Override
    public int addUser(User user){
        if(queryUserByLoginname(user)!=null){
            log.info("【code:1004 msg:用户名已存在】");
            return 2;
        }else{
            //插入前走加密流程
            String md5password = MD5Util.SysMd5(user.getLoginname(),user.getPassword());
            user.setPassword(md5password);
            //插入db
            return userMapper.insert(user);
        }

    }

    @Override
    public int editUser(User user) {
        return userMapper.updateById(user);
    }

}
