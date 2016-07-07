package org.pqh.entity;

import java.util.Comparator;

/**
 * Created by 10295 on 2016/7/4.
 */
public class ComparatorAvPlay implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        AvPlay avPlay= (AvPlay) o1;
        AvPlay avPlay1= (AvPlay) o2;
        return avPlay.getPlay()-avPlay1.getPlay();
    }
}
