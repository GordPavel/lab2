package threads;

import functions.functions.Function;
import functions.functions.basic.Log;

public class Generator extends Thread{
    private Task      task;
    private Semaphore semaphore;

    public Generator( Task task, Semaphore semaphore ){
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run(){
        for( int i = 0, n = task.getTasks() ; i < n ; i++ ){
            try{
                semaphore.beginWrite();
                Function log         = new Log( Math.random() * 10 + 1 );
                double   randomLeft  = Math.random() * 100 + Double.MIN_VALUE;
                double   randomRight = 100.0 + Math.random() * 100;
                double   randomDelta = Math.random() + Double.MIN_VALUE;

                task.setFunction( log );
                task.setLeftBorder( randomLeft );
                task.setRightBorder( randomRight );
                task.setDiscretization( randomDelta );

                semaphore.endWrite();
                System.out.println( i + ") " + "Source " + randomLeft + " " + randomRight + " " + randomDelta );
            }catch( InterruptedException e ){
                Thread.currentThread().interrupt();
            }

        }
    }

}
