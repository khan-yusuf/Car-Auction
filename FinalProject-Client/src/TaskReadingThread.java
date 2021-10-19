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
import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class TaskReadingThread implements Runnable{
    Socket socket;
    Client client;
    DataInputStream fromServer;

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public TaskReadingThread(Client client, Socket socket) {
        this.client = client;
        this.socket = socket;
    }

    /**
     * updates GUI elements after reading in new message from server
     */
    @Override
    public void run() {

        while (true) {
            try {
                fromServer = new DataInputStream(socket.getInputStream());

                client.message = gson.fromJson(fromServer.readUTF(), Message.class);

                Platform.runLater(() -> client.updateAuctionData());
                Platform.runLater(() -> client.updateSoldPane());
                Platform.runLater(() -> client.updateHistoryPane());
            } catch (IOException e) {
                System.out.println("Error reading from server: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


}
