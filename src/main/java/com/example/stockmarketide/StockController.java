package com.example.stockmarketide;


import java.util.ArrayList;
import java.util.HashMap;

import javafx.event.Event;
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
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.event.*;

import org.json.simple.JSONObject;

import java.io.File;

public class StockController{


    DirectoryChooser dChooser = new DirectoryChooser();


    //This var is to store the overall project folder
    public static File folder = null;


    // This var is used to store the current open tabs we have
    public HashMap<String, TabFile> openFiles = new HashMap<String, TabFile>();

    public JSONObject currData;

    // This var is the actual code we have in the textarea
    @FXML
    private TextArea codeText;

    // This is the overall tab bar that we will add tabs to as we open files
    @FXML
    private TabPane fileTabPane;

    // This is the search bar in the right tab, used to look up crypto names
    @FXML
    private TextField cryptoSearchBar;

    // This is the search button for the above search bar
    @FXML
    private Button cryptoButton;

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

    // The following is the checkboxes in the variables tab
    @FXML
    private CheckBox supply;

    @FXML
    private CheckBox maxSupply;

    @FXML
    private CheckBox marketCapUsd;

    @FXML
    private CheckBox volumeUsd24Hr;

    @FXML
    private CheckBox priceUsd;

    @FXML
    private CheckBox changePercent24Hr;

    @FXML
    private CheckBox vwap24Hr;


    //This will add a new file to the current project
    @FXML
    void newFile(ActionEvent event) {

    }

    //This will create an entirely new project
    @FXML
    void newProject(ActionEvent event) {

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

    // Right now this is gonna update currData instance variable to the most recent data
    // pulled by the API call this triggers, using the listed cryptos in the search bar
    // and the checked boxes in "variables" tab. Then, clears search bar text.
    @FXML
    private void handleCryptoSearch() {
        String searchBarText = cryptoSearchBar.getText();
        String[] coins = searchBarText.split("[\\s*,*]+");
        assert coins.length != 0;

        CheckBox[] checkboxes = {supply, maxSupply, marketCapUsd, volumeUsd24Hr, priceUsd, changePercent24Hr, vwap24Hr};
        ArrayList<String> constructVars = new ArrayList<>();
        for (CheckBox box : checkboxes) {
            if (box.isSelected()) {
                constructVars.add(box.getId());
            }
        }
        String[] variables = constructVars.toArray(new String[0]);

        currData = API.fetch(coins, variables);

        cryptoSearchBar.setText("");
        System.out.println(currData);
    }




}
