package com.example.stockmarketide;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.Tab;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.event.*;
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

    // Thisvar is used to store the current open tabs we have
    public HashMap<String, TabFile> openFiles = new HashMap<String, TabFile>();
    public HashMap<String, MenuItem> openFilesMenu = new HashMap<String, MenuItem>();

    Tab prevTab = null;

    public HashMap<String, HashMap<String, String>> currData = new HashMap<String, HashMap<String, String>>();

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

    @FXML
    private TextField deleteSearchBar;

    @FXML
    private Text currentCoins;

    @FXML
    private Text coinNotFound;

    @FXML
    private AnchorPane ap;

    // This is the left-hand bar that we can see all of the files within a project
    @FXML
    private TreeView<String> fileView;

    //This menu will update with the names of different files we have open so that we can run them


    //We need to update this label every time we run something.
    @FXML
    private TextArea terminal;

    private final Map<String, String> varDescriptions = Map.of(
            "supply", "The number of cryptocurrency coins or tokens that are publicly available and circulating in the market.\n" +
                    "Reference with [Your Coin].supply",
            "maxSupply", "Quantifies the maximum amount of coins that will ever exist, including the coins that will be mined or made available in the future.\n" +
                    "Reference with [Your Coin].maxSupply",
            "marketCapUsd", "Market capitalization (or market cap) is the total value of all the coins that have been mined.\n" +
                    "Reference with [Your Coin].marketCapUsd",
            "volumeUsd24Hr", "Сryptocurrency volume is the amount of a given cryptocurrency traded throughout a particular time period.\n" +
                    "Reference with [Your Coin].volumeUsd24Hr",
            "priceUsd", "Price for 1 token in US Dollars\n" +
                    "Reference with [Your Coin].priceUsd",
            "changePercent24Hr", "The change is the difference (in percent) between the price now compared to the price around this time 24 hours ago.\n" +
                    "Reference with [Your Coin].changePercent24Hr",
            "vwap24Hr", "VWAP is calculated by totaling the dollars traded for every transaction (price multiplied by the volume) and then dividing by the total shares traded.\n" +
                    "Reference with [Your Coin].vwap24Hr"
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

        if(event.getClickCount() == 2) {
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
            tab.setOnCloseRequest(new EventHandler<Event>() {
                @Override
                public void handle(Event arg0) {
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


    //Updates local coin data of those inputted by the user
    //Updates 'Current Coins' with coins with have local data of
    //Checks if there were any coins not found and informs user if there is
    @FXML
    private void handleCryptoSearch() {
        if (cryptoSearchBar.getText().matches("\\s*")) {
            cryptoSearchBar.setText("");
            return;
        }

        String searchBarText = cryptoSearchBar.getText();
        String[] userInput = searchBarText.split("[\\s*,*]+");
        HashMap<String, HashMap<String, String>> fetchedData = API.fetch(userInput);

        for (String id : fetchedData.keySet()) {
            currData.put(id, fetchedData.get(id));
        }

        coinNotFound(userInput);
        addRunningCoin();

        cryptoSearchBar.setText("");
    }

    @FXML
    private void handleDeleteButton() {
        String deleteText = deleteSearchBar.getText();
        String[] userInput = deleteText.split("[\\s*,*]+");
        if (userInput.length == 0) {
            return;
        }

        ArrayList<String> removedCoins = new ArrayList<>();

        for (String c : userInput) {
            if (currData.containsKey(c)) {
                currData.remove(c);
                removedCoins.add(c);
            }
        }

        if (!removedCoins.isEmpty()) {
            String delStr = "The following coins were deleted:\n";
            for (String c : removedCoins) {
                delStr += "- " + c + "\n";
            }
            coinNotFound.setText(delStr);
            addRunningCoin();
        } else {
            coinNotFound.setText("");
        }
        deleteSearchBar.setText("");
    }


    //CRYPTOSEARCH HELPERS

    private void coinNotFound(String[] coins) {
        ArrayList<String> notFound = new ArrayList<>();
        for (String coin : coins) {
            if ( !currData.containsKey(coin) ) {
                notFound.add(coin);
            }
        }
        if (notFound.isEmpty()) {
            coinNotFound.setText("");
        } else {
            String errStr = "The following coins were not found:\n";
            for (String coin : notFound) {
                errStr += "- " + coin + "\n";
            }
            coinNotFound.setText(errStr);
        }
    }

    //Adds any new coins to the running list in the 'Current Coins' tab
    private void addRunningCoin() {
        if (currData.keySet().size() == 0) {
            currentCoins.setText("There are no running coins. Search for some in the 'Crypto' tab.");
        }
        else {
            String str = "Current tracked coins:\n";
            for (Object coin : currData.keySet()) {
                str += "- " + coin + "\n";
            }
            currentCoins.setText(str);
        }
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
                try {
                    // This gets the user code
                    String usercode = Files.readString(Path.of(openFiles.get(name).file.getAbsolutePath()));

                    String tempPath = System.getProperty("user.dir") + "/tempfile.txt";

                    String totalStringToWrite = classInit;
                    for (String key : currData.keySet()) {
                        HashMap<String, String> coin = currData.get(key);
                        String declare = key + " = Coin(" + coin.get("supply") + ", " + coin.get("maxSupply") + ", " +
                                coin.get("marketCapUsd") + ", " + coin.get("volumeUsd24Hr") + ", " +
                                coin.get("priceUsd") + ", " + coin.get("changePercent24Hr") + ", " +
                                coin.get("vwap24Hr") + ")";
                        totalStringToWrite += "\n" + declare + "\n";
                    }
                    System.out.println(totalStringToWrite);
                    totalStringToWrite += "\n" + usercode;

                    File temp = new File(tempPath);

                    FileWriter myWriter = new FileWriter(temp.getAbsolutePath());
                    myWriter.write(totalStringToWrite);
                    myWriter.flush();
                    myWriter.close();

                    /* This was for running the file itself without the stuff we added in for the Coin class
                    String[] cmd = {
                            "python",
                            openFiles.get(name).file.getAbsolutePath(),
                    };
                    */

                    String[] cmd = {
                            "python",
                            temp.getAbsolutePath(),
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
                    temp.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        runMenu.getItems().add(i);
        openFilesMenu.put(name, i);
    }
}
