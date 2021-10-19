import java.util.Objects;

public class Car {
    private int id;
    private String model;
    private String description;
    private double buyItNowPrice;
    private double currentBid;
    private Boolean carSold;
    private String image;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id == car.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", description='" + description + '\'' +
                ", buyItNowPrice=" + buyItNowPrice +
                ", currentBid=" + currentBid +
                ", image='" + image + '\'' +
                '}';
    }

    public Car(int id, String model, String description, double buyItNowPrice, double currentBid, String image) {
        this.id = id;
        this.model = model;
        this.description = description;
        this.buyItNowPrice = buyItNowPrice;
        this.currentBid = currentBid;
        this.carSold = false;
        this.image = image;
    }

    public Boolean getCarSold() { return carSold; }

    public void setCarSold(Boolean carSold) { this.carSold = carSold; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getModel() { return model; }

    public void setModel(String model) { this.model = model; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public double getBuyItNowPrice() { return buyItNowPrice; }

    public void setBuyItNowPrice(double buyItNowPrice) { this.buyItNowPrice = buyItNowPrice; }

    public double getCurrentBid() { return currentBid; }

    public void setCurrentBid(double currentBid) { this.currentBid = currentBid; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }
}
