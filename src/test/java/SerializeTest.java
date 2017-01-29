import functions.Functions;
import functions.LinkedListTabulatedFunction;
import functions.TabulatedFunction;
import functions.TabulatedFunctions;
import functions.functions.basic.Cos;
import functions.functions.basic.Exp;
import junit.framework.TestCase;

import java.io.*;

public class SerializeTest extends TestCase implements Test{

    public void runTest(){
        try{
            testSerialize();
        }catch( Exception e ){
            e.printStackTrace();
        }
    }

    public void testSerialize() throws IOException, ClassNotFoundException{
        TabulatedFunctions.setTabulatedFunctionFactory( new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory() );
        TabulatedFunction function =
                TabulatedFunctions.tabulate( Functions.mult( new Exp(), new Cos() ), 0, 11, 12 );
        PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream  inputStream  = new PipedInputStream( outputStream );

        ObjectOutputStream serialize = new ObjectOutputStream( outputStream );
        serialize.writeObject( function );
        serialize.close();

        ObjectInputStream deserialize = new ObjectInputStream( inputStream );
        TabulatedFunction newFunction = ( TabulatedFunction ) deserialize.readObject();
        deserialize.close();

        assertTrue( function.equals( newFunction ) );
    }

}
