package GraficApp;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewFunctionController{

    public TextField        leftDomainBorderTextField;
    public TextField        rightDomainBorderTextField;
    public Spinner<Integer> pointsCountSpinner;
    public Label            errorTextLabel;

    static int    whichButtonClicked;
    static Stage  thisStage;
    static Double leftDomainBorder, rightDomainBorder;
    static Integer pointsCount;

    public void okAction(){
        errorTextLabel.setVisible( false );
        try{
            leftDomainBorder = Double.parseDouble( leftDomainBorderTextField.getText() );
            rightDomainBorder = Double.parseDouble( rightDomainBorderTextField.getText() );
            pointsCount = pointsCountSpinner.getValue();
            whichButtonClicked = Main.OK;
            thisStage.hide();
        }catch( NumberFormatException e ){
            new Alert( Alert.AlertType.ERROR, "Not a number!" ).showAndWait();
        }catch( IllegalArgumentException e ){
            errorTextLabel.setVisible( true );
            errorTextLabel.setText( e.getMessage() );
        }
    }

    @FXML
    public void cancelAction(){
        whichButtonClicked = Main.CANCEL;
        thisStage.hide();
    }
}
