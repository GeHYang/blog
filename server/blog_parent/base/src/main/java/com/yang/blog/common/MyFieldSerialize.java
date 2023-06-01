package com.yang.blog.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author: Yang
 * @create: 2023-05-14
 * @Description: 自定义字段序列化器
 */
public class MyFieldSerialize extends JsonSerializer<String> {
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        value = value.replaceAll("(\\S{1,3})(\\w+)((@.*)|(\\d{3}))","$1*****$3");
        gen.writeString(value);
    }

    public static void main(String[] args) {
        String regx = "(\\S{1,3})(\\w+)((@.*)|(\\d{3}))";
        String value = "haksfhdaksfjka@163.com";
        value = value.replaceAll(regx,"$1****$3");
        System.out.println(value);
        value = "18888888888";
        value = value.replaceAll(regx,"$1****$3");
        System.out.println(value);
    }
}
