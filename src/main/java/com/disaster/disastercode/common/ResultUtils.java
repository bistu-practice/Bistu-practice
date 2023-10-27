package com.disaster.disastercode.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.disaster.disastercode.DTO.CustomPageDTO;
import com.disaster.disastercode.exception.BusinessException;
import org.apache.ibatis.jdbc.Null;

/**
 * 返回工具类
 * 含正确返回，错误返回的静态方法
 */
public class ResultUtils {
    //成功，返回代码0，ok，数据
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, "ok", data);
    }
    //当接收到IPage分页对象，进行处理
    public static <T> BaseResponse<CustomPageDTO<T>> page(IPage<T> page) {
        CustomPageDTO<T> pageDTO = new CustomPageDTO<>(page);
        return new BaseResponse<>(0, "ok", pageDTO);
    }
    //返回错误对象,错误代码,message,description为“”
    public static BaseResponse<Null> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }
    //含错误描述
    public static BaseResponse<Null> error(BusinessException businessException) {
        return new BaseResponse<>(businessException);
    }
}
