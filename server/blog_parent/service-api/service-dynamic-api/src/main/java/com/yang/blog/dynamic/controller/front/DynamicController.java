package com.yang.blog.dynamic.controller.front;

import com.yang.blog.pojo.dto.DynamicDto;
import com.yang.blog.pojo.vo.DynamicVo;
import com.yang.blog.dynamic.service.DynamicService;
import com.yang.blog.model.CodeConstant;
import com.yang.blog.model.PageQuery;
import com.yang.blog.model.PageResult;
import com.yang.blog.model.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: Yang
 * @create: 2023-04-23
 * @Description:
 */
@RestController
@RequestMapping("/front/dynamic")
public class DynamicController {

    @Resource
    private DynamicService dynamicService;

    @PostMapping("/page")
    public Result<PageResult> page(@RequestBody(required = false) PageQuery pageQuery){
        return Result.ok(true, CodeConstant.SUCCESS, "动态分页查询成功", dynamicService.page(pageQuery));
    }

    @PostMapping
    public Result add(@RequestBody DynamicDto dynamicDto, @RequestHeader("Authorization")String token){
        return dynamicService.add(dynamicDto, token);
    }

    @GetMapping("/{id}")
    public Result<DynamicVo> getById(@PathVariable("id") Long id){
        return dynamicService.selectById(id);
    }

    @DeleteMapping("/{id}")
    public Result delById(@PathVariable("id") Long id, @RequestHeader(value = "Authorization")String token){
        return dynamicService.delById(id, token);
    }
}
