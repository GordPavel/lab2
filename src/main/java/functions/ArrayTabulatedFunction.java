package functions;

import functions.exceptions.FunctionPointIndexOutOfBoundsException;
import functions.exceptions.InappropriateFunctionPointException;
import functions.functions.TabulatedFunctionFactory;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction{

    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory{
        @Override
        public TabulatedFunction createTabulatedFunction( double leftX, double rightX, int pointsCount ){
            return new ArrayTabulatedFunction( leftX , rightX , pointsCount );
        }

        @Override
        public TabulatedFunction createTabulatedFunction( double leftX, double rightX, double[] values ){
            return new ArrayTabulatedFunction( leftX , rightX , values );
        }

        @Override
        public TabulatedFunction createTabulatedFunction( FunctionPoint[] points ){
            return new ArrayTabulatedFunction( points );
        }
    }

    private FunctionPoint[] function;

    public ArrayTabulatedFunction( double leftX, double rightX, int pointsCount ){
        super( leftX, rightX, pointsCount );
        function = new FunctionPoint[ pointsCount ];
        double sh = ( rightX - leftX ) / ( pointsCount - 1 );
        for( int i = 0 ; i < pointsCount ; i++ )
            function[ i ] = new FunctionPoint( leftX + sh * i, 0 );
    }

    public ArrayTabulatedFunction( double leftX, double rightX, double[] values ){
        this( leftX, rightX, values.length );
        for( int i = 0, j = getPointsCount() ; i < j ; i++ )
            this.setPointY( i, values[ i ] );
    }

    public ArrayTabulatedFunction( FunctionPoint[] points ){
        super( points[ 0 ].getX(), points[ points.length - 1 ].getX(), points.length );
        function = new FunctionPoint[ points.length ];
        try{
            for( int i = 0 ; i < points.length ; i++ )
                function[ i ] = ( FunctionPoint ) points[ i ].clone();
        }catch( CloneNotSupportedException e ){
            e.printStackTrace();
        }
    }

    public double getLeftDomainBorder(){
        return function[ 0 ].getX();
    }

    public double getRightDomainBorder(){
        return function[ function.length - 1 ].getX();
    }

    public double getFunctionValue( double x ){
        if( this.getLeftDomainBorder() - x > EPSILON || x - getRightDomainBorder() > EPSILON ){
            return Double.NaN;
        }else{
            for( int i = 0, j = function.length - 1 ; i < j ; i++ ){
                if( isEqual( x, function[ i ].getX() ) )
                    return function[ i ].getY();
                else if( Math.abs(
                        x - function[ i ].getX() ) < ( function[ i + 1 ].getX() - function[ i ].getX() ) - EPSILON ){
                    return function[ i ].getY() + ( function[ i + 1 ].getY() - function[ i ].getY() ) * ( x - function[ i ].getX() ) / ( function[ i + 1 ].getX() - function[ i ].getX() );
                }
            }
        }
        if( isEqual( x, function[ function.length - 1 ].getX() ) )
            return function[ function.length - 1 ].getY();
        return Double.NaN;
    }

    public int getPointsCount(){
        return function.length;
    }

    public FunctionPoint getPoint( int index ){
        if( !( index >= 0 && index < this.getPointsCount() ) )
            throw new FunctionPointIndexOutOfBoundsException( "Index error" );
        return function[ index ];
    }

    public void setPoint( int index, FunctionPoint point ) throws InappropriateFunctionPointException{
        super.setPoint( index, point );
        try{
            if( index == 0 && function[ index + 1 ].getX() - point.getX() > EPSILON )
                function[ index ] = ( FunctionPoint ) point.clone();
            else if( index == this.getPointsCount() - 1 && point.getX() - function[ index - 1 ].getX() > EPSILON )
                function[ index ] = ( FunctionPoint ) point.clone();
            else if( isInMiddle( index, point ) )
                function[ index ] = ( FunctionPoint ) point.clone();
            else
                throw new InappropriateFunctionPointException( "Edit point error." );

        }catch( CloneNotSupportedException e ){
            System.out.println( e.getMessage() );
        }
    }

    public void deletePoint( int index ){
        if( getPointsCount() < 3 )
            throw new IllegalStateException( "Points count error." );

        if( !( index >= 0 && index < this.getPointsCount() ) )
            throw new FunctionPointIndexOutOfBoundsException( "Index error." );

        FunctionPoint[] newFunction = new FunctionPoint[ this.getPointsCount() - 1 ];
        if( index == 0 )
            System.arraycopy( function, 1, newFunction, 0, getPointsCount() - 1 );
        else if( index == this.getPointsCount() - 1 )
            System.arraycopy( function, 0, newFunction, 0, getPointsCount() - 1 );
        else
            System.arraycopy( function, 0, newFunction, 0, index );
        System.arraycopy( function, index + 1, newFunction, index, getPointsCount() - index - 1 );

        function = newFunction;
    }

    public void addPoint( FunctionPoint point ) throws InappropriateFunctionPointException{
        FunctionPoint[] newFunction = new FunctionPoint[ this.getPointsCount() + 1 ];
        if( hasPointX( point.getX() ) )
            throw new InappropriateFunctionPointException( "This point has exists." );
        try{
            if( getLeftDomainBorder() - point.getX() > EPSILON ){
                newFunction[ 0 ] = ( FunctionPoint ) point.clone();
                System.arraycopy( function, 0, newFunction, 1, getPointsCount() );
            }else if( point.getX() - getRightDomainBorder() > EPSILON ){
                newFunction[ newFunction.length - 1 ] = ( FunctionPoint ) point.clone();
                System.arraycopy( function, 0, newFunction, 0, getPointsCount() );
            }else{
                int index;
                for( index = 1; point.getX() - function[ index ].getX() > EPSILON ; index++ )
                    ;
                System.arraycopy( function, 0, newFunction, 0, index );
                newFunction[ index ] = ( FunctionPoint ) point.clone();
                System.arraycopy( function, index, newFunction, index + 1, getPointsCount() - index );
            }
        }catch( CloneNotSupportedException e ){
            e.printStackTrace();
        }
        function = newFunction;
    }

    public void addPointToTail( FunctionPoint point ) throws InappropriateFunctionPointException{
        if( this.getRightDomainBorder() - point.getX() > EPSILON )
            throw new IllegalArgumentException( "Domain border error." );
        this.addPoint( point );
    }

    private boolean isInMiddle( int index, FunctionPoint point ){
        return point.getX() - function[ index - 1 ].getX() > EPSILON && function[ index + 1 ].getX() - point.getX() > EPSILON;
    }

    public boolean hasPointX( double x ){
        for( FunctionPoint point : function )
            if( isEqual( x, point.getX() ) )
                return true;
        return false;
    }

    public FunctionPoint[] getFunction(){
        return function;
    }
}
