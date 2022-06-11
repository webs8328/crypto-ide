package com.example.stockmarketide;


import java.io.FileWriter;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.*;

import java.io.File;
import java.util.Optional;

public class StockController{


    DirectoryChooser dChooser = new DirectoryChooser();
    FileChooser fileChooser = new FileChooser();


    //This var is to store the overall project folder
    public static File folder = null;


    // This var is used to store the current open tabs we have
    public HashMap<String, TabFile> openFiles = new HashMap<String, TabFile>();


    Tab prevTab = null;

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
        if (folder == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Error");
            errorAlert.setContentText("Must open or create a project first");
            errorAlert.showAndWait();
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newFilePopUp.fxml"));
        DialogPane dialogPane = fxmlLoader.load();
        NewFilePopUpController sc = fxmlLoader.getController();
        sc.setNewFilePopupLabel("Choose a new .txt File Name ");

        Dialog<ButtonType> dialog = new Dialog<ButtonType>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("New File");
        Optional<ButtonType> op = dialog.showAndWait();

        if (op.get() == ButtonType.OK) {
            String name = sc.getNewFileName();
            if (name == "") {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("You did not enter a File Name");
                errorAlert.showAndWait();
                return;
            }
            name = name + ".txt";
            try {
                File f = new File(folder.getAbsolutePath() + "/" + name);

                if (!f.createNewFile()) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Input not valid");
                    errorAlert.setContentText("File name already exists");
                    errorAlert.showAndWait();
                    return;
                }
                createTree(f, fileView.getRoot());

            } catch (IOException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Error");
                errorAlert.setContentText("An Error Occurred ");
                errorAlert.showAndWait();
                return;
            }


        }


    }

    //This will create an entirely new project
    @FXML
    void newProject(ActionEvent event) throws IOException{
        clearStuff();
        Stage stage = (Stage) ap.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newFilePopUp.fxml"));
        DialogPane dialogPane = fxmlLoader.load();
        NewFilePopUpController sc = fxmlLoader.getController();
        sc.setNewFilePopupLabel("Choose a new Project Name");
        Dialog<ButtonType> dialog = new Dialog<ButtonType>();

        dialog.setDialogPane(dialogPane);
        dialog.setTitle("New Project");
        Optional<ButtonType> op = dialog.showAndWait();
        dChooser.setTitle("Select where you want this Project to be located");
        File file = dChooser.showDialog(stage);
        if (file == null) {
            return;
        }
        if (op.get() == ButtonType.OK) {
            String name = sc.getNewFileName();
            if (name == "") {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("You did not enter a Project Name");
                errorAlert.showAndWait();
                return;
            }
            String path = file.getAbsolutePath();

            Boolean worked = new File(path + "/" + name).mkdirs();

            if (!worked) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Something went wrong!");
                errorAlert.setContentText("Possible Solution: Make sure the name is not already taken!");
                errorAlert.showAndWait();
                return;
            }
            File f = new File(path + "/" + name);
            folder = f;



        }









        TreeItem<String> root = new TreeItem<String>("Name");

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

            tab.setOnCloseRequest(new EventHandler<Event>()
            {
                @Override
                public void handle(Event arg0)
                {
                    codeText.clear();
                    prevTab = null;
                    openFiles.remove(tab.getText());
                }
            });

            TabFile tabfile = new TabFile(new File(folder.getAbsolutePath() + "/" + name), tab);

            Tab tab2 = fileTabPane.getSelectionModel().getSelectedItem();
            int len = fileTabPane.getTabs().size();
            if (tab2 != null && len != 0) {
                System.out.println(tab2.getText());
                openFiles.get(tab2.getText()).text = codeText.getText();
            }

            openFiles.put(name, tabfile);
            codeText.setText(tabfile.text);
            prevTab = tab;
            fileTabPane.getTabs().add(tab);
            fileTabPane.getSelectionModel().select(tab);

        }

    }

    @FXML
    void openProject(ActionEvent event) {
        clearStuff();
        Stage stage = (Stage) ap.getScene().getWindow();
        dChooser.setTitle("Select a Project");
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
        Tab tab = fileTabPane.getSelectionModel().getSelectedItem();
        if (tab != null) {
            openFiles.get(tab.getText()).text = codeText.getText();
        }

        for (File f : folder.listFiles()) {
            try {
                if (openFiles.containsKey(f.getName())) {

                    FileWriter myWriter = new FileWriter(f.getAbsolutePath());
                    myWriter.write(openFiles.get(f.getName()).text);
                    myWriter.flush();
                    myWriter.close();
                }

            } catch (IOException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Something went wrong!");
                errorAlert.setContentText("An error occurred while saving");
                errorAlert.showAndWait();
                return;
            }
        }
    }



    @FXML
    void changeText(Event event) {
        Tab tab = fileTabPane.getSelectionModel().getSelectedItem();

        String tabname = tab.getText();

        if (prevTab != null && fileTabPane.getTabs().contains(prevTab)) {
            String prevText = codeText.getText();
            openFiles.get(prevTab.getText()).text = prevText;
        }


        prevTab = tab;

        codeText.setText(openFiles.get(tabname).text);


    }


    void clearStuff() {
        openFiles.clear();
        fileTabPane.getTabs().clear();
        codeText.clear();
        folder = null;
    }

}
