package org.pqh.test;

import org.pqh.util.FindResourcesUtil;

/**
 * Created by 10295 on 2016/7/16.
 */
public class TaskBtacg implements Runnable{
    private int page;
    private String keyword;
    public static int pages;

    public TaskBtacg(int page, String keyword) {
        this.page = page;
        this.keyword = keyword;
    }

    public boolean getFlag(){
        return pages!=0&&page>pages;
    }
    @Override
    public void run() {
        FindResourcesUtil.eachPage(keyword,page);
    }
}
