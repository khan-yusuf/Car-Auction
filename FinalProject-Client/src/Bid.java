/*
 * EE422C FinalProject submission by
 * Yusuf Khan
 * yk7862
 * 17125
 * Slip days used: 0
 * Spring 2021
 */


public class Bid {

    //class used solely for displaying bid history
    String client;
    String carModel;
    double bidAmount;
    boolean sold;

    public Bid(String client, String carModel, double bidAmount, boolean sold) {
        this.client = client;
        this.carModel = carModel;
        this.bidAmount = bidAmount;
        this.sold = sold;
    }

    @Override
    public String toString() {
        if(sold){
            return client + " bid $" + bidAmount + " on the " + carModel + " (sold)";
        }
        return client + " bid $" + bidAmount + " on the " + carModel;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }
}