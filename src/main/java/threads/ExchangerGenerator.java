package threads;

import functions.functions.Function;
import functions.functions.basic.Log;

import java.util.concurrent.Exchanger;

public class ExchangerGenerator implements Runnable{

    private Exchanger<Task> taskExchanger;
    private Integer         countTasks;

    public ExchangerGenerator( Exchanger<Task> taskExchanger, Integer countTasks ){
        this.taskExchanger = taskExchanger;
        this.countTasks = countTasks;
    }

    @Override
    public void run(){
        Function log;
        Double   randomLeft, randomRight, randomDelta;
        for( int i = 0, n = countTasks ; i < n ; i++ ){
            log = new Log( Math.random() * 10 + 1 );
            randomLeft = Math.random() * 100 + Double.MIN_VALUE;
            randomRight = 100.0 + Math.random() * 100;
            randomDelta = Math.random() + Double.MIN_VALUE;
            try{
                taskExchanger.exchange( new Task( log, randomLeft, randomRight, randomDelta ) );
            }catch( InterruptedException e ){
                e.printStackTrace();
            }
            System.out.println( i + ") " + "Source " + randomLeft + " " + randomRight + " " + randomDelta );
        }
    }
}
