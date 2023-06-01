package com.yang.blog.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.blog.model.CodeConstant;
import com.yang.blog.model.Result;
import com.yang.blog.model.StatusEnum;
import com.yang.blog.pojo.dto.UserDto;
import com.yang.blog.pojo.entity.EmailEntity;
import com.yang.blog.user.mapper.UserMapper;
import com.yang.blog.pojo.vo.UserVo;
import com.yang.blog.user.pojo.entity.User;
import com.yang.blog.utils.*;
import io.jsonwebtoken.Claims;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.yang.blog.user.service.UserService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2023-04-23 23:14:25
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private RedisCache redisCache;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private PasswordEncoder passwordEncoder; // 加密方式
    private final String UPLOAD_PATH = "http://uploadApplication/";// upload服务地址
    @Value("${base.staticPath}")
    private String staticPath;
    private String absPath = System.getProperties().getProperty("user.dir") + "/service-api/service-user-api/src/main/resources/temp/";

    @Override
    public UserVo selectById(Long id) {
        User user = getById(id);
        UserVo userVo = BeanCopyUtils.convert(user, UserVo.class);
        return userVo;
    }

    @Override
    public Result emailRegisterCode(String email) {
        // 1、非空判断
        if(!StringUtils.hasText(email)){
            return Result.error(CodeConstant.ERROR, "邮箱号不能为空");
        }
        // 1.2、校验邮箱号格式
        if(!email.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")){
            return Result.error(CodeConstant.ERROR, "邮箱号格式错误");
        }
        // 2、根据邮箱号查询数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = getOne(queryWrapper);

        // 3、判断该邮箱是否已注册账号
        if(!Objects.isNull(user)) {
            return Result.error(CodeConstant.ERROR, "该邮箱号已注册");
        }
        // 4、生成随机验证码
        String id = String.valueOf(IdWorker.getId());
        // 4.1、将id字符串截取后面四位
        id = id.substring(id.length() - 4);
        // 5、设定过期时间
        redisCache.setCacheObject("code:" + email, id, 5, TimeUnit.MINUTES); // 设置5分钟超时时间
        // 6、通过邮箱服务器发送验证码
        EmailEntity emailEntity = new EmailEntity(
                "您好，感谢您在本平台注册了账号，账号验证码为：<b style='color: blue;'>" + id + "</b>", "注册", email);

        rabbitTemplate.convertAndSend("email.code", emailEntity);

        return Result.ok(CodeConstant.SUCCESS, "验证码发送成功");
    }

    @Override
    public Result emailRegister(UserDto userDto) {
        // 1、非空校验
        if(!StringUtils.hasText(userDto.getEmail()) || !StringUtils.hasText(userDto.getPassword()) || Objects.isNull(userDto.getCode())){
            return Result.error(CodeConstant.ERROR, "信息校验失败");
        }
        // 2、判断验证码是否正确获取超时
        String rCode = redisCache.getCacheObject("code:" + userDto.getEmail());
        if(!StringUtils.hasText(rCode)){
            return Result.error(CodeConstant.ERROR, "验证码错误");
        }
        if(!rCode.equals(userDto.getCode())){
            return Result.error(CodeConstant.ERROR, "验证码错误");
        }
        // 2.1 验证码正确，清除redis中的验证码
        redisCache.deleteObject("code:" + userDto.getEmail());
        // 3、对密码进行加密处理
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        // 4、构建用户信息
        User user = BeanCopyUtils.convert(userDto, User.class);
        // 4.1 随机选择头像
        Random random = new Random();
        int index = random.nextInt(6) + 1;
        user.setAvatar("/avatar/default-" + index + ".jpeg");
        // 4.2 随机用户名:根据UUID生成随机用户名
        user.setUsername(UUIDUtil.get10UUID());
        // 4.3 昵称
        user.setNickName("用户_" + user.getUsername());
        // 5、保存用户信息
        save(user);
        return Result.ok(CodeConstant.SUCCESS, "注册成功");
    }

    @Override
    public Result<UserVo> login(UserDto userDto) {

        // 1、账号信息非空校验
        if(Objects.isNull(userDto) || !StringUtils.hasText(userDto.getUsername()) &&
                !StringUtils.hasText(userDto.getPhoneNumber()) && !StringUtils.hasText(userDto.getEmail())){
            return Result.error(CodeConstant.ERROR, "用户名不存在");
        }
        // 2、密码非空校验
        if(!StringUtils.hasText(userDto.getPassword())){
            return Result.error(CodeConstant.ERROR, "密码错误");
        }
        // 3、通过数据库查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // 3.1 查询条件
        queryWrapper.eq(User::getUsername, userDto.getUsername())
                .or()
                .eq(User::getEmail, userDto.getEmail())
                .or()
                .eq(User::getPhoneNumber, userDto.getPhoneNumber());

        User user = getOne(queryWrapper);
        // 查到的信息为空，则用户不存在
        if(Objects.isNull(user)){
            return Result.error(CodeConstant.ERROR, "用户名不存在");
        }
        // 4、判断密码是否正确
        // 4.1 密码比对
        if(!passwordEncoder.matches(userDto.getPassword(), user.getPassword())){
            return Result.error(CodeConstant.ERROR, "密码错误");
        }
        // 5、封装用户信息
        UserVo convert = BeanCopyUtils.convert(user, UserVo.class);
        // 6、生成token令牌，用于登录校验:使用用户id生成
        String token = JwtUtil.createJWT(convert.getId());
        // 7、将token存入redis中
        redisCache.setCacheObject("blog-login-token:" + convert.getId(), token, 1, TimeUnit.DAYS);

        return Result.ok(CodeConstant.SUCCESS, token, convert);
    }

    @Override
    public Result<String> editCover(MultipartFile file, String token) {
        String userId;
        // 1、根据token解析用户id
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            return Result.error(StatusEnum.ILLEGAL_REQUEST);
        }
        // 2、如果用户存在，则将封面发送到upload服务
       Result result = uploadFile(file);
        // 3、等待upload服务响应封面网络地址
        String coverUrl = (String) result.getData();
        coverUrl.replace(staticPath, "");
        // 4、修改数据库
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getCover, coverUrl)
                        .eq(User::getId, userId);
        update(updateWrapper);

        // 5、将封面网络地址反馈给客户端
        return Result.ok(StatusEnum.UPDATE_SUCCESS, coverUrl);
    }

    @Override
    public Result<String> editInfo(MultipartFile avatar, UserDto userDto, String token) {
        if (userDto == null) userDto = new UserDto();
        // 1、token解析出用户id
        Long userId;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = Long.valueOf(claims.getSubject());
        } catch (Exception e) {
            return Result.error(StatusEnum.ILLEGAL_REQUEST);
        }
        // 2、保存图片
        String newAvatar = "";
        if(avatar != null && !avatar.isEmpty()){
            Result<String> result = uploadFile(avatar);
            if(result.getFlag()){
                newAvatar = result.getData();// 新图片链接，返回给客户端
                // 如果保存成功，则修改头像信息
                userDto.setAvatar(result.getData().replace(staticPath, ""));
                // 3、删除旧头像
                // 3.1、查询用户旧头像
                User user = getById(userId);
                String oldAvatar = user.getAvatar();
                // 如果使用的不是默认头像，则删除 1657987979018780674
                if(!oldAvatar.contains("/avatar/default")){
                    // 提取文件名
                    oldAvatar = oldAvatar.substring(oldAvatar.lastIndexOf("/"));
                    ParameterizedTypeReference<Result> parameterizedTypeReference = new ParameterizedTypeReference<Result>() {};
                    // 提交删除文件请求
                    RestTemplateUtils.req(UPLOAD_PATH + oldAvatar, null, HttpMethod.DELETE, null, parameterizedTypeReference);
                }
            }
        }

        // 4、根据userId修改信息
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(StringUtils.hasText(userDto.getNickName()),User::getNickName, userDto.getNickName())
                .set(!Objects.isNull((userDto.getSex())),User::getSex, userDto.getSex())
                .set(!Objects.isNull(userDto.getBirthday()),User::getBirthday, userDto.getBirthday())
                .set(StringUtils.hasText(userDto.getAvatar()), User::getAvatar, userDto.getAvatar())
                .eq(User::getId, userId);
        // 5、修改
        update(updateWrapper);
        return Result.ok(StatusEnum.UPDATE_SUCCESS, newAvatar);
    }

    @Override
    public Result<String> updatePass(String oldPass, String newPass, Long userId) {
        // 1、校验信息
        if (!StringUtils.hasText(oldPass) || !StringUtils.hasText(newPass)){
            return Result.error(StatusEnum.PARAM_CHECK_FAIL);
        }
        // 2、查询用户密码
        User user = getById(userId);

        // 3、判断旧密码是否一致
        if(!passwordEncoder.matches(oldPass, user.getPassword())){
            return Result.error(StatusEnum.PASSWORD_NOT_EQ);
        }
        // 4、判断新旧密码是否一致
        if(oldPass.equals(newPass)){
            return Result.ok(StatusEnum.UPDATE_SUCCESS);
        }
        // 5、修改密码
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getPassword, passwordEncoder.encode(newPass))
                        .eq(User::getId, userId);
        update(updateWrapper);
        return Result.ok(StatusEnum.UPDATE_SUCCESS);
    }

    @Override
    public Result<String> logout(Long userId) {
        // 1、清空redis数据
        redisCache.deleteObject("blog-login-token:" + userId);
        return Result.ok(StatusEnum.REQUEST_SUCCESS);
    }


    /**
     * 上传文件到upload服务
     * @param file
     * @return
     */
    private Result uploadFile(MultipartFile file){
        // 2.1、将文件保存为临时文件
        FileSystemResource resource;
        File tempFile = null;

        try {
            tempFile = new File(absPath + file.getOriginalFilename());
            file.transferTo(tempFile);
            resource = new FileSystemResource(tempFile);
        } catch (IOException e) {
            if(tempFile != null && tempFile.exists()){
                tempFile.delete();// 删除图片
            }
            return Result.error(StatusEnum.FAIL);
        }
        // 2.2、构建参数用于传输
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", resource);
        ParameterizedTypeReference<Result> parameterizedTypeReference = new ParameterizedTypeReference<Result>() {};
        Result result = RestTemplateUtils.postFile(UPLOAD_PATH + "upload", map, parameterizedTypeReference);
        // 5、删除临时文件
        tempFile.delete();// 删除图片
        return result;
    }
}

