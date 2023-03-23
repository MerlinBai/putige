package com.itheima.common;

/**
 * @ClassName GlobalExceptionHandler
 * @Description TODO
 * @Author Bai
 * @Date 2023/3/14 09 : 19
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 *全局异常处理
 */
   @ControllerAdvice(annotations = {RestController.class, Controller.class})
   @ResponseBody
   @Slf4j
public class GlobalExceptionHandler {
    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){

        if (ex.getMessage().contains("Duplicate entry")){
            String[]split=ex.getMessage().split(" ");
//            System.out.println(split.toString());
            for (String s : split) {
                System.out.print(s);
            }
//            for(int i=15;i<=)
            String msg="用户名："+split[2]+"已存在";
           return R.error(msg);
        }

        return R.error("未知错误");
       }




    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        return R.error(ex.getMessage());
    }
}
