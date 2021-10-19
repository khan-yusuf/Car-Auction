/*
 * EE422C FinalProject submission by
 * Yusuf Khan
 * yk7862
 * 17125
 * Slip days used: 0
 * Spring 2021
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


public class Client extends Application {

	DataOutputStream toServer;
	public Message message = null;
	AtomicInteger selectedId = new AtomicInteger(-1);
	static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	public String user;

	//JAVAFX
	GridPane auctionPane = new GridPane();
	GridPane itemPane = new GridPane();
	AnchorPane soldPane = new AnchorPane();
	ListView<Car> soldListView = new ListView<>();
	AnchorPane historyPane = new AnchorPane();
	ListView<Bid> historyView = new ListView<>();

	@Override
	public void start(Stage primaryStage) {
		try{
			Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			primaryStage.setTitle("Login to continue");
			primaryStage.setScene(new Scene(root, 600, 400));
			primaryStage.show();
		} catch (IOException e){
			System.out.println("Can't find Login.fxml");
		}

	}

	public void updateHistoryPane(){
		historyPane.getChildren().clear();
		historyView.setPrefWidth(925);
		if(message.getHistoryList() != null){
			ObservableList<Bid> historyObsList = FXCollections.observableList(message.getHistoryList());
			historyView.setItems(historyObsList);
		}
		historyPane.getChildren().add(historyView);
	}

	public void updateSoldPane(){
		soldPane.getChildren().clear();
		soldListView.setPrefWidth(925);
		if(message.getSoldList() != null){
			ObservableList<Car> soldCarObservableList = FXCollections.observableList(message.getSoldList());
			soldListView.setItems(soldCarObservableList);
		}
		soldPane.getChildren().add(soldListView);
	}

	/**
	 * populates GUI elements according to Car list
	 * called everytime client receives new input from server
	 */
	public void updateAuctionData(){
		auctionPane.getChildren().clear();
		itemPane.getChildren().clear();

		int itemCount = 0;
		ToggleGroup toggleGroup = new ToggleGroup();

		//TITLE PANE
		BorderPane titlePane = new BorderPane();
		Label title = new Label();
		title.setText("NOTE: ALL BIDS AND PRICES ARE LISTED IN 1000's");
		title.setTextFill(Color.web("#F08080"));
		Label userLabel = new Label("User: " + user);
		titlePane.setRight(userLabel);
		userLabel.setTextFill(Color.web("#FFFFFF"));
		titlePane.setPadding(new Insets(10, 10, 10, 10));
		titlePane.setCenter(title);
		BorderPane.setAlignment(title, Pos.CENTER);

		//ITEM GUI
		for(Car car: message.getCarList()){
			try{

				//INFO LABELS
				Label modelVal = new Label();
				modelVal.setText(car.getModel());
				modelVal.setTextFill(Color.web("#FFFFFF"));
				Label descriptionVal = new Label();
				descriptionVal.setText(car.getDescription());
				descriptionVal.setTextFill(Color.web("#FFFFFF"));
				Label currentBidVal = new Label();
				currentBidVal.setTextFill(Color.web("#FFFFFF"));
				if(car.getCarSold()){
					currentBidVal.setText("Sold for: $ " + car.getCurrentBid());
				}
				else{
					currentBidVal.setText("Current Bid: $" + car.getCurrentBid());
				}
				Label buyPriceVal = new Label();
				buyPriceVal.setTextFill(Color.web("#FFFFFF"));
				buyPriceVal.setText("Buy now price: $" + car.getBuyItNowPrice());

				//RB
				RadioButton rb = new RadioButton();
				toggleGroup.getToggles().add(rb);

				//Observable list used to track changes in rb selection
				toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
					if (newValue != null ) {
						selectedId.set(toggleGroup.getToggles().indexOf(newValue));
					}
				});

				//IMAGES
				Image carImage = new Image(new FileInputStream("src/Resources/carImages/" + car.getImage()));
				ImageView carView = new ImageView(carImage);
				carView.setFitHeight(80);
				carView.setFitWidth(160);
				carView.setPreserveRatio(true);

				GridPane item = new GridPane();
				item.setPadding(new Insets(10, 10, 10, 10));
				item.setVgap(10); //spaces between nodes
				item.setHgap(10);
				item.add(modelVal,0,0);
				item.add(carView,0,1);
				item.add(descriptionVal,0,2);
				item.add(buyPriceVal,0,6);
				item.add(currentBidVal, 0,7);
				item.add(rb, 0, 11);

				itemPane.add(item, itemCount,0);
				itemCount++;


			} catch (FileNotFoundException e){
				e.printStackTrace();
			}
		}
		itemPane.setPadding(new Insets(10, 10, 10, 10));
		itemPane.setVgap(10); //spaces between nodes
		itemPane.setHgap(10);


		//CONTROL PANE
		GridPane controlPane = new GridPane();
		Button btnBid = new Button("BID");
		btnBid.setTextFill(Color.LIGHTCORAL);
		btnBid.setStyle("-fx-text-fill: #F08080");
		btnBid.setPrefSize(50, 24);
		Tooltip tt = new Tooltip("Click to Bid!");
		tt.setStyle("-fx-font: normal bold 13 Langdon; "
				+ "-fx-base: #AE3522; "
				+ "-fx-text-fill: #F08080;");
		btnBid.setTooltip(tt);

		Button exitButton = new Button("EXIT");
		exitButton.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: LIGHTCORAL");
		Tooltip exitTT = new Tooltip("Click to leave site!");
		exitTT.setStyle("-fx-font: normal bold 13 Langdon; "
				+ "-fx-base: #AE3522; "
				+ "-fx-text-fill: #F08080;");
		exitButton.setTooltip(exitTT);
		exitButton.setOnAction(exit ->{
			ButtonType exitConfirm = new ButtonType("Yes, exit", ButtonBar.ButtonData.OK_DONE);
			Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to leave?", ButtonType.CANCEL, exitConfirm); //Error dialogue stage
			a.setTitle("Exit?");
			a.setHeaderText(null);
			Optional<ButtonType> result = a.showAndWait();
			if(result.get() == exitConfirm){
				System.exit(0);
				Platform.exit();
			}
		});

		TextField bidValue = new TextField();
		bidValue.setPromptText("Enter bid value");
		btnBid.setOnAction(bidEvent -> {
			for(Car car : message.getCarList()){
				if(car.getId() == selectedId.intValue()){
					try{
						double bid = Double.parseDouble(bidValue.getText());
						if(car.getCarSold()){
							Alert a = new Alert(Alert.AlertType.WARNING); //Error dialogue stage
							a.setTitle("Car Unavailable!");
							a.setContentText("This " + car.getModel() + " was already sold for $" + car.getCurrentBid() + ". Please select a different car");
							a.setHeaderText(null);
							a.showAndWait();
						}
						else if(bid >= car.getBuyItNowPrice()){ //someone bought car for buyit now price
							Alert a = new Alert(Alert.AlertType.CONFIRMATION); //Error dialogue stage
							a.setTitle("Confirmation of Sale");
							a.setContentText("You are about to secure this " + car.getModel() + " for $" + bid + ". Continue?");
							a.setHeaderText(null);
							Optional<ButtonType> result = a.showAndWait();
							if(result.get() == ButtonType.OK){
								Message outgoingMessage = new Message(user, bid, selectedId.intValue(), message.getCarList(), true, false, message.getSoldList(), message.getHistoryList());
								System.out.println("Bid amount sent to server: $" + bid);
								toServer.writeUTF(gson.toJson(outgoingMessage));
								toServer.flush();
								playBuySound(); //plays sound
								Alert success = new Alert(Alert.AlertType.INFORMATION);
								success.setTitle("Success!");
								success.setContentText("Congratulations, you have successfully secured this " + car.getModel() + "! " +
										"Please check your inbox for payment details and warranty information.");
								success.setHeaderText(null);
								success.showAndWait();

							}
						}
						else if(bid > car.getCurrentBid()){ //regular bid
							Alert a = new Alert(Alert.AlertType.CONFIRMATION); //Error dialogue stage
							a.setTitle("Confirmation on Bid");
							a.setContentText("Continue with bid of $" + bid + " on this " + car.getModel() + "?");
							a.setHeaderText(null);
							Optional<ButtonType> result = a.showAndWait();
							if(result.get() == ButtonType.OK){
								Message outgoingMessage = new Message(user, bid, selectedId.intValue(), message.getCarList(), false, false, message.getSoldList(), message.getHistoryList());
								System.out.println("Bid amount sent to server: $" + bid);
								toServer.writeUTF(gson.toJson(outgoingMessage));
								toServer.flush();
								playBidSound(); //plays ding
								Alert success = new Alert(Alert.AlertType.INFORMATION);
								success.setTitle("Success!");
								success.setContentText("You have bid $" + bid + " on the " + car.getModel() + "! To see your previous bids, see the 'Bid History' tab.");
								success.setHeaderText(null);
								success.showAndWait();
							}

						}
						else{
							throw new Exception(); //bid too low, or not an int
						}
					}
					catch(IOException e){ e.printStackTrace();}
					catch(Exception e){
						Alert a = new Alert(Alert.AlertType.ERROR); //Error dialogue stage
						a.setTitle("Invalid Bid");
						a.setContentText("Enter a valid amount greater than $" + car.getCurrentBid() + " to bid on this " + car.getModel());
						a.setHeaderText(null);
						a.showAndWait();
					}
					break;
				}
			}
		});
		controlPane.setPadding(new Insets(10, 10, 10, 10));
		controlPane.setVgap(10); //spaces between nodes
		controlPane.setHgap(10);
		controlPane.add(exitButton, 60, 0);
		controlPane.add(btnBid, 36, 0);
		controlPane.add(bidValue, 35,0);

		auctionPane.add(itemPane, 0, 1);
		auctionPane.add(titlePane, 0, 0);
		auctionPane.add(controlPane,0,2);

	}

	MediaPlayer carPlayer;
	private void playBidSound() {
		String s = "src/Resources/Sounds/StartCar.mp3";
		Media media = new Media(Paths.get(s).toUri().toString());
		carPlayer = new MediaPlayer(media);
		carPlayer.play();
	}

	MediaPlayer buyPlayer;
	private void playBuySound() {
		String s = "src/Resources/Sounds/peelOut.mp3";
		Media media = new Media(Paths.get(s).toUri().toString());
		buyPlayer = new MediaPlayer(media);
		buyPlayer.play();
	}

	public void setUpNetworking(){
		try {
			Socket socket = new Socket(ConnectionConfiguration.host, ConnectionConfiguration.port);
			toServer = new DataOutputStream(socket.getOutputStream()); //output stream to send data to the server
			TaskReadingThread task = new TaskReadingThread(this, socket); //thread reads message from server continuously
			Thread thread = new Thread(task);
			thread.start();
		} catch (IOException ex) {
			System.out.println("connection to server failed");
		}
	}

	public static void main(String[] args) { launch(args); }

}
