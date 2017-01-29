package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable, Cloneable{
    private double x;
    private double y;

    public FunctionPoint(){
        this.x = 0;
        this.y = 0;
    }

    public FunctionPoint( FunctionPoint point ){
        this.x = point.getX();
        this.y = point.getY();
    }

    public FunctionPoint( double x, double y ){
        this.x = x;
        this.y = y;
    }

    public FunctionPoint( byte[] bytes ){
        if( bytes.length != 16 )
            throw new IllegalArgumentException( "Длинна массива не равна 16" );
        byte[] xBytes = new byte[ 8 ];
        System.arraycopy( bytes, 0, xBytes, 0, 8 );
        byte[] yBytes = new byte[ 8 ];
        System.arraycopy( bytes, 8, yBytes, 0, 8 );
        x = Double.longBitsToDouble( bytesToLong( xBytes ) );
        y = Double.longBitsToDouble( bytesToLong( yBytes ) );
    }

    public double getX(){
        return x;
    }

    public void setX( double x ){
        this.x = x;
    }

    public double getY(){
        return y;
    }

    public void setY( double y ){
        this.y = y;
    }

    byte[] getBytes(){
        byte[] bytes = new byte[ 16 ];
        System.arraycopy( longToBytes( Double.doubleToLongBits( x ) ), 0, bytes, 0, 8 );
        System.arraycopy( longToBytes( Double.doubleToLongBits( y ) ), 0, bytes, 8, 8 );
        return bytes;
    }

    private byte[] longToBytes( long l ){
        byte[] result = new byte[ 8 ];
        for( int i = 7 ; i >= 0 ; i-- ){
            result[ i ] = ( byte ) ( l & 0xFF );
            l >>= 8;
        }
        return result;
    }

    private long bytesToLong( byte[] b ){
        long result = 0;
        for( int i = 0 ; i < 8 ; i++ ){
            result <<= 8;
            result |= ( b[ i ] & 0xFF );
        }
        return result;
    }

    @Override
    public boolean equals( Object obj ){
        if( !( obj instanceof FunctionPoint ) )
            return false;
        return AbstractTabulatedFunction.isEqual( x, ( ( FunctionPoint ) obj ).x ) && AbstractTabulatedFunction.isEqual(
                y, ( ( FunctionPoint ) obj ).y );
    }

    @Override
    public int hashCode(){
        return ( Double.toString( x ) + "," + Double.toString( y ) ).hashCode();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException{
        super.clone();
        return new FunctionPoint( this.x, this.y );
    }

    @Override
    public String toString(){
        return "(" + x + ";" + y + ")";
    }
}
