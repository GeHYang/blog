package com.yang.blog.utils;

import com.yang.blog.model.QueryModel;
import com.yang.blog.pojo.dto.UserDto;
import com.yang.blog.pojo.vo.UserVo;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: Yang
 * @create: 2023-05-16
 * @Description:
 */
public class Test {

    public static void main(String[] args) throws Exception {
//        testMD5();
//        testBCrypt();
//        testReflect();
//        testReflect1();
//        testReplace();
//        testBeanCopy();
        testListSort();
    }

    private static void testListSort() {
        List<Integer> list = new ArrayList<>();
        list.add(30);
        list.add(15);
        list.add(23);
        list.add(11);
        list.add(1);
    }

    private static void testBeanCopy(){
        UserDto userDto = new UserDto();
        userDto.setAvatar("123456");
        UserVo convert = BeanCopyUtils.convert(userDto, UserVo.class);
    }

    private static void testReplace(){
        String str = "[\"http://192.168.0.4:32002/static/image/1657646334238810114.jpeg\",\"http://192.168.0.4:32002/static/image/1657646347425701890.jpeg\"]";
        System.out.println(str.replaceAll("http://192.168.0.4:32002/static", ""));
    }

    private static void testReflect1() {
        QueryModel queryModel = new QueryModel();
        Map<String, Object> reflect = ReflectEntity.reflect(queryModel, true, true);
        reflect.forEach((key, val) -> {
            System.out.println(key + "  " + val);
        });
    }

    private static void testReflect() throws IllegalAccessException {
        QueryModel queryModel = new QueryModel();
        Field[] fields = queryModel.getClass().getDeclaredFields();// 拿到QueryModel的字段
        for (Field field : fields) {
            System.out.println(field.getName());
            // 关闭安全校验
            field.setAccessible(true);
            System.out.println(field.get(queryModel));
        }
    }

    private static void testBCrypt(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("123456");
        String encode1 = encoder.encode("123456");
        System.out.println(encode1);
        System.out.println(encode);
        System.out.println(encoder.matches("123456", "$2a$10$Pj.mCSQ1VcspdKrFhapvruAH2P1tNwSIgO5h23qD6m/WSZOvTjC4."));
        System.out.println(encoder.matches("123456", "$2a$10$Pj.mCSQ1VcspdKrFhapvruAH2P1tNwSIgO5h23qD6m/WSZOvTjC4."));

    }

    private static void testMD5() throws NoSuchAlgorithmException {
        String pass = "123456";
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(pass.getBytes());
        byte[] digest = md5.digest();
        System.out.println(ByteUtils.toHexString(digest));
        pass = "123456";
        md5 = MessageDigest.getInstance("MD5");
        md5.update(pass.getBytes());
        digest = md5.digest();
        System.out.println(ByteUtils.toHexString(digest));
        System.out.println("---------------------------------------------------------");
        MessageDigest sha = MessageDigest.getInstance("SHA");
        sha.update(pass.getBytes());
        byte[] digest1 = sha.digest();
        System.out.println(ByteUtils.toHexString(digest1));
        sha = MessageDigest.getInstance("SHA");
        sha.update(pass.getBytes());
        digest1 = sha.digest();
        System.out.println(ByteUtils.toHexString(digest1));
    }
}
