package functions.functions.meta;

import functions.functions.Function;

public class Power implements Function{
    private Function function;
    private double power;

    public Power( Function function, double power ){
        this.function = function;
        this.power = power;
    }

    public double getLeftDomainBorder(){
        return function.getLeftDomainBorder();
    }

    public double getRightDomainBorder(){
        return function.getRightDomainBorder();
    }

    public double getFunctionValue( double x ){
        return Math.pow( function.getFunctionValue( x ) , power );
    }
}
