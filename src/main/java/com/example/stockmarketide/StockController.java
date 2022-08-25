package com.example.stockmarketide;
import java.io.*;
import java.util.ArrayList;
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
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.*;

import org.json.simple.JSONObject;

import java.util.Map;
import java.util.Optional;

public class StockController{

    String classInit = "class Coin:\n" +
            "    \n" +
            "    def __init__(self, supply, maxSupply, marketCapUsd, volumeUsed24Hr, priceUsd, changePercent24Hr, vwap24Hr):\n" +
            "        self.supply = supply\n" +
            "        self.maxSupply = maxSupply\n" +
            "        self.marketCapUsd = marketCapUsd\n" +
            "        self.volumeUsed24Hr = volumeUsed24Hr\n" +
            "        self.priceUsd = priceUsd\n" +
            "        self.changePercent24Hr = changePercent24Hr\n" +
            "        self.vwap24Hr = vwap24Hr\n" +
            "        \n" +
            "    def getSupply(self):\n" +
            "        return self.supply\n" +
            "    def getMaxSupply(self):\n" +
            "        return self.maxSupply\n" +
            "    def getMarketCapUsd(self):\n" +
            "        return self.marketCapUsd\n" +
            "    def getVolumeUsed24Hr(self):\n" +
            "        return self.volumeUsed24Hr\n" +
            "    def getPriceUsd(self):\n" +
            "        return self.priceUsd\n" +
            "    def getChangePercent24Hr(self):\n" +
            "        return self.changePercent24Hr\n" +
            "    def getVwap24Hr(self):\n" +
            "        return self.vwap24Hr\n" +
            "        \n" +
            "    def setSupply(self, newVal):\n" +
            "        self.supply = newVal\n" +
            "    def setMaxSupply(self, newVal):\n" +
            "        self.maxSupply = newVal\n" +
            "    def setMarketCapUsd(self, newVal):\n" +
            "        self.marketCapUsd = newVal\n" +
            "    def setVolumeUsed24Hr(self, newVal):\n" +
            "        self.volumeUsed24Hr = newVal\n" +
            "    def setPriceUsd(self, newVal):\n" +
            "        self.priceUsd = newVal\n" +
            "    def setChangePercent24Hr(self, newVal):\n" +
            "        self.changePercent24Hr = newVal\n" +
            "    def setVwap24Hr(self, newVal):\n" +
            "        self.vwap24Hr = newVal\n" +
            "    ";
    ArrayList<String> coinInits = new ArrayList<String>();
    DirectoryChooser dChooser = new DirectoryChooser();
    FileChooser fileChooser = new FileChooser();


    //This var is to store the overall project folder
    public static File folder = null;


    // This var is used to store the current open tabs we have
    public HashMap<String, TabFile> openFiles = new HashMap<String, TabFile>();
    public HashMap<String, MenuItem> openFilesMenu = new HashMap<String, MenuItem>();



    Tab prevTab = null;

    public JSONObject currData;

    @FXML
    private Text descBox;

    @FXML
    private Menu runMenu;

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
    private ScrollPane scrollPane;


    @FXML
    private AnchorPane ap;

    // This is the left-hand bar that we can see all of the files within a project
    @FXML
    private TreeView<String> fileView;

    //This menu will update with the names of different files we have open so that we can run them


    //We need to update this label every time we run something.
    @FXML
    private TextArea terminal;

    // The following is the checkboxes in the variables tab
    @FXML
    private Button supply;

    @FXML
    private Button maxSupply;

    @FXML
    private Button marketCapUsd;

    @FXML
    private Button volumeUsd24Hr;

    @FXML
    private Button priceUsd;

    @FXML
    private Button changePercent24Hr;

    @FXML
    private Button vwap24Hr;

    private final String[] buttonStrings = {"supply", "maxSupply", "marketCapUsd", "volumeUsd24Hr", "priceUsd", "changePercent24Hr", "vwap24Hr"};

