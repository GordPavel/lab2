package GraficApp;

import functions.FunctionPoint;
import functions.LinkedListTabulatedFunction;
import functions.exceptions.InappropriateFunctionPointException;
import functions.functions.Function;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class FunctionController implements Initializable{
    private Stage dialogStage;
    static  Stage            thisStage;
    private FunctionDocument functionDocument;
    private MyLoader         functionLoader;
    private FileChooser      fileChooser;

    public TableView<FunctionPoint>           table;
    public TableColumn<FunctionPoint, Double> columnX;
    public TableColumn<FunctionPoint, Double> columnY;
    public TextField                          newPointXTextField;
    public TextField                          newPointYTextField;

    public void initialize( URL location, ResourceBundle resources ){

        dialogStage = new Stage();
//        Выставление диалогового окна
        NewFunctionController.thisStage = dialogStage;
        Parent root = null;
        try{
            root = FXMLLoader.load( getClass().getResource( "/newFunctionWindow.fxml" ) );
        }catch( IOException e ){
            e.printStackTrace();
        }
        dialogStage.setTitle( "Function parameters" );
        dialogStage.setScene( new Scene( root, 400, 178 ) );
        dialogStage.setResizable( false );
        dialogStage.initModality( Modality.APPLICATION_MODAL );
        dialogStage.setOnCloseRequest( event -> {
            NewFunctionController.whichButtonClicked = Main.CANCEL;
            dialogStage.hide();
        } );
//        Таблица
        StringConverter<Double> converter = new DoubleStringConverter();
        columnX.setCellValueFactory( new PropertyValueFactory<>( "x" ) );
        columnX.setCellFactory( TextFieldTableCell.forTableColumn( converter ) );
        columnX.setOnEditCommit( event -> {
            try{
                functionDocument.setPointX( table.getFocusModel().getFocusedIndex(), event.getNewValue() );
            }catch( InappropriateFunctionPointException e ){
                new Alert( Alert.AlertType.ERROR, "You can't change on this value." ).showAndWait();
            }catch( Exception e ){
                new Alert( Alert.AlertType.ERROR, "Anything goes wrong." ).showAndWait();
            }
            repaint();
            table.refresh();
        } );

        columnY.setCellValueFactory( new PropertyValueFactory<>( "y" ) );
        columnY.setCellFactory( TextFieldTableCell.forTableColumn( converter ) );
        columnY.setOnEditCommit( event -> {
            try{
                functionDocument.setPointY( table.getFocusModel().getFocusedIndex(), event.getNewValue() );
            }catch( InappropriateFunctionPointException e ){
                new Alert( Alert.AlertType.ERROR, "You can't change on this value." ).showAndWait();
            }catch( Exception e ){
                new Alert( Alert.AlertType.ERROR, "Anything goes wrong." ).showAndWait();
            }
            repaint();
            table.refresh();
        } );

        functionDocument = new FunctionDocument( new LinkedListTabulatedFunction( 0, 10, 11 ) );
        repaint();
        fileChooser = new FileChooser();
        functionLoader = new MyLoader( new HashMap<>() );
    }

    public void onClickNewDocumentItem(){
//        Новая функция
        if( functionDocument.isModified() )
            onClickSaveItem();
        dialogStage.showAndWait();
        if( NewFunctionController.whichButtonClicked == Main.OK ){
            functionDocument.newFunction( NewFunctionController.leftDomainBorder,
                                          NewFunctionController.rightDomainBorder, NewFunctionController.pointsCount );
            functionDocument.setFileName( "Untitled" );
            repaint();
        }
    }

    public void onClickOpenItem(){
//        открыть из файла
        File file = fileChooser.showOpenDialog( thisStage );
        try{
            if( file != null )
                functionDocument.loadFunction( String.valueOf( file ) );
            functionDocument.setFileName( file.getName() );
            repaint();
        }catch( IOException e ){
            new Alert( Alert.AlertType.ERROR, "Error during loading" ).showAndWait();
        }catch( Exception e ){
            new Alert( Alert.AlertType.ERROR, "Error in the function" ).showAndWait();
        }
    }

    public void onClickSaveItem(){
//        Сохранение в файл
        if( functionDocument.isFileNameAssigned() ){
            try{
                this.functionDocument.saveFunction();
            }catch( Exception e ){
                new Alert( Alert.AlertType.ERROR, "Saving error." ).showAndWait();
            }
        }else
            onClickSaveAsItem();
    }

    public void onClickSaveAsItem(){
//        Сохранить как
        File file = fileChooser.showSaveDialog( thisStage );
        if( file != null )
            try{
                functionDocument.saveFunctionAs( file + "" );
                functionDocument.setFileName( file.getName() );
            }catch( IOException e ){
                new Alert( Alert.AlertType.ERROR, "File error." ).showAndWait();
            }
    }

    public void onClickLoadAndTabulateItem(){
//        Табулирование байт-кода
        File file = fileChooser.showOpenDialog( thisStage );
        if( file != null ){
            dialogStage.showAndWait();
            if( NewFunctionController.whichButtonClicked == Main.OK ){
                try{
                    Function function;
                    if( !functionLoader.alreadyLoadedClasses.containsKey( file + "" ) ){
                        Class loadedClass = functionLoader.loadClass( file + "" );
                        function = ( Function ) loadedClass.newInstance();
                        functionLoader.alreadyLoadedClasses.put( file + "", loadedClass );
                    }else
                        function = ( Function ) functionLoader.alreadyLoadedClasses.get( file + "" ).newInstance();
                    functionDocument.tabulateFunction( function, NewFunctionController.leftDomainBorder,
                                                       NewFunctionController.rightDomainBorder,
                                                       NewFunctionController.pointsCount );
                    repaint();
                }catch( IllegalAccessException | ClassNotFoundException | InstantiationException e ){
                    new Alert( Alert.AlertType.ERROR, "Error in class" ).showAndWait();
                }catch( ClassCastException e ){
                    new Alert( Alert.AlertType.ERROR, "Loaded class is not a function." ).showAndWait();
                }catch( Exception e ){
                    new Alert( Alert.AlertType.ERROR, e.getMessage() ).showAndWait();
                }
            }
        }
    }

    public void onClickAddPoint(){
//        Добавление точки
        if( newPointXTextField.getText().equals( "" ) || newPointYTextField.getText().equals( "" ) )
            new Alert( Alert.AlertType.ERROR, "Write anything." ).showAndWait();
        try{
            double x = Double.parseDouble( newPointXTextField.getText() );
            double y = Double.parseDouble( newPointYTextField.getText() );
            functionDocument.addPoint( new FunctionPoint( x, y ) );
            newPointXTextField.setText( "" );
            newPointYTextField.setText( "" );
            repaint();
        }catch( InappropriateFunctionPointException e ){
            new Alert( Alert.AlertType.ERROR, e.getMessage() ).showAndWait();
        }catch( NumberFormatException e ){
            new Alert( Alert.AlertType.ERROR, "Number format error." ).showAndWait();
        }
    }

    public void onClickDeleteButton(){
//        Удаление точки
        try{
            ObservableList<FunctionPoint> allPoints = table.getItems();
            functionDocument.deletePoint( table.getSelectionModel().getFocusedIndex() );
            allPoints.remove( table.getSelectionModel().getFocusedIndex() );
            repaint();
        }catch( Exception e ){
            new Alert( Alert.AlertType.ERROR, e.getMessage() ).showAndWait();
        }
    }

    void onExitCLick( WindowEvent event ){
//        Закрытие основного окна
        if( functionDocument.isModified() ){
            String type = new Alert( Alert.AlertType.CONFIRMATION, "Do you really want to exit?", ButtonType.CANCEL,
                                     ButtonType.CLOSE ).showAndWait().toString();
            if( type.equals( "Optional[ButtonType [text=Close, buttonData=CANCEL_CLOSE]]" ) )
                thisStage.close();
            else
                event.consume();
        }else
            thisStage.close();
    }

    private void repaint(){
        table.setItems( getPoints( functionDocument ) );
        if( thisStage != null )
            thisStage.setTitle( functionDocument.getFileName() );
    }

    private ObservableList<FunctionPoint> getPoints( FunctionDocument document ){
        ObservableList<FunctionPoint> points = FXCollections.observableArrayList();
        document.forEach( points::add );
        return points;
    }
}

