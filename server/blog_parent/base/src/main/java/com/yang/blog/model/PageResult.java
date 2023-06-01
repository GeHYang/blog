package com.yang.blog.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Yang
 * @create: 2023-04-23
 * @Description: 分页查询统一响应实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> implements Serializable {

    private Long total; // 总数
    private Long current; // 当前页数
    private Long size; // 分页大小
    private Long pages; // 总页数
    private List<T> data; // 数据

    public PageResult(Page page, List<T> data){
        this.total = page.getTotal();
        current = page.getCurrent();
        size = page.getSize();
        pages = page.getPages();
        this.data = data;
    }

}
