package mainPackage;

public class OrderInfo {
    public int orderID;
    public String name;
    public String email;
    public String phoneNo;
    public int status;
    public String address;
    public int shippingCosts;
    public int totalAmount;

    public String dateString;


    /*
    * Enums in Java Suck
    */

    public static final int PENDING = 0;
    public static final int PROCESSING = 1;
    public static final int SHIPPED = 2;
    public static final int DELIVERED = 3;
    public static final int CANCELED = 4;
    public static final int RETURNED = 5;

    public static String statusToString(int status) {
        return switch (status) {
            case PENDING -> "Pending";
            case PROCESSING -> "Processing";
            case SHIPPED -> "Shipped";
            case DELIVERED -> "Delivered";
            case CANCELED -> "Canceled";
            case RETURNED -> "Returned";
            default -> "Unknown";
        };
    }

    public OrderInfo() {
        this.orderID = 0; // You can choose a suitable default value
        this.name = "";
        this.email = "";
        this.phoneNo = "";
        this.status = PENDING; // Set a default status, e.g., PENDING
        this.address = "";
        this.shippingCosts = 0; // You can choose a suitable default value
        this.totalAmount = 0; // You can choose a suitable default value
        this.dateString = "";
    }

    public void print() {
        System.out.println("Order ID: " + orderID);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Phone Number: " + phoneNo);
        System.out.println("Status: " + statusToString(status));
        System.out.println("Address: " + address);
        System.out.println("Shipping Costs: " + shippingCosts);
        System.out.println("Total Amount: " + totalAmount);
        System.out.println("Date: " + dateString);
    }
}
