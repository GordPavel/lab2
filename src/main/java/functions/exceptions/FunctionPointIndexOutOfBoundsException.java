package functions.exceptions;

public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException{
    private String message;

    public FunctionPointIndexOutOfBoundsException( String message ){
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
