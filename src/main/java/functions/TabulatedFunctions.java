package functions;

import functions.functions.Function;
import functions.functions.TabulatedFunctionFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class TabulatedFunctions{

    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    public static void setTabulatedFunctionFactory( TabulatedFunctionFactory factory ){
        TabulatedFunctions.factory = factory;
    }

    public static TabulatedFunction createTabulatedFunction( double leftX, double rightX, int pointsCount ){
        return factory.createTabulatedFunction( leftX, rightX, pointsCount );
    }

    public static TabulatedFunction createTabulatedFunction( double leftX, double rightX, double[] values ){
        return factory.createTabulatedFunction( leftX, rightX, values );
    }

    public static TabulatedFunction createTabulatedFunction( Class<? extends TabulatedFunction> myClass, double leftX,
                                                             double rightX, int pointsCount ){
        TabulatedFunction temp;
        try{
            temp = myClass.getConstructor( double.class, double.class, int.class ).newInstance(
                    new Object[]{ leftX , rightX , pointsCount } );
        }catch( NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e ){
            throw new IllegalArgumentException( e );
        }
        return temp;
    }

    public static TabulatedFunction createTabulatedFunction( Class<? extends TabulatedFunction> myClass, double leftX,
                                                             double rightX, double[] values ){
        TabulatedFunction temp;
        try{
            temp = myClass.getConstructor( double.class, double.class, values.getClass() ).newInstance(
                    new Object[]{ leftX , rightX , values } );
        }catch( NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e ){
            throw new IllegalArgumentException( e );
        }
        return temp;
    }

    public static TabulatedFunction createTabulatedFunction( FunctionPoint[] points ){
        return factory.createTabulatedFunction( points );
    }

    public static TabulatedFunction createTabulatedFunction( Class<? extends TabulatedFunction> myClass,
                                                             FunctionPoint[] a ){
        TabulatedFunction temp;
        try{
            temp = myClass.getConstructor( FunctionPoint[].class ).newInstance( new Object[]{ a } );
        }catch( NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | IllegalArgumentException e ){
            throw new IllegalArgumentException( e );
        }
        return temp;
    }

    private TabulatedFunctions(){
    }

    public static TabulatedFunction tabulate( Function function, double leftX, double rightX, double discretization ){
        return createTabulatedFunction( GetterPoints.getPoints( function, leftX, rightX, discretization ) );
    }

    public static TabulatedFunction tabulate( Class<? extends TabulatedFunction> myClass, Function function,
                                              double leftX, double rightX, double discretization ){
        return createTabulatedFunction( myClass, GetterPoints.getPoints( function, leftX, rightX, discretization ) );
    }

    public static TabulatedFunction tabulate( Function function, double leftX, double rightX, int pointsCount ){
        return createTabulatedFunction( GetterPoints.getPoints( function, leftX, rightX, pointsCount ) );
    }

    public static TabulatedFunction tabulate( Class<? extends TabulatedFunction> myClass, Function function,
                                              double leftX, double rightX, int pointsCount ){
        return createTabulatedFunction( myClass, GetterPoints.getPoints( function, leftX, rightX, pointsCount ) );
    }

    public static void outputTabulatedFunction( TabulatedFunction function, OutputStream out ){
        DataOutputStream outputStream = new DataOutputStream( out );
        try{
            outputStream.writeInt( function.getPointsCount() );
            for( FunctionPoint point : function.getFunction() ){
                outputStream.writeDouble( point.getX() );
                outputStream.writeDouble( point.getY() );
            }
            outputStream.flush();
        }catch( IOException e ){
            throw new IllegalArgumentException( "Ошибка потока" );
        }
    }

    public static TabulatedFunction inputTabulatedFunction( InputStream in ){
        return createTabulatedFunction( GetterPoints.getPoints( in ) );
    }

    public static TabulatedFunction inputTabulatedFunction( Class<? extends TabulatedFunction> myClass,
                                                            InputStream in ){
        return createTabulatedFunction( myClass, GetterPoints.getPoints( in ) );
    }

    public static void writeTabulatedFunction( TabulatedFunction function, Writer out ) throws IOException{
        Writer writer = new BufferedWriter( out );
        writer.write( String.valueOf( function.getPointsCount() ) );
        writer.write( '\n' );
        for( FunctionPoint point : function.getFunction() ){
            writer.write( String.valueOf( point.getX() ) );
            writer.write( ' ' );
            writer.write( String.valueOf( point.getY() ) );
            writer.write( '\n' );
        }
        writer.flush();
    }

    public static TabulatedFunction readTabulatedFunction( Reader in ){
        return createTabulatedFunction( GetterPoints.getPoints( in ) );
    }

    public static TabulatedFunction readTabulatedFunction( Class<? extends TabulatedFunction> myClass, Reader in ){
        return createTabulatedFunction( myClass, GetterPoints.getPoints( in ) );
    }

    private static class GetterPoints{
        static FunctionPoint[] getPoints( Function function, double leftX, double rightX, double discretization ){
            if( leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder() )
                throw new IllegalArgumentException( "Borders error" );
            ArrayList<FunctionPoint> points = new ArrayList<>();
            for( double x = leftX ; rightX - x > TabulatedFunction.EPSILON ; x += discretization )
                points.add( new FunctionPoint( x, function.getFunctionValue( x ) ) );
            points.add( new FunctionPoint( rightX, function.getFunctionValue( rightX ) ) );
            FunctionPoint[] result = new FunctionPoint[ points.size() ];
            return points.toArray( result );
        }

        private static FunctionPoint[] getPoints( Function function, double leftX, double rightX, int pointsCount ){
            if( leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder() )
                throw new IllegalArgumentException( "Borders error" );
            double          sh     = ( rightX - leftX ) / ( pointsCount - 1 );
            FunctionPoint[] points = new FunctionPoint[ pointsCount ];
            for( int i = 0 ; i < pointsCount ; i++ ){
                double x = leftX + sh * i;
                points[ i ] = new FunctionPoint( x, function.getFunctionValue( x ) );
            }
            return points;
        }

        private static FunctionPoint[] getPoints( InputStream in ){
            DataInputStream inputStream = new DataInputStream( in );
            try{
                FunctionPoint[] points = new FunctionPoint[ inputStream.readInt() ];
                for( int i = 0, j = points.length ; i < j ; i++ )
                    points[ i ] = new FunctionPoint( inputStream.readDouble(), inputStream.readDouble() );
                return points;
            }catch( IOException e ){
                throw new IllegalArgumentException( "Ошибка потока" );
            }
        }

        private static FunctionPoint[] getPoints( Reader in ){
            try{
                StreamTokenizer tokenizer = new StreamTokenizer( in );
                int             pointsCount;
                if( tokenizer.nextToken() == StreamTokenizer.TT_NUMBER )
                    pointsCount = ( int ) tokenizer.nval;
                else
                    throw new IOException();
                FunctionPoint[] points = new FunctionPoint[ pointsCount ];
                double          x, y;
                for( int i = 0 ; tokenizer.nextToken() != StreamTokenizer.TT_EOF ; i++ ){
                    x = tokenizer.nval;
                    tokenizer.nextToken();
                    y = tokenizer.nval;
                    points[ i ] = new FunctionPoint( x, y );
                }
                return points;
            }catch( IOException e ){
                throw new IllegalArgumentException( "Ошибка потока" );
            }
        }
    }
}
