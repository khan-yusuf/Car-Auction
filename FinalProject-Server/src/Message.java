/*
 * EE422C FinalProject submission by
 * Yusuf Khan
 * yk7862
 * 17125
 * Slip days used: 0
 * Spring 2021
 */

import java.util.ArrayList;

public class Message {
    private String client;
    private double bidAmt = 0.0;
    private int id = 0; //0,1,2,3,4,..
    private ArrayList<Car> carList = null;
    private Boolean sold = false;
    private Boolean init = false;
    private ArrayList<Car> soldList = null;
    private ArrayList<Bid> historyList = null;

    @Override
    public String toString() {
        return "Message{" +
                "client='" + client + '\'' +
                ", bidAmt=" + bidAmt +
                ", id=" + id +
                ", carList=" + carList +
                ", sold=" + sold +
                ", init=" + init +
                ", soldList=" + soldList +
                ", historyList=" + historyList +
                '}';
    }

    public Message(Boolean init){
        this.init = init;
    }

    public Message(String client, double bidAmt, int id, ArrayList<Car> carList, Boolean sold, Boolean init, ArrayList<Car> soldList, ArrayList<Bid> historyList) {
        this.client = client;
        this.bidAmt = bidAmt;
        this.id = id;
        this.carList = carList;
        this.sold = sold;
        this.init = init;
        this.soldList = soldList;
        this.historyList = historyList;
    }

    public Message(String client, ArrayList<Car> carList, ArrayList<Car> soldList, ArrayList<Bid> historyList){
        this.client = client;
        this.carList = carList;
        this.soldList = soldList;
        this.historyList = historyList;
    }

    public String getClient() { return client; }

    public void setClient(String client) { this.client = client; }

    public ArrayList<Bid> getHistoryList() { return historyList; }

    public void setHistoryList(ArrayList<Bid> historyList) { this.historyList = historyList; }

    public ArrayList<Car> getSoldList() { return soldList; }

    public void setSoldList(ArrayList<Car> soldList) { this.soldList = soldList; }

    public double getBidAmt() { return bidAmt; }

    public void setBidAmt(double bidAmt) { this.bidAmt = bidAmt; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public ArrayList<Car> getCarList() { return carList; }

    public void setCarList(ArrayList<Car> carList) { this.carList = carList; }

    public Boolean getSold() { return sold; }

    public void setSold(Boolean sold) { this.sold = sold; }

    public Boolean getInit() { return init; }

    public void setInit(Boolean init) { this.init = init; }
}