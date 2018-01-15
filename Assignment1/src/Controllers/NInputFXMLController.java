/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Rija Fahim
 */
public class NInputFXMLController implements Initializable {

    Assignment1 obj;

    @FXML
    private JFXButton submit;
    
    @FXML
    private JFXTextField Nid;

    @FXML
    void writeN(ActionEvent event)
    {

    }

    @FXML
    void submit(ActionEvent event) throws IOException 
    {
        Assignment1.N = Nid.getText();
        Parent home_page = FXMLLoader.load(getClass().getResource("/XMLS/MainGrid.fxml"));
        Scene home_page_scene = new Scene(home_page);
        Stage S1 = (Stage)((Node)event.getSource()).getScene().getWindow();
        S1.setScene(home_page_scene);
        S1.show();   
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        
    }    
    
}