    private final Map<String, String> varDescriptions = Map.of(
            "supply", "The number of cryptocurrency coins or tokens that are publicly available and circulating in the market.\n" +
                    "Reference with Coin.supply",
            "maxSupply", "Quantifies the maximum amount of coins that will ever exist, including the coins that will be mined or made available in the future.\n" +
                    "Reference with Coin.maxSupply",
            "marketCapUsd", "Market capitalization (or market cap) is the total value of all the coins that have been mined.\n" +
                    "Reference with Coin.marketCapUsd",
            "volumeUsd24Hr", "Сryptocurrency volume is the amount of a given cryptocurrency traded throughout a particular time period.\n" +
                    "Reference with volumeUsd24Hr",
            "priceUsd", "Price for 1 token in US Dollars\n" +
                    "Reference with Coin.priceUsd",
            "changePercent24Hr", "The change is the difference (in percent) between the price now compared to the price around this time 24 hours ago.\n" +
                    "Reference with Coin.changePercent24Hr",
            "vwap24Hr", "VWAP is calculated by totaling the dollars traded for every transaction (price multiplied by the volume) and then dividing by the total shares traded.\n" +
                    "Reference with Coin.vwap24Hr"
    );


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
            TreeItem<String> root = new TreeItem<String>(name);

            fileView.setRoot(root);


        }


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
                    MenuItem i = openFilesMenu.get(tab.getText());
                    runMenu.getItems().remove(i);
                    openFilesMenu.remove(tab.getText());
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
            makeMenuItem(name);
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


    // Right now this is gonna update currData instance variable to the most recent data
    // pulled by the API call this triggers, using the listed cryptos in the search bar
    // and the checked boxes in "variables" tab. Then, clears search bar text.

    //updated to save every checkbox for every coin
    @FXML
    private void handleCryptoSearch() {
        String searchBarText = cryptoSearchBar.getText();
        String[] coins = searchBarText.split("[\\s*,*]+");
        assert coins.length != 0;

        currData = API.fetch(coins, buttonStrings);
        
        cryptoSearchBar.setText("");
        System.out.println(currData);
    }

    //handlers for all buttons
    @FXML
    private void handleSupply() {
        descBox.setText(varDescriptions.get("supply"));
    }

    @FXML
    private void handleMaxSupply() {
        descBox.setText(varDescriptions.get("maxSupply"));
    }

    @FXML
    private void handleMarketCapUsd() {
        descBox.setText(varDescriptions.get("marketCapUsd"));
    }

    @FXML
    private void handleVolumeUsd24Hr() {
        descBox.setText(varDescriptions.get("volumeUsd24Hr"));
    }

    @FXML
    private void handlePriceUsd() {
        descBox.setText(varDescriptions.get("priceUsd"));
    }

    @FXML
    private void handleChangePercent24Hr() {
        descBox.setText(varDescriptions.get("changePercent24Hr"));
    }

    @FXML
    private void handleVwap24Hr() {
        descBox.setText(varDescriptions.get("vwap24Hr"));
    }




    void clearStuff() {
        openFiles.clear();
        openFilesMenu.clear();
        runMenu.getItems().clear();
        fileTabPane.getTabs().clear();
        codeText.clear();
        folder = null;
    }




    void makeMenuItem(String name) {
        MenuItem i = new MenuItem(name);
        i.setOnAction(new EventHandler(){
            @Override
            public void handle(Event t) {

                String[] cmd = {
                        "python",
                        openFiles.get(name).file.getAbsolutePath(),
                };
                try {
                    String s = null;
                    Process p = Runtime.getRuntime().exec(cmd);

                    BufferedReader stdInput = new BufferedReader(new
                            InputStreamReader(p.getInputStream()));

                    BufferedReader stdError = new BufferedReader(new
                            InputStreamReader(p.getErrorStream()));

                    // read the output from the command
                    while ((s = stdInput.readLine()) != null) {
                        terminal.appendText( "\n" + s);
                    }

                    // read any errors from the attempted command
                    while ((s = stdError.readLine()) != null) {
                        terminal.appendText("\n" + s);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        runMenu.getItems().add(i);
        openFilesMenu.put(name, i);
    }




}
