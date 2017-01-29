import functions.AbstractTabulatedFunction;
import functions.Functions;
import functions.TabulatedFunction;
import functions.functions.Function;
import functions.functions.basic.Cos;
import functions.functions.basic.Exp;
import functions.functions.basic.Log;
import functions.functions.basic.Sin;
import junit.framework.TestCase;

public class FunctionsTest extends TestCase implements Test{

    public void runTest(){
        testComposition();
        testMult();
        testIntegral();
        testPower();
        testScale();
        testShift();
        testSum();
    }

    public void testIntegral(){
        assertTrue( Math.abs(
                Functions.getIntegral( new Exp(), 0, 1, 0.0000001 ) - Math.E + 1 ) < TabulatedFunction.EPSILON );
    }

    public void testComposition(){
        Function composition = Functions.composition( new Log(), new Exp() );
        assertTrue( AbstractTabulatedFunction.isEqual( composition.getFunctionValue( 1 ), 1 ) );
        assertTrue( AbstractTabulatedFunction.isEqual( composition.getFunctionValue( 2 ), 2 ) );
        assertTrue( AbstractTabulatedFunction.isEqual( composition.getFunctionValue( 3 ), 3 ) );
    }

    public void testMult(){
        Function mult = Functions.mult( new Cos(), new Sin() );
        assertTrue( AbstractTabulatedFunction.isEqual( mult.getFunctionValue( 0 ), 0 ) );
        assertTrue( AbstractTabulatedFunction.isEqual( mult.getFunctionValue( Cos.PI ), 0 ) );
        assertTrue( AbstractTabulatedFunction.isEqual( mult.getFunctionValue( Cos.PI / 2 ), 0 ) );
    }

    public void testPower(){
        Function power = Functions.power( new Exp(), 2 );
        assertTrue( AbstractTabulatedFunction.isEqual( power.getFunctionValue( 3 ), Math.pow( Math.exp( 3 ), 2 ) ) );
    }

    public void testScale(){
        Function scale = Functions.scale( new Exp(), 1, 2 );
        assertTrue( AbstractTabulatedFunction.isEqual( scale.getFunctionValue( 0 ), 2 ) );
    }

    public void testShift(){
        Function shift = Functions.shift( new Exp(), -1, 1 );
        assertTrue( AbstractTabulatedFunction.isEqual( shift.getFunctionValue( -1 ), Math.exp( 0 ) + 1 ) );
    }

    public void testSum(){
        Function sum = Functions.sum( Functions.power( new Sin(), 2 ), Functions.power( new Cos(), 2 ) );
        assertTrue( AbstractTabulatedFunction.isEqual( sum.getFunctionValue( 1 ), 1 ) );
        assertTrue( AbstractTabulatedFunction.isEqual( sum.getFunctionValue( 2 ), 1 ) );
    }
}
