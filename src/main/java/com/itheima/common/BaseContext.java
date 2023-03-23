package com.itheima.common;

/**
 * @ClassName BaseContext
 * @Description TODO
 * @Author Bai
 * @Date 2023/3/14 16 : 41
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();


    public static void setCurrentId(Long id){

        threadLocal.set(id);
    }

    public static Long getCurrentId(){

        return threadLocal.get();
    }

}
