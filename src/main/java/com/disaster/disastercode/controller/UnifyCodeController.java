package com.disaster.disastercode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.disaster.disastercode.Annotation.LogAnnotation;
import com.disaster.disastercode.DTO.CustomPageDTO;
import com.disaster.disastercode.DTO.UploadUrlResponse;
import com.disaster.disastercode.VO.DetailDisasterForm;
import com.disaster.disastercode.VO.UnifyCode;
import com.disaster.disastercode.common.BaseResponse;
import com.disaster.disastercode.common.ErrorCode;
import com.disaster.disastercode.common.ResultUtils;
import com.disaster.disastercode.exception.BusinessException;
import com.disaster.disastercode.model.domain.DetailDisaster;
import com.disaster.disastercode.model.request.DecodeOneRequest;
import com.disaster.disastercode.model.request.UploadUrlRequest;
import com.disaster.disastercode.service.DetailDisasterService;
import com.disaster.disastercode.service.OssService;
import com.disaster.disastercode.service.UnifyCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/unifyCode")
@Tag(name="统一编码控制器",description = "")
public class UnifyCodeController {
    @Resource
    private UnifyCodeService unifyCodeService;
    @Resource
    private DetailDisasterService detailDisasterService;
    @Resource
    private OssService ossService;

    @GetMapping("/test")
    public BaseResponse<String> testHello(@RequestParam("income") String income) {
        if (StringUtils.isBlank(income))
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        return ResultUtils.success(income);
    }

    /**
     * 解码,输入一个编码值，返回改变该编码的详细信息
     */
    @PostMapping("/decodeOne")
    @Operation(summary = "解码,输入一个编码值，返回改变该编码的详细信息")
    @LogAnnotation(businessType = 1,content = "解码,输入一个编码值，返回改变该编码的详细信息")
    public BaseResponse<DetailDisasterForm> decodeOne(@RequestBody DecodeOneRequest decodeOneRequest) {

        UnifyCode unifyCode = new UnifyCode(decodeOneRequest.getCode(), decodeOneRequest.getDescription());
        DetailDisasterForm infoFromCode = unifyCodeService.getInfoFromCode(unifyCode);
        return ResultUtils.success(infoFromCode);
    }

