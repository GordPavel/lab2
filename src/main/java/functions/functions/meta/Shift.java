package functions.functions.meta;

import functions.functions.Function;

public class Shift implements Function{
    private Function function;
    private double   shiftX;
    private double   shiftY;

    public Shift( Function function, double shiftX, double shiftY ){
        this.function = function;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }

    public double getLeftDomainBorder(){
        return function.getLeftDomainBorder() - shiftX;
    }

    public double getRightDomainBorder(){
        return function.getRightDomainBorder() - shiftX;
    }

    public double getFunctionValue( double x ){
        return function.getFunctionValue( x - this.shiftX ) + shiftY;
    }
}
