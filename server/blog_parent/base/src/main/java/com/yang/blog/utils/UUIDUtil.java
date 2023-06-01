package com.yang.blog.utils;

import java.util.Random;
import java.util.UUID;

/**
 * @author: Yang
 * @create: 2023-05-08
 * @Description:
 */
public class UUIDUtil {

    public static String get10UUID(){
        // 1.最大支持1-9个集群机器部署
        int machineId = new Random().nextInt(10);
        // 2.生成uuid的hashCode值
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        // 3.有可能是负数
        if(hashCodeV < 0) {
            hashCodeV = - hashCodeV;
        }
        // 4.结果
        String value =  machineId + String.format("%010d", hashCodeV);
        return value;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            System.out.println(get10UUID());
        }
    }

}
