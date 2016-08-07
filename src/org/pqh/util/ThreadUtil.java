package org.pqh.util;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 10295 on 2016/8/4.
 */
public class ThreadUtil {
    private static Logger log= TestSlf4j.getLogger(ThreadUtil.class);
    /**
     *
     * @param c
     * @param methods
     */
    public static void threadRun(Class c, String methods[]){

        try {
            final Object obj=c.newInstance();
            for(String methodName:methods) {
                final Method method = c.getDeclaredMethod(methodName);
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            method.invoke(obj);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("找不到方法");
        } catch (InstantiationException e) {
            TestSlf4j.outputLog(e,log);
        } catch (IllegalAccessException e) {
            TestSlf4j.outputLog(e,log);
        }
    }

    /**
     * 等待指定时长
     * @param log
     * @param time 毫秒
     */
    public static void sleep(Logger log,long time){
        try {
            if(time>=1000) {
                log.info("等待"+time + "ms");
            }
            Thread.sleep(time);
        } catch (InterruptedException e) {
            TestSlf4j.outputLog(e,log);
        }
    }
}
