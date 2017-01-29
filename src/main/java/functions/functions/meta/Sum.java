package functions.functions.meta;

import functions.functions.Function;

public class Sum implements Function{
    private Function first;
    private Function second;

    public Sum( Function first, Function second ){
        this.first = first;
        this.second = second;
    }

    public double getLeftDomainBorder(){
        return Math.max( first.getLeftDomainBorder(), second.getLeftDomainBorder() );
    }

    public double getRightDomainBorder(){
        return Math.min( first.getRightDomainBorder(), second.getRightDomainBorder() );
    }

    public double getFunctionValue( double x ){
        return first.getFunctionValue( x ) + second.getFunctionValue( x );
    }
}
