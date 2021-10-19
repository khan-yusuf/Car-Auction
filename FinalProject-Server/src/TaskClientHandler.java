/*
 * EE422C FinalProject submission by
 * Yusuf Khan
 * yk7862
 * 17125
 * Slip days used: 0
 * Spring 2021
 */

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class TaskClientHandler implements Runnable, Observer {
    Server server;
    Socket socket;
    DataInputStream fromClient;
    DataOutputStream toClient;

    Object lock = new Object();
    Gson gson = new Gson();

    public TaskClientHandler(Server server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;

    }

    /**
     * main logic for interpreting messages from client and then sending new messages back to them
     * ie updating those messages
     */
    public void run() {
        try {
            fromClient = new DataInputStream(socket.getInputStream());
            toClient = new DataOutputStream(socket.getOutputStream());

            while (true) {
                synchronized (lock){ //synchronize client actions
                    server.stateMessage = gson.fromJson(fromClient.readUTF(), Message.class);

                    if(server.stateMessage.getHistoryList() == null){ //null list
                        ArrayList<Bid> newList = new ArrayList<>();
                        for(Car car : server.stateMessage.getCarList()){
                            if(car.getId() == server.stateMessage.getId()){
                                Bid newBid = new Bid(server.stateMessage.getClient(), car.getModel(), server.stateMessage.getBidAmt(), false);
                                if(server.stateMessage.getSold()){
                                    newBid.setSold(true);
                                }
                                newList.add(newBid);
                                server.stateMessage.setHistoryList(newList);
                                break;
                            }
                        }
                    }
                    else{
                        for(Car car : server.stateMessage.getCarList()){
                            if(car.getId() == server.stateMessage.getId()){ //for bid history
                                Bid newBid = new Bid(server.stateMessage.getClient(), car.getModel(), server.stateMessage.getBidAmt(), false);
                                if(server.stateMessage.getSold()){
                                    newBid.setSold(true);
                                }
                                server.stateMessage.getHistoryList().add(newBid);
                                break;
                            }
                        }
                    }

                    if(server.stateMessage.getSold()){ //sold bid message
                        for(Car car : server.stateMessage.getCarList()){
                            if(car.getId() == server.stateMessage.getId()){
                                car.setCurrentBid(server.stateMessage.getBidAmt());
                                car.setCarSold(true);
                                if(server.stateMessage.getSoldList() == null){ //first sold item
                                    ArrayList<Car> list = new ArrayList<>();
                                    list.add(car);
                                    server.stateMessage.setSoldList(list);
                                }
                                else{ //list already has some sold items
                                    server.stateMessage.getSoldList().add(car); //adds sold car to list
                                    //server.stateMessage.setSoldList(server.stateMessage.getSoldList()); //updates list
                                }
                                break;
                            }
                        }
                        server.broadcast(gson.toJson(new Message(server.stateMessage.getClient(), server.stateMessage.getCarList(), server.stateMessage.getSoldList(), server.stateMessage.getHistoryList())));
                    }
                    else if(server.stateMessage.getBidAmt() > 0){ //regular bid
                        for(Car car : server.stateMessage.getCarList()){
                            if(car.getId() == server.stateMessage.getId()){
                                car.setCurrentBid(server.stateMessage.getBidAmt());
                                break;
                            }
                        }
                        server.broadcast(gson.toJson(new Message(server.stateMessage.getClient(), server.stateMessage.getCarList(), server.stateMessage.getSoldList(), server.stateMessage.getHistoryList())));
                    }
                }

            }
        } catch (IOException e) {
            System.out.println("client left");
        }
    }

    /**
     * sends message to all clients based on observable pattern
     * @param o observable
     * @param arg cast to string message
     */
    @Override
    public void update(Observable o, Object arg) {
        try {
            toClient.writeUTF((String) arg);
            toClient.flush();
        } catch (IOException e) {
            try {
                this.socket.close();
            } catch (IOException ioException) {
                System.out.println("Unable to close socket!");
            }
        }
    }
}
