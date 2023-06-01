package com.yang.blog.dynamic.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.blog.model.*;
import com.yang.blog.pojo.dto.DynamicDto;
import com.yang.blog.dynamic.pojo.entity.Dynamic;
import com.yang.blog.dynamic.mapper.DynamicMapper;
import com.yang.blog.pojo.entity.BaseEntity;
import com.yang.blog.pojo.vo.DynamicVo;
import com.yang.blog.dynamic.service.DynamicService;
import com.yang.blog.pojo.vo.UserVo;
import com.yang.blog.utils.BeanCopyUtils;
import com.yang.blog.utils.JwtUtil;
import com.yang.blog.utils.ReflectEntity;
import com.yang.blog.utils.RestTemplateUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * (Dynamic)表服务实现类
 *
 * @author makejava
 * @since 2023-04-23 17:11:31
 */
@Service("dynamicService")
public class DynamicServiceImpl extends ServiceImpl<DynamicMapper, Dynamic> implements DynamicService {

    @Value("${base.staticPath}")
    private String staticPath;


    /**
     * 分页查询动态信息
     * @param pageQuery
     * @return
     */
    @Override
    public PageResult page(PageQuery pageQuery) {
        if(Objects.isNull(pageQuery)){
            pageQuery = new PageQuery();
        }
        Page<Dynamic> page = new Page<>(pageQuery.getCurrent(), pageQuery.getSize());
        QueryWrapper<Dynamic> queryWrapper = new QueryWrapper<>();
        // 按创建时间逆序查询
        queryWrapper.orderByDesc("create_time");
        // 拿到条件
        Map<String, Object> map = ReflectEntity.reflect(pageQuery.getQueryModel(), true);
        map.forEach(queryWrapper::eq);

        page(page, queryWrapper);

        // 查询到的动态数据
        List<Dynamic> records = page.getRecords();
        // 循环遍历，创建vo
        List<DynamicVo> dynamicVoList =new ArrayList<>();
        for(Dynamic dynamic : records){
            DynamicVo dynamicVo = BeanCopyUtils.convert(dynamic, DynamicVo.class);
            // 根据id查询用户信息
            UserVo user = getUserVoById(dynamic.getCreateBy());
            dynamicVo.setUser(user);
            // 如果有图片信息，则将图片信息转数组
            if(StringUtils.hasText(dynamic.getImgs())){
                String imgs = dynamic.getImgs().replace("/image", staticPath + "/image");
                dynamicVo.setImgs(JSON.parseObject(imgs, String[].class));
            }
            // 对评论数量进行查询
            Integer commentCount = getCommentCount(dynamicVo.getId());
            dynamicVo.setCommentCount(commentCount);
            dynamicVoList.add(dynamicVo);
        }
        // 封装pageResult
        PageResult<DynamicVo> pageResult = new PageResult<>(page, dynamicVoList);

        return pageResult;
    }

    @Override
    public Result add(DynamicDto dynamicDto, String token) {
        // 1、非空校验
        // 1.1 登录校验
        if(!StringUtils.hasText(token)){
            return Result.error(StatusEnum.ILLEGAL_REQUEST);
        }
        // 1.1.2 解析token获取发布者id
        Long userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = Long.valueOf(claims.getSubject());
        } catch (Exception e) {
            return Result.error(StatusEnum.LOGIN_EXPIRED);
        }

        // 1.2 内容校验
        if(Objects.isNull(dynamicDto)) {
            return Result.error();
        }
        // 2、保存
        Dynamic dynamic = BeanCopyUtils.convert(dynamicDto, Dynamic.class);
        // 添加发布者id
        dynamic.setCreateBy(userId);
        String[] imgs = dynamicDto.getImgs();
        // 字符串数组转字符串
        dynamic.setImgs(JSON.toJSONString(imgs));
        // 将图片路径提取，只保存static后面的部分
        if(dynamic.getImgs().contains(staticPath)){
            dynamic.setImgs(dynamic.getImgs().replace(staticPath, ""));
        }
        boolean save = save(dynamic);
        return Result.ok(save, CodeConstant.SUCCESS, "发布成功");
    }

    @Override
    public Result<DynamicVo> selectById(Long id) {
        if(id == null || id == 0){
            return Result.error();
        }

        // 1、根据id查询动态内容
        Dynamic dynamic = getById(id);
        // 2、根据动态创建者id查询发布者信息
        UserVo userVo = getUserVoById(dynamic.getCreateBy());
        // 3、封装Vo
        DynamicVo dynamicVo = BeanCopyUtils.convert(dynamic, DynamicVo.class);
        // 3.1 对imgs进行解析
        dynamicVo.setImgs(JSON.parseObject(dynamic.getImgs(), String[].class));
        // 4、用户信息封装
        dynamicVo.setUser(userVo);
        // 对评论数量进行查询
        Integer commentCount = getCommentCount(dynamicVo.getId());
        dynamicVo.setCommentCount(commentCount);
        // 5、返回结果
        return Result.ok(true, CodeConstant.SUCCESS, "查询成功", dynamicVo);
    }

    @Override
    public Result delById(Long id, String token) {

        // 1.1 登录校验
        if(!StringUtils.hasText(token)){
            return Result.error(StatusEnum.ILLEGAL_REQUEST);
        }
        // 1.1.2 解析token获取发布者id
        Long userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = Long.valueOf(claims.getSubject());
        } catch (Exception e) {
            return Result.error(StatusEnum.LOGIN_EXPIRED);
        }

        if(id == null || id == 0){
            return Result.error();
        }
        // 逻辑删除自实现
        LambdaUpdateWrapper<Dynamic> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Dynamic::getUpdateBy, userId)
                .set(Dynamic::getDelFlag, 1)
                .eq(Dynamic::getId, id)
                .eq(Dynamic::getCreateBy, userId)
                .eq(Dynamic::getDelFlag, 0);
        boolean flag = update(updateWrapper);

        return Result.ok(flag, CodeConstant.SUCCESS, "请求成功");
    }
}

