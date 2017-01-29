package threads;

import functions.Functions;
import functions.functions.Function;

public class SimpleIntegrator implements Runnable{

    private final Task task;

    public SimpleIntegrator( Task task ){
        this.task = task;
    }

    @Override
    public void run(){
        for( int i = 0, n = task.getTasks() ; i < n ; ){
            Double left, right, delta, x;
            while( !SimpleGenerator.flag )
                Thread.yield();
            synchronized( task ){
                Function function = task.getFunction();
                left = task.getLeftBorder();
                right = task.getRightBorder();
                delta = task.getDiscretization();
                x = Functions.getIntegral( function, left, right, delta );
            }
            System.out.println( ++i + ") " + "Result " + left + " " + right + " " + delta + " " + x );
        }
    }
}
