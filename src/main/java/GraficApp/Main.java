package GraficApp;

import functions.*;
import functions.functions.basic.Cos;
import functions.functions.basic.Line;
import functions.functions.basic.Log;
import functions.functions.basic.Sin;
import functions.functions.meta.Power;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import threads.*;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main extends Application{

    static final int OK     = 322;
    static final int CANCEL = 1337;

    @Override
    public void start( Stage primaryStage ) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation( getClass().getResource( "/functionWindow.fxml" ) );
        Parent                   root       = loader.load();
        final FunctionController controller = loader.getController();
        primaryStage.setTitle( "Untitled" );
        primaryStage.setScene( new Scene( root, 640, 480 ) );
        primaryStage.setResizable( true );
        primaryStage.setOnCloseRequest( controller::onExitCLick );
        FunctionController.thisStage = primaryStage;
        primaryStage.show();
    }

    public static void main( String[] args ) throws InterruptedException{
        testExchanger();
    }

    public static void testExchanger(){
        Exchanger<Task> exchanger = new Exchanger<>();

        Thread generator = new Thread( new ExchangerGenerator( exchanger, 100 ) );
        generator.setName( "Generator" );
        Thread integrator = new Thread( new ExchangerIntegrator( exchanger, 100 ) );
        integrator.setName( "Integrator" );

        generator.start();
        integrator.start();
    }

    public static void testConcurrentAPIIntegration() throws InterruptedException{
        ReadWriteLock lock = new ReentrantReadWriteLock();
        Task          task = new Task();
        task.setTasks( 100 );

        LockGenerator generator = new LockGenerator( task, lock );
        generator.setName( "Generator" );
        LockIntegrator integrator = new LockIntegrator( task, lock );
        integrator.setName( "Integrator" );

        generator.start();
        integrator.start();
    }

    public static void testFactory(){
        System.out.println( TabulatedFunctions.tabulate( new Cos(), 0, Math.PI, 11 ).getClass() );

        TabulatedFunctions.setTabulatedFunctionFactory(
                new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory() );
        System.out.println( TabulatedFunctions.tabulate( new Cos(), 0, Math.PI, 11 ).getClass() );

        TabulatedFunctions.setTabulatedFunctionFactory( new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory() );
        System.out.println( TabulatedFunctions.tabulate( new Cos(), 0, Math.PI, 11 ).getClass() );
    }

    public static void testReflexiveFactory(){
        TabulatedFunction f;


        f = TabulatedFunctions.createTabulatedFunction( ArrayTabulatedFunction.class, 0, 10, 3 );
        System.out.println( f.getClass() );
        System.out.println( f );
        System.out.println();

        f = TabulatedFunctions.createTabulatedFunction( ArrayTabulatedFunction.class, 0, 10, new double[]{ 0 , 10 } );
        System.out.println( f.getClass() );
        System.out.println( f );
        System.out.println();

        f = TabulatedFunctions.createTabulatedFunction( LinkedListTabulatedFunction.class,
                                                        new FunctionPoint[]{ new FunctionPoint( 0, 0 ) ,
                                                                             new FunctionPoint( 10, 10 ) } );
        System.out.println( f.getClass() );
        System.out.println( f );
        System.out.println();

        f = TabulatedFunctions.tabulate( LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 11 );
        System.out.println( f.getClass() );
        System.out.println( f );
    }

    public static void testForEach(){
        for( FunctionPoint point : TabulatedFunctions.tabulate( new Power( new Line(), 2 ), 0, 10, 11 ) )
            System.out.println( point );
    }

    public static void complicatedThreads() throws InterruptedException{
        Task task = new Task();
        task.setTasks( 100 );
        Semaphore semaphore = new Semaphore();

        Generator first = new Generator( task, semaphore );
        first.setName( "Generator" );
        Integrator second = new Integrator( task, semaphore );
        second.setName( "Integrator" );

        first.start();
        second.start();
        Thread.sleep( 50 );
        System.out.println( "Interrupt" );
        first.interrupt();
        second.interrupt();
    }

    private static void simpleThreads(){
        Task task = new Task();
        task.setTasks( 100 );

        Thread generator = new Thread( new SimpleGenerator( task ) );
        generator.setPriority( Thread.MIN_PRIORITY );
        generator.start();

        Thread integrator = new Thread( new SimpleIntegrator( task ) );
        integrator.setPriority( Thread.MAX_PRIORITY );
        integrator.start();
    }

    public static void nonThread(){
        Task task = new Task();
        task.setTasks( 100 );
        Random random = new Random( new Date().getTime() );
        for( int i = 0 ; i < task.getTasks() ; i++ ){
            Double osn = random.nextDouble() * 10 + 1;
            task.setFunction( new Log( osn ) );
            task.setLeftBorder( random.nextDouble() * 100 + Double.MIN_VALUE );
            task.setRightBorder( random.nextDouble() * 100 + 100 );
            task.setDiscretization( random.nextDouble() + Double.MIN_VALUE );
            System.out.println(
                    "Source log:" + osn + " leftX:" + task.getLeftBorder() + " rightX:" + task.getRightBorder() + " discretization:" + task.getDiscretization() );
            System.out.println(
                    "Result log:" + osn + " leftX:" + task.getLeftBorder() + " rightX:" + task.getRightBorder() + " discretization:" + task.getDiscretization() + " " + Functions.getIntegral(
                            task.getFunction(), task.getLeftBorder(), task.getRightBorder(),
                            task.getDiscretization() ) );
            System.out.println();
        }
    }
}
