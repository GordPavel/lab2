package threads;

import functions.Functions;
import functions.functions.Function;

import java.util.concurrent.Exchanger;

public class ExchangerIntegrator implements Runnable{

    private Exchanger<Task> taskExchanger;
    private Integer         countTasks;

    public ExchangerIntegrator( Exchanger<Task> taskExchanger, Integer countTasks ){
        this.taskExchanger = taskExchanger;
        this.countTasks = countTasks;
    }

    @Override
    public void run(){
        Task task = null;
        for( int i = 0, n = countTasks ; i < n ; i++ ){
            try{
                task = taskExchanger.exchange( null );
            }catch( InterruptedException e ){
                e.printStackTrace();
            }
            Function function;
            double   left, right, delta;
            function = task.getFunction();
            left = task.getLeftBorder();
            right = task.getRightBorder();
            delta = task.getDiscretization();
            Double x = Functions.getIntegral( function, left, right, delta );
            System.out.println( i + ") " + "Result " + left + " " + right + " " + delta + " " + x );
        }
    }
}

