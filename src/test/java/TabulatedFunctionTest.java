import functions.*;
import functions.exceptions.InappropriateFunctionPointException;
import functions.functions.Function;
import functions.functions.basic.Line;
import functions.functions.basic.Sin;
import junit.framework.TestCase;

public class TabulatedFunctionTest extends TestCase implements Test{

    public void runTest(){
        testToString();
        testHash();
        testEquals();
        try{
            testClone();
        }catch( Exception e ){
            e.printStackTrace();
        }
    }

    public void testToString(){
        Function function = Functions.power( new Sin(), 2 );
        TabulatedFunctions.setTabulatedFunctionFactory( new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory() );
        assertTrue( TabulatedFunctions.tabulate( function, 0, 2 * Sin.PI, 5 )
                                      .equals( TabulatedFunctions.tabulate( function, 0, 2 * Sin.PI, 5 ) ) );
        assertTrue( TabulatedFunctions.tabulate( new Line(), 0, 2, 3 ).toString()
                                      .equals( "{(0.0;0.0),(1.0;1.0),(2.0;2.0)}" ) );
    }

    public void testHash(){
        TabulatedFunction a = TabulatedFunctions.tabulate( Functions.power( new Sin(), 2 ), 0, 2 * Sin.PI, 5 );
        TabulatedFunction b = TabulatedFunctions.tabulate( Functions.power( new Sin(), 2 ), 0, 2 * Sin.PI, 6 );
        assertTrue( a.hashCode() == a.hashCode() );
        assertFalse( a.hashCode() == b.hashCode() );
    }

    public void testEquals(){
        TabulatedFunctions.setTabulatedFunctionFactory( new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory() );
        TabulatedFunction function = TabulatedFunctions.tabulate( new Line(), 0, 5, 6 );
        assertTrue( function.equals( new LinkedListTabulatedFunction( 0, 5, new double[]{ 0 , 1 , 2 , 3 , 4 , 5 } ) ) );
        assertFalse(
                function.equals( new LinkedListTabulatedFunction( 0, 5, new double[]{ 0 , 1 , 2 , 3 , 4 , 5 , 6 } ) ) );
    }

    public void testClone() throws CloneNotSupportedException, InappropriateFunctionPointException{
        TabulatedFunctions.setTabulatedFunctionFactory( new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory() );
        TabulatedFunction function1 = TabulatedFunctions.tabulate( new Line(), 0, 5, 6 );
        TabulatedFunction function2 = ( TabulatedFunction ) function1.clone();
        assertEquals( function2, new LinkedListTabulatedFunction( 0, 5, new double[]{ 0 , 1 , 2 , 3 , 4 , 5 } ) );
        function1.addPointToTail( new FunctionPoint( 6, 6 ) );
        assertEquals( function2, new LinkedListTabulatedFunction( 0, 5, new double[]{ 0 , 1 , 2 , 3 , 4 , 5 } ) );
    }
}
