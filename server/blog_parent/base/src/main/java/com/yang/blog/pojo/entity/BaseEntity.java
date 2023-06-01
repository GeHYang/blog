package com.yang.blog.pojo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: Yang
 * @create: 2023-04-23
 * @Description: 数据库公共字段
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity implements Serializable {
    //创建时间
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

    //创建者id
    @ApiModelProperty(name = "createBy", value = "创建者id")
    private Long createBy;

    //更新时间
    @ApiModelProperty(name = "updateTime", value = "更新时间")
    private Date updateTime;

    //更新者id
    @ApiModelProperty(name = "updateBy", value = "更新者id")
    private Long updateBy;

    //删除状态：0正常，1已删除
    @ApiModelProperty(name = "delFlag", value = "删除状态：0正常，1已删除")
    private Integer delFlag;
}
