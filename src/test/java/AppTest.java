import junit.framework.TestCase;

import java.util.LinkedList;
import java.util.List;

public class AppTest extends TestCase{
    private List<Test> tests;

    {
        tests = new LinkedList<>();
        tests.add( new ArrayTabulatedFunctionTest() );
        tests.add( new LinkedListTabulatedFunctionTest() );
        tests.add( new FunctionsTest() );
        tests.add( new SerializeTest() );
        tests.add( new TabulatedFunctionsTest() );
        tests.add( new TabulatedFunctionTest() );
    }

    public void test(){
        for( Test test : tests )
            test.runTest();
    }
}
