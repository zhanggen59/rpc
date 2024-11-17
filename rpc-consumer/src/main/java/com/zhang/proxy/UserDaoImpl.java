package com.zhang.proxy;

/**
 * @ClassName UserDaoImpl
 * @Description
 * @Author zhanggen
 * @Date 2024/11/2 11:10
 * @Version 1.0
 */
public class UserDaoImpl implements UserDao{
    @Override
    public void insert() {
        System.out.println("insert user success");
    }
}
