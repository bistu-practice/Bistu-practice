package com.disaster.disastercode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.disaster.disastercode.DTO.SafeUserDTO;
import com.disaster.disastercode.common.ErrorCode;
import com.disaster.disastercode.exception.BusinessException;
import com.disaster.disastercode.model.domain.User;
import com.disaster.disastercode.service.UserService;
import com.disaster.disastercode.mapper.UserMapper;
import com.disaster.disastercode.utils.JWTUtils;
import com.disaster.disastercode.utils.UserCheckUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author 12639
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2022-11-07 17:13:12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService{


    private static final String SALT = "ymcymc";
    @Resource
    private UserMapper userMapper;

    @Override
    public int userRegister(String userAccount, String userPassword, String userName) {
        //1.校验输入
        //都不为空
        UserCheckUtils.checkAccountAndPassword(userAccount, userPassword);
        if (StringUtils.isAnyBlank(userName))
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        //昵称校验
        if (userName.length() > 20)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户昵称必须为1-20位");
        //2.校验账号唯一性
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserAccount, userAccount);
        long count = this.count(userLambdaQueryWrapper);
        if (count > 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户名重复");
        //3.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        //4.入库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName(userName);

        boolean rst = this.save(user);
        if (!rst)
            throw new BusinessException(ErrorCode.INNER_ERROR, "插入用户信息失败");
        return user.getId();
    }

    @Override
    public String userLogin(String userAccount, String userPassword) {
        //1.检查参数
        UserCheckUtils.checkAccountAndPassword(userAccount, userPassword);
        //2.检查用户名密码匹配
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, userAccount);
        queryWrapper.eq(User::getUserPassword, encryptPassword);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.LOGIN_FAIL, "用户名或密码错误");
        }
        return JWTUtils.createToken(user.getId());
    }

    @Override
    public SafeUserDTO getCurrentUser(HttpServletRequest request) {
        Integer userId = JWTUtils.getTokenInfoId(request);
        SafeUserDTO userWithProject = this.ToSafeUser(this.getById(userId));
        if (userWithProject == null)
            throw new BusinessException(ErrorCode.INNER_ERROR, "数据库获取不到当前用户信息");
        return userWithProject;
    }

    @Override
    public Boolean updateUser(User user, HttpServletRequest request) {
        //1.检查参数
        UserCheckUtils.checkUpdateSelfUserInput(user);
        //2.判断是否为本人
        Integer userId = JWTUtils.getTokenInfoId(request);
        if (!Objects.equals(userId, user.getId()))
            throw new BusinessException(ErrorCode.INNER_ERROR, "用户不匹配");
        //3.提取部分参数修改
        User updateSelfUser = UserCheckUtils.getUpdateSelfUser(user);
        return this.updateById(updateSelfUser);
    }

    @Override
    public Boolean changeUserPwd(String userAccount, String userPassword) {
        //1.检查参数
        UserCheckUtils.checkAccountAndPassword(userAccount, userPassword);
        //2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        //3.检查是否存在该用户
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserAccount, userAccount);
        long count = this.count(userLambdaQueryWrapper);
        if (count <= 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不存在该用户");
        //4.修改密码
        User user = new User();
        user.setUserPassword(encryptPassword);
        return this.update(user, userLambdaQueryWrapper);
    }

    public SafeUserDTO ToSafeUser(User user) {
        SafeUserDTO safeUser = new SafeUserDTO();
        safeUser.setId(user.getId());
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setUserName(user.getUserName());
        safeUser.setAvatarUrl(user.getAvatarUrl());
        safeUser.setUserJob(user.getUserJob());
        return safeUser;
    }

}



