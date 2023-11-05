package com.disaster.disastercode.controller;


import com.disaster.disastercode.Annotation.LogAnnotation;
import com.disaster.disastercode.DTO.SafeUserDTO;
import com.disaster.disastercode.common.BaseResponse;
import com.disaster.disastercode.common.ErrorCode;
import com.disaster.disastercode.common.ResultUtils;
import com.disaster.disastercode.exception.BusinessException;
import com.disaster.disastercode.model.domain.User;
import com.disaster.disastercode.model.request.UserRegisterRequest;
import com.disaster.disastercode.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/test")
    public BaseResponse<String> test(@RequestParam Map<String, String> params) {
        String str = "收到信息了" + "params:" + params;
        return ResultUtils.success(str);
    }

    @PostMapping("/register")
    @LogAnnotation(businessType = 0,content = "用户注册")
    @Operation(summary = "用户注册")
    public BaseResponse<Integer> userRegister(@RequestBody(required = false) UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null)
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String userName = userRegisterRequest.getUserName();
        int rst = userService.userRegister(userAccount, userPassword, userName);
        return ResultUtils.success(rst);
    }

    @PostMapping("/login")
    @LogAnnotation(businessType = 0,content = "用户登录")
    @Operation(summary = "用户登录")
    public BaseResponse<String> userLogin(@RequestBody(required = false) UserRegisterRequest userRegisterRequest) {
        //1.检查整个输入参数
        if (userRegisterRequest == null)
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        //2.提取service需要的参数
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        //3.调用service方法
        String token = userService.userLogin(userAccount, userPassword);
        //4.处理返回格式
        return ResultUtils.success(token);
    }

    @GetMapping("/currentUser")
    @LogAnnotation(businessType = 0,content = "获取当前登录用户")
    @Operation(summary = "获取当前登录用户")
    public BaseResponse<SafeUserDTO> getCurrentUser(HttpServletRequest request) {
        SafeUserDTO userWithProjectDTO = userService.getCurrentUser(request);
        return ResultUtils.success(userWithProjectDTO);
    }

    @PutMapping("/update")
    @LogAnnotation(businessType = 0,content = "更新用户信息")
    @Operation(summary = "更新用户信息")
    public BaseResponse<Boolean> updateUserSelf(@RequestBody(required = false) User user, HttpServletRequest request) {
        if (user == null)
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        Boolean rst = userService.updateUser(user, request);
        if (!rst)
            throw new BusinessException(ErrorCode.INNER_ERROR, "更新用户失败");
        return ResultUtils.success(true);
    }

    @PostMapping("/changePwd")
    @LogAnnotation(businessType = 0,content = "用户变更密码")
    @Operation(summary = "用户变更密码")
    public BaseResponse<Boolean> changeUserPwd(@RequestBody(required = false) UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null)
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        Boolean rst = userService.changeUserPwd(userRegisterRequest.getUserAccount(), userRegisterRequest.getUserPassword());
        if (!rst)
            throw new BusinessException(ErrorCode.INNER_ERROR, "修改密码失败");
        return ResultUtils.success(true);
    }
}
