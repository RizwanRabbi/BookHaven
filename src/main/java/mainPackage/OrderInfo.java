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

    public OrderInfo() {}

}
