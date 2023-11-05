package com.disaster.disastercode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.disaster.disastercode.DTO.SafeUserDTO;
import com.disaster.disastercode.model.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
* @author s2485523800
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-10-26 13:29:55
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param userName 用户名称
     * @return 新用户id
     */
    int userRegister(String userAccount, String userPassword,String userName);

    /**
     * 用户登录
     * @param userAccount 账号
     * @param userPassword 密码
     * @return token
     */
    String userLogin(String userAccount, String userPassword,HttpServletRequest request);

    /**
     * 获得登录的用户信息(包含组长组，成员组)
     * @param request 当前请求
     * @return 用户信息
     */
    SafeUserDTO getCurrentUser(HttpServletRequest request);

    /**
     * 用户更新自己的信息(名称，头像地址，性别，手机号码，邮箱)
     * @param user 更新的信息
     * @return 是否成功更新
     */
    Boolean updateUser(User user, HttpServletRequest request);

    /**
     * 修改用户密码
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @return 是否修改成功
     */
    Boolean changeUserPwd(String userAccount, String userPassword);
}
