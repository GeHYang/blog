package com.yang.blog.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Yang
 * @create: 2023-04-23
 * @Description: Bean拷贝工具类
 */
public class BeanCopyUtils {

    private BeanCopyUtils(){}

    public static <T>T convert(Object source, Class<T> clazz){
        T result = null;
        try {
            result = clazz.newInstance();
            BeanUtils.copyProperties(source, result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static <V, T>List<T> convert(List<V> sourceList, Class<T> clazz ){
        List<T> list = new ArrayList<>();
        for (V source : sourceList) {
            list.add(convert(source, clazz));
        }
        return list;
    }

}
