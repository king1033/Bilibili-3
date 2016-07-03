package org.pqh.test;

import org.pqh.service.InsertService;

/**
 * Created by 10295 on 2016/6/12.
 */
public class Task_1 implements Runnable{
    private int cid;
    private InsertService insertService;

    public Task_1(int cid, InsertService insertService) {
        this.cid = cid;
        this.insertService = insertService;
    }

    @Override
    public void run() {
        insertService.insertVstorage(cid);
    }
}
