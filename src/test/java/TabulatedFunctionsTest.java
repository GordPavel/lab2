import functions.ArrayTabulatedFunction;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import functions.TabulatedFunctions;
import functions.functions.basic.Cos;
import functions.functions.basic.Line;
import functions.functions.basic.Sin;
import functions.functions.meta.Power;
import functions.functions.meta.Sum;
import junit.framework.TestCase;

import java.io.*;

public class TabulatedFunctionsTest extends TestCase implements Test{

    public void runTest(){
        try{
            testArrayByteStream();
            testArraySymbolStream();
            testListByteStream();
            testListSymbolStream();
            testFactory();
        }catch( Exception e ){
            e.printStackTrace();
        }
    }

    public void testFactory(){
        assertEquals( "class functions.ArrayTabulatedFunction " + ( new ArrayTabulatedFunction( 0, 10,
                                                                                                new double[]{ 0 , 1 ,
                                                                                                              2 , 3 ,
                                                                                                              4 , 5 ,
                                                                                                              6 , 7 ,
                                                                                                              8 , 9 ,
                                                                                                              10 } ).toString() ),
                      TabulatedFunctions.tabulate( new Line(), 0, 10,
                                                   11 ).getClass() + " " + TabulatedFunctions.tabulate( new Line(), 0,
                                                                                                        10,
                                                                                                        11 ).toString() );
        TabulatedFunctions.setTabulatedFunctionFactory(
                new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory() );
        assertEquals( "class functions.LinkedListTabulatedFunction " + ( new ArrayTabulatedFunction( 0, 10,
                                                                                                     new double[]{ 0 ,
                                                                                                                   1 ,
                                                                                                                   2 ,
                                                                                                                   3 ,
                                                                                                                   4 ,
                                                                                                                   5 ,
                                                                                                                   6 ,
                                                                                                                   7 ,
                                                                                                                   8 ,
                                                                                                                   9 ,
                                                                                                                   10 } ).toString() ),
                      TabulatedFunctions.tabulate( new Line(), 0, 10,
                                                   11 ).getClass() + " " + TabulatedFunctions.tabulate( new Line(), 0,
                                                                                                        10,
                                                                                                        11 ).toString() );
    }

    public void testArrayByteStream() throws IOException{
        TabulatedFunction function = TabulatedFunctions.tabulate(
                new Sum( new Power( new Sin(), 2 ), new Power( new Cos(), 2 ) ), 0, 10, 11 );
        PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream  inputStream  = new PipedInputStream( outputStream );

        TabulatedFunctions.outputTabulatedFunction( function, outputStream );
        outputStream.close();

        TabulatedFunction newFunction = TabulatedFunctions.inputTabulatedFunction( inputStream );
        inputStream.close();
        assertTrue( function.equals( newFunction ) );
    }

    public void testListByteStream() throws IOException{
        TabulatedFunction function = TabulatedFunctions.tabulate(
                new Sum( new Power( new Sin(), 2 ), new Power( new Cos(), 2 ) ), 0, 10, 11 );
        PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream  inputStream  = new PipedInputStream( outputStream );

        TabulatedFunctions.outputTabulatedFunction( function, outputStream );
        outputStream.close();

        TabulatedFunction newFunction = TabulatedFunctions.inputTabulatedFunction( LinkedListTabulatedFunction.class,
                                                                                   inputStream );
        inputStream.close();

        assertTrue( function.equals( newFunction ) );
    }

    public void testArraySymbolStream() throws IOException{
        TabulatedFunction function     = TabulatedFunctions.tabulate( new Power( new Sin(), 2 ), 0, 10, 11 );
        PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream  inputStream  = new PipedInputStream( outputStream );

        Writer writer = new OutputStreamWriter( outputStream );
        TabulatedFunctions.writeTabulatedFunction( function, writer );
        writer.close();

        Reader            reader      = new InputStreamReader( inputStream );
        TabulatedFunction newFunction = TabulatedFunctions.readTabulatedFunction( reader );
        reader.close();

        assertEquals( function, newFunction );
    }

    public void testListSymbolStream() throws IOException{
        TabulatedFunction function     = TabulatedFunctions.tabulate( new Power( new Sin(), 2 ), 0, 10, 11 );
        PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream  inputStream  = new PipedInputStream( outputStream );

        Writer writer = new OutputStreamWriter( outputStream );
        TabulatedFunctions.writeTabulatedFunction( function, writer );
        writer.close();

        Reader            reader      = new InputStreamReader( inputStream );
        TabulatedFunction newFunction = TabulatedFunctions.readTabulatedFunction( LinkedListTabulatedFunction.class,
                                                                                  reader );
        reader.close();

        assertTrue( function.equals( newFunction ) );
    }
}
