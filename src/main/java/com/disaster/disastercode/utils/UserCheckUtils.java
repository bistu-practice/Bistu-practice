package com.disaster.disastercode.utils;

import com.disaster.disastercode.common.ErrorCode;
import com.disaster.disastercode.exception.BusinessException;
import com.disaster.disastercode.model.domain.User;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserCheckUtils {
    //检查有没有id
    public static void checkInputUserId(User user) {
        Integer id = user.getId();
        if(id == null || id <= 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id错误");
    }

    //获得更新自身的user字段
    public static User getUpdateSelfUser(User user) {
        User updateSelfUser = new User();
        updateSelfUser.setId(user.getId());
        updateSelfUser.setUserName(user.getUserName());
        updateSelfUser.setAvatarUrl(user.getAvatarUrl());
        updateSelfUser.setUserJob(user.getUserJob());
        return  updateSelfUser;
    }
    //检查账号名和密码
    public static void checkAccountAndPassword(String userAccount, String userPassword) {
        //都不为空
        if (StringUtils.isAnyBlank(userAccount, userPassword))
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        //账户名不包含特殊字符
        //只含字母数字下划线,6-16位
        String reg = "^\\w{6,16}$";
        Matcher matcherAccount = Pattern.compile(reg).matcher(userAccount);
        if (!matcherAccount.find())
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名必须为6-16位且只含字母数字下划线");
        //密码校验
        Matcher matcherPassword = Pattern.compile(reg).matcher(userPassword);
        if (!matcherPassword.find())
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码必须为6-16位且只含字母数字下划线");
    }

    //检查更新自身时的输入值
    public static void checkUpdateSelfUserInput(User user) {
        //id
        checkInputUserId(user);
        //昵称校验
        String userName = user.getUserName();
        if (StringUtils.isNotBlank(userName) && userName.length() > 20)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户昵称必须为1-20位");
        //头像地址
        String avatarUrl = user.getAvatarUrl();
        if (StringUtils.isNotBlank(avatarUrl) && avatarUrl.length() > 500)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "头像地址必须少于500个字符");
        //职位
        String userJob = user.getUserJob();
        if (StringUtils.isBlank(userJob) && userJob.length() > 20)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "职位必须少于20个字符");
    }
}
