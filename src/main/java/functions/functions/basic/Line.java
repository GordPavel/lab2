package functions.functions.basic;

import functions.functions.Function;

public class Line implements Function{

    private int k;
    private int b;

    public Line(){
        this( 1, 0 );
    }

    public Line( int k, int b ){
        this.k = k;
        this.b = b;
    }

    public double getLeftDomainBorder(){
        return - Double.MAX_VALUE;
    }
    public double getRightDomainBorder(){
        return Double.MAX_VALUE;
    }

    public double getFunctionValue( double x ){
        return k * x + b;
    }
}
