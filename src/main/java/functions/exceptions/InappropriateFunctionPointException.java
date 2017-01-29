package functions.exceptions;

public class InappropriateFunctionPointException extends Exception{
    private String message;

    public InappropriateFunctionPointException( String message ){
        this.message = message;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
