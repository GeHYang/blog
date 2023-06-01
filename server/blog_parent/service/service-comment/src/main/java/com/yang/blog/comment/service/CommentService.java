package com.yang.blog.comment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.blog.comment.entity.Comment;
import com.yang.blog.model.PageQuery;
import com.yang.blog.model.PageResult;
import com.yang.blog.model.Result;
import com.yang.blog.pojo.dto.CommentDto;
import com.yang.blog.pojo.vo.CommentVo;
import com.yang.blog.pojo.vo.UserVo;
import com.yang.blog.service.BaseService;
import com.yang.blog.utils.RestTemplateUtils;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;


/**
 * (Comment)表服务接口
 *
 * @author makejava
 * @since 2023-05-16 11:56:45
 */
public interface CommentService extends BaseService<Comment> {

    /**
     * 发起评论
     * @param commentDto
     * @return
     */
    Result comment(CommentDto commentDto, Long userId);

    /**
     * 根据动态id查询评论信息
     * @param dynamicId
     * @return
     */
    Result<List<CommentVo>> getCommentByDynamicId(Long dynamicId);

    /**
     * 评论分页查询
     * @param dynamicId
     * @param pageQuery
     * @return
     */
    Result<PageResult<CommentVo>> commentPage(Long dynamicId, PageQuery pageQuery);

    /**
     * 根据动态id获取评论数量
     * @param dynamicId
     * @return
     */
    Result<Integer> commentCountByDynamicId(Long dynamicId);
}
