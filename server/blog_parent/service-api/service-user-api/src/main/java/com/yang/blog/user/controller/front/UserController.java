package com.yang.blog.user.controller.front;

import com.alibaba.fastjson.JSON;
import com.yang.blog.model.CodeConstant;
import com.yang.blog.model.Result;
import com.yang.blog.pojo.dto.UserDto;
import com.yang.blog.pojo.vo.UserVo;
import com.yang.blog.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * @author: Yang
 * @create: 2023-04-26
 * @Description:
 */
@Api(description = "客户端用户接口")
@RestController
@RequestMapping("/front/user")
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "根据用户id查询用户信息")
    @GetMapping("/{id}")
    public Result<UserVo> getUserById(@ApiParam(value = "用户id", example = "1") @PathVariable("id") Long id){
        UserVo userVo = userService.selectById(id);
        return Result.ok(true, CodeConstant.SUCCESS, "请求成功", userVo);
    }
    /**
     * 获取验证码接口
     * @param email
     * @return
     */
    @ApiOperation(value = "邮箱验证码获取", notes = "通过邮箱号获取验证码")
    @PostMapping("/emailCode")
    public Result emailRegisterCode(@RequestParam("email") String email){
        return userService.emailRegisterCode(email);
    }
    /**
     * email注册
     * @param userDto
     * @return
     */
    @ApiOperation(value = "邮箱注册", notes = "根据邮箱号、密码以及邮箱验证码注册")
    @PostMapping("/emailRegister")
    public Result emailRegister(@RequestBody UserDto userDto){
        return userService.emailRegister(userDto);
    }

    @PostMapping("/login")
    public Result<UserVo> login(@RequestBody UserDto userDto){
        return userService.login(userDto);
    }

    @ApiOperation(value = "设置封面")
    @PostMapping("/editCover")
    public Result<String> editCover(MultipartFile file, @RequestHeader("Authorization") String token){
        return userService.editCover(file, token);
    }
    /**
     * 修改用户信息
     * @param avatar
     * @param userDtoStr
     * @param token
     * @return
     */
    @PostMapping("/editInfo")
    public Result<String> editInfo(@RequestParam(value = "avatar", required = false) MultipartFile avatar,
                                   @RequestParam(value = "userDto", required = false) String userDtoStr,
                                   @RequestHeader("Authorization") String token){

        UserDto userDto = JSON.parseObject(userDtoStr, UserDto.class);
        return userService.editInfo(avatar, userDto, token);
    }

    @ApiOperation("修改密码")
    @PatchMapping
    public Result<String> updatePass(@ApiParam("旧密码") String oldPass,
                                     @ApiParam("新密码") String newPass,
                                     @ApiIgnore @RequestHeader("user-id") Long userId){
        return userService.updatePass(oldPass, newPass, userId);
    }

    @ApiOperation("退出登录")
    @GetMapping("/logout")
    public Result<String> logout(@ApiIgnore @RequestHeader("user-id") Long userId){
        return userService.logout(userId);
    }
}
