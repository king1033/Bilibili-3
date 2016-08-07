package org.pqh.test;

import org.pqh.service.InsertService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 10295 on 2016/5/9.
 */
public class TaskCid implements Runnable {
    private int cid;
    public static InsertService insertService;
    private Method method;

    public TaskCid(int cid, String methodName) {
        this.cid = cid;
        try {
            this.method = insertService.getClass().getDeclaredMethod(methodName, Integer.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            method.invoke(insertService,cid);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}
