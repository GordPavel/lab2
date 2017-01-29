import functions.*;
import functions.exceptions.FunctionPointIndexOutOfBoundsException;
import functions.exceptions.InappropriateFunctionPointException;
import functions.functions.basic.Line;
import functions.functions.meta.Power;
import junit.framework.TestCase;

public class ArrayTabulatedFunctionTest extends TestCase implements Test{

    public void runTest(){
        this.testAddAndDelete();
        this.testGetValue();
        this.testForEach();
    }

    public void testAddAndDelete(){
        TabulatedFunction function = TabulatedFunctions.tabulate( new Line(), 0, 5, 6 );
        assertEquals( function, new ArrayTabulatedFunction( 0, 5, new double[]{ 0 , 1 , 2 , 3 , 4 , 5 } ) );
        try{
            function.addPoint( new FunctionPoint( 6, 6 ) );
            assertEquals( function, new ArrayTabulatedFunction( 0, 6, new double[]{ 0 , 1 , 2 , 3 , 4 , 5 , 6 } ) );
        }catch( InappropriateFunctionPointException e ){
            assertTrue( "Добавление точки [ 6 ; 6 ]" , false );
        }
        try{
            function.addPoint( new FunctionPoint( 6, 6 ) );
            assertTrue( false );
        }catch( InappropriateFunctionPointException e ){
            assertEquals( function, new ArrayTabulatedFunction( 0, 6, new double[]{ 0 , 1 , 2 , 3 , 4 , 5 , 6 } ) );
        }
        function.deletePoint( 6 );
        assertEquals( function, new ArrayTabulatedFunction( 0, 5, new double[]{ 0 , 1 , 2 , 3 , 4 , 5 } ) );
        try{
            function.deletePoint( 6 );
            assertTrue( false );
        }catch( FunctionPointIndexOutOfBoundsException e ){
            assertEquals( function, new ArrayTabulatedFunction( 0, 5, new double[]{ 0 , 1 , 2 , 3 , 4 , 5 } ) );
        }
        function.deletePoint( 5 );
        function.deletePoint( 4 );
        function.deletePoint( 3 );
        function.deletePoint( 2 );
        try{
            function.deletePoint( 1 );
            assertTrue( false );
        }catch( IllegalStateException e ){
            assertEquals( function, new ArrayTabulatedFunction( 0, 1, new double[]{ 0 , 1 } ) );
        }
        try{
            function.addPointToTail( new FunctionPoint( new FunctionPoint( 2, 2 ) ) );
            assertEquals( function, new ArrayTabulatedFunction( 0, 2, new double[]{ 0 , 1 , 2 } ) );
        }catch( InappropriateFunctionPointException e ){
            assertTrue( false );
        }
        try{
            function.addPointToTail( new FunctionPoint( new FunctionPoint( 2, 2 ) ) );
            assertTrue( false );
        }catch( InappropriateFunctionPointException e ){
            assertEquals( function, new ArrayTabulatedFunction( 0, 2, new double[]{ 0 , 1 , 2 } ) );
        }
        try{
            function.addPointToTail( new FunctionPoint( new FunctionPoint( 1.5, 1.5 ) ) );
            assertTrue( false );
        }catch( IllegalArgumentException e ){
            assertEquals( function, new ArrayTabulatedFunction( 0, 2, new double[]{ 0 , 1 , 2 } ) );
        }catch( InappropriateFunctionPointException e ){
            assertTrue( false );
        }
        try{
            function.addPoint( new FunctionPoint( 1.5, 1.5 ) );
            assertEquals( function, new ArrayTabulatedFunction(
                    new FunctionPoint[]{ new FunctionPoint( 0, 0 ) , new FunctionPoint( 1, 1 ) ,
                                         new FunctionPoint( 1.5, 1.5 ) , new FunctionPoint( 2, 2 ) } ) );
        }catch( InappropriateFunctionPointException e ){
            assertTrue( false );
        }
        try{
            function.addPoint( new FunctionPoint( 1.5, 1.5 ) );
            assertTrue( false );
        }catch( InappropriateFunctionPointException e ){
            assertEquals( function, new ArrayTabulatedFunction(
                    new FunctionPoint[]{ new FunctionPoint( 0, 0 ) , new FunctionPoint( 1, 1 ) ,
                                         new FunctionPoint( 1.5, 1.5 ) , new FunctionPoint( 2, 2 ) } ) );
        }
    }

    public void testGetValue(){
        TabulatedFunction function = TabulatedFunctions.tabulate( new Line(), 0, 5, 6 );
        assertTrue( AbstractTabulatedFunction.isEqual( function.getFunctionValue( 0 ), 0 ) );
        assertTrue( AbstractTabulatedFunction.isEqual( function.getFunctionValue( 0.5 ), 0.5 ) );
        assertFalse(
                AbstractTabulatedFunction.isEqual( function.getFunctionValue( 66 ), function.getFunctionValue( 66 ) ) );
        assertFalse( AbstractTabulatedFunction.isEqual( function.getFunctionValue( 5 ), 6 ) );
    }

    public void testForEach(){
        TabulatedFunction function = TabulatedFunctions.tabulate( new Power( new Line(), 2 ), 0, 10, 11 );
        String            result   = "{";
        for( FunctionPoint point : function )
            result += point.toString() + ",";
        assertEquals( result.substring( 0, result.length() - 1 ) + "}", function.toString() );
    }
}
