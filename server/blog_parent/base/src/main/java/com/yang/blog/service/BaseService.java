package com.yang.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.blog.model.Result;
import com.yang.blog.pojo.vo.UserVo;
import com.yang.blog.utils.RestTemplateUtils;
import org.springframework.core.ParameterizedTypeReference;

public interface BaseService<T> extends IService<T> {
    String USER_PATH = "http://userApplication/front/user/";// 用户服务
    /**
     * 根据id查询用户信息
     * @param userId
     * @return
     */
    default UserVo getUserVoById(Long userId) {
        ParameterizedTypeReference<Result<UserVo>> resultParameterizedTypeReference = new ParameterizedTypeReference<Result<UserVo>>() {};
        Result<UserVo> result = RestTemplateUtils.get(USER_PATH + userId,
                null, resultParameterizedTypeReference);
        return result.getData();
    }
}
