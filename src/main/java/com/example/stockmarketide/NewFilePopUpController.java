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
import javafx.scene.control.TextField;
public class NewFilePopUpController {

    @FXML
    private TextField newFileName;


    String getNewFileName() {
        String s = newFileName.getCharacters().toString();
        return s;
    }

}
