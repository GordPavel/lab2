import functions.*;
import functions.exceptions.FunctionPointIndexOutOfBoundsException;
import functions.exceptions.InappropriateFunctionPointException;
import functions.functions.basic.Line;
import junit.framework.TestCase;

public class LinkedListTabulatedFunctionTest extends TestCase implements Test{

    public void runTest(){
        this.testAddAndDelete();
        this.testGetValue();
    }

    public void testAddAndDelete(){
        TabulatedFunctions.setTabulatedFunctionFactory( new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory() );
        TabulatedFunction function = TabulatedFunctions.tabulate( new Line(), 0, 5, 6 );
        assertEquals( function, new LinkedListTabulatedFunction( 0, 5, new double[]{ 0 , 1 , 2 , 3 , 4 , 5 } ) );
        try{
            function.addPoint( new FunctionPoint( 6, 6 ) );
            assertEquals( function,
                          new LinkedListTabulatedFunction( 0, 6, new double[]{ 0 , 1 , 2 , 3 , 4 , 5 , 6 } ) );
        }catch( InappropriateFunctionPointException e ){
            assertTrue( false );
        }
        try{
            function.addPoint( new FunctionPoint( 6, 6 ) );
            assertTrue( false );
        }catch( InappropriateFunctionPointException e ){
            assertEquals( function,
                          new LinkedListTabulatedFunction( 0, 6, new double[]{ 0 , 1 , 2 , 3 , 4 , 5 , 6 } ) );
        }
        function.deletePoint( 6 );
        assertEquals( function, new LinkedListTabulatedFunction( 0, 5, new double[]{ 0 , 1 , 2 , 3 , 4 , 5 } ) );
        try{
            function.deletePoint( 6 );
            assertTrue( false );
        }catch( FunctionPointIndexOutOfBoundsException e ){
            assertEquals( function, new LinkedListTabulatedFunction( 0, 5, new double[]{ 0 , 1 , 2 , 3 , 4 , 5 } ) );
        }
        function.deletePoint( 5 );
        function.deletePoint( 4 );
        function.deletePoint( 3 );
        function.deletePoint( 2 );
        try{
            function.deletePoint( 1 );
            assertTrue( false );
        }catch( IllegalStateException e ){
            assertEquals( function, new LinkedListTabulatedFunction( 0, 1, new double[]{ 0 , 1 } ) );
        }
        try{
            function.addPoint( new FunctionPoint( new FunctionPoint( 2, 2 ) ) );
            assertEquals( function, new LinkedListTabulatedFunction( 0, 2, new double[]{ 0 , 1 , 2 } ) );
        }catch( InappropriateFunctionPointException e ){
            assertTrue( false );
        }
        try{
            function.addPoint( new FunctionPoint( new FunctionPoint( 2, 2 ) ) );
            assertTrue( false );
        }catch( InappropriateFunctionPointException e ){
            assertEquals( function, new LinkedListTabulatedFunction( 0, 2, new double[]{ 0 , 1 , 2 } ) );
        }
        try{
            function.addPoint( new FunctionPoint( 1.5, 1.5 ) );
            assertEquals( function, new LinkedListTabulatedFunction(
                    new FunctionPoint[]{ new FunctionPoint( 0, 0 ) , new FunctionPoint( 1, 1 ) ,
                                         new FunctionPoint( 1.5, 1.5 ) , new FunctionPoint( 2, 2 ) } ) );
        }catch( InappropriateFunctionPointException e ){
            assertTrue( false );
        }
        try{
            function.addPoint( new FunctionPoint( 1.5, 1.5 ) );
            assertTrue( false );
        }catch( InappropriateFunctionPointException e ){
            assertEquals( function, new LinkedListTabulatedFunction(
                    new FunctionPoint[]{ new FunctionPoint( 0, 0 ) , new FunctionPoint( 1, 1 ) ,
                                         new FunctionPoint( 1.5, 1.5 ) , new FunctionPoint( 2, 2 ) } ) );
        }
    }

    public void testGetValue(){
        TabulatedFunctions.setTabulatedFunctionFactory( new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory() );
        TabulatedFunction function = TabulatedFunctions.tabulate( new Line(), 0, 5, 6 );
        assertTrue( AbstractTabulatedFunction.isEqual( function.getFunctionValue( 0 ), 0 ) );
        assertTrue( AbstractTabulatedFunction.isEqual( function.getFunctionValue( 0.5 ), 0.5 ) );
        assertFalse(
                AbstractTabulatedFunction.isEqual( function.getFunctionValue( 66 ), function.getFunctionValue( 66 ) ) );
        assertFalse( AbstractTabulatedFunction.isEqual( function.getFunctionValue( 5 ), 6 ) );
    }
}

