package com.example.stockmarketide;


import javafx.scene.control.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import java.io.File;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class TabFile {
    File file;
    String text;
    String name;
    Path path;


    public void TabFile(File f) throws Exception {
        this.file = f;
        this.name = f.getName();
        String filepath = f.getAbsolutePath();
        this.path = Path.of(filepath);
        this.text = Files.readString(path);
    }


    public String getText() {
        return this.text;
    }

    public void setText(String newText) throws Exception{
        Files.writeString(path, newText);
    }



}
