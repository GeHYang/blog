package com.yang.blog.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Yang
 * @create: 2023-05-17
 * @Description: 将实体反射出来
 */
public class ReflectEntity<T> {

    /**
     * 将实体反射成key/val形式
     * @param source 源数据
     * @param removeHump 是否将字段名的驼峰改为下划线
     * @param isNull 字段值为空时是否获取
     * @return
     * @param <T>
     */
    public static <T>Map<String, Object> reflect(T source, boolean removeHump, boolean isNull){
        if (Objects.isNull(source)){
            return new HashMap<>();
        }
        Class<?> clazz = source.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> map = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            // 转下划线命名
            if(removeHump){
                fieldName = removeHump(fieldName);
            }
            try {
                Object obj = field.get(source);
                if(Objects.isNull(obj) && !isNull){
                    continue;
                }
                map.put(fieldName, obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     *
     * @param source
     * @return
     * @param <T>
     */
    public static <T>Map<String, Object> reflect(T source){
        return reflect(source, false, false);
    }

    /**
     * 将实体反射成key/val形式
     * @param source 源数据
     * @param removeHump 是否将字段名的驼峰改为下划线
     * @return
     * @param <T>
     */
    public static <T>Map<String, Object> reflect(T source, boolean removeHump){
        return reflect(source, removeHump, false);
    }

    /**
     * 去掉驼峰
     * @param fieldName 字段名
     * @return
     */
    private static String removeHump(String fieldName){
        String regx = "[A-Z]";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(fieldName);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()){
            matcher.appendReplacement(sb, "_" + matcher.group().toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
