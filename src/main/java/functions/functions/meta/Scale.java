package functions.functions.meta;

import functions.AbstractTabulatedFunction;
import functions.functions.Function;

public class Scale implements Function{
    private Function function;
    private double   scaleX;
    private double   scaleY;

    public Scale( Function function, double scaleX, double scaleY ){
        this.function = function;
        if( AbstractTabulatedFunction.isEqual( scaleX, 0 ) || AbstractTabulatedFunction.isEqual( scaleY, 0 ) )
            throw new IllegalArgumentException( "Коэффициент растяжения = 0" );
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public double getLeftDomainBorder(){
        return function.getLeftDomainBorder() * this.scaleX;
    }

    public double getRightDomainBorder(){
        return function.getRightDomainBorder() * this.scaleX;
    }

    public double getFunctionValue( double x ){
        return function.getFunctionValue( x * this.scaleX ) * this.scaleY;
    }
}
