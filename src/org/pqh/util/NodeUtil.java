package org.pqh.util;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by 10295 on 2016/8/4.
 * 节点工具类
 */
public class NodeUtil {
    private static Logger log= TestSlf4j.getLogger(NodeUtil.class);
    /**
     * 获取子节点
     * @param classname
     * @param Field
     * @return
     */
    public static String getChildNode(String classname, String Field){
        java.lang.reflect.Field[] fields=null;
        try {
            fields=Class.forName(classname).getDeclaredFields();
            for(Field field:fields){
                if(field.getName().equals(Field)){
                    if(field.getType().getName().equals("java.util.List")){
                        String genericType = field.getGenericType().toString().replaceAll("java.util.List<|>","");
                        return genericType;
                    }else {
                        return field.getType().getName();
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            TestSlf4j.outputLog(e,log);
        }
        return null;
    }

    /**
     * 获取父节点
     * @return
     */
    public static String getParentsNode(String classname){
        Class c= null;
        try {
            c = Class.forName(classname);
            return c.getMethod("getParents").invoke(c.newInstance()).toString();
        } catch (ClassNotFoundException e) {
            TestSlf4j.outputLog(e,log);
        } catch (NoSuchMethodException e) {
            TestSlf4j.outputLog(e,log);
        } catch (IllegalAccessException e) {
            TestSlf4j.outputLog(e,log);
        } catch (InstantiationException e) {
            TestSlf4j.outputLog(e,log);
        } catch (InvocationTargetException e) {
            TestSlf4j.outputLog(e,log);
        }
        return null;
    }
}
