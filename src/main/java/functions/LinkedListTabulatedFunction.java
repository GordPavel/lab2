package functions;

import functions.exceptions.FunctionPointIndexOutOfBoundsException;
import functions.exceptions.InappropriateFunctionPointException;
import functions.functions.TabulatedFunctionFactory;

import java.io.Serializable;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction{

    public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory{
        @Override
        public TabulatedFunction createTabulatedFunction( double leftX, double rightX, int pointsCount ){
            return new LinkedListTabulatedFunction( leftX , rightX , pointsCount );
        }

        @Override
        public TabulatedFunction createTabulatedFunction( double leftX, double rightX, double[] values ){
            return new LinkedListTabulatedFunction( leftX , rightX , values );
        }

        @Override
        public TabulatedFunction createTabulatedFunction( FunctionPoint[] points ){
            return new LinkedListTabulatedFunction( points );
        }
    }

    private FunctionNode head;
    private int          count;

    public LinkedListTabulatedFunction( double leftX, double rightX, int pointsCount ){
        super( leftX, rightX, pointsCount );
        head = new FunctionNode();
        head.previous = head.next = head;
        count = 0;
        double sh = ( rightX - leftX ) / ( pointsCount - 1 );
        try{
            for( int i = 0 ; i < pointsCount ; i++ )
                this.addPointToTail( new FunctionPoint( ( leftX + sh * i ), 0 ) );
        }catch( InappropriateFunctionPointException e ){
            System.out.println( e.getMessage() );
        }
    }

    public LinkedListTabulatedFunction( double leftX, double rightX, double[] values ){
        this( leftX, rightX, values.length );
        for( int i = 0, j = getPointsCount() ; i < j ; i++ )
            this.setPointY( i, values[ i ] );
    }

    public LinkedListTabulatedFunction( FunctionPoint[] points ){
        super( points[ 0 ].getX(), points[ points.length - 1 ].getX(), points.length );
        if( !isSort( points ) )
            throw new IllegalArgumentException( "Not sorted array." );
        head = new FunctionNode();
        head.previous = head.next = head;
        count = 0;
        try{
            for( FunctionPoint point : points )
                this.addPointToTail( point );
        }catch( InappropriateFunctionPointException e ){
            e.printStackTrace();
        }
    }

    private FunctionNode getNodeByIndex( int index ){
        if( index < 0 || index >= count )
            throw new FunctionPointIndexOutOfBoundsException( "Index error." );
        FunctionNode node = head.next;
        int          i    = 0;
        while( true ){
            if( i == index )
                return node;
            node = node.next;
            i++;
        }
    }

    public double getLeftDomainBorder(){
        return head.next.point.getX();
    }

    public double getRightDomainBorder(){
        return head.previous.point.getX();
    }

    public double getFunctionValue( double x ){
        if( this.getLeftDomainBorder() - x > EPSILON || x - getRightDomainBorder() > EPSILON )
            return Double.NaN;
        FunctionNode node = head.next;
        while( x - node.point.getX() > EPSILON )
            node = node.next;
        if( ArrayTabulatedFunction.isEqual( node.point.getX(), x ) )
            return node.point.getY();
        return node.point.getY() + ( node.next.point.getY() - node.point.getY() ) * ( x - node.point.getX() ) / ( node.next.point.getX() - node.point.getX() );
    }

    public int getPointsCount(){
        return count;
    }

    public FunctionPoint getPoint( int index ){
        if( !( index >= 0 && index < this.getPointsCount() ) )
            throw new FunctionPointIndexOutOfBoundsException( "Index error." );
        return getNodeByIndex( index ).point;
    }

    public void setPoint( int index, FunctionPoint point ) throws InappropriateFunctionPointException{
        super.setPoint( index , point );
        FunctionNode node = getNodeByIndex( index );
        try{
            if( index == 0 && node.next.point.getX() - node.point.getX() > EPSILON )
                node.point = ( FunctionPoint ) point.clone();
            else if( index == this.getPointsCount() - 1 && node.point.getX() - node.previous.point.getX() > EPSILON )
                node.point = ( FunctionPoint ) point.clone();
            else if( node.point.getX() - node.previous.point.getX() > EPSILON && node.next.point.getX() - node.point.getX() > EPSILON )
                node.point = ( FunctionPoint ) point.clone();
            else
                throw new InappropriateFunctionPointException( "Edit point error." );
        }catch( CloneNotSupportedException e ){
            System.out.println( e.getMessage() );
        }
    }

    public void deletePoint( int index ){
        if( getPointsCount() < 3 )
            throw new IllegalStateException( "Points count error" );
        if( index < 0 || index >= count )
            throw new FunctionPointIndexOutOfBoundsException( "Index error." );
        FunctionNode node = this.getNodeByIndex( index );
        node.previous.next = node.next;
        node.next.previous = node.previous;
        node = null;
        count--;
    }

    public void addPoint( FunctionPoint point ) throws InappropriateFunctionPointException{
        if( hasPointX( point.getX() ) )
            throw new InappropriateFunctionPointException( "This point has exists." );
        if( point.getX() - getRightDomainBorder() > EPSILON )
            addPointToTail( point );
        else{
            FunctionNode nextNode = head.next;
            while( point.getX() - nextNode.point.getX() > EPSILON )
                nextNode = nextNode.next;
            FunctionNode newNode = new FunctionNode();
            newNode.point = point;
            newNode.next = nextNode;
            newNode.previous = nextNode.previous;
            newNode.previous.next = newNode;
            nextNode.previous = newNode;
            count++;
        }
    }

    public void addPointToTail( FunctionPoint point ) throws InappropriateFunctionPointException{
        if( hasPointX( point.getX() ) )
            throw new InappropriateFunctionPointException( "This point has exists." );
        FunctionNode newNode = new FunctionNode();
        newNode.point = point;
        newNode.next = head;
        newNode.previous = head.previous;
        head.previous.next = newNode;
        head.previous = newNode;
        count++;
    }

    public FunctionPoint[] getFunction(){
        FunctionPoint[] function  = new FunctionPoint[ count ];
        FunctionNode    goingNode = head.next;
        for( int i = 0 ; goingNode != head ; i++ ){
            function[ i ] = goingNode.point;
            goingNode = goingNode.next;
        }
        return function;
    }

    public boolean hasPointX( double x ){
        FunctionNode node = head;
        while( ( node = node.next ) != head )
            if( isEqual( x, node.point.getX() ) )
                return true;
        return false;
    }

    private class FunctionNode implements Serializable{
        FunctionPoint point;
        FunctionNode  previous;
        FunctionNode  next;
    }
}
