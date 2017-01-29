package threads;

import functions.functions.Function;
import functions.functions.basic.Log;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;

public class LockGenerator extends Thread{

    private Task          task;
    private ReadWriteLock lock;

    public LockGenerator( Task task, ReadWriteLock lock ){
        this.task = task;
        this.lock = lock;
    }

    @Override
    public void run(){
        for( int i = 0, n = task.getTasks() ; i < n ; i++ ){
            Function log;
            double   randomLeft, randomRight, randomDelta;
            try{
                lock.writeLock().lock();
                log = new Log( Math.random() * 10 + 1 );
                randomLeft = Math.random() * 100 + Double.MIN_VALUE;
                randomRight = 100.0 + Math.random() * 100;
                randomDelta = Math.random() + Double.MIN_VALUE;
                task.setFunction( log );
                task.setLeftBorder( randomLeft );
                task.setRightBorder( randomRight );
                task.setDiscretization( randomDelta );
                TimeUnit.NANOSECONDS.sleep( 300 );
            }catch( InterruptedException e ){
                return;
            }finally{
                lock.writeLock().unlock();
            }
            System.out.println( i + ") " + "Source " + randomLeft + " " + randomRight + " " + randomDelta );
        }
    }
}
