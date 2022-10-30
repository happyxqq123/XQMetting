package com.xqmetting.server.lock;

public abstract class AbstractLock  implements Lock{

    @Override
    public void lock() {
        //尝试获取锁
        if(tryLock()){
            System.out.println("------获取锁-------");
        }else{
            //等待锁 阻塞
            waitLock();
            //重试
            lock();
        }
    }

    protected abstract boolean tryLock();

    protected abstract void waitLock();

}
