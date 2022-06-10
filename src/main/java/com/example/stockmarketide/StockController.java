package com.example.stockmarketide;


import java.io.IOException;
import java.util.HashMap;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.Tab;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.event.*;

import java.io.File;

public class StockController{


    DirectoryChooser dChooser = new DirectoryChooser();


    //This var is to store the overall project folder
    public static File folder = null;


    // This var is used to store the current open tabs we have
    public HashMap<String, TabFile> openFiles = new HashMap<String, TabFile>();



    // This var is the actual code we have in the textarea
    @FXML
    private TextArea codeText;

    // This is the overall tab bar that we will add tabs to as we open files
    @FXML
    private TabPane fileTabPane;


    @FXML
    private AnchorPane ap;

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
    void newFile(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newFilePopUp.fxml"));
        DialogPane dialogPane = fxmlLoader.load();
        StockController sc = fxmlLoader.getController();

    }

    //This will create an entirely new project
    @FXML
    void newProject(ActionEvent event) {
        clearStuff();
        TreeItem<String> root = new TreeItem<String>("UnsavedProject");

        fileView.setRoot(root);

    }





    @FXML
    void openFile(MouseEvent event) throws Exception {
        SingleSelectionModel<Tab> selectionModel = fileTabPane.getSelectionModel();

        if(event.getClickCount() == 2)
        {
            TreeItem<String> item = fileView.getSelectionModel().getSelectedItem();

            String name = item.getValue();
            if (openFiles.containsKey(name)) {
                return;
            }
            // Create New Tab
            Tab tab = new Tab();
            tab.setText(name);
            tab.setOnSelectionChanged(new EventHandler<Event>() {
                @Override
                public void handle(Event t) {
                    changeText(t);
                }
            });
            TabFile tabfile = new TabFile(new File(folder.getAbsolutePath() + "/" + name), tab);
            openFiles.put(name, tabfile);
            fileTabPane.getTabs().add(tab);
            codeText.setText(tabfile.text);
            selectionModel.select(tab);
        }

    }

    @FXML
    void openProject(ActionEvent event) {
        clearStuff();
        Stage stage = (Stage) ap.getScene().getWindow();
        File file = dChooser.showDialog(stage);
        if (file == null) {
            return;
        }
        folder = file;
        TreeItem<String> root = new TreeItem<String>(file.getName());


        // create tree
        for (File f : file.listFiles()) {

            createTree(f, root);

        }

        fileView.setRoot(root);
    }






    public static void createTree(File file, TreeItem<String> parent) {
        System.out.println(file.getName());
        if (file.isDirectory()) {
            TreeItem<String> treeItem = new TreeItem<String>(file.getName());
            parent.getChildren().add(treeItem);
            for (File f : file.listFiles()) {
                createTree(f, treeItem);
            }
        } else  {
            parent.getChildren().add(new TreeItem<>(file.getName()));
        }
    }

    @FXML
    void save(ActionEvent event) {

    }

    @FXML
    void saveAs(ActionEvent event) {

    }

    @FXML
    void changeText(Event event) {
        Tab tab = fileTabPane.getSelectionModel().getSelectedItem();
        String tabname = tab.getText();
        codeText.setText(openFiles.get(tabname).text);


    }


    void clearStuff() {
        openFiles.clear();
        fileTabPane.getTabs().clear();
        codeText.clear();
    }



}