    /**
     * 将编码解码后插入一条详细文本灾情
     */
    @PutMapping("decodeOneInsert")
    @Operation(summary = "将编码解码后插入一条详细文本灾情")
    @LogAnnotation(businessType = 1,content = "将编码解码后插入一条详细文本灾情")
    public BaseResponse<Boolean> decodeOneInsert(@RequestBody DecodeOneRequest decodeOneRequest) {
        if (StringUtils.isAnyBlank(decodeOneRequest.getCode(), decodeOneRequest.getDescription())) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        }
        UnifyCode unifyCode = new UnifyCode(decodeOneRequest.getCode(), decodeOneRequest.getDescription());
        // 检查载体类型
        DetailDisasterForm infoFromCode = unifyCodeService.getInfoFromCode(unifyCode);
        if (!infoFromCode.getCodeType().equals("文字"))
            throw new BusinessException(ErrorCode.CODE_ERROR, "请输入文字类型的编码");
        boolean rst = detailDisasterService.save(infoFromCode.formDetailDisaster());
        if (rst) {
            return ResultUtils.success(true);
        } else {
            throw new BusinessException(ErrorCode.INNER_ERROR, "插入文字灾情失败");
        }
    }


    /**
     * 将多媒体灾情信息上传
     *
     * @return 是否成功
     */
    @PostMapping("/decodeOneWithFile")
    @LogAnnotation(businessType = 1,content = "将多媒体灾情信息上传")
    @Operation(summary = "将多媒体灾情信息上传")
    public BaseResponse<Boolean> decodeOneWithFile(@RequestBody(required = false) UploadUrlRequest uploadUrlRequest) {
        // 检查参数
        if (uploadUrlRequest == null)
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        String fileName = uploadUrlRequest.getFileName();
        String code = uploadUrlRequest.getCode();
        if (StringUtils.isAnyBlank(fileName, code))
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        // 检查是否存在该文件
        Boolean fileExist = ossService.isFileExist(fileName);
        if (!fileExist)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该文件不存在!");
        UnifyCode unifyCode = new UnifyCode(code, fileName);
        DetailDisasterForm infoFromCode = unifyCodeService.getInfoFromCode(unifyCode);
        boolean save = detailDisasterService.save(infoFromCode.formDetailDisaster());
        if (!save)
            throw new BusinessException(ErrorCode.INNER_ERROR, "插入灾情数据失败");
        return ResultUtils.success(true);

    }


    /**
     * 通过excel文件读入文本类型编码
     *
     * @param file
     * @return
     */
    @PostMapping("insertByExcel")
    @LogAnnotation(businessType = 1,content = "通过excel文件读入文本类型编码")
    @Operation(summary = "通过excel文件读入文本类型编码")
    public BaseResponse<Boolean> insertByExcel(@RequestPart("file") MultipartFile file) {
        System.out.println(file);
        String contentType = file.getContentType();
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "文件上传为空");
        }
        if (!contentType.equals("application/vnd.ms-excel") && !contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请上传excel文件");
        }
        unifyCodeService.insertCodeByExcel(file);
        return ResultUtils.success(true);
    }

    /**
     * 分页获取灾情信息，按时间倒序
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/getInfo")
    @LogAnnotation(businessType = 1,content = "分页获取灾情信息，按时间倒序")
    @Operation(summary = "分页获取灾情信息，按时间倒序")
    public BaseResponse<CustomPageDTO<DetailDisasterForm>> getPagedDisasterInfo(
            @RequestParam(required = false) Integer pageIndex,
            @RequestParam(required = false) Integer pageSize) {
        if (pageIndex == null || pageSize == null)
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        if (pageIndex <= 0 || pageSize <= 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        LambdaQueryWrapper<DetailDisaster> disasterInfoListWrapper = new LambdaQueryWrapper<>();
        disasterInfoListWrapper.orderByDesc(DetailDisaster::getOccurTime);
        Page<DetailDisaster> detailDisasterPage = new Page<>(pageIndex, pageSize);
        Page<DetailDisaster> page = detailDisasterService.page(detailDisasterPage, disasterInfoListWrapper);
        IPage<DetailDisasterForm> convertPage = page.convert(DetailDisaster::formDetailDisasterForm);
        return ResultUtils.page(convertPage);
    }

    /**
     * 输入文件名和编码，返回临时上传地址
     * 注意contentType必须一致
     *
     * @param uploadUrlRequest
     * @return
     */
    @PostMapping("/getUploadUrl")
    @LogAnnotation(businessType = 1,content = "输入文件名和编码，返回临时上传地址")
    @Operation(summary = "输入文件名和编码，返回临时上传地址")
    public BaseResponse<UploadUrlResponse> getUploadUrl(@RequestBody(required = false) UploadUrlRequest uploadUrlRequest) {
        // 检查参数
        if (uploadUrlRequest == null)
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        UploadUrlResponse uploadUrlResponse = ossService.getUploadTempUrl(uploadUrlRequest);
        return ResultUtils.success(uploadUrlResponse);
    }

    /**
     * 删除编码条目
     */
    @DeleteMapping("/deleteOneCode")
    @LogAnnotation(businessType = 1,content = "删除编码条目")
    @Operation(summary = "删除编码条目")
    public BaseResponse<Boolean> deleteOneCode(@RequestParam(required = false) String id) {
        if (StringUtils.isBlank(id))
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        // 读取该条信息
        DetailDisaster detailDisaster = detailDisasterService.getById(id);
        // 判断类型
        String codeType = detailDisaster.getCodeType();
        if (codeType.equals("文字")) {
            detailDisasterService.removeById(id);
            return ResultUtils.success(true);
        }
        // 阿里云oss删除
        String fileName = detailDisaster.getDescription();
        ossService.deleteFile(fileName);
        detailDisasterService.removeById(id);
        return ResultUtils.success(true);
    }

    /**
     * 获取临时get请求
     */
    @PostMapping("/getDownloadUrl")
    @LogAnnotation(businessType = 1,content = "获取临时get下载请求")
    @Operation(summary = "获取临时get下载请求")
    public BaseResponse<UploadUrlResponse> getDownloadUrl(@RequestBody(required = false) UploadUrlRequest uploadUrlRequest) {
        // 检查参数
        String fileName = uploadUrlRequest.getFileName();
        if (StringUtils.isAnyBlank(fileName))
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        UploadUrlResponse downloadTempUrl = ossService.getDownloadTempUrl(fileName);
        return ResultUtils.success(downloadTempUrl);
    }

    /**
     * 获得灾情统计数据
     */
    @GetMapping("/getStatistics")
    @LogAnnotation(businessType = 1,content = "获得灾情统计数据")
    @Operation(summary = "获得灾情统计数据")
    public BaseResponse<Map<String, Object>> getStatistics(@RequestParam(required = false) Integer month) {
        if (month == null) {
            month = 6;
        } else if (month <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "month不能小于等于0");
        }
        Map<String, Object> statisticsData = detailDisasterService.getStatisticsData(month);
        return ResultUtils.success(statisticsData);
    }
}
