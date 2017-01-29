package GraficApp;

import functions.ArrayTabulatedFunction;
import functions.FunctionPoint;
import functions.TabulatedFunction;
import functions.TabulatedFunctions;
import functions.exceptions.InappropriateFunctionPointException;
import functions.functions.Function;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class FunctionDocument implements TabulatedFunction{

    private TabulatedFunction function;
    private String            fileName;
    private boolean modified = false;
    private boolean fileNameAssigned;

    FunctionDocument( TabulatedFunction function ){
        this.function = function;
    }

    public int getPointsCount(){
        return function.getPointsCount();
    }

    public FunctionPoint getPoint( int index ){
        return function.getPoint( index );
    }

    public void setPoint( int index, FunctionPoint point ) throws InappropriateFunctionPointException{
        function.setPoint( index, point );
        modified = true;
    }

    public String getFileName(){
        return fileName;
    }

    public void setFileName( String fileName ){
        this.fileName = fileName;
    }

    public double getPointX( int index ){
        return function.getPointX( index );
    }

    public void setPointX( int index, double x ) throws InappropriateFunctionPointException{
        function.setPointX( index, x );
        modified = true;
    }

    public double getPointY( int index ){
        return function.getPointY( index );
    }

    public void setPointY( int index, double y ) throws InappropriateFunctionPointException{
        function.setPointY( index, y );
        modified = true;
    }

    public void deletePoint( int index ){
        function.deletePoint( index );
        modified = true;
    }

    public void addPoint( FunctionPoint point ) throws InappropriateFunctionPointException{
        function.addPoint( point );
        modified = true;
    }

    public void addPointToTail( FunctionPoint point ) throws InappropriateFunctionPointException{
        function.addPointToTail( point );
        modified = true;
    }

    public FunctionPoint[] getFunction(){
        return function.getFunction();
    }

    public void setFunction( TabulatedFunction function ){
        this.function = function;
        modified = true;
    }

    public boolean hasPointX( double x ){
        return function.hasPointX( x );
    }

    public byte[] getBytes(){
        return function.getBytes();
    }

    @Override
    public boolean equals( Object obj ){
        if( !( obj instanceof FunctionDocument ) )
            return false;
        FunctionDocument document = ( FunctionDocument ) obj;
        return document.fileName.equals(
                this.fileName ) && document.fileNameAssigned == this.fileNameAssigned && document.modified == this.modified && document.function.equals(
                this.function );
    }

    public Object clone() throws CloneNotSupportedException{
        FunctionDocument clone = new FunctionDocument( this.function );
        clone.fileName = this.fileName;
        clone.modified = this.modified;
        clone.fileNameAssigned = this.fileNameAssigned;
        return clone;
    }

    public double getLeftDomainBorder(){
        return function.getLeftDomainBorder();
    }

    public double getRightDomainBorder(){
        return function.getRightDomainBorder();
    }

    public double getFunctionValue( double x ){
        return function.getFunctionValue( x );
    }

    boolean isFileNameAssigned(){
        return fileNameAssigned;
    }

    boolean isModified(){
        return modified;
    }

    void newFunction( double leftX, double rightX, int pointsCount ){
        this.function = new ArrayTabulatedFunction( leftX, rightX, pointsCount );
    }

    void saveFunction() throws IOException{
        TabulatedFunctions.writeTabulatedFunction( function, new FileWriter( fileName ) );
    }

    void saveFunctionAs( String fileName ) throws IOException{
        FileWriter fileWriter = new FileWriter( fileName );
        TabulatedFunctions.writeTabulatedFunction( this.function, fileWriter );
        this.modified = false;
        this.fileName = fileName;
        this.fileNameAssigned = true;
        fileWriter.close();
    }

    void loadFunction( String fileName ) throws IOException{
        function = TabulatedFunctions.readTabulatedFunction( new FileReader( fileName ) );
    }

    void tabulateFunction( Function function, double leftX, double rightX, int pointsCount ){
        this.function = TabulatedFunctions.tabulate( function, leftX, rightX, pointsCount );
    }
}
