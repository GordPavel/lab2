package functions.functions.basic;

import functions.functions.Function;

public abstract class TrigonometricFunction implements Function{

    public final static double PI = Math.PI;

    public double getLeftDomainBorder(){
        return -Double.MAX_VALUE;
    }

    public double getRightDomainBorder(){
        return Double.MAX_VALUE;
    }
}
