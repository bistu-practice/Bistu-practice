package com.disaster.disastercode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.disaster.disastercode.model.domain.OperLog;

/**
 * (Operlog)表服务接口
 *
 * @author makejava
 * @since 2023-10-26 20:30:14
 */
public interface OperlogService extends IService<OperLog> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    OperLog queryById(Integer id);

    /**
     * 分页查询
     *
     * @param operlog 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */


    /**
     * 新增数据
     *
     * @param operlog 实例对象
     * @return 实例对象
     */
    OperLog insert(OperLog operlog);

    /**
     * 修改数据
     *
     * @param operlog 实例对象
     * @return 实例对象
     */
    OperLog update(OperLog operlog);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
