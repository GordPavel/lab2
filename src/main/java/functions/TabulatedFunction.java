package functions;

import functions.exceptions.InappropriateFunctionPointException;
import functions.functions.Function;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public interface TabulatedFunction extends Function, Cloneable, Serializable, Iterable<FunctionPoint>{
    double EPSILON = 10e-7;

    @Override
    default Iterator<FunctionPoint> iterator(){
        return new Iterator<FunctionPoint>(){

            int index = -1;

            @Override
            public boolean hasNext(){
                return index < getPointsCount() - 1;
            }

            @Override
            public void remove(){
                throw new UnsupportedOperationException();
            }

            @Override
            public FunctionPoint next(){
                if( !hasNext() )
                    throw new NoSuchElementException();
                return getPoint( ++index );
            }
        };
    }

    int getPointsCount();

    FunctionPoint getPoint( int index );

    void setPoint( int index, FunctionPoint point ) throws InappropriateFunctionPointException;

    double getPointX( int index );

    void setPointX( int index, double x ) throws InappropriateFunctionPointException;

    double getPointY( int index );

    void setPointY( int index, double y ) throws InappropriateFunctionPointException;

    void deletePoint( int index );

    void addPoint( FunctionPoint point ) throws InappropriateFunctionPointException;

    void addPointToTail( FunctionPoint point ) throws InappropriateFunctionPointException;

    FunctionPoint[] getFunction();

    boolean hasPointX( double x );

    byte[] getBytes();

    Object clone() throws CloneNotSupportedException;

    boolean equals( Object obj );
}
