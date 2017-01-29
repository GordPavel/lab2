package threads;

import functions.Functions;
import functions.functions.Function;

public class Integrator extends Thread{
    private Task      task;
    private Semaphore semaphore;

    public Integrator( Task task, Semaphore semaphore ){
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run(){
        for( int i = 0, n = task.getTasks() ; i < n ; i++ ){
            try{
                semaphore.beginRead();
                Function function = task.getFunction();
                double   left     = task.getLeftBorder();
                double   right    = task.getRightBorder();
                double   delta    = task.getDiscretization();
                double   x        = Functions.getIntegral( function, left, right, delta );
                semaphore.endRead();
                System.out.println( i + ") " + "Result " + left + " " + right + " " + delta + " " + x );
            }catch( InterruptedException e ){
                Thread.currentThread().interrupt();
            }

        }
    }
}

