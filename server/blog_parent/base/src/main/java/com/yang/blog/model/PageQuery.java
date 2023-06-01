package com.yang.blog.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Yang
 * @create: 2023-04-23
 * @Description: 分页查询信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "分页查询")
public class PageQuery {
    @ApiModelProperty(value = "当前页面")
    private Integer current = 1; // 当前页面
    @ApiModelProperty(value = "页面大小")
    private Integer size = 10; // 页面大小
    @ApiModelProperty(value = "查询条件")
    private QueryModel queryModel; // 查询条件

}
