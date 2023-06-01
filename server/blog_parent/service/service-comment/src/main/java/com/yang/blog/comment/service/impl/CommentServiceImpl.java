package com.yang.blog.comment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.blog.comment.entity.Comment;
import com.yang.blog.comment.mapper.CommentMapper;
import com.yang.blog.comment.service.CommentService;
import com.yang.blog.model.*;
import com.yang.blog.pojo.dto.CommentDto;
import com.yang.blog.pojo.vo.CommentVo;
import com.yang.blog.pojo.vo.UserVo;
import com.yang.blog.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * (Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-05-16 11:56:45
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    public Result<CommentVo> comment(CommentDto commentDto, Long userId) {
        if(Objects.isNull(commentDto)){
            return Result.error(StatusEnum.PARAM_CHECK_FAIL);
        }
        Comment comment = BeanCopyUtils.convert(commentDto, Comment.class);
        // 设置评论人id
        comment.setCreateBy(userId);
        // 手动设置评论id
        comment.setId(IdWorker.getId());
        // 保存评论
        save(comment);
        // 根据id查询评论信息
        comment = getById(comment.getId());
        // 封装VO
        CommentVo commentVo = BeanCopyUtils.convert(comment, CommentVo.class);
        // 查询用户信息
        UserVo user = getUserVoById(comment.getCreateBy());
        commentVo.setUser(user);
        // 查询回复人信息
        if(comment.getReplyId() != null){
            Comment replayComment = getById(comment.getReplyId());
            commentVo.setReplyUser(getUserVoById(replayComment.getCreateBy()));
        }
        return Result.ok(StatusEnum.REQUEST_SUCCESS, commentVo);
    }

    @Override
    public Result<List<CommentVo>> getCommentByDynamicId(Long dynamicId) {
        // 1、查询一级评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getDynamicId, dynamicId)
                .isNull(Comment::getPid).orderByDesc(Comment::getCreateTime);
        // 创建初始分页查询条件
        PageQuery pageQuery = new PageQuery();
        List<CommentVo> commentVoList = getCommentVoList(queryWrapper, pageQuery);
        pageQuery.setSize(1);
        // 3、查询二级评论
        getChildrenByPid(dynamicId, pageQuery, commentVoList);

        return Result.ok(StatusEnum.REQUEST_SUCCESS, commentVoList);
    }

    @Override
    public Result<PageResult<CommentVo>> commentPage(Long dynamicId, PageQuery pageQuery) {
        if(Objects.isNull(pageQuery.getQueryModel())){
            pageQuery.setQueryModel(new QueryModel());
        }
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getDynamicId, dynamicId).orderByDesc(Comment::getCreateTime);
        if(pageQuery.getQueryModel().getId() == null){
            queryWrapper.isNull(Comment::getPid);
        } else {
            queryWrapper.eq(Comment::getPid, pageQuery.getQueryModel().getId());
        }
        Page<Comment> page = new Page<>(pageQuery.getCurrent(), pageQuery.getSize());
        page(page, queryWrapper);
        List<CommentVo> commentVoList = getCommentVoList(queryWrapper, pageQuery, page);
        // 如果分页查询的是一级评论，则在查询一条二级评论添加进去
        if(pageQuery.getQueryModel().getId() == null) {
            pageQuery.setCurrent(1);
            pageQuery.setSize(1);
            getChildrenByPid(dynamicId, pageQuery, commentVoList);
        }

        PageResult<CommentVo> pageResult = new PageResult<>(page, commentVoList);
        return Result.ok(StatusEnum.REQUEST_SUCCESS, pageResult);
    }

    @Override
    public Result<Integer> commentCountByDynamicId(Long dynamicId) {
        if(dynamicId == null){
            return Result.error(StatusEnum.PARAM_CHECK_FAIL);
        }
        // 查询所有文章id为dynamicId的评论属性
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getDynamicId, dynamicId);
        int count = count(queryWrapper);

        return Result.ok(StatusEnum.REQUEST_SUCCESS, count);
    }

    /**
     * 根据父评论id查询子评论
     * @param dynamicId
     * @param pageQuery
     * @param commentVoList
     */
    private void getChildrenByPid(Long dynamicId, PageQuery pageQuery, List<CommentVo> commentVoList) {
        LambdaQueryWrapper<Comment> queryWrapper;
        for (CommentVo commentVo : commentVoList) {
            // 1、根据动态id、父评论id查询所有子评论，按照时间降序排列
            queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Comment::getDynamicId, dynamicId)
                    .eq(Comment::getPid, commentVo.getId()).orderByDesc(Comment::getCreateTime);
            List<CommentVo> childrenVo = getCommentVoList(queryWrapper, pageQuery);
            commentVo.setChildren(childrenVo);
        }
    }

    /**
     * 获取评论列表
     * @param queryWrapper 查询条件
     * @return 查询结果
     */
    private List<CommentVo> getCommentVoList(LambdaQueryWrapper<Comment> queryWrapper, PageQuery pageQuery, Page<Comment> page) {
        if(page == null){
            page = new Page<>(pageQuery.getCurrent(), pageQuery.getSize());
            page(page, queryWrapper);
        }

        List<Comment> children = page.getRecords();
        List<CommentVo> childrenVo = new ArrayList<>();
        for (Comment child : children) {
            CommentVo childVo = BeanCopyUtils.convert(child, CommentVo.class);
            // 获取发布者的用户信息
            UserVo userVo = getUserVoById(child.getCreateBy());
            // 封装用户信息
            childVo.setUser(userVo);
            // 获取评论的那条评论的发布者
            if(child.getReplyId() != null){
                Comment comment = getById(child.getReplyId());
                // 获取发布者信息
                UserVo replyUser = getUserVoById(comment.getCreateBy());
                childVo.setReplyUser(replyUser);
            }
            // 封装到VO
            childrenVo.add(childVo);
        }
        return childrenVo;
    }

    private List<CommentVo> getCommentVoList(LambdaQueryWrapper<Comment> queryWrapper, PageQuery pageQuery){
        return getCommentVoList(queryWrapper, pageQuery, null);
    }
}

