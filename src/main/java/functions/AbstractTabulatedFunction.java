package functions;

import functions.exceptions.FunctionPointIndexOutOfBoundsException;
import functions.exceptions.InappropriateFunctionPointException;

import java.io.Serializable;
import java.util.Arrays;

public abstract class AbstractTabulatedFunction implements TabulatedFunction, Serializable{

    AbstractTabulatedFunction( double leftX, double rightX, int pointsCount ){
        if( leftX >= rightX )
            throw new IllegalArgumentException( "Левая граница не может быть больше или равной правой" );
        if( pointsCount < 2 )
            throw new IllegalArgumentException( "Должно быть как минимум 2 точки" );
    }

    public double getPointX( int index ){
        return getPoint( index ).getX();
    }

    public void setPointX( int index, double x ) throws InappropriateFunctionPointException{
        try{
            FunctionPoint newPoint = ( FunctionPoint ) this.getPoint( index ).clone();
            newPoint.setX( x );
            this.setPoint( index, newPoint );
        }catch( CloneNotSupportedException e ){
            e.getMessage();
        }
    }

    public double getPointY( int index ){
        return getPoint( index ).getY();
    }

    public void setPointY( int index, double y ){
        FunctionPoint newPoint = this.getPoint( index );
        newPoint.setY( y );
        try{
            this.setPoint( index, newPoint );
        }catch( InappropriateFunctionPointException e ){
//            Никогда не должно сработать
            System.out.println( e.getMessage() );
        }
    }

    public void setPoint( int index, FunctionPoint point ) throws InappropriateFunctionPointException{
        if( !( index >= 0 && index < this.getPointsCount() ) )
            throw new FunctionPointIndexOutOfBoundsException( "Index error." );
        if( point.equals( this.getPoint( index ) ) )
            return;
        if( this.hasPointX( point.getX() ) )
            throw new InappropriateFunctionPointException( "Edit point error." );
    }

    public byte[] getBytes(){
        byte[]          bytes  = new byte[ 16 * this.getPointsCount() ];
        FunctionPoint[] points = this.getFunction();
        for( int i = 0, j = this.getPointsCount() ; i < j ; i++ ){
            System.arraycopy( points[ i ].getBytes(), 0, bytes, i * 16, 16 );
        }
        return bytes;
    }

    boolean isSort( FunctionPoint[] points ){
        for( int i = 1, j = points.length ; i < j ; i++ ){
            if( points[ i - 1 ].getX() > points[ i ].getX() || isEqual( points[ i - 1 ].getX(), points[ i ].getX() ) ){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString(){
        String result = "{";
        for( FunctionPoint point : this.getFunction() )
            result += point.toString() + ",";
        result = result.substring( 0, result.length() - 1 );
        result += "}";
        return result;
    }

    @Override
    public boolean equals( Object obj ){
        return obj instanceof TabulatedFunction && isEqual( this.getFunction(),
                                                            ( ( TabulatedFunction ) obj ).getFunction() );
    }

    @Override
    public int hashCode(){
        return Arrays.hashCode( this.getFunction() );
    }

    public Object clone() throws CloneNotSupportedException{
        if( this instanceof ArrayTabulatedFunction ){
            return new ArrayTabulatedFunction( this.getFunction() );
        }else{
            return new LinkedListTabulatedFunction( this.getFunction() );
        }
    }

    private static boolean isEqual( FunctionPoint[] a, FunctionPoint[] b ){
        if( a.length != b.length )
            return false;
        for( int i = 0, j = a.length ; i < j ; i++ ){
            if( !a[ i ].equals( b[ i ] ) )
                return false;
        }
        return true;
    }

    public static boolean isEqual( double a, double b ){
        return Math.abs( a - b ) <= EPSILON;
    }
}
