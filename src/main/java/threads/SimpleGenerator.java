package threads;

import functions.functions.basic.Log;

public class SimpleGenerator implements Runnable{

    private final Task task;
    static boolean flag = false;

    public SimpleGenerator( Task task ){
        this.task = task;
    }

    @Override
    public void run(){
        for( int i = 0, n = task.getTasks() ; i < n ; ){
            Double osn         = 1.0 + ( Math.random() * 10 ) % 9;
            Double randomLeft  = Math.random() * 100 + Double.MIN_VALUE;
            Double randomRight = 100.0 + Math.random() * 100;
            Double randomDelta = Math.random();
            synchronized( task ){
                task.setFunction( new Log( osn ) );
                task.setLeftBorder( randomLeft );
                task.setRightBorder( randomRight );
                task.setDiscretization( randomDelta );
                flag = true;
            }
            System.out.println( ++i + ") " + "Source " + randomLeft + " " + randomRight + " " + randomDelta );
        }
    }
}
