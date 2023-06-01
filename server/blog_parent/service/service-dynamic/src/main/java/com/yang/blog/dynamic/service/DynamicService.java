package com.yang.blog.dynamic.service;

import com.yang.blog.pojo.dto.DynamicDto;
import com.yang.blog.dynamic.pojo.entity.Dynamic;
import com.yang.blog.pojo.vo.DynamicVo;
import com.yang.blog.model.PageQuery;
import com.yang.blog.model.PageResult;
import com.yang.blog.model.Result;
import com.yang.blog.pojo.vo.UserVo;
import com.yang.blog.service.BaseService;
import com.yang.blog.utils.RestTemplateUtils;
import org.springframework.core.ParameterizedTypeReference;


/**
 * (Dynamic)表服务接口
 *
 * @author makejava
 * @since 2023-04-23 17:11:30
 */
public interface DynamicService extends BaseService<Dynamic> {

    PageResult page(PageQuery pageQuery);

    /**
     * 发布动态
     * @param dynamicDto
     * @return
     */
    Result add(DynamicDto dynamicDto, String token);

    /**
     * 根据动态id查询动态详情
     * @param id
     * @return
     */
    Result<DynamicVo> selectById(Long id);

    /**
     * 动态删除
     * @param id
     * @return
     */
    Result delById(Long id, String token);


    String COMMENT_PATH = "http://commentApplication/front/comment/";// 用户服务
    /**
     * 根据文章id查询文章评论数量
     * @param dynamicId
     * @return
     */
    default Integer getCommentCount(Long dynamicId) {
        ParameterizedTypeReference<Result<Integer>> resultParameterizedTypeReference = new ParameterizedTypeReference<Result<Integer>>() {};
        Result<Integer> result = RestTemplateUtils.get(COMMENT_PATH + "count/" + dynamicId,
                null, resultParameterizedTypeReference);
        return result.getData();
    }

}
