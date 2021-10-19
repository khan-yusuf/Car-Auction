/*
 * EE422C FinalProject submission by
 * Yusuf Khan
 * yk7862
 * 17125
 * Slip days used: 0
 * Spring 2021
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;

public class Server extends Observable {
	static Server server;
	static Gson gson = new Gson();
	Message stateMessage;

	public static void main(String[] args) {
		try {
			server = new Server();
			server.initialize();
			server.setUpNetworking();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * initialization of data from json file
	 */
	public void initialize(){
		server.stateMessage = new Message(true); //carList empty
		server.stateMessage.setCarList(readJsonFile());
		System.out.println("initialized server data state");
	}

	/**
	 * new thread for accepting new clients at anytime while server is running
	 */
	public void setUpNetworking() {
		new Thread(() -> {
			try {
				ServerSocket serverSocket = new ServerSocket(ConnectionConfiguration.port); // Create a server socket

				while (true) { //continuous listening for connections
					Socket clientSocket = serverSocket.accept();
					TaskClientHandler connection = new TaskClientHandler(this, clientSocket);
					this.addObserver(connection);

					new Thread(connection).start(); //new thread for each connection
					System.out.println("new connection!");
					server.broadcast(gson.toJson(server.stateMessage));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();

	}


	/**
	 * reads json file and returns the arraylist of json objects as a string
	 * @return carList
	 */
	public ArrayList<Car> readJsonFile(){
		ArrayList<Car> carList = null;
		try (FileReader reader = new FileReader("CarCatalog.json"))
		{
			Type userListType = new TypeToken<ArrayList<Car>>(){}.getType();
			carList = gson.fromJson(reader, userListType);
			for(Car car : carList){
				car.setCarSold(false);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return carList;
	}

	/**
	 * using observable pattern to update clients via update method
	 * @param string message in json form
	 */
	public void broadcast(String string){
		setChanged();
		notifyObservers(string);
	}

}
