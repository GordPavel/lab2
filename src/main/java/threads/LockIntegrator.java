package threads;

import functions.Functions;
import functions.functions.Function;

import java.util.concurrent.locks.ReadWriteLock;

public class LockIntegrator extends Thread{

    private Task          task;
    private ReadWriteLock lock;

    public LockIntegrator( Task task, ReadWriteLock lock ){
        this.task = task;
        this.lock = lock;
    }

    @Override
    public void run(){
        for( int i = 0, n = task.getTasks() ; i < n ; i++ ){
            Function function;
            double   left, right, delta , x;
            try{
                lock.readLock().lock();
                function = task.getFunction();
                left = task.getLeftBorder();
                right = task.getRightBorder();
                delta = task.getDiscretization();
                x = Functions.getIntegral( function, left, right, delta );
            }finally{
                lock.readLock().unlock();
            }
            System.out.println( i + ") " + "Result " + left + " " + right + " " + delta + " " + x );
        }
    }
}
