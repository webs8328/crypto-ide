package com.example.stockmarketide;


import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.Tab;

import java.io.File;

public class StockController{


    //This var is to store the overall project folder
    public static File folder = null;


    // This var is used to store the current open tabs we have
    public HashMap<String, TabFile> openFiles;



    // This var is the actual code we have in the textarea
    @FXML
    private TextArea codeText;

    // This is the overall tab bar that we will add tabs to as we open files
    @FXML
    private TabPane fileTabPane;

    // This is the left-hand bar that we can see all of the files within a project
    @FXML
    private TreeView<String> fileView;

    //This menu will update with the names of different files we have open so that we can run them
    @FXML
    private Menu runMenu;

    //We need to update this label every time we run something.
    @FXML
    private Label terminal;


    //This will add a new file to the current project
    @FXML
    void newFile(ActionEvent event) {

    }

    //This will create an entirely new project
    @FXML
    void newProject(ActionEvent event) {

    }



    @FXML
    void openFile(MouseEvent event) {
        if(event.getClickCount() == 2)
        {
            TreeItem<String> item = fileView.getSelectionModel().getSelectedItem();
            String name = item.getValue();

            if (openFiles.containsKey(name)) {

            }
            // Create New Tab
            Tab tabdata = new Tab();
            Label tabALabel = new Label("Test");
            tabdata.setGraphic(tabALabel);

            DataStage.addNewTab(tabdata);
        }

    }

    @FXML
    void openProject(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {

    }

    @FXML
    void saveAs(ActionEvent event) {

    }

}
