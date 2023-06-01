package com.yang.blog.comment.controller.front;

import com.yang.blog.comment.service.CommentService;
import com.yang.blog.model.PageQuery;
import com.yang.blog.model.PageResult;
import com.yang.blog.model.Result;
import com.yang.blog.pojo.dto.CommentDto;
import com.yang.blog.pojo.vo.CommentVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: Yang
 * @create: 2023-05-16
 * @Description:
 */
@RestController
@RequestMapping("/front/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @ApiOperation("发表评论")
    @PostMapping
    public Result<CommentVo> comment(@RequestBody CommentDto commentDto, @ApiIgnore @RequestHeader("user-id") Long userId){
        return commentService.comment(commentDto, userId);
    }

    @GetMapping("/{dynamicId}")
    public Result<List<CommentVo>> getCommentByDynamicId(@PathVariable("dynamicId") Long dynamicId){
        return commentService.getCommentByDynamicId(dynamicId);
    }
    @PostMapping("/{dynamicId}")
    public Result<PageResult<CommentVo>> commentPage(@PathVariable Long dynamicId, @RequestBody PageQuery pageQuery){
        return commentService.commentPage(dynamicId, pageQuery);
    }

    @GetMapping("/count/{dynamicId}")
    public Result<Integer> commentCountByDynamicId(@PathVariable("dynamicId") Long dynamicId){
        return commentService.commentCountByDynamicId(dynamicId);
    }
}
