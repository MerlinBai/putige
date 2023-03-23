package com.itheima.common;

/**
 * @ClassName CustomException
 * @Description TODO
 * @Author Bai
 * @Date 2023/3/15 14 : 36
 */

/**
 * 自定义业务异常
 */
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }

}
