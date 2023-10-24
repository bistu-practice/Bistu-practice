package com.disaster.disastercode.DTO;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

/**
 * 自定义分页类，减少原本mybatis-plus分页的那么多数据，只暴露有必要的
 * @param <T>
 */
@Data
public class CustomPageDTO<T> {
    private List<T> content;
    private Long total;

    public CustomPageDTO(IPage<T> page) {
        this.content = page.getRecords();
        this.total = page.getTotal();
    }
}