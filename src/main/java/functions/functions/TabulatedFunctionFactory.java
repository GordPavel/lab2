package functions.functions;

import functions.FunctionPoint;
import functions.TabulatedFunction;

public interface TabulatedFunctionFactory{
    TabulatedFunction createTabulatedFunction( double leftX, double rightX, int pointsCount );

    TabulatedFunction createTabulatedFunction( double leftX, double rightX, double[] values );

    TabulatedFunction createTabulatedFunction( FunctionPoint[] points );
}
