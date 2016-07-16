package org.pqh.entity;

import org.apache.log4j.Logger;
import org.pqh.util.TestSlf4j;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * Created by 10295 on 2016/7/4.
 */
public class ComparatorAvPlay implements Comparator {
    private String fieldName;
    private static Logger log= TestSlf4j.getLogger(ComparatorAvPlay.class);
    public ComparatorAvPlay(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public int compare(Object o1, Object o2) {
        Field field=null;
        try {
            field=o1.getClass().getDeclaredField(this.fieldName);
            field.setAccessible(true);
            o1=field.get(o1);
            o2=field.get(o2);
            Double d=Double.parseDouble(o1.toString())-Double.parseDouble(o2.toString());
            return d>0?1:-1;
        } catch (NoSuchFieldException e) {
            TestSlf4j.outputLog(e,log);
        } catch (IllegalAccessException e) {
            TestSlf4j.outputLog(e,log);
        }
        return 0;
    }
}
