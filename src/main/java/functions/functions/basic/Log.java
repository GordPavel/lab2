package functions.functions.basic;

import functions.functions.Function;

public class Log implements Function{

    private double osn;

    public Log(){
        osn = E;
    }

    public Log( double osn ){
        this.osn = osn;
    }

    public double getLeftDomainBorder(){
        return Double.MIN_VALUE;
    }

    public double getRightDomainBorder(){
        return Double.MAX_VALUE;
    }

    public double getFunctionValue( double x ){
        return Math.log( x ) / Math.log( osn );
    }
}
