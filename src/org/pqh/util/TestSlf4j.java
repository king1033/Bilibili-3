package org.pqh.util;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by 10295 on 2016/5/29.
 */
public class TestSlf4j {

    public static Logger getLogger(Class c){
        PropertyConfigurator.configure("src\\log4j.properties");
        return Logger.getLogger(c);
    }
}
