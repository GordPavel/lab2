package functions;

import functions.functions.Function;
import functions.functions.meta.*;

public class Functions{

    private Functions(){
    }

    public static Double getIntegral( Function f, double leftBorder, double rightBorder, double discretization ){
        if( discretization <= 0 )
            throw new IllegalArgumentException( "Discretization error" );
        FunctionPoint[] points        = TabulatedFunctions.tabulate( f, leftBorder, rightBorder,
                                                                     discretization ).getFunction();
        double          integralValue = 0;
        for( int i = 1 ; i < points.length ; i++ )
            integralValue += ( points[ i ].getX() - points[ i - 1 ].getX() ) * ( points[ i - 1 ].getY() + points[ i ].getY() ) / 2;
        return integralValue;
    }

    public static Function shift( Function f, double shiftX, double shiftY ){
        return new Shift( f, shiftX, shiftY );
    }

    public static Function scale( Function f, double scaleX, double scaleY ){
        return new Scale( f, scaleX, scaleY );
    }

    public static Function power( Function f, double power ){
        return new Power( f, power );
    }

    public static Function sum( Function f1, Function f2 ){
        return new Sum( f1, f2 );
    }

    public static Function mult( Function f1, Function f2 ){
        return new Mult( f1, f2 );
    }

    public static Function composition( Function outer, Function inner ){
        return new Composition( outer, inner );
    }

}
