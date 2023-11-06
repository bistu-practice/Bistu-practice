package com.disaster.disastercode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.disaster.disastercode.Annotation.LogAnnotation;
import com.disaster.disastercode.Annotation.RateLimiter;
import com.disaster.disastercode.DTO.CustomPageDTO;
import com.disaster.disastercode.common.BaseResponse;
import com.disaster.disastercode.common.ErrorCode;
import com.disaster.disastercode.common.ResultUtils;
import com.disaster.disastercode.exception.BusinessException;
import com.disaster.disastercode.model.domain.OperLog;
import com.disaster.disastercode.service.OperlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Operlog)表控制层
 *
 * @author makejava
 * @since 2023-10-26 20:30:05
 */
@RestController
@RequestMapping("/api/operlog")
public class OperlogController {
    /**
     * 服务对象
     */
    @Resource
    private OperlogService operlogService;
    @RateLimiter(value = 2, timeout = 100)
    @GetMapping("/rateLimiter")
    public String rateLimiter() {
        return "你不能总是看到我，快速刷新我看一下！";
    }
    @GetMapping
    @LogAnnotation(businessType = 1,content = "日志的分页查找")
    @RateLimiter(value = 2, timeout = 100)
    public BaseResponse<CustomPageDTO<OperLog>> queryByPage(@RequestParam(required = false) Integer pageIndex,
                                                                @RequestParam(required = false) Integer pageSize) {
        if (pageIndex == null || pageSize == null)
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR);
        if (pageIndex <= 0 || pageSize <= 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        LambdaQueryWrapper<OperLog> operationListWrapper = new LambdaQueryWrapper<>();
        operationListWrapper.orderByDesc(OperLog::getOperationTime);
        Page<OperLog> operLogPage = new Page<>(pageIndex, pageSize);
        Page<OperLog> page = operlogService.page(operLogPage, operationListWrapper);
        return ResultUtils.page(page);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<OperLog> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.operlogService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param operlog 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<OperLog> add(OperLog operlog) {
        return ResponseEntity.ok(this.operlogService.insert(operlog));
    }

    /**
     * 编辑数据
     *
     * @param operlog 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<OperLog> edit(OperLog operlog) {
        return ResponseEntity.ok(this.operlogService.update(operlog));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.operlogService.deleteById(id));
    }

}

