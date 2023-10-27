package com.disaster.disastercode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.disaster.disastercode.model.domain.OperLog;
import org.apache.ibatis.annotations.Param;
import java.util.List;


/**
 * (Operlog)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-26 20:30:05
 */
public interface OperlogDao extends BaseMapper<OperLog> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    OperLog queryById(Integer id);



    /**
     * 统计总行数
     *
     * @param operlog 查询条件
     * @return 总行数
     */
    long count(OperLog operlog);

    /**
     * 新增数据
     *
     * @param operlog 实例对象
     * @return 影响行数
     */
    int insert(OperLog operlog);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Operlog> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<OperLog> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Operlog> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<OperLog> entities);

    /**
     * 修改数据
     *
     * @param operlog 实例对象
     * @return 影响行数
     */
    int update(OperLog operlog);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

