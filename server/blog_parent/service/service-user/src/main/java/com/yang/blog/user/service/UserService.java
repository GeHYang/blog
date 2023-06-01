package com.yang.blog.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.blog.model.Result;
import com.yang.blog.pojo.dto.UserDto;
import com.yang.blog.pojo.vo.UserVo;
import com.yang.blog.user.pojo.entity.User;
import org.springframework.web.multipart.MultipartFile;


/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2023-04-23 23:14:25
 */
public interface UserService extends IService<User> {

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    UserVo selectById(Long id);

    /**
     * 邮箱注册账号
     * @param email
     * @return
     */
    Result emailRegisterCode(String email);

    /**
     * 通过邮箱注册账号
     * @param userDto
     * @return
     */
    Result emailRegister(UserDto userDto);

    /**
     * 用户登录
     * @param userDto
     * @return
     */
    Result<UserVo> login(UserDto userDto);

    /**
     * 设置封面
     * @param file
     * @param token
     * @return
     */
    Result<String> editCover(MultipartFile file, String token);

    /**
     * 修改用户基本信息
     * @param userDto
     * @param token
     * @return
     */
    Result<String> editInfo(MultipartFile avatar, UserDto userDto, String token);

    /**
     * 密码修改
     * @param oldPass
     * @param newPass
     * @param userId
     * @return
     */
    Result<String> updatePass(String oldPass, String newPass, Long userId);

    /**
     * 退出登录
     * @param userId
     * @return
     */
    Result<String> logout(Long userId);
}
