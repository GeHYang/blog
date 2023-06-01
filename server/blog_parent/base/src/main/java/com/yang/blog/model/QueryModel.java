package com.yang.blog.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询条件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "查询条件模型")
public class QueryModel{
    @ApiModelProperty(value = "创建人")
    private Long createBy;
    @ApiModelProperty(value = "id", example = "1")
    private Long id;
}