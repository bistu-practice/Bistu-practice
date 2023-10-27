package com.disaster.disastercode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.disaster.disastercode.model.domain.OperLog;
import com.disaster.disastercode.mapper.OperlogDao;
import com.disaster.disastercode.service.OperlogService;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;

/**
 * (Operlog)表服务实现类
 *
 * @author makejava
 * @since 2023-10-26 20:30:14
 */
@Service("operlogService")
public class OperlogServiceImpl extends ServiceImpl<OperlogDao, OperLog> implements OperlogService {
    @Resource
    private OperlogDao operlogDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public OperLog queryById(Integer id) {
        return this.operlogDao.queryById(id);
    }

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
    @Override
    public OperLog insert(OperLog operlog) {
        this.operlogDao.insert(operlog);
        return operlog;
    }

    /**
     * 修改数据
     *
     * @param operlog 实例对象
     * @return 实例对象
     */
    @Override
    public OperLog update(OperLog operlog) {
        this.operlogDao.update(operlog);
        return this.queryById(operlog.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.operlogDao.deleteById(id) > 0;
    }
}
