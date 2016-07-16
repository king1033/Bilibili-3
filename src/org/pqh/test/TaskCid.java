package org.pqh.test;

import org.pqh.service.InsertService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by 10295 on 2016/5/9.
 */
public class TaskCid implements Runnable {
    private int cid;
    private InsertService insertService;

    public TaskCid(int cid, InsertService insertService) {
        this.cid = cid;
        this.insertService=insertService;
    }


    @Override
    public void run() {
        insertService.insertCid(cid);
     }
    public void print(ThreadPoolTaskExecutor taskExecutor){
        System.out.println("当前活动线程"+taskExecutor.getActiveCount());
    }

}
