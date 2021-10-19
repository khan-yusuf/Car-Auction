/*
 * EE422C FinalProject submission by
 * Yusuf Khan
 * yk7862
 * 17125
 * Slip days used: 0
 * Spring 2021
 */


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Controller {
    static Client client = new Client();
    Boolean successfulLogin = false;



    @FXML
    private Button loginButton;
    @FXML
    private Label wrongLogin;
    @FXML
    public TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Hyperlink guestButton;

    /**
     * when login button is pressed + successful login, hide stage and open up auction stage
     * @param actionEvent
     */
    public void UserLogin(ActionEvent actionEvent) {
        checkLogin();
        if(successfulLogin){
            client.setUpNetworking();
            client.user = username.getText();
            TabPane tabPane = new TabPane();
            Stage auctionStage = new Stage();
            auctionStage.setTitle("USED CAR AUCTION");

            tabPane.setBackground(new Background(new BackgroundFill(Color.web("#778899"), CornerRadii.EMPTY, Insets.EMPTY)));
            Tab soldTab = new Tab("Sold Cars");
            Tab historyTab = new Tab("Bid History");
            Tab auctionTab = new Tab("Auction");
            tabPane.getTabs().addAll(auctionTab, soldTab, historyTab);
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

            //TABS
            auctionTab.setContent(client.auctionPane);
            soldTab.setContent(client.soldPane);
            historyTab.setContent(client.historyPane);
            auctionStage.setScene(new Scene(tabPane, 925, 500));
            auctionStage.show();
            tabPane.lookup(".tab-pane .tab-header-area .tab-header-background").setStyle("-fx-background-color: #778899;");
        }
    }

    /**
     * if sign in as guest is pressed
     */
    public void guestLogin(){
        password.setText("japan");
        username.setText("<guest>");
        UserLogin(new ActionEvent());
    }

    /**
     * check login information for correct password
     */
    private void checkLogin() {

        if(password.getText().equals("japan")){
            wrongLogin.setText("Success!");

            loginButton.getScene().getWindow().hide();
            successfulLogin = true;

        }
        else if(username.getText().isEmpty() || password.getText().isEmpty()){
            wrongLogin.setText("Please enter your login data.");
        }
        else{
            wrongLogin.setText("Wrong username or password!");
        }
    }


}
